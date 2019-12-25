package com.zhiqi.campusassistant.ui.launch.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.ming.base.activity.ActivityManager;
import com.ming.base.util.StatusBarUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseFilterActivity;
import com.zhiqi.campusassistant.ui.launch.fragment.NavigationFragment;

/**
 * Created by ming on 2016/11/22.
 * 引导页
 */

public class NavigationActivity extends BaseFilterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManager.getInstance().popAllActivity();
        StatusBarUtil.setTransparent(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_common_empty_framelayout);
        initView();
    }

    private void initView() {
        //startActivity(new Intent(this, NavigationActivity.class));
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, new NavigationFragment());
            ft.setCustomAnimations(R.anim.translucent_enter, R.anim.translucent_exit);
            ft.commitAllowingStateLoss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
