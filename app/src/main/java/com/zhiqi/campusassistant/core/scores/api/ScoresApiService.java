package com.zhiqi.campusassistant.core.scores.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.scores.entity.CourseScoreInfo;
import com.zhiqi.campusassistant.core.scores.entity.ScoreDetail;
import com.zhiqi.campusassistant.core.scores.entity.SemesterInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ming on 2017/5/11.
 * 成绩查询API
 */

public interface ScoresApiService {

    /**
     * 查询学期信息
     *
     * @param studentNo
     * @return
     */
    @GET(HttpUrlConstant.GET_SCORES_SEMESTER)
    Observable<BaseResultData<SemesterInfo>> getSemesterInfo(@Query("student_no") String studentNo);

    /**
     * 查询课程成绩
     *
     * @param schoolYear
     * @param semester
     * @return
     */
    @GET(HttpUrlConstant.GET_COURSE_SCORES)
    Observable<BaseResultData<List<CourseScoreInfo>>> getCourseScores(@Query("school_year") String schoolYear, @Query("semester") String semester);


    /**
     * 查询成绩详情
     *
     * @param schoolYear
     * @param semester
     * @param courseName
     * @return
     */
    @GET(HttpUrlConstant.GET_SCORE_DETAIL)
    Observable<BaseResultData<ScoreDetail>> getScoreDetail(@Query("school_year") String schoolYear,
                                                           @Query("semester") String semester, @Query("course_name") String courseName);
}
