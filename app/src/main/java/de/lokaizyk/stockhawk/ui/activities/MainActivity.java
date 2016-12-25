package de.lokaizyk.stockhawk.ui.activities;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
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

    public ObservableList<StockItemViewModel> mStockItems = new ObservableArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isConnected = NetworkUtil.isConnected(this);
        if (savedInstanceState == null){
            // Run the initialize task service so that some stocks appear upon an empty database
            if (isConnected){
                StockIntentService.initialize(this);
            } else{
                NetworkUtil.noNetworkToast(this);
            }
        }
        if (isConnected) {
            StockTaskService.runPeriodic(this);
        }
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
        mStockItems.addAll(StockProvider.loadStocksFromDb());
    }

    @Override
    public void onItemClicked(StockItemViewModel item, int position) {
        Toast.makeText(this, "Clicked " + item.getSymbol(), Toast.LENGTH_SHORT).show();
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
            // TODO: 23.12.16 show dialog and add symbol after checked
            new MaterialDialog.Builder(this).title(R.string.symbol_search)
                    .content(R.string.content_test)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(R.string.input_hint, R.string.input_prefill, (dialog, input) -> {
                        // TODO: 25.12.16 load from db
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
