package de.lokaizyk.stockhawk.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MenuItem;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.databinding.ActivityStockDetailsBinding;
import de.lokaizyk.stockhawk.ui.fragments.StockDetailsFragment;

/**
 * Created by lars on 26.12.16.
 */

public class StockDetailsActivity extends BaseBindingActivity<ActivityStockDetailsBinding> {

    private static final String TAG = StockDetailsActivity.class.getSimpleName();

    public static final String EXTRA_SYMBOL = "extraSymbol";

    public static final String EXTRA_ENTRIES = "extraEntries";

    public static void start(Context context, String symbol) {
        if (context != null && !TextUtils.isEmpty(symbol)) {
            Intent intent = new Intent(context, StockDetailsActivity.class);
            intent.putExtra(EXTRA_SYMBOL, symbol);
            context.startActivity(intent);
        }
    }

    public static Intent getIntent(String symbol) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SYMBOL, symbol);
        return intent;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(StockDetailsFragment.TAG);
        if (fragment == null) {
            fragment = StockDetailsFragment.get(getIntent().getStringExtra(EXTRA_SYMBOL));
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, StockDetailsFragment.TAG)
                .commitAllowingStateLoss();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
