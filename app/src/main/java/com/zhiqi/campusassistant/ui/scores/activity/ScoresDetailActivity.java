package com.zhiqi.campusassistant.ui.scores.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.scores.dagger.component.DaggerScoresComponent;
import com.zhiqi.campusassistant.core.scores.dagger.module.ScoresModule;
import com.zhiqi.campusassistant.core.scores.entity.ScoreDetail;
import com.zhiqi.campusassistant.core.scores.presenter.ScoresPresenter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ming on 2017/5/13.
 */

public class ScoresDetailActivity extends BaseLoadActivity<ScoreDetail> implements ILoadView<ScoreDetail> {

    @BindView(R.id.score_year)
    TextView scoreYear;
    @BindView(R.id.semester)
    TextView semesterTxt;
    @BindView(R.id.course_type)
    TextView courseType;
    @BindView(R.id.course_name)
    TextView courseNameTxt;
    @BindView(R.id.course_teacher)
    TextView courseTeacher;
    @BindView(R.id.score_type)
    TextView scoreType;
    @BindView(R.id.score_usually)
    TextView scoreUsually;
    @BindView(R.id.score_of_term)
    TextView scoreOfTerm;
    @BindView(R.id.score_total)
    TextView scoreTotal;
    @BindView(R.id.score_retest)
    TextView scoreRetest;
    @BindView(R.id.score_rebuild)
    TextView scoreRebuild;
    @BindView(R.id.score_credits)
    TextView scoreCredits;

    @Inject
    ScoresPresenter mPresenter;

    private String courseName;

    private String schoolYear;

    private String semester;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_scores_detail;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initData();
    }

    private void initDagger() {
        DaggerScoresComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .scoresModule(new ScoresModule())
                .build()
                .inject(this);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        courseName = intent.getStringExtra(AppConstant.EXTRA_COURSE_NAME);
        schoolYear = intent.getStringExtra(AppConstant.EXTRA_SCORE_SCHOOL_YEAR);
        semester = intent.getStringExtra(AppConstant.EXTRA_SEMESTER);
        refresh();
    }

    @Override
    protected void onRefresh() {
        mPresenter.loadScoreDetail(schoolYear, semester, courseName, this);
    }

    @Override
    public void onLoadData(ScoreDetail data) {
        super.onLoadData(data);
        if (data == null) {
            return;
        }
        scoreYear.setText(data.school_year);
        semesterTxt.setText(data.semester);
        courseNameTxt.setText(data.course_name);
        courseType.setText(data.course_type);
        courseTeacher.setText(data.teacher);
        scoreType.setText(data.examine_mode);
        scoreUsually.setText(getScore(data.usual_score));
        scoreOfTerm.setText(getScore(data.final_score));
        scoreTotal.setText(getScore(data.score));
        scoreRetest.setText(getScore(data.retest_score));
        scoreRebuild.setText(getScore(data.rebuild_score));
        scoreCredits.setText(getScore(data.credits));
    }

    private String getScore(String score) {
        return TextUtils.isEmpty(score) ? getString(R.string.common_nothing) : score;
    }
}
