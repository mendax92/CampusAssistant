package com.ming.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * 简单数据保存配置类<BR>
 */
public class Preference {
    private SharedPreferences preferences;

    public Preference(Context context, String name) {
        preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public boolean getBoolean(String key, boolean defValue) {
        if (preferences == null) {
            return defValue;
        }
        return preferences.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        if (preferences == null) {
            return defValue;
        }
        return preferences.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        if (preferences == null) {
            return defValue;
        }
        return preferences.getInt(key, defValue);
    }

    public Long getLong(String key, long defValue) {
        if (preferences == null) {
            return defValue;
        }
        return preferences.getLong(key, defValue);
    }

    public String getString(String key, String defValue) {
        if (preferences == null) {
            return defValue;
        }
        return preferences.getString(key, defValue);
    }

    public boolean setBoolean(String key, boolean value) {
        if (preferences == null) {
            return false;
        }
        return preferences.edit().putBoolean(key, value).commit();
    }

    public boolean setFloat(String key, float value) {
        if (preferences == null) {
            return false;
        }
        return preferences.edit().putFloat(key, value).commit();
    }

    public boolean setInt(String key, int value) {
        if (preferences == null) {
            return false;
        }
        return preferences.edit().putInt(key, value).commit();
    }

    public boolean setLong(String key, long value) {
        if (preferences == null) {
            return false;
        }
        return preferences.edit().putLong(key, value).commit();
    }

    public boolean setString(String key, String value) {
        if (preferences == null) {
            return false;
        }
        return preferences.edit().putString(key, value).commit();
    }

    public Map<String, ?> getAll() {
        if (preferences == null) {
            return null;
        }
        return preferences.getAll();
    }
}
