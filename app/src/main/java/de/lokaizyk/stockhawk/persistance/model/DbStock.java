package de.lokaizyk.stockhawk.persistance.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lars on 26.12.16.
 */
@Entity
public class DbStock {

    @Transient
    private static final String SYMBOL = "symbol";

    @Transient
    private static final String PERCENT_CHANGE = "percent_change";

    @Transient
    private static final String CHANGE = "change";

    @Transient
    private static final String BID_PRICE = "bid_price";

    @Transient
    private static final String CREATED = "created";

    @Transient
    private static final String IS_UP = "is_up";

    @Transient
    private static final String IS_CURRENT = "is_current";

    @Id
    private Long id;

    @Property(nameInDb = SYMBOL)
    private String symbol;

    @Property(nameInDb = PERCENT_CHANGE)
    private String percentChange;

    @Property(nameInDb = CHANGE)
    private String change;

    @Property(nameInDb = BID_PRICE)
    private String bidprice;

    @Property(nameInDb = CREATED)
    private long created;

    @Property(nameInDb = IS_UP)
    private boolean isUp;

    @Property(nameInDb = IS_CURRENT)
    private boolean isCurrent;

    @Generated(hash = 2042582362)
    public DbStock(Long id, String symbol, String percentChange, String change,
            String bidprice, long created, boolean isUp, boolean isCurrent) {
        this.id = id;
        this.symbol = symbol;
        this.percentChange = percentChange;
        this.change = change;
        this.bidprice = bidprice;
        this.created = created;
        this.isUp = isUp;
        this.isCurrent = isCurrent;
    }

    @Generated(hash = 1272532665)
    public DbStock() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPercentChange() {
        return this.percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public String getChange() {
        return this.change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getBidprice() {
        return this.bidprice;
    }

    public void setBidprice(String bidprice) {
        this.bidprice = bidprice;
    }

    public long getCreated() {
        return this.created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public boolean getIsUp() {
        return this.isUp;
    }

    public void setIsUp(boolean isUp) {
        this.isUp = isUp;
    }

    public boolean getIsCurrent() {
        return this.isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
}
