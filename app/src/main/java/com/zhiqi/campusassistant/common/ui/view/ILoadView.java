package com.zhiqi.campusassistant.common.ui.view;

/**
 * Created by Edmin on 2016/9/4 0004.
 * view加载回调
 */
public interface ILoadView<T> {

    void onLoadData(T data);

    void onFailed(int errorCode, String message);
}
