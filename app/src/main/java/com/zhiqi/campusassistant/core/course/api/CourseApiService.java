package com.zhiqi.campusassistant.core.course.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.course.entity.CourseData;
import com.zhiqi.campusassistant.core.course.entity.CourseInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ming on 2016/12/16.
 * 课程表后台接口
 */

public interface CourseApiService {

    /**
     * 查询课程列表
     *
     * @return
     */
    @GET(HttpUrlConstant.QUERY_COURSE_LIST)
    Observable<BaseResultData<CourseData>> queryCourseList();

    /**
     * 查询课程详情
     *
     * @return
     */
    @GET(HttpUrlConstant.QUERY_COURSE_DETAIL)
    Observable<BaseResultData<CourseInfo>> queryCourseDetail(@Query("course_id") long courseId);
}
