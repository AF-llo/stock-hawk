package de.lokaizyk.stockhawk.logic.model;

/**
 * Created by lars on 23.12.16.
 */

public class StockItemViewModel {

    private boolean isUp = false;

    private String symbol = "";

    private String price = "";

    private String change = "";

    private String percentChange = "";

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }
}
