package de.lokaizyk.stockhawk.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.databinding.ActivityStockDetailsBinding;
import de.lokaizyk.stockhawk.logic.StockProvider;

/**
 * Created by lars on 26.12.16.
 */

public class StockDetailsActivity extends BaseBindingActivity<ActivityStockDetailsBinding> {

    private static final String TAG = StockDetailsActivity.class.getSimpleName();

    public static final String EXTRA_SYMBOL = "extraSymbol";

    public static final String EXTRA_ENTRIES = "extraEntries";

    public ArrayList<Entry> mEntries = new ArrayList<>();

    public static void start(Context context, String symbol) {
        if (context != null && !TextUtils.isEmpty(symbol)) {
            Intent intent = new Intent(context, StockDetailsActivity.class);
            intent.putExtra(EXTRA_SYMBOL, symbol);
            context.startActivity(intent);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mEntries = (ObservableArrayList) savedInstanceState.getParcelableArrayList(EXTRA_ENTRIES);
            getBinding().lineChart.setEntries(mEntries);
        } else {
            getBinding().lineChart.post(this::updateContent);
        }
    }

    @Override
    protected int getLayoutRessourceId() {
        return R.layout.activity_stock_details;
    }

    @Override
    protected void onBindingInitialized() {
        setTitle(getIntent().getExtras().getString(EXTRA_SYMBOL));
    }

    @Override
    protected void updateContent() {
        mEntries.clear();
        mEntries.addAll(StockProvider.loadStockEntriesFromDb(getIntent().getExtras().getString(EXTRA_SYMBOL)));
        getBinding().lineChart.setEntries(mEntries);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(EXTRA_ENTRIES, mEntries);
        super.onSaveInstanceState(outState);
    }
}
