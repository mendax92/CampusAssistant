package com.ming.base.http.client;

/**
 * Created by ming on 17-7-3.
 * retrofit工厂
 */

public interface RetrofitFactory {

    /**
     * 提供retrofit
     *
     * @return
     */
    RetrofitClient provideRetrofitClient();
}
