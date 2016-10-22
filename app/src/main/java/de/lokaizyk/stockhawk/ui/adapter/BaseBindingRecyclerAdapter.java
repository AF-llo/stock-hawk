package de.lokaizyk.stockhawk.ui.adapter;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import de.lokaizyk.stockhawk.ui.viewholder.ViewHolder;

/**
 * Created by lars on 03.10.16.
 */

public abstract class BaseBindingRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private ObservableList<T> mItems = new ObservableArrayList<>();

    private OnItemClickListener<T> mOnItemClickListener;

    public void setItems(ObservableList<T> items) {
        if (items != null) {
            mItems = items;
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final T item = mItems.get(position);
        holder.bindItemItem(item);
        if (mOnItemClickListener != null){
            View view = holder.itemView;
            view.setOnClickListener(v -> mOnItemClickListener.onItemClicked(item, holder.getAdapterPosition()));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface OnItemClickListener<T> {
        void onItemClicked(T item, int position);
    }
}
