package com.ming.base.http.client;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by ming on 17-7-12.
 * retrofit 客户端bean
 */

public class RetrofitClient {

    private Retrofit retrofit;

    private OkHttpClient client;

    public RetrofitClient(Retrofit retrofit, OkHttpClient client) {
        this.retrofit = retrofit;
        this.client = client;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public OkHttpClient getOkHttpClient() {
        return client;
    }
}
