package com.zhiqi.campusassistant.core.lost.api;

import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.lost.entity.LostApplyRequest;
import com.zhiqi.campusassistant.core.lost.entity.LostInfo;
import com.zhiqi.campusassistant.core.lost.entity.LostTypeVersion;
import com.zhiqi.campusassistant.core.lost.entity.MyLostInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ming on 2017/5/5.
 * 失物招领API
 */

public interface LostFoundApiService {

    /**
     * 查询失物招领信息
     *
     * @param lostType
     * @param page
     * @param pageSize
     * @return
     */
    @GET(HttpUrlConstant.GET_LOST_LIST)
    Observable<BaseResultData<BasePageData<LostInfo>>> getLostList(@Query("publish_type") int lostType, @Query("page_no") int page, @Query("page_size") int pageSize);

    /**
     * 获取我的失物招领列表
     *
     * @return
     */
    @GET(HttpUrlConstant.GET_MY_LOST)
    Observable<BaseResultData<BasePageData<MyLostInfo>>> getMyLostList(@Query("page_no") int page, @Query("page_size") int pageSize);

    /**
     * 获取失物类型
     *
     * @return
     */
    @GET(HttpUrlConstant.GET_LOST_TYPE)
    Observable<BaseResultData<LostTypeVersion>> getLostType();


    /**
     * 失物招领申请
     *
     * @param applyInfo
     * @return
     */
    @POST(HttpUrlConstant.REQUEST_LOST_APPLY)
    Observable<BaseResultData<List<String>>> requestLostApply(@Body LostApplyRequest applyInfo);

    /**
     * 失物招领操作
     *
     * @param id
     * @param action
     * @return
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.DO_LOST_ACTION)
    Observable<BaseResultData> doAction(@Field("form_id") int id, @Field("action_id") int action);
}
