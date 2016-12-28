package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 28.12.16.
 */

public class HistoricalQuote {


    @SerializedName("Symbol")
    private String symbol;

    @SerializedName("Date")
    private String date;

    @SerializedName("Open")
    private String open;

    @SerializedName("High")
    private String high;

    @SerializedName("Low")
    private String low;

    @SerializedName("Close")
    private String close;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

}
