package com.zhiqi.campusassistant.common.http;

import android.content.Context;

import com.ming.base.http.client.BaseRetrofitFactory;
import com.ming.base.http.client.LongRetrofitFactory;
import com.ming.base.http.client.RetrofitClient;
import com.zhiqi.campusassistant.config.HttpUrlConstant;

import okhttp3.Interceptor;
import retrofit2.Retrofit;

/**
 * Created by Edmin on 2016/8/30 0030.
 * Retrofit的实体类
 */
public class RestApiAdapter {

    private static RestApiAdapter instance;

    private RetrofitClient baseClient;

    private RetrofitClient longClient;

    private RestApiAdapter(Context context) {
        initRetrofit(context.getApplicationContext());
    }

    public static RestApiAdapter getInstance(final Context context) {
        if (instance == null) {
            synchronized (RestApiAdapter.class) {
                if (instance == null) {
                    instance = new RestApiAdapter(context);
                }
            }
        }
        return instance;
    }

    private void initRetrofit(Context context) {
        // 添加HttpInterceptor拦截器
        baseClient = provideBaseRetrofitFactory(context.getApplicationContext());
        longClient = LongRetrofitFactory.getRetrofitClient(HttpUrlConstant.BASE_URL);
    }

    private RetrofitClient provideBaseRetrofitFactory(final Context context) {
        return new BaseRetrofitFactory(HttpUrlConstant.BASE_URL) {

            @Override
            public boolean supportSSL() {
                return false;
            }

            @Override
            public Interceptor interceptor() {
                return new HttpInterceptor(context);
            }
        }.provideRetrofitClient();
    }

    public Retrofit getLongRetrofit() {
        return longClient != null ? longClient.getRetrofit() : null;
    }

    public void cancelAll() {
        if (baseClient != null) {
            baseClient.getOkHttpClient().dispatcher().cancelAll();
        }
        if (longClient != null) {
            longClient.getOkHttpClient().dispatcher().cancelAll();
        }
    }

    public Retrofit getRetrofit() {
        return baseClient.getRetrofit();
    }

    /**
     * 获取API
     *
     * @param apiClass
     * @param <T>
     * @return
     */
    public <T> T getService(Class<T> apiClass) {
        T t = getRetrofit().create(apiClass);
        return t;
    }

    /**
     * 获取长连接API
     *
     * @param apiClass
     * @param <T>
     * @return
     */
    public <T> T getLongConnectService(Class<T> apiClass) {
        return getLongRetrofit().create(apiClass);
    }
}
