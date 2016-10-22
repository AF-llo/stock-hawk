package de.lokaizyk.stockhawk.logic;

/**
 * Created by lars on 22.10.16.
 */

public class StockProvider {

    private StockProvider() {
    }

    

    private static class StockProviderLazyHolder {
       private static StockProvider INSTANCE = new StockProvider();
    }

    public static StockProvider getInstance() {
       return StockProviderLazyHolder.INSTANCE;
    }

}
