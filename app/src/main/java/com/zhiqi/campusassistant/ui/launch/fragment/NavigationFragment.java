package com.zhiqi.campusassistant.ui.launch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ming.base.util.StatusBarUtil;
import com.ming.base.widget.CircleView;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.fragment.BaseFilterFragment;
import com.zhiqi.campusassistant.ui.launch.widget.NavigationAdapter;
import com.zhiqi.campusassistant.ui.login.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

/**
 * Created by ming on 2017/4/1.
 * 导航fragment
 */

public class NavigationFragment extends BaseFilterFragment {

    @BindView(R.id.navigation_pager)
    ViewPager navPager;

    @BindView(R.id.pager_point)
    LinearLayout pagerPoint;

    private NavigationAdapter mPageAdapter;

    private int currentIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarUtil.setTransparent(getActivity());
        initView();
    }

    private void initView() {
        mPageAdapter = new NavigationAdapter(getActivity());
        navPager.setAdapter(mPageAdapter);
        currentIndex = navPager.getCurrentItem();
        int count = mPageAdapter.getCount();
        int circleSize = getResources().getDimensionPixelSize(R.dimen.nav_pager_point_size);
        int margin = getResources().getDimensionPixelOffset(R.dimen.common_margin_xs);
        for (int i = 0; i < count; i++) {
            CircleView circleView = new CircleView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(circleSize, circleSize);
            if (i != count - 1) {
                params.rightMargin = margin;
            }
            circleView.setLayoutParams(params);
            if (i == currentIndex) {
                circleView.setCircleColorResource(R.color.white);
            } else {
                circleView.setCircleColorResource(R.color.white_50_percent);
            }
            pagerPoint.addView(circleView);
        }
    }

    @OnPageChange(R.id.navigation_pager)
    void onPageSelected(int position) {
        View view = pagerPoint.getChildAt(currentIndex);
        if (view != null && view instanceof CircleView) {
            ((CircleView) view).setCircleColorResource(R.color.white_50_percent);
        }
        view = pagerPoint.getChildAt(position);
        if (view != null && view instanceof CircleView) {
            ((CircleView) view).setCircleColorResource(R.color.white);
        }
        currentIndex = position;
    }

    @OnClick(R.id.login_btn)
    void click(View view) {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
