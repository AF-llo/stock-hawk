package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 28.12.16.
 */

public class HistoricalQuery extends BaseQuery {

    @SerializedName("results")
    private HistoricalResult results;

    public HistoricalResult getResults() {
        return results;
    }

    public void setResults(HistoricalResult results) {
        this.results = results;
    }

}
