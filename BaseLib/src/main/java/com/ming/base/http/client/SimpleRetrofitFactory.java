package com.ming.base.http.client;

/**
 * Created by ming on 17-7-3.
 * 简单retrofit工厂
 */

public class SimpleRetrofitFactory extends BaseRetrofitFactory {

    private static RetrofitClient client = null;

    private SimpleRetrofitFactory(String baseUrl) {
        super(baseUrl);
    }

    /**
     * 获取retrofit
     *
     * @param url
     * @return
     */
    public static RetrofitClient getRetrofitClient(String url) {
        if (client == null) {
            synchronized (SimpleRetrofitFactory.class) {
                if (client == null) {
                    RetrofitFactory factory = new SimpleRetrofitFactory(url);
                    client = factory.provideRetrofitClient();
                }
            }
        }
        return client;
    }
}
