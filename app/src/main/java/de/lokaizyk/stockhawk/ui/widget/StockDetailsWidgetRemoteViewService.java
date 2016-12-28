package de.lokaizyk.stockhawk.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by lars on 28.12.16.
 */

public class StockDetailsWidgetRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockDetailsViewsFactory(this);
    }
}
