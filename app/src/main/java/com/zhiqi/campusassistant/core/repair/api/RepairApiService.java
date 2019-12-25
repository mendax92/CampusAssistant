package com.zhiqi.campusassistant.core.repair.api;

import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BasePageParam;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplicantInfo;
import com.zhiqi.campusassistant.core.repair.entity.RepairApplyRequest;
import com.zhiqi.campusassistant.core.repair.entity.RepairApprovalParam;
import com.zhiqi.campusassistant.core.repair.entity.RepairDetails;
import com.zhiqi.campusassistant.core.repair.entity.RepairDictionary;
import com.zhiqi.campusassistant.core.repair.entity.RepairInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ming on 2017/1/14.
 * 维修API接口
 */

public interface RepairApiService {

    /**
     * 查询维修记录
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET(HttpUrlConstant.QUERY_REPAIR_RECORD)
    Observable<BaseResultData<BasePageData<RepairInfo>>> queryRecordList(@Query("page_no") int pageNo, @Query("page_size") int pageSize);

    /**
     * 查询维修相关的数据字典
     *
     * @return
     */
    @GET(HttpUrlConstant.QUERY_REPAIR_DATA_DICTIONARY)
    Observable<BaseResultData<RepairDictionary>> queryDataDictionary();


    /**
     * 维修请求
     *
     * @param requestBody
     * @return
     */
    @POST(HttpUrlConstant.REQUEST_REPAIR_APPLY)
    Observable<BaseResultData<List<String>>> requestRepairApply(@Body RepairApplyRequest requestBody);


    /**
     * 维修请求
     *
     * @param requestBody
     * @return
     */
    @POST(HttpUrlConstant.REQUEST_UPDATE_REPAIR_APPLY)
    Observable<BaseResultData<List<String>>> updateRepairApply(@Body RepairApplyRequest requestBody);

    /**
     * 维修详情
     *
     * @param id
     * @param role
     * @return
     */
    @GET(HttpUrlConstant.QUERY_REPAIR_DETAILS)
    Observable<BaseResultData<RepairDetails>> queryRepairDetails(@Query("form_id") long id, @Query("role") int role);

    /**
     * 维修操作
     *
     * @param formId
     * @param repairActionId
     * @param comment
     * @return
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.DO_REPAIR_ACTIONS)
    Observable<BaseResultData> doAction(@Field("form_id") long formId, @Field("action_id") int repairActionId, @Field("comment") String comment);

    /**
     * 查询维修审批列表
     *
     * @param param
     * @return
     */
    @POST(HttpUrlConstant.QUERY_REPAIR_APPROVAL_LIST)
    Observable<BaseResultData<BasePageData<RepairInfo>>> queryRepairApprovalList(@Body RepairApprovalParam param);

    /**
     * 查询报修人列表
     *
     * @param param
     * @return
     */
    @POST(HttpUrlConstant.QUERY_REPAIR_APPLICANT_LIST)
    Observable<BaseResultData<BasePageData<RepairApplicantInfo>>> queryRepairUserInfo(@Body BasePageParam param);

}
