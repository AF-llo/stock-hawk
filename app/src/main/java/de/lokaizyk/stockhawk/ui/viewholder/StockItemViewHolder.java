package de.lokaizyk.stockhawk.ui.viewholder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import de.lokaizyk.stockhawk

        .BR;
import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.logic.model.StockItemViewModel;

/**
 * Created by lars on 23.12.16.
 */

public class StockItemViewHolder extends ViewHolder<StockItemViewModel> {

    public StockItemViewHolder(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container, R.layout.stock_item, BR.stockItem);
    }

    @Override
    protected void onItemBound(StockItemViewModel item) {

    }
}
