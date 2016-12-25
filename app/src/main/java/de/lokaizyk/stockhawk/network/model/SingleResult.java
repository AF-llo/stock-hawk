package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 25.12.16.
 */

public class SingleResult {

    @SerializedName("quote")
    private Quote quote = null;

    public Quote getQuote() {
        return quote;
    }

    public void setQuotes(Quote quote) {
        this.quote = quote;
    }

}
