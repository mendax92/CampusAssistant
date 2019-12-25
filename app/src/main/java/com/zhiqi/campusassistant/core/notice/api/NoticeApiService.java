package com.zhiqi.campusassistant.core.notice.api;

import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.notice.entity.NoticeInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ming on 2017/5/3.
 * 通知公告API
 */

public interface NoticeApiService {

    /**
     * 获取通知公告列表
     *
     * @param page
     * @return
     */
    @GET(HttpUrlConstant.GET_NOTICE_LIST)
    Observable<BaseResultData<BasePageData<NoticeInfo>>> getNoticeList(@Query("page_no") int page, @Query("page_size") int pageSize);
}
