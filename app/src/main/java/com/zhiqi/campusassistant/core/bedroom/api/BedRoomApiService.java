package com.zhiqi.campusassistant.core.bedroom.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.bedroom.entity.BedChooseInfo;
import com.zhiqi.campusassistant.core.bedroom.entity.BedRoomInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ming on 2017/7/30.
 * 床位宿舍信息API
 */

public interface BedRoomApiService {

    /**
     * 获取宿舍列表
     *
     * @return
     */
    @GET(HttpUrlConstant.GET_BEDROOM_LIST)
    Observable<BaseResultData<BedRoomInfo>> getRoomList();

    /**
     * 获取宿舍选择信息
     *
     * @return
     */
    @GET(HttpUrlConstant.GET_BEDROOM_CHOOSE)
    Observable<BaseResultData<BedChooseInfo>> getBedChooseInfo(@Query("room_id") long roomId);

    /**
     * 选择床位
     *
     * @return
     */
    @POST(HttpUrlConstant.REQUEST_CHOOSE_ROOM_APPLY)
    @FormUrlEncoded
    Observable<BaseResultData> chooseBed(@Field("room_id") long roomId, @Field("bed_id") long bedId);
}
