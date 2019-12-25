package com.zhiqi.campusassistant.ui.course.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ming.base.util.TimeUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.course.dagger.component.DaggerCourseComponent;
import com.zhiqi.campusassistant.core.course.dagger.module.CourseModule;
import com.zhiqi.campusassistant.core.course.entity.CourseInfo;
import com.zhiqi.campusassistant.core.course.presenter.CoursePresenter;
import com.zhiqi.campusassistant.ui.user.activity.UserFeedbackActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ming on 2016/12/19.
 * 课程表详情
 */

public class CourseDetailActivity extends BaseToolbarActivity implements ILoadView<CourseInfo> {

    @BindView(R.id.course_name)
    TextView courseName;
    @BindView(R.id.course_classroom)
    TextView courseClassroom;
    @BindView(R.id.course_teacher)
    TextView courseTeacher;
    @BindView(R.id.course_lesson)
    TextView courseLesson;
    @BindView(R.id.course_weeks)
    TextView courseWeeks;

    @Inject
    CoursePresenter mPresenter;

    private CourseInfo courseInfo;

    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ButterKnife.bind(this);
        initDagger();
        initData();
        initView();
    }

    private void initDagger() {
        DaggerCourseComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .courseModule(new CourseModule())
                .build()
                .inject(this);
    }

    private void initData() {
        Intent intent = getIntent();
        courseInfo = intent.getParcelableExtra(AppConstant.EXTRA_COURSE_INFO);
        if (courseInfo == null) {
            courseId = intent.getLongExtra(AppConstant.EXTRA_COURSE_ID, -1);
            if (courseId > 0) {
                mPresenter.queryCourseDetail(courseId, this);
            } else {
                finish();
            }
        }
    }

    private void initView() {
        if (courseInfo == null) {
            return;
        }
        courseName.setText(courseInfo.name);
        courseClassroom.setText(courseInfo.location);
        courseTeacher.setText(courseInfo.teacher);
        String lessonText = TimeUtil.getWeekOfChinese(courseInfo.weekday, getString(R.string.course_day_week))
                + " " + getString(R.string.course_lesson_detail, courseInfo.class_start, courseInfo.class_end);
        courseLesson.setText(lessonText);
        courseWeeks.setText(getString(R.string.course_weeks_start_end, courseInfo.week_start, courseInfo.week_end));
    }

    @OnClick(R.id.course_error_text)
    public void onClick() {
        startActivity(new Intent(this, UserFeedbackActivity.class));
    }

    @Override
    public void onLoadData(CourseInfo data) {
        courseInfo = data;
        initView();
    }

    @Override
    public void onFailed(int errorCode, String message) {
        ToastUtil.show(this, message);
    }

    @Override
    protected void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }
}
