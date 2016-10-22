package de.lokaizyk.stockhawk.ui.activities;

import android.os.Bundle;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.databinding.ActivityMainBinding;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected int getLayoutRessourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onBindingInitialized() {

    }
}
