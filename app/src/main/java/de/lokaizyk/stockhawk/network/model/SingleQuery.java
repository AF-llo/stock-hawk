package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lars on 25.12.16.
 */

public class SingleQuery {


    @SerializedName("count")
    private Integer count;

    @SerializedName("created")
    private Date created;

    @SerializedName("lang")
    private String lang;

    @SerializedName("results")
    private SingleResult results;

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

    public SingleResult getResults() {
        return results;
    }

    public void setResults(SingleResult results) {
        this.results = results;
    }

}
