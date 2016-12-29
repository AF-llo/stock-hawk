package de.lokaizyk.stockhawk.util;

/**
 * Created by lars on 29.12.16.
 */

public interface ItemTouchHelperAdapter {

    void onItemSelected();

    void onItemClear();

    void onItemActive(boolean isActive);

}
