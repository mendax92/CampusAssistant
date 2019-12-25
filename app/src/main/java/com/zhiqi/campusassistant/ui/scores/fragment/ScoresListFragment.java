package com.zhiqi.campusassistant.ui.scores.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.decoration.HorizontalDividerItemDecoration;
import com.ming.base.widget.recyclerView.listener.OnItemClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.fragment.BaseRefreshListFragment;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.scores.dagger.component.DaggerScoresComponent;
import com.zhiqi.campusassistant.core.scores.dagger.module.ScoresModule;
import com.zhiqi.campusassistant.core.scores.entity.CourseScoreInfo;
import com.zhiqi.campusassistant.core.scores.presenter.ScoresPresenter;
import com.zhiqi.campusassistant.ui.scores.activity.ScoresDetailActivity;
import com.zhiqi.campusassistant.ui.scores.widget.CourseScoresAdapter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ming on 17-11-20.
 * 成绩列表fragment
 */

public class ScoresListFragment extends BaseRefreshListFragment<CourseScoreInfo> implements ILoadView<List<CourseScoreInfo>> {

    @Inject
    ScoresPresenter mPresenter;

    private boolean firstLoad = true;

    private String schoolYear;
    private String semester;

    public static ScoresListFragment newInstance(String schoolYear, String semester) {
        Bundle args = new Bundle();
        args.putString(AppConstant.EXTRA_SCORE_SCHOOL_YEAR, schoolYear);
        args.putString(AppConstant.EXTRA_SEMESTER, semester);
        ScoresListFragment fragment = new ScoresListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDagger();
        initView();
        initData();
    }

    private void initDagger() {
        DaggerScoresComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .scoresModule(new ScoresModule())
                .build()
                .inject(this);
    }

    public void refresh(String schoolYear, String semester) {
        this.schoolYear = schoolYear;
        this.semester = semester;
        if (!firstLoad) {
            mRefreshLayout.setRefreshing(true);
        }
        refresh();
    }

    private void initView() {
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                CourseScoreInfo info = mAdapter.getItem(position);
                if (info == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), ScoresDetailActivity.class);
                intent.putExtra(AppConstant.EXTRA_COURSE_NAME, info.course_name);
                intent.putExtra(AppConstant.EXTRA_SCORE_SCHOOL_YEAR, schoolYear);
                intent.putExtra(AppConstant.EXTRA_SEMESTER, schoolYear);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        Bundle args = getArguments();
        if (args != null) {
            schoolYear = args.getString(AppConstant.EXTRA_COURSE_NAME, null);
            semester = args.getString(AppConstant.EXTRA_SEMESTER);
        }
    }

    @Override
    protected void onRefresh() {
        firstLoad = false;
        if (!TextUtils.isEmpty(schoolYear) && !TextUtils.isEmpty(semester)) {
            mPresenter.loadCourseScores(schoolYear, semester, this);
        } else {
            onLoadData(null);
        }
    }

    @Override
    protected RecyclerView.ItemDecoration provideItemDecoration() {
        return new HorizontalDividerItemDecoration.Builder(getActivity())
                .backgroundResource(R.color.white)
                .marginProvider(new HorizontalDividerItemDecoration.MarginProvider() {
                    @Override
                    public int dividerLeftMargin(int position, RecyclerView parent) {
                        if (position == mAdapter.getItemCount() - 1) {
                            return 0;
                        }
                        return getResources().getDimensionPixelSize(R.dimen.common_margin_s);
                    }

                    @Override
                    public int dividerRightMargin(int position, RecyclerView parent) {
                        return 0;
                    }
                })
                .showLastDivider()
                .build();
    }

    @Override
    protected BaseQuickAdapter<CourseScoreInfo> provideAdapter() {
        return new CourseScoresAdapter();
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.release();
        }
        super.onDestroy();
    }
}
