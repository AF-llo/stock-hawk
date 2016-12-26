package de.lokaizyk.stockhawk.util;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import de.lokaizyk.stockhawk.ui.adapter.ItemDismissAdapter;

/**
 * Created by sam_chordas on 10/6/15.
 * credit to Paul Burke (ipaulpro)
 * this class enables swipe to delete in RecyclerView
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback{

  private final ItemDismissAdapter mAdapter;

  public SimpleItemTouchHelperCallback(ItemDismissAdapter adapter){
    mAdapter = adapter;
  }

  @Override
  public boolean isItemViewSwipeEnabled(){
    return true;
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){
    final int dragFlags = 0;
    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
    return makeMovementFlags(dragFlags, swipeFlags);
  }

  @Override
  public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder sourceViewHolder, RecyclerView.ViewHolder targetViewHolder){
    return true;
  }

  @Override
  public void onSwiped(RecyclerView.ViewHolder viewHolder, int i){
    mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
  }
}
