package com.ming.photopicker.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * Created by donglua on 2016/10/19.
 */

public class PermissionsUtils {

    public static boolean checkReadStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        int readStoragePermissionState = checkPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        boolean readStoragePermissionGranted = readStoragePermissionState == PackageManager.PERMISSION_GRANTED;

        if (!readStoragePermissionGranted) {
            ActivityCompat.requestPermissions(activity,
                    PermissionsConstant.PERMISSIONS_EXTERNAL_READ,
                    PermissionsConstant.REQUEST_EXTERNAL_READ);
        }
        return readStoragePermissionGranted;
    }

    public static boolean checkWriteStoragePermission(Fragment fragment) {

        int writeStoragePermissionState = checkPermission(fragment.getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        boolean writeStoragePermissionGranted = writeStoragePermissionState == PackageManager.PERMISSION_GRANTED;

        if (!writeStoragePermissionGranted) {
            fragment.requestPermissions(PermissionsConstant.PERMISSIONS_EXTERNAL_WRITE,
                    PermissionsConstant.REQUEST_EXTERNAL_WRITE);
        }
        return writeStoragePermissionGranted;
    }

    public static boolean checkCameraPermission(Fragment fragment) {
        int cameraPermissionState = checkPermission(fragment.getContext(), android.Manifest.permission.CAMERA);

        boolean cameraPermissionGranted = cameraPermissionState == PackageManager.PERMISSION_GRANTED;

        if (!cameraPermissionGranted) {
            fragment.requestPermissions(PermissionsConstant.PERMISSIONS_CAMERA,
                    PermissionsConstant.REQUEST_CAMERA);
        }
        return cameraPermissionGranted;
    }

    public static int checkPermission(Context context, String permission) {
        if (context == null || permission == null) {
            return PackageManager.PERMISSION_GRANTED;
        }
        if (Build.VERSION.SDK_INT < 23) {
            return checkOp(context, permission);
        } else {
            return ContextCompat.checkSelfPermission(context, permission);
        }
    }

    /**
     * 检查应用权限
     *
     * @param context   上下文
     * @param permision 权限对应的AppOps常量
     * @return 是否有该权限
     */
    private static int checkOp(Context context, String permision) {

        int mode = PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 || Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return mode;
        }
        try {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class<AppOpsManager> appOpsManagerClass = AppOpsManager.class;
            Method checkOpMethod = appOpsManagerClass.getDeclaredMethod("permissionToOp", String.class);
            checkOpMethod.setAccessible(true);
            String opStr = (String) checkOpMethod.invoke(appOpsManager, permision);
            if (!TextUtils.isEmpty(opStr)) {
                checkOpMethod = appOpsManagerClass.getDeclaredMethod("checkOp", String.class, int.class, String.class);
                checkOpMethod.setAccessible(true);
                mode = (int) checkOpMethod.invoke(appOpsManager, opStr, Process.myUid(), context.getPackageName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mode != PackageManager.PERMISSION_GRANTED ? PackageManager.PERMISSION_DENIED : mode;
    }

}
