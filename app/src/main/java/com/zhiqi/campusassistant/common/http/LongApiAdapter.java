package com.zhiqi.campusassistant.common.http;

import com.ming.base.http.client.AbsRetrofitAdapter;
import com.ming.base.http.client.LongRetrofitFactory;
import com.ming.base.http.client.RetrofitClient;
import com.zhiqi.campusassistant.config.HttpUrlConstant;

/**
 * Created by ming on 17-11-13.
 * 长链接api适配器
 */

public class LongApiAdapter extends AbsRetrofitAdapter {

    @Override
    public RetrofitClient getRetrofitClient() {
        return LongRetrofitFactory.getRetrofitClient(HttpUrlConstant.BASE_URL);
    }
}
