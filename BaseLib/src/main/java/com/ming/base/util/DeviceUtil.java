package com.ming.base.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.net.NetworkInterface;

public class DeviceUtil {

    private static final String TAG = "DeviceUtil";

    /**
     * 获取屏幕宽和高，单位为像素
     */
    public static int[] getWindWidthAndHeight(Context context) {
        int num[] = new int[2];
        DisplayMetrics metrics = new DisplayMetrics();
        if (context != null) {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(metrics);
        }
        num[0] = metrics.widthPixels;
        num[1] = metrics.heightPixels;
        return num;
    }

    /**
     * 获取设备类型，A1为android 手机, A2为android pad
     */
    public static String getDeviceType(Context context) {
        String reType = "A1";
        if (context != null) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
            double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
            double screenInches = Math.sqrt(x + y);
            if (screenInches > 6.0) {
                reType = "A2";
            }
        }
        return reType;
    }

    /**
     * 描述:(dp转像素)
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 判断sd卡是否可用
     */
    public static boolean haveSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static int getPackageVersion(Context context) {
        int versionCode = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取应用版本名称
     *
     * @param context
     * @return
     */
    public static String getPackageVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getNetworkOperator(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkOperator();
    }

    /**
     * 获取手机服务商名称
     *
     * @param context
     * @return
     */
    public static String getNetworkOperatorName(Context context) {
        String IMSI = getNetworkOperator(context);
        if (TextUtils.isEmpty(IMSI)) {
            return "unknown";
        }
        String networkOperatorName = IMSI;
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
                || IMSI.startsWith("46007") || IMSI.startsWith("46008")) {
            networkOperatorName = "China Mobile";
        } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")
                || IMSI.startsWith("46009")) {
            networkOperatorName = "China Unicom";
        } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005")
                || IMSI.startsWith("46011")) {
            networkOperatorName = "China Telecom";
        } else if (IMSI.startsWith("46020")) {
            networkOperatorName = "China Tietong";
        } else if (IMSI.equals("46004")) {
            networkOperatorName = "Global Star Satellite";
        }
        return networkOperatorName;
    }

    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        if (android.os.Build.VERSION.SDK_INT < 23) {
            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wifi.getConnectionInfo().getMacAddress();
        } else {
            try {
                return getLocalMacAddressByReflect(context);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    public static String getDeviceId(Context context) {
        String androidDeviceId = null;

        // get internal android device id
        try {
            androidDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(androidDeviceId)) {
            Log.i(TAG, "getDeviceId androidDeviceId:" + androidDeviceId);
            return androidDeviceId;
        }

        String telephonyDeviceId = null;

        // get telephony id
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyDeviceId = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getDeviceId telephonyDeviceId:" + telephonyDeviceId);
        return androidDeviceId;
    }

    private static String getLocalMacAddressByReflect(Context context) {
        try {
            String interfaceName = SystemPropertiesProxy.get(context, "wifi.interface", "wlan0");
            NetworkInterface interfaces = NetworkInterface.getByName(interfaceName);
            byte[] mac = interfaces.getHardwareAddress();
            if (mac == null) {
                return null;
            }
            StringBuilder buf = new StringBuilder();
            for (byte aMac : mac) {
                buf.append(String.format("%02X:", aMac));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            return buf.toString();
        } catch (Exception ex) {
        } // for now eat exceptions
        return null;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return
     */
    public static final boolean isOpenGPS(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }


    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
