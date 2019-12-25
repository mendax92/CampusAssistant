package com.ming.base.util;

import android.text.TextUtils;

import java.lang.reflect.Field;

/**
 * Created by ming on 2017/2/13.
 * 反射工具类
 */

public class ReflectUtil {

    /**
     * 根据对象和字段名称获取值
     *
     * @param obj
     * @param fieldName
     * @param <T>
     * @param <O>
     * @return
     */
    public static <T, O> T getValue(O obj, String fieldName) {
        if (obj == null || TextUtils.isEmpty(fieldName)) {
            return null;
        }
        try {
            Field field = getDeclaredField(obj, fieldName);
            if (field != null) {
                field.setAccessible(true);
                return (T) field.get(obj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 设置字段值
     *
     * @param obj
     * @param fieldName
     * @param value
     * @param <T>
     * @param <O>
     */
    public static <T, O> void setValue(O obj, String fieldName, T value) {
        if (obj == null || TextUtils.isEmpty(fieldName)) {
            return;
        }
        try {
            Field field = getDeclaredField(obj, fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, value);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @return 父类中的属性对象
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;

        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {
            }
        }

        return null;
    }
}
