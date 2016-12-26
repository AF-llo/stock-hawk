package de.lokaizyk.stockhawk.ui.activities;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Observable;
import java.util.Observer;

import de.lokaizyk.stockhawk.persistance.DbManager;

/**
 * Created by lars on 22.10.2016
 */
public abstract class BaseBindingActivity<T extends ViewDataBinding> extends AppCompatActivity implements Observer{

    private T mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayoutRessourceId());
        onBindingInitialized();
        if (savedInstanceState == null) {
            updateContent();
        }
    }

    /**
     * Must return the LayoutID of the layout that should be set as content.
     */
    protected abstract int getLayoutRessourceId();

    /**
     * Must be implemented to set Variables to the created ViewDataBinding. This method is called in
     * onCreate() direct after DataBindingUtil.setContentView();
     *
     * @see #getBinding()
     */
    protected abstract void onBindingInitialized();

    public T getBinding() {
        return mBinding;
    }

    @Override
    protected void onPause() {
        super.onPause();
        DbManager.getInstance().unregisterForContentChange(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DbManager.getInstance().registerForContentChange(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        updateContent();
    }

    protected abstract void updateContent();
}
