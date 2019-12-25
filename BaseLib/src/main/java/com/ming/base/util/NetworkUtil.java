package com.ming.base.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 网络工具类
 */
public class NetworkUtil {

    public enum NetworkType {
        NETWORK_NULL, NETWORK_UNKNOWN, NETWORK_WIFI, NETWORK_2G, NETWORK_3G, NETWORK_4G
    }

    /**
     * 判断是否支持WIFI
     *
     * @param context
     * @return true表示不支持，false表示不支持
     */
    public static boolean isSupportWIFI(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null;
    }

    /**
     * [wifi是否可用]<br/>
     * 功能详细描述
     *
     * @param context
     * @return
     */
    public static boolean isWifiEnable(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    /**
     * 判断当前是否有可用的网络,2G网络、3G网络、wifi可使用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cwjManager == null) {
            return false;
        }
        NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

    public static NetworkType getMobileNetworkType(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NetworkType.NETWORK_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NetworkType.NETWORK_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NetworkType.NETWORK_4G;
            default:
                return NetworkType.NETWORK_UNKNOWN;
        }
    }

    public static NetworkType getMobileNetworkType(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = telMgr.getNetworkType();
        return getMobileNetworkType(networkType);
    }

    /**
     * 判断是否支持GPRS
     *
     * @param context
     * @return true表示是GPRS，否则不是GPRS
     */
    public static boolean isSupport2G(Context context) {
        NetworkType type = getMobileNetworkType(context);
        return type == NetworkType.NETWORK_2G || type == NetworkType.NETWORK_4G || type == NetworkType.NETWORK_4G;
    }

    /**
     * 判断是否支持3G网络
     *
     * @param context
     * @return true表示是3G，否则不是3G
     */
    public static boolean isSupport3G(Context context) {
        NetworkType type = getMobileNetworkType(context);
        return type == NetworkType.NETWORK_4G || type == NetworkType.NETWORK_4G;
    }

    /**
     * 判断是否支持4G网络
     *
     * @param context
     * @return true表示是4G，否则不是4G
     */
    public static boolean isSupport4G(Context context) {
        NetworkType type = getMobileNetworkType(context);
        return type == NetworkType.NETWORK_4G;
    }

    /**
     * 判断3G是否可用
     *
     * @param context
     * @return true 可用， false不可用
     */
    public static boolean is3GConnected(Context context) {

        if (!isSupport3G(context)) {
            return false;
        }

        ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        int netType = info.getType();
        int netSubtype = info.getSubtype();

        if (netType != ConnectivityManager.TYPE_MOBILE) {
            return false;
        }

        NetworkType type = getMobileNetworkType(netSubtype);
        return type == NetworkType.NETWORK_3G || type == NetworkType.NETWORK_4G;
    }

    /**
     * 判断GPRS是否可用
     *
     * @param context
     * @return true 可用， false不可用
     */
    public static boolean is2GConnected(Context context) {

        if (!isSupport2G(context)) {
            return false;
        }

        ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        int netType = info.getType();
        int netSubtype = info.getSubtype();
        if (netType != ConnectivityManager.TYPE_MOBILE) {
            return false;
        }
        NetworkType type = getMobileNetworkType(netSubtype);
        return type == NetworkType.NETWORK_2G || type == NetworkType.NETWORK_3G || type == NetworkType.NETWORK_4G;
    }

    /**
     * 判断Wifi是否可用
     *
     * @param context
     * @return true 可用， false不可用
     */
    public static boolean isWifiConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断网络连接的类型
     *
     * @param context
     * @return
     */
    public static NetworkType getNetworkType(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return NetworkType.NETWORK_NULL;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info == null) {
            return NetworkType.NETWORK_NULL;
        }
        if (info.isConnected()) {
            final int type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                return NetworkType.NETWORK_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                return getMobileNetworkType(info.getSubtype());
            }
        }
        return NetworkType.NETWORK_NULL;
    }

    /**
     * 设置数据流量是否可用
     *
     * @param context
     * @param enable
     */
    public static void setMobileDataEnable(Context context, boolean enable) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            Class<?> conmanClass = Class.forName(connectivityManager.getClass().getName());
            Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            Object iConnectivityManager = iConnectivityManagerField.get(connectivityManager);
            Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled",
                    Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enable);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取当前已连接的wifi热点的名称
     *
     * @param ctx
     * @return
     */
    public static String getCurrConnectWifiSSID(Context ctx) {
        if (ctx == null) {
            return null;
        }
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            return wifiInfo.getSSID();
        }
        return null;
    }

    public static void setWifiEnable(Context ctx, boolean enable) {
        if (ctx == null) {
            return;
        }
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(enable);
    }

    /**
     * 是否已经连接上网络
     *
     * @param context
     * @return
     * @author Jijia.Deng
     */
    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cwjManager == null) {
            return false;
        }
        NetworkInfo networkInfo = cwjManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }
}
