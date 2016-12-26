package de.lokaizyk.stockhawk.logic;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.lokaizyk.stockhawk.logic.model.StockItemViewModel;
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

    @Nullable
    public static StockItemViewModel stockItemFromDbStock(DbStock dbStock) {
        if (dbStock == null) {
            return null;
        }
        StockItemViewModel stockItem = new StockItemViewModel();
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
        dbStock.setBidprice(String.format(Locale.US, "%.2f", Float.parseFloat(quote.getBid())));
        dbStock.setCreated(time);
        dbStock.setIsUp(quote.getChange().charAt(0) != '-');
        dbStock.setIsCurrent(true);
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
        change = String.format(Locale.US, "%.2f", round);
        StringBuffer changeBuffer = new StringBuffer(change);
        changeBuffer.insert(0, weight);
        changeBuffer.append(ampersand);
        change = changeBuffer.toString();
        return change;
    }
}
