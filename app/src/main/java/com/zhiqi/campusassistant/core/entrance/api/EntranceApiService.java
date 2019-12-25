package com.zhiqi.campusassistant.core.entrance.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.entrance.entity.EntranceInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by ming on 2017/8/16.
 * 报道指南API
 */

public interface EntranceApiService {

    /**
     * 获取报道指南列表
     *
     * @return
     */
    @GET(HttpUrlConstant.GET_ENTRANCE_LIST)
    Observable<BaseResultData<List<EntranceInfo>>> getEntranceList();
}
