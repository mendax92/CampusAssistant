package com.ming.base.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * Created by Edmin
 */
public class PermissionUtil {

    private static final String TAG = "PermissionUtil";

    public static int checkPermission(Context context, String permission) {
        if (context == null || permission == null) {
            return PackageManager.PERMISSION_GRANTED;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
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
        Log.d(TAG, "checkPermissionMode api level=" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2 || Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return mode;
        }
        try {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Class<AppOpsManager> appOpsManagerClass = AppOpsManager.class;
            Method checkOpMethod = appOpsManagerClass.getDeclaredMethod("permissionToOp", String.class);
            checkOpMethod.setAccessible(true);
            String opStr = (String) checkOpMethod.invoke(appOpsManager, permision);
            Log.i(TAG, "opStr:" + opStr);
            if (!TextUtils.isEmpty(opStr)) {
                checkOpMethod = appOpsManagerClass.getDeclaredMethod("checkOp", String.class, int.class, String.class);
                checkOpMethod.setAccessible(true);
                mode = (int) checkOpMethod.invoke(appOpsManager, opStr, Process.myUid(), context.getPackageName());
                Log.d(TAG, "checkPermissionMode mode=" + mode + " api level=" + Build.VERSION.SDK_INT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mode != PackageManager.PERMISSION_GRANTED ? PackageManager.PERMISSION_DENIED : mode;
    }
}
