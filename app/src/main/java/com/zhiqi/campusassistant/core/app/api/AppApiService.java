package com.zhiqi.campusassistant.core.app.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.app.entity.BannerInfos;
import com.zhiqi.campusassistant.core.app.entity.CheckAppGoWhere;
import com.zhiqi.campusassistant.core.app.entity.ModuleCategory;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ming on 2017/3/11.
 * 应用API接口
 */

public interface AppApiService {

    /**
     * 查询应用列表信息
     *
     * @return
     */
    @GET(HttpUrlConstant.QUERY_APP_LIST)
    Observable<BaseResultData<List<ModuleCategory>>> queryAppList();

    /**
     * 获取banner
     *
     * @return
     */
    @GET(HttpUrlConstant.ADVERTISE_GETADVER)
    Observable<BaseResultData<List<BannerInfos.BannerInfo>>> getAdver(@Query("flag")int  flag);
    /**
     * 获取banner
     *
     * @return
     */
    @GET(HttpUrlConstant.APP_REDIRECT)
    Observable<BaseResultData<CheckAppGoWhere>> checkAppToWhere(@Query("interfacename")String  interfacename);
}
