package de.lokaizyk.stockhawk.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.app.sync.StockHawkSyncAdapter;
import de.lokaizyk.stockhawk.databinding.ActivityMainBinding;
import de.lokaizyk.stockhawk.logic.StockProvider;
import de.lokaizyk.stockhawk.logic.model.StockItemViewModel;
import de.lokaizyk.stockhawk.ui.fragments.StockDetailsFragment;
import de.lokaizyk.stockhawk.ui.fragments.StocksFragment;
import de.lokaizyk.stockhawk.util.DeviceUtil;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> implements StocksFragment.Callback {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static Intent getIntent(Context context, String symbol) {
        Intent intent = new Intent(context, MainActivity.class);
        if (intent != null) {
            intent.putExtra(StockDetailsActivity.EXTRA_SYMBOL, symbol);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(StockDetailsActivity.EXTRA_SYMBOL)) {
            String symbol = extras.getString(StockDetailsActivity.EXTRA_SYMBOL);
            if (!TextUtils.isEmpty(symbol)) {
                StockItemViewModel selectedItem = new StockItemViewModel();
                selectedItem.setSymbol(symbol);
                onStockSelected(selectedItem);
            }
        }
    }

    @Override
    protected int getLayoutRessourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onBindingInitialized() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            addSymbol();
            return true;
        }
        if (id == R.id.action_change_units){
            StockProvider.toggleShowPercent();
            StocksFragment fragment = (StocksFragment) getSupportFragmentManager().findFragmentById(R.id.stocks_fragment);
            fragment.updateContent();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addSymbol() {
        if (DeviceUtil.isConnected(this)) {
            new MaterialDialog.Builder(this).title(R.string.symbol_search)
                    .content(R.string.content_test)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(R.string.input_hint, R.string.input_prefill, (dialog, input) -> {
                        String symbol = input.toString();
                        if (StockProvider.loadStockFromDb(symbol) != null) {
                            Toast.makeText(MainActivity.this, getString(R.string.stock_exists), Toast.LENGTH_LONG).show();
                        } else {
                            StockHawkSyncAdapter.addSymbol(this, symbol);
                        }
                    })
                    .show();
        } else {
            DeviceUtil.noNetworkToast(this);
        }
    }

    @Override
    public void onStockSelected(StockItemViewModel stockItem) {
        if (DeviceUtil.isTablet(this)) {
            Fragment fragment = StockDetailsFragment.get(stockItem.getSymbol());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, StockDetailsFragment.TAG)
                    .commit();
        } else {
            StockDetailsActivity.start(this, stockItem.getSymbol());
        }
    }
}
