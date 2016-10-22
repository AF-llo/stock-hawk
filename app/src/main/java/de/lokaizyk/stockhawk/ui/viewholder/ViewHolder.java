package de.lokaizyk.stockhawk.ui.viewholder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by lars on 03.10.16.
 */
public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

    protected ViewDataBinding mBinding;

    private int itemId;

    public ViewHolder(LayoutInflater inflater, ViewGroup container, int layoutId, int itemId) {
        this(DataBindingUtil.inflate(inflater, layoutId, container, false));
        this.itemId = itemId;
    }

    private ViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    public void bindItemItem(T item) {
        mBinding.setVariable(itemId, item);
        onItemBound(item);
    }

    protected abstract void onItemBound(T item);

}
