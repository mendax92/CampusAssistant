package com.zhiqi.campusassistant.core.usercenter.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.usercenter.entity.FeedbackRequest;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ming on 2017/3/17.
 * 用户中心API
 */

public interface UserCenterApiService {

    /**
     * 用户反馈
     *
     * @param request
     * @return
     */
    @POST(HttpUrlConstant.USER_CENTER_FEEDBACK)
    Observable<BaseResultData> feedback(@Body FeedbackRequest request);
}
