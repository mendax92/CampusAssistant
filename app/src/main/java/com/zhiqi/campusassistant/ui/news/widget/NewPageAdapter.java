package com.zhiqi.campusassistant.ui.news.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.zhiqi.campusassistant.core.news.entity.CategoryInfo;
import com.zhiqi.campusassistant.ui.news.fragment.NewsFragment;

import java.util.List;

/**
 * Created by ming on 2017/3/11.
 */

public class NewPageAdapter extends FragmentPagerAdapter {

    private List<CategoryInfo> data;

    public NewPageAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
    }

    public void setData(List<CategoryInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (data != null && position < data.size()) {
            return NewsFragment.newInstance(data.get(position).id);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (data != null && position < data.size()) {
            return data.get(position).name;
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }
}
