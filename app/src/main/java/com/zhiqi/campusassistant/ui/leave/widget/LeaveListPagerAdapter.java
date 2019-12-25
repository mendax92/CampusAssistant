package com.zhiqi.campusassistant.ui.leave.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.ui.leave.fragment.LeaveApprovalFragment;

/**
 * Created by ming on 2017/3/10.
 * 请假类型fragment适配器
 */

public class LeaveListPagerAdapter extends FragmentStatePagerAdapter {

    private int[] status;
    private String[] statusText;
    private int userType;

    public LeaveListPagerAdapter(int userType, AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        this.userType = userType;
        status = activity.getResources().getIntArray(R.array.leave_query_status);
        statusText = activity.getResources().getStringArray(R.array.leave_query_text);
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= status.length) {
            return null;
        }
        return LeaveApprovalFragment.newInstance(status[position], userType);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position >= statusText.length) {
            return null;
        }
        return statusText[position];
    }

    @Override
    public int getCount() {
        return status.length;
    }
}
