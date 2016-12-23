package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 23.12.16.
 */

public class Quote {

    @SerializedName("symbol")
    private String symbol;

    @SerializedName("Bid")
    private String bid;

    @SerializedName("Change")
    private String change;

    @SerializedName("ChangeinPercent")
    private String changeinPercent;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangeinPercent() {
        return changeinPercent;
    }

    public void setChangeinPercent(String changeinPercent) {
        this.changeinPercent = changeinPercent;
    }

}
