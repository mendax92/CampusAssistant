package com.zhiqi.campusassistant.core.course.presenter;

import android.content.Context;

import com.ming.base.greendao.rx2.RxDao;
import com.ming.base.http.HttpManager;
import com.ming.base.rx2.SimpleObserver;
import com.ming.base.util.Log;
import com.ming.base.util.TimeUtil;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.presenter.SimplePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.core.course.api.CourseApiService;
import com.zhiqi.campusassistant.core.course.entity.CourseData;
import com.zhiqi.campusassistant.core.course.entity.CourseInfo;
import com.zhiqi.campusassistant.core.course.entity.UICourseData;
import com.zhiqi.campusassistant.core.course.entity.UICourseInfo;
import com.zhiqi.campusassistant.core.course.entity.WeekType;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by ming on 2016/12/16.
 * 课程表presenter
 */

public class CoursePresenter extends SimplePresenter {

    private static final String TAG = "CoursePresenter";

    private static final int MIN_LESSONS = 16;

    private CourseApiService mApiService;

    private RxDao<CacheData, String> cacheDao;

    public CoursePresenter(Context context, CourseApiService apiService, AppDaoSession daoSession) {
        super(context);
        this.mApiService = apiService;
        this.cacheDao = daoSession.getRxDao(daoSession.getCacheDataDao());
    }

    /**
     * 查询课程表
     *
     * @param week        第几周的课程
     * @param loadNewwork 强制加载网络
     * @param loadView    回调view
     */
    public void queryCourseList(final int week, boolean loadNewwork, final ILoadView<UICourseData> loadView) {
        if (loadNewwork) {
            Observable<BaseResultData<CourseData>> observable = mApiService.queryCourseList();
            HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<CourseData>>(mContext) {
                @Override
                public void onSuccess(BaseResultData<CourseData> result) {
                    final CourseData data = result.data;
                    if (data != null) {
                        cacheDao.insertOrReplace(new CacheData<>(CacheKey.COURSE_CACHE, data))
                                .flatMap(new Function<CacheData, ObservableSource<UICourseData>>() {
                                    @Override
                                    public ObservableSource<UICourseData> apply(CacheData cacheData) throws Exception {
                                        return Observable.just(parseCourse(data, week));
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(SimpleObserver.create(new Consumer<UICourseData>() {
                                    @Override
                                    public void accept(UICourseData uiCourseData) throws Exception {
                                        if (!isReleased(loadView)) {
                                            loadView.onLoadData(uiCourseData);
                                        }
                                    }
                                }));
                    } else if (!isReleased(loadView)) {
                        loadView.onLoadData(null);
                    }
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    if (loadView != null) {
                        loadView.onFailed(errorCode, message);
                    }
                }
            });
        } else {
            cacheDao.load(CacheKey.COURSE_CACHE)
                    .flatMap(new Function<CacheData, ObservableSource<UICourseData>>() {
                        @Override
                        public ObservableSource<UICourseData> apply(CacheData cacheData) throws Exception {
                            UICourseData data = null;
                            if (cacheData != null) {
                                CourseData courseData = (CourseData) cacheData.getData(CourseData.class);
                                if (courseData != null) {
                                    data = parseCourse(courseData, week);
                                }
                            }
                            return Observable.just(data);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(SimpleObserver.create(new Consumer<UICourseData>() {
                        @Override
                        public void accept(UICourseData courseData) throws Exception {
                            if (courseData == null) {
                                queryCourseList(week, true, loadView);
                            } else if (!isReleased(loadView)) {
                                loadView.onLoadData(courseData);
                            }
                        }
                    }));
        }
    }

    /**
     * 转换成当前周的课程表
     *
     * @param courseData
     * @param week
     * @return
     */
    private UICourseData parseCourse(CourseData courseData, int week) {
        if (courseData == null) {
            return null;
        }
        UICourseData data = new UICourseData();
        long now = System.currentTimeMillis();
        data.lessonCount = courseData.lessons < MIN_LESSONS ? MIN_LESSONS : courseData.lessons;
        data.weekCount = courseData.weeks;
        long startMonday = TimeUtil.getFirstDayOfWeek(courseData.start_date * 1000L);
        if (week <= 0) {
            week = TimeUtil.weeksBetween(now, startMonday);
            week += 1;
        }
        Log.i(TAG, "week:" + week);
        data.currentWeek = week;
        data.startMonday = startMonday;
        data.mondayTime = TimeUtil.getAfterWeekTime(startMonday, week - 1);
        List<UICourseInfo> infos = null;
        if (courseData.courses != null) {
            infos = new ArrayList<>();
            for (CourseInfo info : courseData.courses) {
                if (isCurrentWeekCourse(week, info)) {
                    Log.i(TAG, "info:" + info);
                    UICourseInfo courseInfo = new UICourseInfo(info);
                    courseInfo.yPos = info.weekday - 1;
                    courseInfo.xPos = info.class_start - 1;
                    if (info.weekday > 5) {
                        data.showEndWeek = true;
                    }
                    infos.add(courseInfo);
                }
            }
        }
        data.courseInfos = infos;
        return data;
    }

    /**
     * 是否为当前周的课程
     *
     * @param week
     * @param info
     * @return
     */
    private boolean isCurrentWeekCourse(int week, CourseInfo info) {
        // 是否为偶数周
        boolean isEvenWeek = week % 2 == 0;
        boolean isValidWeek = week >= info.week_start && week <= info.week_end;
        if (isValidWeek) {
            if (WeekType.EveryWeek == info.week_type) {
                return true;
            } else if (WeekType.EvenWeek == info.week_type && isEvenWeek) {
                return true;
            } else if (WeekType.OddWeek == info.week_type && !isEvenWeek) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查询课程表详情
     *
     * @param courseId
     * @param view
     */
    public void queryCourseDetail(long courseId, final ILoadView<CourseInfo> view) {
        Observable<BaseResultData<CourseInfo>> observable = mApiService.queryCourseDetail(courseId);
        subscribe(observable, view);
    }
}
