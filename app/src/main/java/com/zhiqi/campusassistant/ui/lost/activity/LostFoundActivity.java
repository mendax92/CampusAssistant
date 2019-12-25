package com.zhiqi.campusassistant.ui.lost.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.ui.lost.widget.LostFragmentAdapter;

import butterknife.BindView;

/**
 * Created by ming on 2017/5/5.
 */

public class LostFoundActivity extends BaseToolbarActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;

    @BindView(R.id.tab_viewpager)
    ViewPager mPager;

    private LostFragmentAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tabpager);
        initView();
    }

    private void initView() {
        mPagerAdapter = new LostFragmentAdapter(this);
        mPager.setAdapter(mPagerAdapter);
        mTablayout.setupWithViewPager(mPager);
    }


}
