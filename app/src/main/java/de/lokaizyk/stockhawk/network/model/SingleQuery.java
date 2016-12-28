package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 25.12.16.
 */

public class SingleQuery extends BaseQuery {

    @SerializedName("results")
    private SingleResult results;

    public SingleResult getResults() {
        return results;
    }

    public void setResults(SingleResult results) {
        this.results = results;
    }

}
