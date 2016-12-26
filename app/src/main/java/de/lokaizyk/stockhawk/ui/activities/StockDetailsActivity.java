package de.lokaizyk.stockhawk.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.databinding.ActivityStockDetailsBinding;
import de.lokaizyk.stockhawk.persistance.DbManager;
import de.lokaizyk.stockhawk.persistance.model.DbStock;

/**
 * Created by lars on 26.12.16.
 */

public class StockDetailsActivity extends BaseBindingActivity<ActivityStockDetailsBinding> {

    private static final String TAG = StockDetailsActivity.class.getSimpleName();

    public static final String EXTRA_SYMBOL = "extraSymbol";

    public static void start(Context context, String symbol) {
        if (context != null && !TextUtils.isEmpty(symbol)) {
            Intent intent = new Intent(context, StockDetailsActivity.class);
            intent.putExtra(EXTRA_SYMBOL, symbol);
            context.startActivity(intent);
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
        // TODO: 26.12.16 add provider to load details
        List<DbStock> stocks = DbManager.getInstance().loadStocks(getIntent().getExtras().getString(EXTRA_SYMBOL));
        Log.d(TAG, "loaded " + stocks.size() + " items");
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
}
