package de.lokaizyk.stockhawk.ui.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import de.lokaizyk.stockhawk.persistance.DbManager;


/**
 * Created by lars on 22.10.2016.
 */
public abstract class BaseBindingFragment<T extends ViewDataBinding> extends Fragment implements Observer {

    private T mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        onBindingInitialized();
        return mBinding.getRoot();
    }

    /**
     * Must return the LayoutID of the layout that should be set as content.
     */
    protected abstract int getLayoutId();

    /**
     * Must be implemented to set Variables to the created ViewDataBinding. This method is called in
     * onCreateView() immediatelly after ViewDataBinding is inflated is inflated;
     *
     * @see #getBinding()
     */
    protected abstract void onBindingInitialized();

    public T getBinding() {
        return mBinding;
    }

    @Override
    public void update(Observable o, Object arg) {
        updateContent();
    }

    public abstract void updateContent();

    @Override
    public void onResume() {
        super.onResume();
        DbManager.getInstance().registerForContentChange(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        DbManager.getInstance().unregisterForContentChange(this);
    }
}
