package com.zhiqi.campusassistant.common.http;

import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ming.base.retrofit2.request.NameValuePair;
import com.ming.base.util.GsonUtils;
import com.ming.base.util.Log;
import com.ming.base.util.MD5Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okio.Buffer;

/**
 * Created by ming on 2016/11/18.
 */

public class SignUtil {

    private static final String TAG = "SignUtil";

    /**
     * 对象转参数
     *
     * @param object
     * @return
     */
    public static List<NameValuePair> parseObject(Object object) {
        List<NameValuePair> pairs = new ArrayList<>();
        String json = GsonUtils.toJson(object);
        JsonObject jsonObject = GsonUtils.toJsonObject(json);
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            JsonElement element = entry.getValue();
            String value = GsonUtils.getString(element);
            if (!TextUtils.isEmpty(value)) {
                pairs.add(new NameValuePair(entry.getKey(), value));
            }
        }
        return pairs;
    }


    /**
     * 添加参数
     *
     * @param originalPairs
     * @param addPairs
     * @return
     */
    public static List<NameValuePair> addValuePairs(List<NameValuePair> originalPairs, List<NameValuePair> addPairs) {
        if (originalPairs == null) {
            return addPairs;
        }
        if (addPairs == null || addPairs.isEmpty()) {
            return originalPairs;
        }
        for (NameValuePair pair : addPairs) {
            for (NameValuePair originalPair : addPairs) {
                if (originalPair.getName().equals(pair.getName())) {
                    break;
                }
            }
            originalPairs.add(pair);
        }
        return originalPairs;
    }

    /**
     * 排序
     *
     * @param originalPairs
     * @return
     */
    public static void sortNameValuePair(List<NameValuePair> originalPairs) {
        if (originalPairs == null || originalPairs.isEmpty()) {
            return;
        }
        Collections.sort(originalPairs, new Comparator<NameValuePair>() {
            @Override
            public int compare(NameValuePair nameValuePair, NameValuePair t1) {
                return nameValuePair.getName().compareTo(t1.getName());
            }
        });
    }

    /**
     * 生成签名串
     *
     * @param pairs
     * @param appSecret
     * @return
     */
    public static String generateSign(List<NameValuePair> pairs, String appSecret, String token) {
        sortNameValuePair(pairs);
        Buffer buffer = new Buffer();
        String sign = null;
        try {
            NameValuePair pair;
            for (int i = 0, size = pairs.size(); i < size; i++) {
                pair = pairs.get(i);
                if (i > 0) {
                    buffer.writeByte('&');
                }
                buffer.writeUtf8(pair.getName());
                buffer.writeByte('=');
                buffer.writeUtf8(pair.getValue());
            }
            buffer.writeUtf8(appSecret);
            if (!TextUtils.isEmpty(token)) {
                buffer.writeUtf8(token);
            }
            byte[] signArray = buffer.readByteArray();
            Log.i(TAG, "Before sign:" + new String(signArray));
            sign = MD5Util.getMD5Value(signArray);
            Log.i(TAG, "After sign:" + sign);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            buffer.close();
        }
        return sign;
    }
}
