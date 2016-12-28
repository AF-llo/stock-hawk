package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 28.12.16.
 */

public class HistoricalQueryResponse {

    @SerializedName("query")
    private HistoricalQuery query;

    public HistoricalQuery getQuery() {
        return query;
    }

    public void setQuery(HistoricalQuery query) {
        this.query = query;
    }

}
