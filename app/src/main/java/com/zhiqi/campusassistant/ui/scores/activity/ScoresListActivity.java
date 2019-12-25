package com.zhiqi.campusassistant.ui.scores.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.activity.BaseLoadActivity;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.core.scores.dagger.component.DaggerScoresComponent;
import com.zhiqi.campusassistant.core.scores.dagger.module.ScoresModule;
import com.zhiqi.campusassistant.core.scores.entity.SemesterInfo;
import com.zhiqi.campusassistant.core.scores.presenter.ScoresPresenter;
import com.zhiqi.campusassistant.ui.scores.fragment.ScoresListFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ming on 2017/5/12.
 * 课程成绩列表
 */

public class ScoresListActivity extends BaseLoadActivity<SemesterInfo> implements ILoadView<SemesterInfo> {

    @Inject
    ScoresPresenter mPresenter;

    @BindView(R.id.semester_year)
    TextView semesterYearTxt;

    @BindView(R.id.semester)
    TextView semesterTxt;

    @BindView(R.id.spinner_layout)
    LinearLayout spinnerLayout;

    private SemesterInfo semesterInfo;

    private SemesterInfo.Semester checkedYear;

    private SemesterInfo.Semester checkedSemester;

    private ScoresListFragment scoreFragment;

    @Override
    protected int onCreateView(Bundle savedInstanceState) {
        return R.layout.activity_scores_list;
    }

    @Override
    protected void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        initDagger();
        initView();
        refresh();
    }

    private void initDagger() {
        DaggerScoresComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .scoresModule(new ScoresModule())
                .build()
                .inject(this);
    }

    private void initView() {
        scoreFragment = ScoresListFragment.newInstance(null, null);
        getSupportFragmentManager().beginTransaction().add(R.id.content_list, scoreFragment).commitAllowingStateLoss();
    }

    @Override
    protected void onRefresh() {
        mPresenter.loadSemesterInfo(this);
    }

    private void refreshList() {
        scoreFragment.refresh(checkedYear.id, checkedSemester.id);
    }

    @OnClick({R.id.semester_spinner, R.id.semester_year_spinner})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.semester_spinner:
                showSemesterSpinner();
                break;
            case R.id.semester_year_spinner:
                showYearSpinner();
                break;
        }
    }

    private void showYearSpinner() {
        final List<SemesterInfo.Semester> years = semesterInfo.school_years;
        if (years != null && !years.isEmpty()) {
            List<String> typeNames = new ArrayList<>();
            for (SemesterInfo.Semester info : years) {
                typeNames.add(info.type_name);
            }
            new MaterialDialog.Builder(this)
                    .title(R.string.leave_type)
                    .items(typeNames)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            SemesterInfo.Semester selectType = years.get(position);
                            if (selectType == checkedYear) {
                                return;
                            }
                            semesterYearTxt.setText(selectType.type_name);
                            checkedYear = selectType;
                            refreshList();
                        }
                    })
                    .show();
        }
    }

    private void showSemesterSpinner() {
        final List<SemesterInfo.Semester> semesters = semesterInfo.semester;
        if (semesters != null && !semesters.isEmpty()) {
            List<String> typeNames = new ArrayList<>();
            for (SemesterInfo.Semester info : semesters) {
                typeNames.add(info.type_name);
            }
            new MaterialDialog.Builder(this)
                    .title(R.string.leave_type)
                    .items(typeNames)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            SemesterInfo.Semester selectType = semesters.get(position);
                            if (selectType == checkedSemester) {
                                return;
                            }
                            semesterTxt.setText(selectType.type_name);
                            checkedSemester = selectType;
                            refreshList();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onLoadData(SemesterInfo data) {
        super.onLoadData(data);
        if (data == null) {
            return;
        }
        semesterInfo = data;
        if (data.school_years != null && !data.school_years.isEmpty()) {
            checkedYear = data.school_years.get(0);
            semesterYearTxt.setText(checkedYear.type_name);
        }
        if (data.semester != null && !data.semester.isEmpty()) {
            checkedSemester = data.semester.get(0);
            semesterTxt.setText(checkedSemester.type_name);
        }
        refreshList();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.release();
        }
        super.onDestroy();
    }
}
