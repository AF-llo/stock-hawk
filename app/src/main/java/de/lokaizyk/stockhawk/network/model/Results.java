package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lars on 23.12.16.
 */

public class Results {

    @SerializedName("quote")
    private List<Quote> quotes = null;

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

}
