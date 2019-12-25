package com.zhiqi.campusassistant.common.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ming.base.fragment.BaseFragment;
import com.zhiqi.campusassistant.common.utils.PermissionOp;

/**
 * Created by ming on 2016/10/20.
 * 基础过滤fragment
 */

public abstract class BaseFilterFragment extends BaseFragment {

    private PermissionOp permissionOp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionOp = new PermissionOp();
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
            BaseFilterFragment.this.onPermissionGranted(permissions);
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
