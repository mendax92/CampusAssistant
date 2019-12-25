package com.zhiqi.campusassistant.common.ui.widget;

import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.SoftReference;

/**
 * Created by ming on 2017/3/27.
 * ViewPager adapter
 */

public abstract class ViewPagerAdapter extends PagerAdapter {

    private SparseArrayCompat<SoftReference<View>> views = new SparseArrayCompat<>();

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        SoftReference<View> reference = views.get(position);
        View view = reference != null ? reference.get() : null;
        view = instantiateItem(container, view, position);
        if (view != null) {
            container.addView(view);
            views.put(position, new SoftReference<>(view));
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        SoftReference<View> reference = views.get(position);
        View view = reference != null ? reference.get() : null;
        if (view != null) {
            container.removeView(view);
        } else if (object != null && object instanceof View) {
            container.removeView((View) object);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public abstract View instantiateItem(ViewGroup container, View itemView, int position);
}
