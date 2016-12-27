package de.lokaizyk.stockhawk.ui.activities;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.app.service.StockIntentService;
import de.lokaizyk.stockhawk.app.service.StockTaskService;
import de.lokaizyk.stockhawk.databinding.ActivityMainBinding;
import de.lokaizyk.stockhawk.logic.StockProvider;
import de.lokaizyk.stockhawk.logic.model.StockItemViewModel;
import de.lokaizyk.stockhawk.ui.adapter.BaseBindingRecyclerAdapter;
import de.lokaizyk.stockhawk.ui.adapter.StockAdapter;
import de.lokaizyk.stockhawk.util.NetworkUtil;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> implements BaseBindingRecyclerAdapter.OnItemClickListener<StockItemViewModel> {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRAS_STOCKS = "extraStocks";

    public ObservableArrayList<StockItemViewModel> mStockItems = new ObservableArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isConnected = NetworkUtil.isConnected(this);
        if (savedInstanceState == null){
            updateContent();
            // Run the initialize task service so that some stocks appear upon an empty database
            if (isConnected){
                StockIntentService.initialize(this);
            } else{
                NetworkUtil.noNetworkToast(this);
            }
        } else {
            mStockItems = (ObservableArrayList) savedInstanceState.getParcelableArrayList(EXTRAS_STOCKS);
        }
        if (isConnected) {
            StockTaskService.runPeriodic(this);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected int getLayoutRessourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onBindingInitialized() {
        getBinding().setMainActivity(this);
        getBinding().stockList.setLayoutManager(new LinearLayoutManager(this));
        getBinding().stockList.setAdapter(new StockAdapter());
    }

    @Override
    protected void updateContent() {
        mStockItems.clear();
        mStockItems.addAll(StockProvider.loadCurrentStocksFromDb());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(EXTRAS_STOCKS, mStockItems);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClicked(StockItemViewModel item, int position) {
        StockDetailsActivity.start(this, item.getSymbol());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_change_units){
            StockProvider.toggleShowPercent();
            updateContent();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addSymbol() {
        if (NetworkUtil.isConnected(this)) {
            new MaterialDialog.Builder(this).title(R.string.symbol_search)
                    .content(R.string.content_test)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(R.string.input_hint, R.string.input_prefill, (dialog, input) -> {
                        String symbol = input.toString();
                        if (StockProvider.loadStockFromDb(symbol) != null) {
                            Toast.makeText(MainActivity.this, getString(R.string.stock_exists), Toast.LENGTH_LONG).show();
                        } else {
                            StockIntentService.addSymbol(this, symbol);
                        }
                    })
                    .show();
        } else {
            NetworkUtil.noNetworkToast(this);
        }
    }
}
