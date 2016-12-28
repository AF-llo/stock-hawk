package de.lokaizyk.stockhawk.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;
import java.util.Observer;

import de.lokaizyk.stockhawk.app.sync.StockHawkSyncAdapter;
import de.lokaizyk.stockhawk.persistance.model.DaoMaster;
import de.lokaizyk.stockhawk.persistance.model.DaoSession;
import de.lokaizyk.stockhawk.persistance.model.DbStock;
import de.lokaizyk.stockhawk.persistance.model.DbStockDao;
import de.lokaizyk.stockhawk.util.DeviceUtil;

/**
 * Created by lars on 22.10.16.
 */

public class DbManager {

    private static final String TAG = DbManager.class.getSimpleName();

    private static final String DB_NAME = "stockHawk";

    /**
     * DataManager should be alive while the Application is alive. It is suggested to pass the application
     * context.
     */
    private static WeakReference<Context> mContext;

    private static DbManager mInstance;

    private DaoSession daoSession;

    private Handler mHandler;

    private DbContentObservable contentObservable;

    private DbManager(Context context) {
        mContext = new WeakReference<>(context);
        contentObservable = new DbContentObservable();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext.get(), DB_NAME);
        SQLiteDatabase database = helper.getWritableDatabase();
        daoSession = new DaoMaster(database).newSession();
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Must be called once before accessing
     * @param context ApplicationContext
     */
    public static synchronized void init(Context context) {
        if (mInstance == null) {
            mInstance = new DbManager(context);
        }
    }

    /**
     * Gets the Singleton instance of the DataManager
     */
    public static synchronized DbManager getInstance() {
        checkInitialization();
        return mInstance;
    }

    private static void checkInitialization() {
        if (!isInitialized()) {
            throw new DbManagerNotInitializedException();
        }
    }

    public static boolean isInitialized() {
        return mContext != null;
    }

    // methods

    public void registerForContentChange(Observer observer) {
        contentObservable.addObserver(observer);
    }

    public void unregisterForContentChange(Observer observer) {
        contentObservable.deleteObserver(observer);
    }

    public void notifyObserver() {
        Log.d(TAG, "notifyObserver");
        mHandler.post(() -> {
            if (contentObservable != null) {
                contentObservable.setChangedNow();
                contentObservable.notifyObservers();
            }
        });
    }

    public List<DbStock> loadAll() {
        return daoSession.getDbStockDao().loadAll();
    }

    public List<DbStock> loadAllCurrentStocks() {
        return daoSession.getDbStockDao()
                .queryBuilder()
                .where(DbStockDao.Properties.IsCurrent.eq(true))
                .orderAsc(DbStockDao.Properties.Symbol)
                .build()
                .list();
    }

    public DbStock loadCurrentStock(String symbol) {
        return daoSession.getDbStockDao()
                .queryBuilder()
                .where(DbStockDao.Properties.Symbol.eq(symbol), DbStockDao.Properties.IsCurrent.eq(true))
                .build()
                .unique();
    }

    public List<DbStock> loadStocks(String symbol) {
        QueryBuilder<DbStock> queryBuilder = daoSession.getDbStockDao()
                .queryBuilder()
                .where(DbStockDao.Properties.Symbol.eq(symbol));
        if (DeviceUtil.isRTL(Locale.getDefault())) {
            queryBuilder.orderDesc(DbStockDao.Properties.Id);
        }
        return queryBuilder.build()
                .list();
    }

    public void insertOrReplace(List<DbStock> stocks) {
        DbStockDao stockDao = daoSession.getDbStockDao();
        for (DbStock stock : stocks) {
            if (stock != null) {
                DbStock currentStock = loadCurrentStock(stock.getSymbol());
                if (currentStock != null) {
                    currentStock.setIsCurrent(false);
                    stockDao.insertOrReplace(currentStock);
                }
                stock.setIsCurrent(true);
                stockDao.insertOrReplace(stock);
            }
        }
    }

    public void removeSymbol(String symbol) {
        DbStockDao stockDao = daoSession.getDbStockDao();
        List<DbStock> stocks = loadStocks(symbol);
        for (DbStock stock : stocks) {
            stockDao.delete(stock);
        }
        if (mContext != null && mContext.get() != null) {
            StockHawkSyncAdapter.updateWidgets(mContext.get());
        }
    }

    private static class DbManagerNotInitializedException extends RuntimeException {
        public DbManagerNotInitializedException() {
            super("Please call DataManager.init(Context) before using an instance");
        }
    }

}
