package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 23.12.16.
 */

public class QueryResponse {

    @SerializedName("query")
    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

}
