package com.zhiqi.campusassistant.common.http;

import android.content.Context;

import com.ming.base.http.client.AbsRetrofitAdapter;
import com.ming.base.http.client.BaseRetrofitFactory;
import com.ming.base.http.client.RetrofitClient;
import com.ming.base.util.Log;
import com.zhiqi.campusassistant.config.HttpUrlConstant;

import okhttp3.Interceptor;

/**
 * Created by ming on 17-11-13.
 * http基础api适配器
 */

public class CloupusApiAdapter extends AbsRetrofitAdapter {

    private static final String TAG = "CloupusApiAdapter";

    private Context mContext;

    public CloupusApiAdapter(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public RetrofitClient getRetrofitClient() {
        // 添加HttpInterceptor拦截器
        Log.i(TAG, "init retrofit...");
        return provideBaseRetrofitFactory(mContext);
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
}
