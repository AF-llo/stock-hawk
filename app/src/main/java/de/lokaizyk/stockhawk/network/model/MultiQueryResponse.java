package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 23.12.16.
 */

public class MultiQueryResponse {

    @SerializedName("query")
    private MultipleQuery query;

    public MultipleQuery getQuery() {
        return query;
    }

    public void setQuery(MultipleQuery query) {
        this.query = query;
    }

}
