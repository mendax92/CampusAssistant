package com.zhiqi.campusassistant.core.location.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.location.entity.CampusLocationInfo;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by ming on 2017/8/20.
 * 校园定位API
 */

public interface CampusLocationApiService {

    /**
     * 获取校园定位列表
     *
     * @return
     */
    @GET(HttpUrlConstant.GET_CAMPUS_LOCATION_LIST)
    Observable<BaseResultData<ArrayList<CampusLocationInfo>>> getCampusLocationList();
}
