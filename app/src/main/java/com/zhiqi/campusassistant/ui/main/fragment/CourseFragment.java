package com.zhiqi.campusassistant.ui.main.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ming.base.util.Log;
import com.ming.base.util.TimeUtil;
import com.ming.base.widget.TwoDimensionalLayout;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.listener.OnItemClickListener;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.ui.fragment.BaseToolbarFragment;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.utils.ToastUtil;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.course.dagger.component.DaggerCourseComponent;
import com.zhiqi.campusassistant.core.course.dagger.module.CourseModule;
import com.zhiqi.campusassistant.core.course.entity.CourseInfo;
import com.zhiqi.campusassistant.core.course.entity.UICourseData;
import com.zhiqi.campusassistant.core.course.entity.UICourseInfo;
import com.zhiqi.campusassistant.core.course.presenter.CoursePresenter;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.ui.course.activity.CourseDetailActivity;
import com.zhiqi.campusassistant.ui.main.view.ITabView;
import com.zhiqi.campusassistant.ui.main.widget.CourseAdapter;
import com.zhiqi.campusassistant.ui.main.widget.CourseChoiceAdapter;
import com.zhiqi.campusassistant.ui.user.activity.IntroduceActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ming on 2016/10/20.
 * 通讯录列表
 */
public class CourseFragment extends BaseToolbarFragment implements ITabView, ILoadView<UICourseData> {

    public static final String TAG = "CourseFragment";

    private static final int MIN_LESSONS = 16;

    private static final int MIN_WEEK = 5;

    private static final int MAX_WEEK = 7;

    private int checkedWeek = 0;

    private CourseAdapter mAdapter;
    private TextView weekText;
    private RecyclerView choiceRecyclerView;
    private CourseChoiceAdapter choiceAdapter;
    private PopupWindow mPopupWindow;
    private View mPopupView;

    @BindView(R.id.course_layout)
    TwoDimensionalLayout courseLayout;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Inject
    CoursePresenter mPresenter;

    @Override
    public int onCreateView(Bundle savedInstanceState) {
        initActionbar();
        return R.layout.frag_course;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDagger();
        initView();
        initData();
    }

    private void initActionbar() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_course_action_bar, getToolbar(), false);
        setActionbarLayout(view);

        weekText = ButterKnife.findById(view, R.id.week_name);
        view.setOnClickListener(clickListener);
    }

    private void initDagger() {
        DaggerCourseComponent.builder()
                .applicationComponent(AssistantApplication.getInstance().getApplicationComponent())
                .courseModule(new CourseModule())
                .build()
                .inject(this);
    }

    private void initView() {
        Log.i(TAG, "initView");
        setHasOptionsMenu(true);
        mAdapter = new CourseAdapter();
        courseLayout.setAdapter(mAdapter);
        courseLayout.setOnItemClick(itemClick);

        // popup
        mPopupView = LayoutInflater.from(getContext()).inflate(R.layout.view_course_week_choice, null);
        choiceRecyclerView = ButterKnife.findById(mPopupView, R.id.week_choice_recycler);
        if (choiceAdapter == null) {
            choiceAdapter = new CourseChoiceAdapter();
        }
        choiceRecyclerView.setAdapter(choiceAdapter);
        choiceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        choiceRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                checkedWeek = choiceAdapter.getItem(position);
                weekText.setText(getString(checkedWeek != choiceAdapter.getCurrentWeek() ? R.string.course_no_current_week : R.string.course_week, checkedWeek));
                mPresenter.queryCourseList(checkedWeek, false, CourseFragment.this);
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.queryCourseList(checkedWeek, true, CourseFragment.this);
            }
        });
        courseLayout.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                refreshLayout.setEnabled(dy <= 0);
            }
        });
        courseLayout.setTwoDimensionalSize(MIN_LESSONS, MIN_WEEK);
        mAdapter.setCourseData(null);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i(TAG, "Week check.");
            showChoiceWindow();
        }
    };

    private void showChoiceWindow() {
        if (choiceAdapter.getItemCount() <= 0) {
            ToastUtil.show(getActivity(), R.string.course_no_schedule);
            return;
        }
        int firstItem = checkedWeek - 3;
        choiceRecyclerView.scrollToPosition(firstItem < 0 ? 0 : firstItem);
        mPopupWindow = new PopupWindow(mPopupView, getResources().getDimensionPixelSize(R.dimen.course_choice_week_width),
                getResources().getDimensionPixelSize(R.dimen.course_choice_week_height), true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.showAsDropDown(getToolbar(), getToolbar().getWidth() / 2 - mPopupWindow.getWidth() / 2, 0);
    }

    private TwoDimensionalLayout.OnItemClick itemClick = new TwoDimensionalLayout.OnItemClick() {
        @Override
        public void onColumnHeadItemClick(View view, int row) {
        }

        @Override
        public void onRowHeadItemClick(View view, int column) {
        }

        @Override
        public void onTableItemClick(View view, TwoDimensionalLayout.BaseDataItem item) {
            if (item.item != null && item instanceof UICourseInfo) {
                CourseInfo info = ((UICourseInfo) item).item;
                Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                intent.putExtra(AppConstant.EXTRA_COURSE_INFO, info);
                startActivity(intent);
            }
        }
    };

    private void initData() {
        mPresenter.queryCourseList(checkedWeek, false, this);
    }

    private void setChoiceData(UICourseData data) {
        checkedWeek = data.currentWeek;
        choiceAdapter.setChecked(checkedWeek);
        int weeks = TimeUtil.weeksBetween(System.currentTimeMillis(), data.startMonday) + 1;
        choiceAdapter.setCurrentWeek(weeks);
        weekText.setText(getString(checkedWeek != weeks ? R.string.course_no_current_week : R.string.course_week, checkedWeek));
        if (data.weekCount != choiceAdapter.getItemCount()) {
            List<Integer> weekData = new ArrayList<>();
            for (int i = 0; i < data.weekCount; i++) {
                weekData.add(i + 1);
            }
            choiceAdapter.setNewData(weekData);
        } else {
            choiceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChecked() {
        invalidateActionbar();
    }

    @Override
    public void onUnchecked() {
        Log.i(TAG, "onUnchecked");
        resetActionbar();
    }

    @Override
    public void onLoadData(UICourseData data) {
        Log.i(TAG, "data:" + data);
        refreshLayout.setRefreshing(false);
        if (data != null) {
            setChoiceData(data);
            courseLayout.setTwoDimensionalSize(data.lessonCount, data.showEndWeek ? MAX_WEEK : MIN_WEEK);
        }
        mAdapter.setCourseData(data);
    }

    @Override
    public void onFailed(int errorCode, String message) {
        refreshLayout.setRefreshing(false);
        ToastUtil.show(getContext(), message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.release();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.introduce:
                Intent intent = new Intent(getActivity(), IntroduceActivity.class);
                intent.putExtra(AppConstant.EXTRA_APP_MODULE, ModuleType.CourseSchedule.getValue());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
