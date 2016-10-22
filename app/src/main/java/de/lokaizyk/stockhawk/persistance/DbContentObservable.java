package de.lokaizyk.stockhawk.persistance;

import java.util.Observable;

/**
 * Created by lars on 22.10.16.
 */

public class DbContentObservable extends Observable {

    public void setChangedNow() {
        setChanged();
    }

}
