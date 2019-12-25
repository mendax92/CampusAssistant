package com.zhiqi.campusassistant.ui.launch.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ming.base.util.RxUtil;
import com.ming.base.util.StatusBarUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.ui.activity.BaseFilterActivity;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.ui.launch.fragment.NavigationFragment;
import com.zhiqi.campusassistant.ui.main.activity.HomeActivity;

import butterknife.BindView;

/**
 * Created by Edmin on 2016/10/7 0007.
 * launch activity
 */

public class LaunchActivity extends BaseFilterActivity {

    @BindView(R.id.container)
    ViewGroup container;

    private boolean hasInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions(true, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        setContentView(R.layout.view_common_empty_framelayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasInit) {
            hasInit = true;
            init();
        }
    }

    private void init() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        StatusBarUtil.setTransparent(this);
        RxUtil.postOnMainThread(new Runnable() {
            @Override
            public void run() {
                enterApp();
            }
        }, 600);
    }

    private void enterApp() {
        if (LoginManager.getInstance().isLogin()) {
            startActivity(new Intent(LaunchActivity.this, HomeActivity.class));
            overridePendingTransition(R.anim.translucent_enter, R.anim.translucent_exit);
            finish();
        } else {
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
}
