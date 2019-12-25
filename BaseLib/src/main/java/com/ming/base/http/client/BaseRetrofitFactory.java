package com.ming.base.http.client;

import com.ming.base.retrofit2.converter.GsonConverterFactory;
import com.ming.base.util.GsonUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by ming on 17-7-3.
 * retrofit工厂基础类
 */

public class BaseRetrofitFactory implements RetrofitFactory {

    protected static final int HTTP_TIME_OUT = 20;

    private String baseUrl = null;

    public BaseRetrofitFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 是否支持ssl
     *
     * @return
     */
    public boolean supportSSL() {
        return true;
    }

    /**
     * 超时时间，秒
     *
     * @return
     */
    public long timeout() {
        return HTTP_TIME_OUT;
    }

    public long readTimeout() {
        return timeout();
    }

    public long writeTimeout() {
        return timeout();
    }

    public long connectTimeout() {
        return timeout();
    }

    @Override
    public RetrofitClient provideRetrofitClient() {
        OkHttpClient httpClient = okHttpClient();
        Converter.Factory factory = converterFactory();
        Retrofit.Builder builder = new Retrofit.Builder().client(httpClient).baseUrl(baseUrl);
        if (factory != null) {
            builder.addConverterFactory(factory);
        }
        Retrofit retrofit = builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync()).build();
        return new RetrofitClient(retrofit, httpClient);
    }

    /**
     * 拦截器
     *
     * @return
     */
    public Interceptor interceptor() {
        return null;
    }

    /**
     * 拦截器列表
     *
     * @return
     */
    public List<Interceptor> interceptors() {
        return null;
    }

    /**
     * body转换器
     *
     * @return
     */
    public Converter.Factory converterFactory() {
        // Gson转换器
        return GsonConverterFactory.create(GsonUtils.getGson());
    }

    /**
     * 提供OkHttpClient
     *
     * @return
     */
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(readTimeout(), TimeUnit.SECONDS)
                .writeTimeout(writeTimeout(), TimeUnit.SECONDS)
                .connectTimeout(connectTimeout(), TimeUnit.SECONDS);
        if (supportSSL()) {
            builder.sslSocketFactory(HttpSSLFactory.getInstance().getSSLSocketFactory(), HttpSSLFactory.getInstance().getTrustManager())
                    .hostnameVerifier(HttpSSLFactory.getInstance().getHostnameVerifier());
        }
        List<Interceptor> interceptors = interceptors();
        if (interceptors != null && !interceptors.isEmpty()) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        Interceptor interceptor = interceptor();
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }

        return builder.build();
    }


}
