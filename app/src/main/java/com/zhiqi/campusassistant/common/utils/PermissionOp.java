package com.zhiqi.campusassistant.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ming.base.util.Log;
import com.ming.base.util.PermissionUtil;
import com.zhiqi.campusassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ming on 2016/10/20.
 * <p>
 * 权限操作基础类
 */

public class PermissionOp {

    private static final String TAG = "AbsPermissionOp";

    private static final String PACKAGE_URL_SCHEME = "package:";

    private static final int REQUEST_PERMISSION_CODE = 10001;

    private Activity mActivity;
    // 是否为必须权限
    private boolean must;
    // 正在请求
    private boolean isRequesting;

    private String[] allPermissions;

    private OnPermissionGrantedListener onPermissionGrantedListener;

    public boolean requestPermissions(Fragment fragment, boolean must, String... permissions) {
        if (fragment == null || permissions == null || isRequesting) {
            return false;
        }
        this.mActivity = fragment.getActivity();
        allPermissions = permissions;
        List<String> requestPermissions = checkPermission(fragment.getActivity(), permissions);
        Log.i(TAG, requestPermissions.toString());
        if (!requestPermissions.isEmpty()) {
            isRequesting = true;
            this.must = must;
            String[] requests = new String[requestPermissions.size()];
            requests = requestPermissions.toArray(requests);
            fragment.requestPermissions(requests, REQUEST_PERMISSION_CODE);
            return true;
        } else if (onPermissionGrantedListener != null) {
            onPermissionGrantedListener.onPermissionGranted(allPermissions);
        }
        return false;
    }

    /**
     * 请求权限
     *
     * @param activity
     * @param must
     * @param permissions
     * @return
     */
    public boolean requestPermissions(Activity activity, boolean must, String... permissions) {
        if (activity == null || permissions == null || isRequesting) {
            return false;
        }
        this.mActivity = activity;
        allPermissions = permissions;
        List<String> requestPermissions = checkPermission(activity, permissions);
        Log.i(TAG, requestPermissions.toString());
        if (!requestPermissions.isEmpty()) {
            isRequesting = true;
            this.must = must;
            String[] requests = new String[requestPermissions.size()];
            requests = requestPermissions.toArray(requests);
            ActivityCompat.requestPermissions(activity, requests, REQUEST_PERMISSION_CODE);
            return false;
        } else if (onPermissionGrantedListener != null) {
            onPermissionGrantedListener.onPermissionGranted(allPermissions);
        }
        return true;
    }

    /**
     * 请求权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_PERMISSION_CODE != requestCode) {
            return;
        }
        boolean gotoSetting = false;
        PackageManager pm = mActivity.getApplication().getPackageManager();
        StringBuffer unGrantPermission = new StringBuffer();
        for (int i = 0; i < grantResults.length; i++) {
            int grantResult = grantResults[i];
            String permission = permissions[i];
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                PermissionInfo info = null;
                try {
                    info = pm.getPermissionInfo(permission, 0);
                    PermissionGroupInfo pgi = pm.getPermissionGroupInfo(info.group, 0);
                    unGrantPermission.append(unGrantPermission.length() == 0 ? "" : "/");
                    unGrantPermission.append(pgi.loadLabel(pm).toString());
                } catch (Exception e) {
                    try {
                        if (info != null) {
                            unGrantPermission.append(info.loadLabel(pm).toString());
                        }
                    } catch (Exception ex) {
                    }
                }
                gotoSetting = true;
            }
        }
        Log.i(TAG, "unGrantPermission:" + unGrantPermission.toString());
        if (gotoSetting) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mActivity)
                    .title(R.string.common_tip_permission)
                    .cancelable(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                builder.content(mActivity.getString(R.string.common_tip_goto_appsetting1, unGrantPermission))
                        .positiveText(R.string.common_goto_setting)
                        .negativeText(R.string.common_cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse(PACKAGE_URL_SCHEME + mActivity.getPackageName()));
                                    mActivity.startActivity(intent);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

            } else {
                builder.content(mActivity.getString(R.string.common_tip_goto_appsetting2, unGrantPermission))
                        .negativeText(R.string.common_i_know);
            }
            builder.dismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (must) {
                        mActivity.finish();
                    }
                    isRequesting = false;
                }
            }).show();
        } else {
            isRequesting = false;
            if (onPermissionGrantedListener != null) {
                onPermissionGrantedListener.onPermissionGranted(allPermissions);
            }
        }
    }

    /**
     * 检查权限
     *
     * @param context
     * @param permissions
     * @return 没有获取到权限的权限名称列表
     */
    private List<String> checkPermission(Context context, String... permissions) {
        List<String> requestPermissions = new ArrayList<>();
        int permissionResult;
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            permissionResult = PermissionUtil.checkPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                requestPermissions.add(permission);
            }
        }
        return requestPermissions;
    }

    public void setOnPermissionGrantedListener(OnPermissionGrantedListener onPermissionGrantedListener) {
        this.onPermissionGrantedListener = onPermissionGrantedListener;
    }

    /**
     * 权限授予回调
     */
    public interface OnPermissionGrantedListener {
        void onPermissionGranted(String[] permissions);
    }
}
