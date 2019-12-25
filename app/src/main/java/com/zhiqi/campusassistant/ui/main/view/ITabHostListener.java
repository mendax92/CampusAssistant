package com.zhiqi.campusassistant.ui.main.view;

import android.support.v4.app.Fragment;

/**
 * Created by ming on 2016/9/18.
 */
public interface ITabHostListener {

    void onTabChecked(int index, Fragment fragment);

    void onTabUnchecked(int index, Fragment fragment);

    Fragment getFragment(int index);
}
