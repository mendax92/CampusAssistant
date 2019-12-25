package com.zhiqi.campusassistant.ui.main.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SparseArrayCompat;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.ming.base.activity.ActivityManager;
import com.ming.base.util.Log;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseToolbarActivity;
import com.zhiqi.campusassistant.ui.main.fragment.AppFragment;
import com.zhiqi.campusassistant.ui.main.fragment.CourseFragment;
import com.zhiqi.campusassistant.ui.main.fragment.MessageFragment;
import com.zhiqi.campusassistant.ui.main.fragment.UserCenterFragment;
import com.zhiqi.campusassistant.ui.main.view.ITabHostListener;
import com.zhiqi.campusassistant.ui.main.view.ITabView;
import com.zhiqi.campusassistant.ui.main.widget.FragmentTabHost;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class HomeActivity extends BaseToolbarActivity {

    private static final String TAG = "HomeActivity";

    public static final String CURRENT_PAGE_INDEX = "current_page_index";

    private FragmentTabHost mTabHost;

    private SparseArrayCompat<Drawable> unCheckDrawables = new SparseArrayCompat<>();

    private SparseArrayCompat<AnimationDrawable> checkDrawables = new SparseArrayCompat<>();

    @BindView(R.id.main_tab_group)
    RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManager.getInstance().popAllActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestPermissions(false, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        hideHomeBack();
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            int index = intent.getIntExtra(CURRENT_PAGE_INDEX, -1);
            if (index >= 0) {
                mTabHost.forceSwitchTo(index);
            }
        }
    }

    private void init() {
        mTabHost = new FragmentTabHost(this, mRadioGroup, R.id.main_frag_content, new ITabHostListener() {
            @Override
            public void onTabChecked(int index, Fragment fragment) {
                Log.i(TAG, "onTabChecked index:" + index + "," + mTabHost);
                if (fragment != null && fragment instanceof ITabView) {
                    ((ITabView) fragment).onChecked();
                }
            }

            @Override
            public void onTabUnchecked(int index, Fragment fragment) {
                Log.i(TAG, "onTabUnchecked index:" + index);
                if (fragment != null && fragment instanceof ITabView) {
                    ((ITabView) fragment).onUnchecked();
                }
            }

            @Override
            public Fragment getFragment(int index) {
                Log.i(TAG, "getFragment " + index);
                switch (index) {
                    case 0:
                        return new MessageFragment();
                    case 1:
                        return new AppFragment();
                    case 2:
                        return new CourseFragment();
                    case 3:
                        return new UserCenterFragment();
                    default:
                        return null;
                }
            }
        });
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        int index = intent.getIntExtra(CURRENT_PAGE_INDEX, -1);
        if (index >= 0) {
            mTabHost.forceSwitchTo(index);
        }
    }

    @OnCheckedChanged({R.id.tab_msg, R.id.tab_app, R.id.tab_course, R.id.tab_me})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tab_msg:
                if (isChecked) {
                    Drawable drawable = unCheckDrawables.get(R.id.tab_msg);
                    if (drawable == null) {
                        unCheckDrawables.put(R.id.tab_msg, buttonView.getCompoundDrawables()[1]);
                    }
                    AnimationDrawable anim = checkDrawables.get(R.id.tab_msg);
                    if (anim == null) {
                        anim = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.main_tab_msg_selected);
                        checkDrawables.put(R.id.tab_msg, anim);
                    }
                    buttonView.setCompoundDrawables(null, anim, null, null);
                    anim.stop();
                    anim.start();
                } else {
                    buttonView.setCompoundDrawables(null, unCheckDrawables.get(R.id.tab_msg), null, null);
                }
                break;
            case R.id.tab_app:
                if (isChecked) {
                    Drawable drawable = unCheckDrawables.get(R.id.tab_app);
                    if (drawable == null) {
                        unCheckDrawables.put(R.id.tab_app, buttonView.getCompoundDrawables()[1]);
                    }
                    AnimationDrawable anim = checkDrawables.get(R.id.tab_app);
                    if (anim == null) {
                        anim = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.main_tab_app_selected);
                        checkDrawables.put(R.id.tab_app, anim);
                    }
                    buttonView.setCompoundDrawables(null, anim, null, null);
                    anim.stop();
                    anim.start();
                } else {
                    buttonView.setCompoundDrawables(null, unCheckDrawables.get(R.id.tab_app), null, null);
                }
                break;
            case R.id.tab_course:
                if (isChecked) {
                    Drawable drawable = unCheckDrawables.get(R.id.tab_course);
                    if (drawable == null) {
                        unCheckDrawables.put(R.id.tab_course, buttonView.getCompoundDrawables()[1]);
                    }
                    AnimationDrawable anim = checkDrawables.get(R.id.tab_course);
                    if (anim == null) {
                        anim = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.main_tab_course_selected);
                        checkDrawables.put(R.id.tab_course, anim);
                    }
                    buttonView.setCompoundDrawables(null, anim, null, null);
                    anim.stop();
                    anim.start();
                } else {
                    buttonView.setCompoundDrawables(null, unCheckDrawables.get(R.id.tab_course), null, null);
                }
                break;
            case R.id.tab_me:
                if (isChecked) {
                    Drawable drawable = unCheckDrawables.get(R.id.tab_me);
                    if (drawable == null) {
                        unCheckDrawables.put(R.id.tab_me, buttonView.getCompoundDrawables()[1]);
                    }
                    AnimationDrawable anim = checkDrawables.get(R.id.tab_me);
                    if (anim == null) {
                        anim = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.main_tab_me_selected);
                        checkDrawables.put(R.id.tab_me, anim);
                    }
                    buttonView.setCompoundDrawables(null, anim, null, null);
                    anim.stop();
                    anim.start();
                } else {
                    buttonView.setCompoundDrawables(null, unCheckDrawables.get(R.id.tab_me), null, null);
                }
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int currentIndex = savedInstanceState.getInt(CURRENT_PAGE_INDEX, -1);
            Log.i(TAG, "onRestoreInstanceState currentIndex:" + currentIndex);
            if (currentIndex >= 0) {
                mTabHost.forceSwitchTo(currentIndex);
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState.");
        outState.putInt(CURRENT_PAGE_INDEX, mTabHost.getCurrentIndex());
        super.onSaveInstanceState(outState);
    }
}
