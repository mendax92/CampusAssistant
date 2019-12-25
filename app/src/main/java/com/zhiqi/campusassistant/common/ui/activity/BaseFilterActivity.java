package com.zhiqi.campusassistant.common.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.ming.base.activity.BaseActivity;
import com.ming.base.util.Log;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.utils.PermissionOp;

import butterknife.ButterKnife;

/**
 * Created by ming on 2016/10
 */

public class BaseFilterActivity extends BaseActivity {

    private PermissionOp permissionOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AssistantApplication.getInstance().checkUpgrade();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View contentView = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(contentView);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        try {
            ButterKnife.bind(this);
        } catch (Throwable ex) {
            Log.w("BaseFilterActivity", "ButterKnife bind error, please check or ignore.", ex);
        }
    }

    public boolean requestPermissions(boolean must, String... permissions) {
        if (permissionOp == null) {
            permissionOp = new PermissionOp();
            permissionOp.setOnPermissionGrantedListener(grantedListener);
        }
        return permissionOp.requestPermissions(this, must, permissions);
    }

    private PermissionOp.OnPermissionGrantedListener grantedListener = new PermissionOp.OnPermissionGrantedListener() {
        @Override
        public void onPermissionGranted(String[] permissions) {
            BaseFilterActivity.this.onPermissionGranted(permissions);
        }
    };

    /**
     * 所有请求的权限都允许后才会回调
     *
     * @param permissions
     */
    protected void onPermissionGranted(String[] permissions) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionOp != null) {
            permissionOp.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
