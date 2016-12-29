package de.lokaizyk.stockhawk.ui.fragments;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.app.sync.StockHawkSyncAdapter;
import de.lokaizyk.stockhawk.databinding.FragmentStocksBinding;
import de.lokaizyk.stockhawk.logic.StockProvider;
import de.lokaizyk.stockhawk.logic.model.StockItemViewModel;
import de.lokaizyk.stockhawk.ui.adapter.BaseBindingRecyclerAdapter;
import de.lokaizyk.stockhawk.ui.adapter.StockAdapter;
import de.lokaizyk.stockhawk.util.DeviceUtil;

/**
 * Created by lars on 28.12.16.
 */

public class StocksFragment extends BaseBindingFragment<FragmentStocksBinding> implements BaseBindingRecyclerAdapter.OnItemClickListener<StockItemViewModel> {

    public static final String EXTRAS_STOCKS = "extraStocks";
    private static final String EXTRAS_ACTIVE_ITEM = "extraActiveItem";

    public ObservableArrayList<StockItemViewModel> mStockItems = new ObservableArrayList<>();

    private StockItemViewModel activeItem;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_stocks;
    }

    @Override
    protected void onBindingInitialized() {
        getBinding().setStocksFragment(this);
        getBinding().stockList.setLayoutManager(new LinearLayoutManager(getContext()));
        getBinding().stockList.setAdapter(new StockAdapter());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean isConnected = DeviceUtil.isConnected(getContext());
        if (savedInstanceState == null){
            updateContent();
            // Run the initialize task service so that some stocks appear upon an empty database
            if (!isConnected){
                DeviceUtil.noNetworkToast(getContext());
            }
        } else {
            mStockItems = (ObservableArrayList) savedInstanceState.getParcelableArrayList(EXTRAS_STOCKS);
            activeItem = savedInstanceState.getParcelable(EXTRAS_ACTIVE_ITEM);
        }
        StockHawkSyncAdapter.initializeSyncAdapter(getContext());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(EXTRAS_STOCKS, mStockItems);
        outState.putParcelable(EXTRAS_ACTIVE_ITEM, activeItem);
        super.onSaveInstanceState(outState);
    }

    public void selectItem(String symbol) {
        if (activeItem != null) {
            symbol = activeItem.getSymbol();
        }
        StockItemViewModel item = getItem(symbol);
        if (item != null) {
            onItemClicked(item);
        }
    }

    @Override
    public void onItemClicked(StockItemViewModel item) {
        if (DeviceUtil.isTablet(getContext())) {
            if (!item.isActive.get()) {
                if (activeItem != null) {
                    activeItem.isActive.set(false);
                }
                activeItem = item;
                activeItem.isActive.set(true);
                ((Callback)getActivity()).onStockSelected(item);
            }
        }
    }

    @Override
    public void onItemDeleted(StockItemViewModel item) {
        if (activeItem != null) {
            activeItem.isActive.set(false);
            activeItem = null;
        }
        ((Callback)getActivity()).onStockDeleted(item);
    }

    private StockItemViewModel getItem(String symbol) {
        for (StockItemViewModel stockItem : mStockItems) {
            if (stockItem.getSymbol().equals(symbol)) {
                return stockItem;
            }
        }
        return null;
    }

    @Override
    public void updateContent() {
        mStockItems.clear();
        mStockItems.addAll(StockProvider.loadCurrentStocksFromDb());
        if (activeItem != null) {
            selectItem(activeItem.getSymbol());
        }
    }

    public interface Callback {
        void onStockSelected(StockItemViewModel stockItem);
        void onStockDeleted(StockItemViewModel stockItem);
    }
}
