package de.lokaizyk.stockhawk.ui.fragments;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.data.Entry;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.databinding.FragmentStockDetailsBinding;
import de.lokaizyk.stockhawk.logic.StockProvider;
import de.lokaizyk.stockhawk.ui.activities.StockDetailsActivity;

import static de.lokaizyk.stockhawk.ui.activities.StockDetailsActivity.EXTRA_ENTRIES;

/**
 * Created by lars on 28.12.16.
 */

public class StockDetailsFragment extends BaseBindingFragment<FragmentStockDetailsBinding> {

    public static final String TAG = StockDetailsFragment.class.getSimpleName();

    public ObservableArrayList<Entry> mEntries = new ObservableArrayList<>();

    public static Fragment get(String symbol) {
        Fragment fragment = new StockDetailsFragment();
        if (symbol != null) {
            Bundle arguments = new Bundle();
            arguments.putString(StockDetailsActivity.EXTRA_SYMBOL, symbol);
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_stock_details;
    }

    @Override
    protected void onBindingInitialized() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(TAG, "retain savedInstanceState");
            mEntries = (ObservableArrayList) savedInstanceState.getParcelableArrayList(EXTRA_ENTRIES);
            getBinding().lineChart.setEntries(mEntries);
        } else {
            getBinding().lineChart.post(this::updateContent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(EXTRA_ENTRIES, mEntries);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateContent() {
        mEntries.clear();
        mEntries.addAll(StockProvider.loadStockEntriesFromDb(getArguments().getString(StockDetailsActivity.EXTRA_SYMBOL)));
        getBinding().lineChart.setEntries(mEntries);
    }
}
