package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lars on 28.12.16.
 */

public class HistoricalResult {

    @SerializedName("quote")
    private List<HistoricalQuote> quote = null;

    public List<HistoricalQuote> getQuote() {
        return quote;
    }

    public void setQuote(List<HistoricalQuote> quote) {
        this.quote = quote;
    }

}
