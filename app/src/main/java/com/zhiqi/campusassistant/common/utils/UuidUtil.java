package com.zhiqi.campusassistant.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ming.base.util.DeviceUtil;
import com.ming.base.util.FileUtil;
import com.ming.base.util.Log;
import com.zhiqi.campusassistant.config.AppPathConfig;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取设备唯一标识
 */
public class UuidUtil {
    private static final String TAG = "UuidUtil";
    private static final String PREFERENCE_UUID = "PREFERENCE_UUID";
    private static final String KEY_UUID = "UUID";
    private static String UUID_INSTANCE = null;

    private static final String[] INVALID_MAC_VALUES = new String[]{
            "000000000000", "00:00:00:00:00:00",
            "020000000000", "02:00:00:00:00:00",
            "ffffffffffff", "ff:ff:ff:ff:ff:ff",
            "FFFFFFFFFFFF", "FF:FF:FF:FF:FF:FF"};

    private UuidUtil() {
    }

    /**
     * 获取设备的唯一标识UUID； UUID由系统自动生成，同时保存在SD卡和ROM中 以确保用户卸载程序或更换SD卡后重复生成
     *
     * @return
     */
    public static String getUuid(Context context) {
        if (UUID_INSTANCE == null) {
            synchronized (TAG) {
                if (UUID_INSTANCE == null) {
                    UUID_INSTANCE = getUuidFromStorage(context);
                }
            }
        }
        return UUID_INSTANCE;
    }

    /**
     * 1.首先从rom中获取保存的uid；
     * 2.若上一步为空，则获取设备MAC地址，并转化为小写；
     * 3.若上一步为空，则从文件中获取保存的uid，并判断是否合法；
     * 4.若上一步为空，则重新生成唯一的uuid；
     *
     * @param context
     * @return
     */
    private static String getUuidFromStorage(Context context) {
        // 1.首先从rom中取得uid
        String id = getUuidFromRom(context);
        Log.d(TAG, "getUuidFromStorage getUuidFromRom id: " + id);
        if (!TextUtils.isEmpty(id)) {
            if (!FileUtil.isFileExist(AppPathConfig.UUID_PATH)) {
                // 保存至文件中
                saveUuidToSDcard(id);
            }
        } else {
            // 2.如果rom中获取的uid为空，则获取mac地址
            id = getLocalMacAddress(context);
            Log.d(TAG, "getLocalMacAddress id:" + id);
            if (!checkID(id)) {
                id = DeviceUtil.getDeviceId(context);
                Log.d(TAG, "getDeviceId:" + id);
            }
            if (!TextUtils.isEmpty(id)) {
                saveUuidToSDcard(id);
            } else {
                // 3.从sdcard取得uid，并且判断是否合法
                id = getUuidFromSDcard();
                Log.d(TAG, "getUuidFromStorage getUuidFromSDcard id: " + id);
                if (!checkID(id)) {
                    // 4.sd卡上的uuid不规范，重新生成uuid；
                    id = createUuid();
                    Log.d(TAG, "getUuidFromStorage createUuid id: " + id);
                    saveUuidToSDcard(id);
                }
            }
            // 保存至rom中
            saveUuidToRom(context, id);
        }
        return id;
    }

    private static String createUuid() {
        // 修改策略，如果能获取到系统的mac地址作为设备唯一标识，否则沿用之前逻辑；
        return formatUUID(UUID.randomUUID().toString());
    }

    private static String getUuidFromRom(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFERENCE_UUID, Context.MODE_PRIVATE);
        String id = sp.getString(KEY_UUID, null);
        return id;
    }

    private static void saveUuidToRom(Context ctx, String id) {
        if (id == null) {
            return;
        }
        SharedPreferences sp = ctx.getSharedPreferences(PREFERENCE_UUID, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_UUID, id).commit();
    }

    /**
     * 从SD卡中取得保存的UUID
     *
     * @return
     */
    private static String getUuidFromSDcard() {
        try {
            return FileUtil.readFile(AppPathConfig.UUID_PATH).toString();
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * 保存UUID到SD中
     *
     * @param id
     */
    private static void saveUuidToSDcard(String id) {
        String path = AppPathConfig.UUID_PATH;
        if (path == null || id == null) {
            return;
        }
        FileUtil.writeFile(path, id, false);
    }

    /**
     * 去掉自动生成的UUID中无用的字符
     *
     * @param uuid
     * @return
     */
    private static String formatUUID(String uuid) {
        if (uuid != null) {
            return formatChar(uuid).toLowerCase();
        }
        return uuid;
    }

    private static String formatChar(String source) {
        if (source != null) {
            Pattern p = Pattern.compile("[^a-zA-Z0-9]");
            return p.matcher(source).replaceAll("");
        }
        return null;
    }

    private static String getLocalMacAddress(Context context) {
        String id = DeviceUtil.getMacAddress(context);
        // 检查是否获取的是非法的串
        if (!checkMac(id)) {
            id = null;
        }
        return id != null ? formatChar(id).toLowerCase() : null;
    }


    private static boolean checkMac(String mac) {
        if (TextUtils.isEmpty(mac)) {
            return false;
        }
        Pattern p = Pattern.compile("^([0-9a-fA-F]{2})(([/\\s:-][0-9a-fA-F]{2}){5})$");
        Matcher matcher = p.matcher(mac);
        if (matcher.find()) {
            for (String invalid : INVALID_MAC_VALUES) {
                if (invalid.equals(mac)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 检测UUID是否规范
     *
     * @param id
     * @return
     */
    private static boolean checkID(String id) {
        if (TextUtils.isEmpty(id)) {
            return false;
        }
        for (String invalidId : INVALID_MAC_VALUES) {
            if (id.equals(invalidId)) {
                return false;
            }
        }
        if (id.length() == 32) {
            // 生成的软id uuid；
            Pattern p = Pattern.compile("[0-9|a-f]{32}");
            Matcher matcher = p.matcher(id);
            return matcher.find();
        } else {
            // mac地址
            Pattern p = Pattern.compile("[0-9a-fA-F]{12}");
            Matcher matcher = p.matcher(id);
            return matcher.find();
        }
    }
}
