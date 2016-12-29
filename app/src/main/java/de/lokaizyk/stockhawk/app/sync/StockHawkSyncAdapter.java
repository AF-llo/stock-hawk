package de.lokaizyk.stockhawk.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.logic.StockProvider;
import de.lokaizyk.stockhawk.network.api.YahooApi;
import de.lokaizyk.stockhawk.network.api.YahooApiFactory;
import de.lokaizyk.stockhawk.network.model.HistoricalQueryResponse;
import de.lokaizyk.stockhawk.network.model.HistoricalQuote;
import de.lokaizyk.stockhawk.network.model.MultiQueryResponse;
import de.lokaizyk.stockhawk.network.model.Quote;
import de.lokaizyk.stockhawk.network.model.SingleQueryResponse;
import de.lokaizyk.stockhawk.persistance.DbManager;
import de.lokaizyk.stockhawk.persistance.model.DbStock;
import de.lokaizyk.stockhawk.ui.widget.StockDetailsWidgetProvider;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by lars on 28.12.16.
 */

public class StockHawkSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = StockHawkSyncAdapter.class.getSimpleName();

    public static final String EXTRA_SYMBOL = "symbol";

    public static final int SYNC_INTERVAL = 60 * 60; // every hour
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    private YahooApi mYahooApi;

    public StockHawkSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mYahooApi = new YahooApiFactory().createService();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync()");
        syncStocks(extras);
    }

    public static void syncImmediately(Context context, Bundle bundle) {
        Log.d(TAG, "syncImmediately");
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        Log.d(TAG, "getSyncAccount");
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if ( null == accountManager.getPassword(newAccount) ) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    public static void initializeSyncAdapter(Context context) {
        Log.d(TAG, "initializeSyncAdapter");
        getSyncAccount(context);
    }

    public static void addSymbol(Context context, String symbol) {
        Bundle bundle = null;
        if (!TextUtils.isEmpty(symbol)) {
            bundle = new Bundle();
            bundle.putString(EXTRA_SYMBOL, symbol);
        }
        syncImmediately(context, bundle);
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        Log.d(TAG, "onAccountCreated");
        StockHawkSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Log.d(TAG, "configurePeriodicSync");
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private void syncStocks(Bundle data) {
        boolean isUpdate = !data.containsKey(EXTRA_SYMBOL);
        YahooApi.QueryBuilder queryBuilder = YahooApi.QueryBuilder.currentQuery();
        YahooApi.QueryBuilder historicalBuilder = null;
        if (isUpdate) {
            List<DbStock> dbStocks = DbManager.getInstance().loadAllCurrentStocks();
            if (dbStocks.size() == 0) {
                historicalBuilder = getHistoricalBuilderForOneMonth();
                queryBuilder.addInitialSymbols();
                historicalBuilder.addInitialSymbols();
            } else {
                for (DbStock dbStock : dbStocks) {
                    queryBuilder.addSymbol(dbStock.getSymbol());
                }
            }
        } else {
            historicalBuilder = getHistoricalBuilderForOneMonth();
            String stockInput = data.getString(EXTRA_SYMBOL);
            queryBuilder.addSymbol(stockInput);
            historicalBuilder.addSymbol(stockInput);
        }
        try {
            List<HistoricalQuote> historicalQuotes = null;
            if (historicalBuilder != null) {
                historicalQuotes = loadHistoricalData(historicalBuilder.buildSymbolQueryValue());
            }
            Pair<Long, List<Quote>> loadedQuotes = fetchData(queryBuilder.getSymbolCount(), queryBuilder.buildSymbolQueryValue());
            if (loadedQuotes.second.size() > 0) {
                List<DbStock> stocks = new ArrayList<>();
                for (Quote quote : loadedQuotes.second) {
                    stocks.add(StockProvider.dbStockFromQuote(quote, loadedQuotes.first));
                }
                if (historicalQuotes != null) {
                    List<DbStock> historicalStocks = new ArrayList<>();
                    for (HistoricalQuote historicalQuote : historicalQuotes) {
                        historicalStocks.add(StockProvider.dbStockFromHistoricalQuote(historicalQuote));
                    }
                    DbManager.getInstance().insertOrReplace(historicalStocks);
                }
                DbManager.getInstance().insertOrReplace(stocks);
                DbManager.getInstance().notifyObserver();
                updateWidgets(getContext());
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading data from Backend", e);
        }
    }

    private YahooApi.QueryBuilder getHistoricalBuilderForOneMonth() {
        DateTime end = DateTime.now().minusDays(1);
        DateTime start = end.minusMonths(1);
        return YahooApi.QueryBuilder.historicalQuery(
                start.toString(DateTimeFormat.forPattern(StockProvider.HISTORICAL_QUOTE_DATE_PATTERN)),
                end.toString(DateTimeFormat.forPattern(StockProvider.HISTORICAL_QUOTE_DATE_PATTERN)));
    }

    private List<HistoricalQuote> loadHistoricalData(String query) throws IOException {
        Call<HistoricalQueryResponse> historicalQuery = mYahooApi.loadHistoricalStocks(query);
        Response<HistoricalQueryResponse> historicalResponse = historicalQuery.execute();
        if (historicalResponse == null || historicalResponse.body() == null || historicalResponse.body().getQuery().getResults() == null ) {
            return Collections.emptyList();
        }
        return historicalResponse.body().getQuery().getResults().getQuote();
    }

    public static void updateWidgets(Context context) {
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(StockDetailsWidgetProvider.ACTION_UPDATE).setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }

    private Pair<Long, List<Quote>> fetchData(int symbolCount, String query) throws IOException {
        List<Quote> quotes = new ArrayList<>();
        long time;
        if (symbolCount > 1) {
            Response<MultiQueryResponse> response = mYahooApi.loadStocks(query).execute();
            for (Quote quote : response.body().getQuery().getResults().getQuotes()) {
                if (isValidStock(quote)) {
                    quotes.add(quote);
                }
            }
            time = response.body().getQuery().getCreated().getTime();
        } else {
            Response<SingleQueryResponse> response = mYahooApi.loadStock(query).execute();
            Quote quote = response.body().getQuery().getResults().getQuote();
            if (isValidStock(quote)) {
                quotes.add(quote);
            } else {
                if (invalidStockData(quote)) {
                    if (getContext() != null) {
                        toastOnMainThread(getContext().getString(R.string.invalid_stock_data));
                    }
                } else {
                    if (getContext() != null) {
                        toastOnMainThread(getContext().getString(R.string.stock_not_found));
                    }
                }
            }
            time = response.body().getQuery().getCreated().getTime();
        }
        return new Pair<>(time, quotes);
    }

    private void toastOnMainThread(String message) {
        Context context = getContext();
        if (context != null && !TextUtils.isEmpty(message)) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
        }
    }

    private boolean isValidStock(Quote quote) {
        return !TextUtils.isEmpty(quote.getBid()) && !TextUtils.isEmpty(quote.getChange()) && !TextUtils.isEmpty(quote.getChangeinPercent());
    }

    private boolean invalidStockData(Quote quote) {
        return TextUtils.isEmpty(quote.getBid()) && !TextUtils.isEmpty(quote.getChange()) && !TextUtils.isEmpty(quote.getChangeinPercent());
    }
}
