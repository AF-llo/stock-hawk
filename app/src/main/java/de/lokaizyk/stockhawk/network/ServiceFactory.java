package de.lokaizyk.stockhawk.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import de.lokaizyk.stockhawk.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lars on 22.10.2016.
 */
public class ServiceFactory<T> {

    private Class<T> mServiceClass;

    private String mBaseUrl;

    private static OkHttpClient mClient;

    public ServiceFactory(Class<T> serviceClass, String baseUrl) {
        mServiceClass = serviceClass;
        mBaseUrl = baseUrl;
    }

    public T createService() {
        return getRetrofit().create(mServiceClass);
    }

    /**
     * Creates the base retrofit with baseUrl and GsonConverterFactory. To change the behaviour just
     * overrice this method in sub classes.
     */
    protected Retrofit getRetrofit() {
        if (mClient == null) {
            synchronized (ServiceFactory.class) {
                mClient = getOkHttpClient();
            }
        }
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(mClient)
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Override this method to change client for sub classes
     */
    protected OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .addNetworkInterceptor(new StethoInterceptor())
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}
