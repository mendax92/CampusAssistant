package com.zhiqi.campusassistant.core.appsetting.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.appsetting.entity.UpgradeInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ming on 2017/5/21.
 * app设置API
 */

public interface AppSettingApiService {

    /**
     * 检测app升级
     *
     * @return
     */
    @GET(HttpUrlConstant.CHECK_UPGRADE)
    Observable<BaseResultData<UpgradeInfo>> checkUpgrade(@Query("version_id") int versionId, @Query("platform") String platform);
}
