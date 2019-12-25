package com.zhiqi.campusassistant.core.security.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by ming on 2016/11/21.
 */

public interface HttpTimestampService {

    @GET(HttpUrlConstant.QUERY_SERVER_TIME)
    Observable<BaseResultData<Map<String, Long>>> getServerTime();
}
