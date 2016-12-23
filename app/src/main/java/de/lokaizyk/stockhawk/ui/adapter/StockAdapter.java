package de.lokaizyk.stockhawk.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import de.lokaizyk.stockhawk.logic.model.StockItemViewModel;
import de.lokaizyk.stockhawk.ui.viewholder.StockItemViewHolder;
import de.lokaizyk.stockhawk.ui.viewholder.ViewHolder;

/**
 * Created by lars on 23.12.16.
 */

public class StockAdapter extends BaseBindingRecyclerAdapter<StockItemViewModel> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StockItemViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }
}
