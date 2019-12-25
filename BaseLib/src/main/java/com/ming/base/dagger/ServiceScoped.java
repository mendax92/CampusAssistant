package com.ming.base.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Edmin on 2016/8/28 0028.
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceScoped {
}
