package com.zhiqi.campusassistant.core.message.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.message.entity.ModuleInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by ming on 2016/12/20.
 */

public interface MessageApiService {

    @GET(HttpUrlConstant.QUERY_MESSAGE_LIST)
    Observable<BaseResultData<List<ModuleInfo>>> queryMessageList();
}
