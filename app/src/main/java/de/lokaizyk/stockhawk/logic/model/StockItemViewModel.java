package de.lokaizyk.stockhawk.logic.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lars on 23.12.16.
 */

public class StockItemViewModel implements Parcelable {

    private boolean isUp = false;

    private String symbol = "";

    private String price = "";

    private String change = "";

    private String percentChange = "";

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
        dest.writeByte(this.isUp ? (byte) 1 : (byte) 0);
        dest.writeString(this.symbol);
        dest.writeString(this.price);
        dest.writeString(this.change);
        dest.writeString(this.percentChange);
    }

    public StockItemViewModel() {
    }

    protected StockItemViewModel(Parcel in) {
        this.isUp = in.readByte() != 0;
        this.symbol = in.readString();
        this.price = in.readString();
        this.change = in.readString();
        this.percentChange = in.readString();
    }

    public static final Parcelable.Creator<StockItemViewModel> CREATOR = new Parcelable.Creator<StockItemViewModel>() {
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
