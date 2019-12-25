package com.ming.base.http.interfaces;

/**
 * Created by Edmin on 2016/9/4 0004.
 * http基本回调处理
 */
public interface OnHttpCallback<T> {
    void onFinished(T result);

    void onFailure(Throwable t);
}
