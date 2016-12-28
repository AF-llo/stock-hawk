package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 23.12.16.
 */

public class MultipleQuery extends BaseQuery {

    @SerializedName("results")
    private MultipleResult results;

    public MultipleResult getResults() {
        return results;
    }

    public void setResults(MultipleResult results) {
        this.results = results;
    }

}
