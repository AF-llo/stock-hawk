package de.lokaizyk.stockhawk.ui.activities;

import android.os.Bundle;
import android.util.Log;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.databinding.ActivityMainBinding;
import de.lokaizyk.stockhawk.network.api.YahooApi;
import de.lokaizyk.stockhawk.network.api.YahooApiFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new YahooApiFactory().createService().loadStocks(
                new YahooApi.SymbolBuilder()
                .addInitialSymbols()
                .buildSymbolQueryValue()
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        queryResponse -> {
                            Log.d(TAG, "loaded");
                        },
                        Throwable::printStackTrace
                );
    }

    @Override
    protected int getLayoutRessourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onBindingInitialized() {

    }
}
