package com.ming.base.gson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ming on 2017/8/23.
 * json子类
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface SubType {

    /**
     * 子类类型
     *
     * @return
     */
    Class<?> value();

    /**
     * json中节点名称
     *
     * @return
     */
    String property();
}
