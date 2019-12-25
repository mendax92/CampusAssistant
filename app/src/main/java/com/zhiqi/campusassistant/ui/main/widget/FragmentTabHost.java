package com.zhiqi.campusassistant.ui.main.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.ming.base.util.Log;
import com.ming.base.util.StringUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.ui.main.view.ITabHostListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ming on 2016/9/18.
 * fragment切换
 */
public class FragmentTabHost {

    private static final int VIEW_TAG = R.id.main_tab_group;

    private static final String TAG = "FragmentTabHost";

    private FragmentActivity mActivity;

    private RadioGroup mRadioGroup;

    private List<CompoundButton> btns = new ArrayList<>();

    private SparseArray<Fragment> mFragments = new SparseArray<>();

    private int mFragContentId;

    private int mCurrentIndex = -1;

    private ITabHostListener mListener;

    public FragmentTabHost(FragmentActivity activity, RadioGroup radioGroup, int fragContentId, ITabHostListener listener) {
        this.mActivity = activity;
        this.mRadioGroup = radioGroup;
        this.mFragContentId = fragContentId;
        this.mListener = listener;
        init();
    }

    private void init() {
        int count = mRadioGroup.getChildCount();
        int defaultIndex = -1;
        for (int i = 0; i < count; i++) {
            View view = mRadioGroup.getChildAt(i);
            // 从单选按钮组查找CompoundButton，录入按钮列表，并且设置点击事件
            if (view instanceof CompoundButton) {
                if (defaultIndex < 0) {
                    defaultIndex = i;
                }
                btns.add((CompoundButton) view);
                view.setTag(VIEW_TAG, i);
                view.setOnClickListener(clickListener);
            } else if (view instanceof ViewGroup) {
                // 如果单选按钮组查找出来的是ViewGroup，则从其ViewGroup查找到CompoundButton，并且录入按钮列表，设置点击事件
                ViewGroup group = (ViewGroup) view;
                int childCount = group.getChildCount();
                for (int j = 0; j < childCount; j++) {
                    View child = group.getChildAt(j);
                    if (child instanceof CompoundButton) {
                        if (defaultIndex < 0) {
                            defaultIndex = i;
                        }
                        btns.add((CompoundButton) child);
                        view.setTag(VIEW_TAG, i);
                        view.setOnClickListener(clickListener);
                        break;
                    }
                }
            }
        }
        toFragment(defaultIndex);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int index = (int) view.getTag(VIEW_TAG);
            switchTo(index);
        }
    };

    public CompoundButton[] getCompoundButtons() {
        if (btns == null) {
            return null;
        }
        CompoundButton[] compoundButtons = new CompoundButton[btns.size()];
        return btns.toArray(compoundButtons);
    }

    public Fragment getFragment(int i) {
        return mFragments.get(i);
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    /**
     * 切换fragment
     *
     * @param index
     */
    public void switchTo(int index) {
        toFragment(index);
    }

    /**
     * 强制切换fragment
     *
     * @param index 索引号
     */
    public void forceSwitchTo(int index) {
        mCurrentIndex = -1;
        toFragment(index);
    }

    /**
     * 切换fragment
     *
     * @param index 索引号
     */
    private void toFragment(int index) {
        if (index < 0 || index >= btns.size() || index == mCurrentIndex) {
            return;
        }
        // 设置选中状态按钮
        for (int i = 0; i < btns.size(); i++) {
            CompoundButton btn = btns.get(i);
            if (i == index) {
                Log.i(TAG, "btn:" + btn);
                btn.setChecked(true);
                mRadioGroup.check(btn.getId());
            } else if (btn.isChecked()) {
                btn.setChecked(false);
            }
        }
        FragmentManager fm = mActivity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // 从FragmentManager查找fragment，找到则录入fragment列表
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment == null) {
                    continue;
                }
                String tag = fragment.getTag();
                if (!StringUtil.isBlank(tag) && tag.startsWith(TAG + "_")) {
                    String[] tags = tag.split("_");
                    try {
                        int i = Integer.parseInt(tags[1]);
                        mFragments.put(i, fragment);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        // 如果fragment列表中没有，则调用回调，获得fragment，并录入fragment列表
        Fragment target = mFragments.get(index);
        Fragment source = null;
        if (target == null && mListener != null) {
            target = mListener.getFragment(index);
            if (target != null) {
                mFragments.put(index, target);
                ft.add(mFragContentId, target, TAG + "_" + index);
            }
        }
        // 隐藏其他的fragment，显示当前fragment
        for (int i = 0; i < mFragments.size(); i++) {
            int key = mFragments.keyAt(i);
            if (key == index) {
                continue;
            }
            Fragment other = mFragments.valueAt(i);
            if (other != null && !other.isHidden()) {
                Log.i(TAG, "hide " + other);
                ft.hide(other);
            }
            if (key == mCurrentIndex) {
                source = other;
            }
        }
        if (target != null) {
            ft.show(target);
        }
        ft.commitAllowingStateLoss();
        // 回调选中事件
        if (mListener != null) {
            if (mCurrentIndex >= 0) {
                mListener.onTabUnchecked(mCurrentIndex, source);
            }
            mListener.onTabChecked(index, target);
        }
        mCurrentIndex = index;
    }


}
