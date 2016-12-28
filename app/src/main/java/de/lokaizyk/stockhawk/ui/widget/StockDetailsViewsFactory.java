package de.lokaizyk.stockhawk.ui.widget;

import android.content.Context;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import de.lokaizyk.stockhawk.R;
import de.lokaizyk.stockhawk.logic.StockProvider;
import de.lokaizyk.stockhawk.logic.model.StockItemViewModel;
import de.lokaizyk.stockhawk.persistance.DbManager;
import de.lokaizyk.stockhawk.ui.activities.StockDetailsActivity;

/**
 * Created by lars on 28.12.16.
 */

public class StockDetailsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<StockItemViewModel> mItems;

    private Context mContext;

    public StockDetailsViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        mItems = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        mItems.clear();
        final long identityToken = Binder.clearCallingIdentity();
        if (!DbManager.isInitialized()) {
            DbManager.init(mContext);
        }
        mItems.addAll(StockProvider.loadCurrentStocksFromDb());
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        mItems = null;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || mItems == null || mItems.get(position) == null) {
            return null;
        }
        StockItemViewModel item = mItems.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.stock_item);
        views.setTextViewText(R.id.stock_symbol, item.getSymbol());
        views.setTextViewText(R.id.bid_price, item.getPrice());
        views.setTextViewText(R.id.change, item.getChange());
        views.setInt(R.id.change, "setBackgroundResource", item.isUp() ? R.color.material_green_700 : R.color.material_red_700);

        views.setOnClickFillInIntent(R.id.stock_list_item, StockDetailsActivity.getIntent(item.getSymbol()));
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.stock_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
