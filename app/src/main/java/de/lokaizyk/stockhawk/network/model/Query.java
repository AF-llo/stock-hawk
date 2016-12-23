package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lars on 23.12.16.
 */

public class Query {

    @SerializedName("count")
    private Integer count;

    @SerializedName("created")
    private String created;

    @SerializedName("lang")
    private String lang;

    @SerializedName("results")
    private Results results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

}
