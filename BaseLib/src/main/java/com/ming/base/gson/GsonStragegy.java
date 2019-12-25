package com.ming.base.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by ming on 2017/3/4.
 */

public class GsonStragegy {

    /**
     * 获取序列化忽略规则
     *
     * @return
     */
    public static ExclusionStrategy getSerializeIgnoreStrategy() {
        ExclusionStrategy strategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                Ignore ignore = f.getAnnotation(Ignore.class);
                return ignore != null && ignore.serialize();
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                Ignore ignore = clazz.getAnnotation(Ignore.class);
                return ignore != null && ignore.serialize();
            }
        };
        return strategy;
    }

    /**
     * 获取反序列化忽略规则
     *
     * @return
     */
    public static ExclusionStrategy getDeserializeIgnoreStrategy() {
        ExclusionStrategy strategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                Ignore ignore = f.getAnnotation(Ignore.class);
                return ignore != null && ignore.deserialize();
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                Ignore ignore = clazz.getAnnotation(Ignore.class);
                return ignore != null && ignore.deserialize();
            }
        };
        return strategy;
    }
}
