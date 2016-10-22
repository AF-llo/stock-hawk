package de.lokaizyk.stockhawk.persistance;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Observer;

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

    // TODO: 22.10.16 umcomment when classes are created by GreenDAO

//    private DaoSession daoSession;

    private DbContentObservable contentObservable;

    private DbManager(Context context) {
        mContext = new WeakReference<>(context);
        contentObservable = new DbContentObservable();
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext.get(), DB_NAME);
//        SQLiteDatabase database = helper.getWritableDatabase();
//        daoSession = new DaoMaster(database).newSession();
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
        if (mContext == null) {
            throw new DbManagerNotInitializedException();
        }
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
        new Handler().post(() -> {
            if (contentObservable != null) {
                contentObservable.setChangedNow();
                contentObservable.notifyObservers();
            }
        });
    }

    private static class DbManagerNotInitializedException extends RuntimeException {
        public DbManagerNotInitializedException() {
            super("Please call DataManager.init(Context) before using an instance");
        }
    }

}
