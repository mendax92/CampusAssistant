package com.ming.base.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ming on 2017/8/23.
 * json多态
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface JsonTypeInfo {

    /**
     * json中节点值
     *
     * @return
     */
    String property();

    /**
     * 子类
     *
     * @return
     */
    SubType[] subType();
}
