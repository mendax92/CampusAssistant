package com.zhiqi.campusassistant.common.presenter;

import android.content.Context;

import com.ming.base.http.HttpManager;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/1/19.
 * 基础presenter
 */

public class SimplePresenter extends BasePresenter {

    protected Context mContext;

    public SimplePresenter(Context context) {
        mContext = context.getApplicationContext();
    }


    /**
     * 简单处理网络回调
     *
     * @param observable
     * @param loadView
     * @param <T>
     */
    protected <T> void subscribe(Observable<BaseResultData<T>> observable, final ILoadView<T> loadView) {
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<T>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<T> result) {
                if (!isReleased(loadView)) {
                    loadView.onLoadData(result.data);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onFailed(errorCode, message);
                }
            }
        });
    }

    /**
     * 简单处理请求（只关心结果error_coe,message）
     *
     * @param observable
     * @param loadView
     * @param <T>
     */
    protected <T> void request(Observable<BaseResultData<T>> observable, final IRequestView loadView) {
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<T>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<T> result) {
                if (!isReleased(loadView)) {
                    loadView.onQuest(result.error_code, result.message);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onQuest(errorCode, message);
                }
            }
        });
    }

    /**
     * 简单处理请求，无回调数据（只有结果error_coe,message）
     *
     * @param observable
     * @param loadView
     */
    protected void requestSimple(Observable<BaseResultData> observable, final IRequestView loadView) {
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData>(mContext) {
            @Override
            public void onSuccess(BaseResultData result) {
                if (!isReleased(loadView)) {
                    loadView.onQuest(result.error_code, result.message);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onQuest(errorCode, message);
                }
            }
        });
    }

}
