package com.zhiqi.campusassistant.ui.repair.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.ui.repair.widget.RepairListPagerAdapter;
import com.zhiqi.campusassistant.ui.user.activity.IntroduceActivity;

import butterknife.BindView;

/**
 * Created by ming on 2017/2/12.
 * 维修审批
 */

public class RepairApprovalActivity extends BaseToolbarActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;

    @BindView(R.id.tab_viewpager)
    ViewPager mPager;

    private RepairListPagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tabpager);
        initView();
    }

    private void initView() {
        mPagerAdapter = new RepairListPagerAdapter(this);
        mPager.setAdapter(mPagerAdapter);
        mTablayout.setupWithViewPager(mPager);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_module_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.introduce:
                Intent intent = new Intent(this, IntroduceActivity.class);
                intent.putExtra(AppConstant.EXTRA_APP_MODULE, ModuleType.RepairApproval.getValue());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
