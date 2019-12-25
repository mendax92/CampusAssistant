package com.ming.base.http.client;

/**
 * Created by ming on 17-11-13.
 * 网络适配抽象类
 */

public abstract class AbsRetrofitAdapter implements RetrofitAdapter {

    private RetrofitClient mRetrofitClient;

    @Override
    public abstract RetrofitClient getRetrofitClient();

    @Override
    public <T> T getService(Class<T> apiClass) {
        if (mRetrofitClient == null) {
            synchronized (this) {
                if (mRetrofitClient == null) {
                    mRetrofitClient = getRetrofitClient();
                }
            }
        }
        return mRetrofitClient.getRetrofit().create(apiClass);
    }

    @Override
    public void cancelAll() {
        if (mRetrofitClient != null) {
            mRetrofitClient.getOkHttpClient().dispatcher().cancelAll();
        }
    }
}
