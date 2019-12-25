package com.zhiqi.campusassistant.config;

import android.os.Environment;
import android.text.TextUtils;

import com.ming.base.util.Log;
import com.zhiqi.campusassistant.app.AssistantApplication;

import java.io.File;

/**
 * Created by ming on 2016/10/19.
 */

public class AppPathConfig {

    private static final String TAG = "AppPathConfig";

    /**
     * 设备存储路径
     */
    public static final String DEVICE_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * app缓存路径
     */
    public static String APP_CACHE_PATH;

    /**
     * 应用路径根目录
     */
    public static final String APP_STORAGE_PATH = DEVICE_STORAGE_PATH + "/CampusAssistant";

    /**
     * 设备id路径
     */
    public static final String UUID_PATH = APP_STORAGE_PATH + "/config.ini";

    /**
     * 应用图片路径
     */
    public static final String APP_CACHE_IMAGE = getCachePath() + "/images";

    /**
     * 升级路径
     */
    public static final String APP_UPGRADE_PATH = getCachePath() + "/upgrade";

    /**
     * 二维码缓存路径
     */
    public static final String APP_CACHE_QRCODE = getCachePath() + "/QrCode";

    private static String getCachePath() {
        if (APP_CACHE_PATH == null) {
            try {
                File file = AssistantApplication.getInstance().getExternalCacheDir();
                if (file != null) {
                    APP_CACHE_PATH = file.getAbsolutePath();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (TextUtils.isEmpty(APP_CACHE_PATH)) {
                    File file = AssistantApplication.getInstance().getCacheDir();
                    if (file != null) {
                        APP_CACHE_PATH = file.getAbsolutePath();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, "APP_CACHE_PATH:" + APP_CACHE_PATH);
        }
        return APP_CACHE_PATH;
    }

}
