package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lars on 23.12.16.
 */

public class MultipleQuery {

    @SerializedName("count")
    private Integer count;

    @SerializedName("created")
    private Date created;

    @SerializedName("lang")
    private String lang;

    @SerializedName("results")
    private MultipleResult results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public MultipleResult getResults() {
        return results;
    }

    public void setResults(MultipleResult results) {
        this.results = results;
    }

}
