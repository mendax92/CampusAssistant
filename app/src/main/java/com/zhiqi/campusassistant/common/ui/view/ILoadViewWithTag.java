package com.zhiqi.campusassistant.common.ui.view;

/**
 * Created by ming on 2016/12/19.
 */

public interface ILoadViewWithTag<T, TAG> {

    void onLoadData(T data, TAG tag);

    void onFailed(int errorCode, String message, TAG tag);
}

