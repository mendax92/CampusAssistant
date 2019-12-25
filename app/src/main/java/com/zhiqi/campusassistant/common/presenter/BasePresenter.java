package com.zhiqi.campusassistant.common.presenter;

import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.ILoadViewWithTag;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;

/**
 * Created by ming on 2017/1/19.
 * 基础presenter
 */

public class BasePresenter {

    protected boolean released;

    protected boolean isReleased(ILoadView<?> view) {
        return view == null || released;
    }

    protected boolean isReleased(IRequestView view) {
        return view == null || released;
    }

    protected boolean isReleased(ILoadViewWithTag<?, ?> view) {
        return view == null || released;
    }

    public void release() {
        this.released = true;
    }
}
