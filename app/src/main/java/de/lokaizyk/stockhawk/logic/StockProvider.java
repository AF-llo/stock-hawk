package de.lokaizyk.stockhawk.logic;

import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.lokaizyk.stockhawk.logic.model.StockItemViewModel;
import de.lokaizyk.stockhawk.network.model.HistoricalQuote;
import de.lokaizyk.stockhawk.network.model.Quote;
import de.lokaizyk.stockhawk.persistance.DbManager;
import de.lokaizyk.stockhawk.persistance.model.DbStock;

/**
 * Created by lars on 22.10.16.
 */

public class StockProvider {

    private static boolean showPercent;

    public static boolean getShowPercent() {
        return showPercent;
    }

    public static void toggleShowPercent() {
        showPercent = !showPercent;
    }

    private static final String DEFAULT_CHANGE = "+0.00";

    private static final String DEFAULT_CHANGE_INPERCENT = DEFAULT_CHANGE + "%";

    private static final String FLOAT_FORMAT_PATTERN = "%.2f";

    public static final String HISTORICAL_QUOTE_DATE_PATTERN = "yyyy-MM-dd";

    public static List<StockItemViewModel> loadCurrentStocksFromDb() {
        List<DbStock> currentStocks = DbManager.getInstance().loadAllCurrentStocks();
        List<StockItemViewModel> stocks = new ArrayList<>();
        for (DbStock currentStock : currentStocks) {
            StockItemViewModel stockItem = stockItemFromDbStock(currentStock);
            if (stockItem != null) {
                stocks.add(stockItem);
            }
        }
        return stocks;
    }

    public static StockItemViewModel loadStockFromDb(String symbol) {
        return stockItemFromDbStock(DbManager.getInstance().loadCurrentStock(symbol));
    }

    public static List<Entry> loadStockEntriesFromDb(String symbol) {
        List<DbStock> stocks = DbManager.getInstance().loadStocks(symbol);
        List<Entry> entries = new ArrayList<>();
        int index = 0;
        for (DbStock stock : stocks) {
            if (stock != null) {
                entries.add(new Entry((float) index, Float.valueOf(stock.getBidprice())));
                index++;
            }
        }
        return entries;
    }

    @Nullable
    public static StockItemViewModel stockItemFromDbStock(DbStock dbStock) {
        if (dbStock == null) {
            return null;
        }
        StockItemViewModel stockItem = new StockItemViewModel();
        stockItem.setId(dbStock.getId());
        stockItem.setSymbol(dbStock.getSymbol());
        stockItem.setChange(dbStock.getChange());
        stockItem.setPercentChange(dbStock.getPercentChange());
        stockItem.setPrice(dbStock.getBidprice());
        stockItem.setUp(dbStock.getIsUp());
        return stockItem;
    }

    @Nullable
    public static DbStock dbStockFromQuote(Quote quote, long time) {
        if (quote == null) {
            return null;
        }
        DbStock dbStock = new DbStock();
        dbStock.setSymbol(quote.getSymbol());
        dbStock.setChange(truncateChange(quote.getChange(), false));
        dbStock.setPercentChange(truncateChange(quote.getChangeinPercent(), true));
        dbStock.setBidprice(String.format(Locale.US, FLOAT_FORMAT_PATTERN, Float.parseFloat(quote.getBid())));
        dbStock.setCreated(time);
        dbStock.setIsUp(quote.getChange().charAt(0) != '-');
        dbStock.setIsCurrent(true);
        return dbStock;
    }

    @Nullable
    public static DbStock dbStockFromHistoricalQuote(HistoricalQuote historicalQuote) {
        if (historicalQuote == null) {
            return null;
        }
        DbStock dbStock = new DbStock();
        dbStock.setSymbol(historicalQuote.getSymbol());
        dbStock.setChange(DEFAULT_CHANGE);
        dbStock.setPercentChange(DEFAULT_CHANGE_INPERCENT);
        dbStock.setBidprice(String.format(Locale.US, FLOAT_FORMAT_PATTERN, Float.parseFloat(historicalQuote.getClose())));
        dbStock.setCreated(DateTime.parse(historicalQuote.getDate(), DateTimeFormat.forPattern(HISTORICAL_QUOTE_DATE_PATTERN)).getMillis());
        dbStock.setIsUp(true);
        dbStock.setIsCurrent(false);
        return dbStock;
    }

    private static String truncateChange(String change, boolean isPercentChange){
        String weight = change.substring(0,1);
        String ampersand = "";
        if (isPercentChange){
            ampersand = change.substring(change.length() - 1, change.length());
            change = change.substring(0, change.length() - 1);
        }
        change = change.substring(1, change.length());
        double round = (double) Math.round(Double.parseDouble(change) * 100) / 100;
        change = String.format(Locale.US, FLOAT_FORMAT_PATTERN, round);
        StringBuffer changeBuffer = new StringBuffer(change);
        changeBuffer.insert(0, weight);
        changeBuffer.append(ampersand);
        change = changeBuffer.toString();
        return change;
    }
}
