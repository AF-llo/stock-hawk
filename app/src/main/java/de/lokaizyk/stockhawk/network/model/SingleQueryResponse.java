package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 25.12.16.
 */

public class SingleQueryResponse {

    @SerializedName("query")
    private SingleQuery query;

    public SingleQuery getQuery() {
        return query;
    }

    public void setQuery(SingleQuery query) {
        this.query = query;
    }

}
