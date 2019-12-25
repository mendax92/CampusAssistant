package com.zhiqi.campusassistant.ui.repair.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.ui.repair.fragment.RepairApprovalFragment;

/**
 * Created by ming on 2017/2/12.
 * 维修审批列表pager adapter
 */

public class RepairListPagerAdapter extends FragmentStatePagerAdapter {

    private int[] status;
    private String[] statusText;

    public RepairListPagerAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        status = activity.getResources().getIntArray(R.array.repair_query_status);
        statusText = activity.getResources().getStringArray(R.array.repair_query_text);
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= status.length) {
            return null;
        }
        return RepairApprovalFragment.newInstance(status[position]);
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
