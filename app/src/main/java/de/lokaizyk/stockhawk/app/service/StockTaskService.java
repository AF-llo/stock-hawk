package de.lokaizyk.stockhawk.app.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.logic.StockProvider;
import de.lokaizyk.stockhawk.network.api.YahooApi;
import de.lokaizyk.stockhawk.network.api.YahooApiFactory;
import de.lokaizyk.stockhawk.network.model.MultiQueryResponse;
import de.lokaizyk.stockhawk.network.model.Quote;
import de.lokaizyk.stockhawk.network.model.SingleQueryResponse;
import de.lokaizyk.stockhawk.persistance.DbManager;
import de.lokaizyk.stockhawk.persistance.model.DbStock;
import retrofit2.Response;

/**
 * Created by lars on 23.12.16.
 */

public class StockTaskService extends GcmTaskService {

    private static final String TAG = StockTaskService.class.getSimpleName();

    public static final long PERIOD = 3600L;

    public static final long FLEX = 10L;

    public static final String EXTRA_SYMBOL = "symbol";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({PERIODIC, INIT, ADD})
    public @interface Tag{}

    public static final String PERIODIC = "periodic";
    public static final String INIT = "init";
    public static final String ADD = "add";

    private YahooApi mYahooApi;

    private Context mContext;

    public StockTaskService(Context context) {
        this();
        mContext = context;
    }

    public StockTaskService() {
        mYahooApi = new YahooApiFactory().createService();
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.d(TAG, "onRunTask: Tag=" + taskParams.getTag());
        if (mContext == null) {
            mContext = this;
        }
        YahooApi.SymbolBuilder symbolBuilder = new YahooApi.SymbolBuilder();
        if (taskParams.getTag().equals(INIT) || taskParams.getTag().equals(PERIODIC)) {
            List<DbStock> dbStocks = DbManager.getInstance().loadAllCurrentStocks();
            if (dbStocks.size() == 0) {
                symbolBuilder.addInitialSymbols();
            } else {
                for (DbStock dbStock : dbStocks) {
                    symbolBuilder.addSymbol(dbStock.getSymbol());
                }
            }
        } else if (taskParams.getTag().equals(ADD)) {
            String stockInput = taskParams.getExtras().getString(EXTRA_SYMBOL);
            symbolBuilder.addSymbol(stockInput);
        }
        int result = GcmNetworkManager.RESULT_FAILURE;
        try {
            Pair<Long, List<Quote>> loadedQuotes = fetchData(symbolBuilder.getSymbolCount(), symbolBuilder.buildSymbolQueryValue());
            result = GcmNetworkManager.RESULT_SUCCESS;
            if (loadedQuotes.second.size() > 0) {
                List<DbStock> stocks = new ArrayList<>();
                for (Quote quote : loadedQuotes.second) {
                    stocks.add(StockProvider.dbStockFromQuote(quote, loadedQuotes.first));
                }
                DbManager.getInstance().insertOrReplace(stocks);
                DbManager.getInstance().notifyObserver();
            } else {
                if (mContext != null) {
                    toastOnMainThread(mContext.getString(R.string.stock_not_found));
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading data from Backend", e);
        }
        return result;
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
            }
            time = response.body().getQuery().getCreated().getTime();
        }
        return new Pair<>(time, quotes);
    }

    private void toastOnMainThread(String message) {
        if (mContext != null && !TextUtils.isEmpty(message)) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show());
        }
    }

    private boolean isValidStock(Quote quote    ) {
        return !TextUtils.isEmpty(quote.getBid()) && !TextUtils.isEmpty(quote.getChange()) && !TextUtils.isEmpty(quote.getChangeinPercent());
    }

    public static void runPeriodic(Context context) {
        PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(StockTaskService.class)
                .setPeriod(PERIOD)
                .setFlex(FLEX)
                .setTag(PERIODIC)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setRequiresCharging(false)
                .build();
        // Schedule task with tag "periodic." This ensure that only the stocks present in the DB
        // are updated.
        GcmNetworkManager.getInstance(context).schedule(periodicTask);
    }
}
