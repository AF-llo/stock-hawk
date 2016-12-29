package de.lokaizyk.stockhawk.ui.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import de.lokaizyk.stockhawk.BR;
import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.logic.model.StockItemViewModel;

/**
 * Created by lars on 23.12.16.
 */

public class StockItemViewHolder extends ViewHolder<StockItemViewModel> {

    private StockItemViewModel mItem;

    public StockItemViewHolder(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container, R.layout.stock_item, BR.stockItem);
    }

    @Override
    protected void onItemBound(StockItemViewModel item) {
        mItem = item;
    }

    @Override
    public void onItemSelected() {
        mItem.isSelected.set(true);
    }

    @Override
    public void onItemClear() {
        mItem.isSelected.set(false);
    }

    @Override
    public void onItemActive(boolean isActive) {
        mItem.isActive.set(isActive);
    }
}
