package com.zhiqi.campusassistant.ui.lost.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.lost.entity.LostApplyType;
import com.zhiqi.campusassistant.ui.lost.fragment.LostListFragment;

/**
 * Created by ming on 2017/5/5.
 * 失物招领fragment适配器
 */

public class LostFragmentAdapter extends FragmentStatePagerAdapter {

    private String[] titles;

    private int[] types;

    public LostFragmentAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        titles = activity.getResources().getStringArray(R.array.lost_type_name);
        types = activity.getResources().getIntArray(R.array.lost_type);
    }

    @Override
    public Fragment getItem(int position) {
        return LostListFragment.newInstance(LostApplyType.formatValue(types[position]));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
