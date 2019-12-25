package com.ming.base.http.client;

/**
 * Created by ming on 17-7-3.
 * 长连接retrofit工厂
 */

public class LongRetrofitFactory extends BaseRetrofitFactory {

    private static RetrofitClient client = null;

    private static final int LONG_CONNECT_TIME_OUT = 40;

    public LongRetrofitFactory(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public long timeout() {
        return LONG_CONNECT_TIME_OUT;
    }

    @Override
    public long connectTimeout() {
        return HTTP_TIME_OUT;
    }

    /**
     * 获取retrofit
     *
     * @param url
     * @return
     */
    public static RetrofitClient getRetrofitClient(String url) {
        if (client == null) {
            synchronized (LongRetrofitFactory.class) {
                if (client == null) {
                    RetrofitFactory factory = new LongRetrofitFactory(url);
                    client = factory.provideRetrofitClient();
                }
            }
        }
        return client;
    }
}
