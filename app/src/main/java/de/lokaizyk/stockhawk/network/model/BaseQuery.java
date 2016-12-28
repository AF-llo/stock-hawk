package de.lokaizyk.stockhawk.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lars on 28.12.16.
 */

public class BaseQuery {

    @SerializedName("count")
    private Integer count;

    @SerializedName("created")
    private Date created;

    @SerializedName("lang")
    private String lang;

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

}
