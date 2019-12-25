package com.zhiqi.campusassistant.core.leave.api;

import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.leave.entity.LeaveApprovalParam;
import com.zhiqi.campusassistant.core.leave.entity.LeaveDetails;
import com.zhiqi.campusassistant.core.leave.entity.LeaveInfo;
import com.zhiqi.campusassistant.core.leave.entity.LeaveRequest;
import com.zhiqi.campusassistant.core.leave.entity.VacationData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ming on 2017/1/1.
 */

public interface LeaveApiService {

    /**
     * 请求请假记录
     *
     * @return
     */
    @GET(HttpUrlConstant.QUERY_LEAVE_RECORD)
    Observable<BaseResultData<BasePageData<LeaveInfo>>> queryLeaveRecord(@Query("applicant_id") long applicantId, @Query("page_no") int page, @Query("page_size") int pageSize);

    /**
     * 查询请假类型
     *
     * @return
     */
    @GET(HttpUrlConstant.QUERY_LEAVE_VACATION_TYPES)
    Observable<BaseResultData<VacationData>> queryVacationTypes();

    /**
     * 请假申请
     *
     * @param request
     * @return
     */
    @POST(HttpUrlConstant.REQUEST_LEAVE_APPLY)
    Observable<BaseResultData<List<String>>> requestLeaveApply(@Body LeaveRequest request);

    /**
     * 请假申请
     *
     * @param request
     * @return
     */
    @POST(HttpUrlConstant.REQUEST_UPDATE_LEAVE_APPLY)
    Observable<BaseResultData<List<String>>> updateLeaveApply(@Body LeaveRequest request);

    /**
     * 查询请假详情
     *
     * @param id
     * @return
     */
    @GET(HttpUrlConstant.QUERY_LEAVE_DETAILS)
    Observable<BaseResultData<LeaveDetails>> queryLeaveDetails(@Query("form_id") long id);

    /**
     * 请假操作
     *
     * @param formId
     * @param leaveActionId
     * @param comment
     * @return
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.DO_LEAVE_ACTION)
    Observable<BaseResultData> doAction(@Field("form_id") long formId, @Field("action_id") int leaveActionId, @Field("comment") String comment);

    /**
     * 查询请假审批列表
     *
     * @param param
     * @return
     */
    @POST(HttpUrlConstant.QUERY_LEAVE_APPROVAL_LIST)
    Observable<BaseResultData<BasePageData<LeaveInfo>>> queryLeaveApprovalList(@Body LeaveApprovalParam param);
}
