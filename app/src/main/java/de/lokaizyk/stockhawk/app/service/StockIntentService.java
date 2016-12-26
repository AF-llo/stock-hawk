package de.lokaizyk.stockhawk.app.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.TaskParams;

/**
 * Created by lars on 23.12.16.
 */

public class StockIntentService extends IntentService {

    private static final String EXTRAS_TAG = "tag";

    public StockIntentService(){
        this(StockIntentService.class.getName());
    }

    public StockIntentService(String name) {
        super(name);
    }

    public static void initialize(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, StockIntentService.class);
            intent.putExtra(EXTRAS_TAG, StockTaskService.INIT);
            context.startService(intent);
        }
    }

    public static void addSymbol(Context context, String symbol) {
        if (context != null) {
            Intent intent = new Intent(context, StockIntentService.class);
            intent.putExtra(EXTRAS_TAG, StockTaskService.ADD);
            intent.putExtra(StockTaskService.EXTRA_SYMBOL, symbol == null ? "" : symbol);
            context.startService(intent);
        }
    }

    @Override protected void onHandleIntent(Intent intent) {
        Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service");
        StockTaskService stockTaskService = new StockTaskService(this);
        Bundle args = new Bundle();
        if (intent.getStringExtra(EXTRAS_TAG).equals(StockTaskService.ADD)){
            args.putString(StockTaskService.EXTRA_SYMBOL, intent.getStringExtra(StockTaskService.EXTRA_SYMBOL));
        }
        stockTaskService.onRunTask(new TaskParams(intent.getStringExtra(EXTRAS_TAG), args));
    }

}
