package com.zhiqi.campusassistant.core.login.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Edmin on 2016/11/5 0005.
 */

public interface LoginApiService {

    /**
     * 登录
     *
     * @param userId
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.USER_LOGIN)
    Observable<BaseResultData<LoginUser>> login(@Field("account") String userId, @Field("password") String password);

    /**
     * 退出
     *
     * @return
     */
    @POST(HttpUrlConstant.USER_LOGOUT)
    Observable<BaseResultData> logout();
}
