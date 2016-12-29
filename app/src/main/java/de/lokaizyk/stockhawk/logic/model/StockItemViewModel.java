package de.lokaizyk.stockhawk.logic.model;

import android.databinding.ObservableBoolean;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lars on 23.12.16.
 */

public class StockItemViewModel implements Parcelable {

    private long id = -1;

    private boolean isUp = false;

    private String symbol = "";

    private String price = "";

    private String change = "";

    private String percentChange = "";

    public ObservableBoolean isSelected = new ObservableBoolean(false);

    public ObservableBoolean isActive = new ObservableBoolean(false);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeByte(this.isUp ? (byte) 1 : (byte) 0);
        dest.writeString(this.symbol);
        dest.writeString(this.price);
        dest.writeString(this.change);
        dest.writeString(this.percentChange);
        dest.writeParcelable(this.isSelected, flags);
        dest.writeParcelable(this.isActive, flags);
    }

    public StockItemViewModel() {
    }

    protected StockItemViewModel(Parcel in) {
        this.id = in.readLong();
        this.isUp = in.readByte() != 0;
        this.symbol = in.readString();
        this.price = in.readString();
        this.change = in.readString();
        this.percentChange = in.readString();
        this.isSelected = in.readParcelable(ObservableBoolean.class.getClassLoader());
        this.isActive = in.readParcelable(ObservableBoolean.class.getClassLoader());
    }

    public static final Creator<StockItemViewModel> CREATOR = new Creator<StockItemViewModel>() {
        @Override
        public StockItemViewModel createFromParcel(Parcel source) {
            return new StockItemViewModel(source);
        }

        @Override
        public StockItemViewModel[] newArray(int size) {
            return new StockItemViewModel[size];
        }
    };
}
