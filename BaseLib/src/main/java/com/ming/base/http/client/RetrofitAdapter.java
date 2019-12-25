package com.ming.base.http.client;

/**
 * Created by ming on 17-11-13.
 * retrofit适配器
 */

public interface RetrofitAdapter {

    /**
     * 获取RetrofitClient对象，{@link RetrofitClient}
     *
     * @return
     */
    RetrofitClient getRetrofitClient();

    /**
     * 获取API
     *
     * @param apiClass
     * @param <T>
     * @return
     */
    <T> T getService(Class<T> apiClass);

    /**
     * 取消所有任务
     */
    void cancelAll();
}
