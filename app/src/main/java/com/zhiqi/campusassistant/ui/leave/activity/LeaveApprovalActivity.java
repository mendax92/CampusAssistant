package com.zhiqi.campusassistant.ui.leave.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.message.entity.ModuleType;
import com.zhiqi.campusassistant.ui.leave.widget.LeaveListPagerAdapter;
import com.zhiqi.campusassistant.ui.user.activity.IntroduceActivity;

import butterknife.BindView;

/**
 * Created by ming on 2017/3/10.
 */

public class LeaveApprovalActivity extends BaseToolbarActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;

    @BindView(R.id.tab_viewpager)
    ViewPager mPager;

    private LeaveListPagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tabpager);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        int userType = -1;
        if (intent != null) {
            userType = intent.getIntExtra(AppConstant.EXTRA_LEAVE_USER_TYPE, -1);
        }
        if (userType == AppConstant.VALUE_LEAVE_USER_TYPE_STUDENT) {
            getToolbar().setTitle(R.string.leave_approval_student);
        } else {
            getToolbar().setTitle(R.string.leave_approval_employee);
        }
        mPagerAdapter = new LeaveListPagerAdapter(userType, this);
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
                intent.putExtra(AppConstant.EXTRA_APP_MODULE, ModuleType.LeaveApproval.getValue());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}