package com.zhiqi.campusassistant.core.jpush.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by ming on 2017/3/22.
 * JPush api
 */

public interface JPushApiService {

    /**
     * 设置用户的JPush registrationid
     *
     * @param registrationId
     * @return
     */
    @POST(HttpUrlConstant.PUSH_USER_REGISTRATIONID)
    @FormUrlEncoded
    Observable<BaseResultData> pushUserRegistrationId(@Field("registration_id") String registrationId);
}
