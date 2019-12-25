package com.zhiqi.campusassistant.core.scores.presenter;

import android.content.Context;

import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.presenter.CachePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.scores.api.ScoresApiService;
import com.zhiqi.campusassistant.core.scores.entity.CourseScoreInfo;
import com.zhiqi.campusassistant.core.scores.entity.ScoreDetail;
import com.zhiqi.campusassistant.core.scores.entity.SemesterInfo;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/5/12.
 * 成绩查询
 */

public class ScoresPresenter extends CachePresenter {

    private ScoresApiService mApiService;

    public ScoresPresenter(Context context, ScoresApiService apiService, AppDaoSession daoSession) {
        super(context, daoSession.getRxDao(daoSession.getCacheDataDao()));
        this.mApiService = apiService;
    }

    /**
     * 加载学期信息
     *
     * @param loadView
     */
    public void loadSemesterInfo(ILoadView<SemesterInfo> loadView) {
        LoginUser loginUser = LoginManager.getInstance().getLoginUser();
        if (loginUser == null) {
            return;
        }
        Observable<BaseResultData<SemesterInfo>> observable = mApiService.getSemesterInfo(loginUser.getUser_no());
        handleCache(CacheKey.SCORES_SEMESTER, SemesterInfo.class, observable, loadView);
    }

    /**
     * 查询
     *
     * @param schoolYear
     * @param semester
     * @param loadView
     */
    public void loadCourseScores(String schoolYear, String semester, ILoadView<List<CourseScoreInfo>> loadView) {
        Observable<BaseResultData<List<CourseScoreInfo>>> observable = mApiService.getCourseScores(schoolYear, semester);
        subscribe(observable, loadView);
    }

    /**
     * 查询成绩详情
     *
     * @param schoolYear
     * @param semester
     * @param courseName
     * @param loadView
     */
    public void loadScoreDetail(String schoolYear, String semester, String courseName, ILoadView<ScoreDetail> loadView) {
        Observable<BaseResultData<ScoreDetail>> observable = mApiService.getScoreDetail(schoolYear, semester, courseName);
        subscribe(observable, loadView);
    }
}
