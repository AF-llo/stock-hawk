package de.lokaizyk.stockhawk.logic;

import java.util.ArrayList;
import java.util.List;

import de.lokaizyk.stockhawk.logic.model.StockItemViewModel;

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

    public static List<StockItemViewModel> loadStocksFromDb() {
        // TODO: 23.12.16
        List<StockItemViewModel> stocks = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            StockItemViewModel stockItem = new StockItemViewModel();
            stockItem.setSymbol("SYMB " + i);
            int change = i * 100;
            stockItem.setChange(String.valueOf(change));
            stockItem.setPercentChange(String.valueOf(change / 100));
            stockItem.setPrice(String.valueOf(200.0 * i));
            if (i % 2 == 0) {
                stockItem.setUp(true);
            }
            stocks.add(stockItem);
        }
        return stocks;
    }

    public static StockItemViewModel loadStockFromDb(String symbol) {
        // TODO: 25.12.16
        return null;
    }

}
