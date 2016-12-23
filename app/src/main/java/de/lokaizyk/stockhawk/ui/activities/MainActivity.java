package de.lokaizyk.stockhawk.ui.activities;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.lokaizyk.stockhawk.R;
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
        // TODO: 23.12.16 initialize task to update data

        updateContent();
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
            // TODO: 23.12.16  check if connected and show dialog
            Toast.makeText(this, "Add Symbol", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        }
    }
}
