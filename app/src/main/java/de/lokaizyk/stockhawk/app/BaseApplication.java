package de.lokaizyk.stockhawk.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by lars on 22.10.16.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
