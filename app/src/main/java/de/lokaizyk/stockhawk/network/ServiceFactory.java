package de.lokaizyk.stockhawk.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import de.lokaizyk.stockhawk.BuildConfig;
import de.lokaizyk.stockhawk.network.api.YahooApi;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
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
                .client(mClient)
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().setDateFormat(YahooApi.DATE_PATTERN)
                        .create()
                ))
                .build();
    }

    /**
     * Override this method to change client for sub classes
     */
    protected OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request.url();
                    url = url.newBuilder()
                            .addQueryParameter(YahooApi.PARAM_FORMAT, YahooApi.VALUE_FORMAT_JSON)
                            .addQueryParameter(YahooApi.PARAM_DIAGNOSTICS, YahooApi.VALUE_DIAGNOSTICS_FALSE)
                            .addQueryParameter(YahooApi.PARAM_ENV, YahooApi.VALUE_ENV_STORE)
                            .addQueryParameter(YahooApi.PARAM_CALLBACK, YahooApi.VALUE_CALLBACK_EMPTY)
                            .build();
                    Request.Builder requestBuilder = request.newBuilder()
                            .url(url);

                    Request changedRequest = requestBuilder.build();
                    return chain.proceed(changedRequest);
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}
