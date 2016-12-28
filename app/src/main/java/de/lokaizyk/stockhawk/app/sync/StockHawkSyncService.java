package de.lokaizyk.stockhawk.app.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by lars on 28.12.16.
 */

public class StockHawkSyncService extends Service {

    private static final String TAG = StockHawkSyncService.class.getSimpleName();

    private static final Object sSyncAdapterLock = new Object();
    private static StockHawkSyncAdapter sStockHawkSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        synchronized (sSyncAdapterLock) {
            if (sStockHawkSyncAdapter == null) {
                sStockHawkSyncAdapter = new StockHawkSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sStockHawkSyncAdapter.getSyncAdapterBinder();
    }

}
