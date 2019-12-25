package com.zhiqi.campusassistant.core.payment.presenter;

import android.content.Context;

import com.ming.base.http.HttpManager;
import com.ming.base.util.Log;
import com.ming.base.util.RxUtil;
import com.ming.pay.IOnPayResult;
import com.ming.pay.IPayService;
import com.ming.pay.PayPlatform;
import com.ming.pay.PayServiceFactory;
import com.ming.pay.WeChatRequest;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.presenter.SimplePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.core.payment.entity.OrderInfo;
import com.zhiqi.campusassistant.core.payment.entity.OrderResult;
import com.zhiqi.campusassistant.core.payment.entity.PayStatus;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/7/30.
 * 支付基础presenter
 */

public class BasePayPresenter extends SimplePresenter {

    private static final String TAG = "BasePayPresenter";

    private static final int RETRY_TIMES = 5;

    public BasePayPresenter(Context context) {
        super(context);
    }


    /**
     * 微信支付
     *
     * @param orderObservable     订单observable
     * @param checkResultProvider 检查结果提供者
     * @param loadView
     */
    protected <T extends OrderInfo> void payWeChat(Observable<BaseResultData<T>> orderObservable,
                                                   final CheckResultProvider<T> checkResultProvider,
                                                   final ILoadView<OrderResult> loadView) {
        payWeChat(orderObservable, null, checkResultProvider, loadView);
    }

    /**
     * 微信支付
     *
     * @param orderObservable     订单observable
     * @param payResultObservable 支付结果observable
     * @param loadView
     */
    protected <T extends OrderInfo> void payWeChat(Observable<BaseResultData<T>> orderObservable,
                                                   final Observable<BaseResultData<OrderResult>> payResultObservable,
                                                   final ILoadView<OrderResult> loadView) {
        payWeChat(orderObservable, payResultObservable, null, loadView);
    }

    /**
     * 微信支付
     *
     * @param orderObservable     订单observable
     * @param payResultObservable 支付结果observable
     * @param checkResultProvider 检查结果提供者
     * @param loadView
     */
    private <T extends OrderInfo> void payWeChat(Observable<BaseResultData<T>> orderObservable,
                                                 final Observable<BaseResultData<OrderResult>> payResultObservable,
                                                 final CheckResultProvider<T> checkResultProvider,
                                                 final ILoadView<OrderResult> loadView) {
        final IPayService weChatService = PayServiceFactory.getPayService(PayPlatform.WECHAT);
        assert weChatService != null;
        if (!weChatService.isAppInstalled()) {
            if (!isReleased(loadView)) {
                RxUtil.postOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        loadView.onFailed(PayStatus.Unsupport.getValue(), mContext.getString(R.string.pay_un_support));
                    }
                });
            }
            return;
        }
        HttpManager.subscribe(orderObservable, new OnHttpFilterCallback<BaseResultData<T>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<T> result) {
                try {
                    PayReq req = result.data.convert2WechatReq();
                    WeChatRequest request = new WeChatRequest(req);
                    Observable<BaseResultData<OrderResult>> checkResultObservable = payResultObservable;
                    if (payResultObservable == null && checkResultProvider != null) {
                        checkResultObservable = checkResultProvider.provideObservable(result.data);
                    }
                    if (checkResultObservable == null) {
                        throw new NullPointerException("CheckResultObservable can't be null.");
                    }
                    weChatService.pay(request, new PayResultCallback(checkResultObservable, loadView));
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(PayStatus.Error.getValue(), mContext.getString(R.string.pay_exception));
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


    public class PayResultCallback implements IOnPayResult {

        private ILoadView<OrderResult> loadView;

        private Observable<BaseResultData<OrderResult>> payResultObservable;

        public PayResultCallback(Observable<BaseResultData<OrderResult>> payResultObservable, ILoadView<OrderResult> loadView) {
            this.payResultObservable = payResultObservable;
            this.loadView = loadView;
        }

        @Override
        public void onSuccess() {
            Log.i(TAG, "Success");
            loadResult(0);
        }

        @Override
        public void onCancel(String errStr) {
            Log.i(TAG, "Cancel : " + errStr);
            if (!isReleased(loadView)) {
                loadView.onFailed(PayStatus.Cancel.getValue(), mContext.getString(R.string.pay_cancel));
            }
        }

        @Override
        public void onError(int errCode, String errStr) {
            Log.i(TAG, "errCode:" + errCode + ", errStr:" + errStr);
            loadResult(0);
        }

        private void loadResult(final int times) {
            Log.i(TAG, "retry times:" + times);
            HttpManager.subscribe(payResultObservable, new OnHttpFilterCallback<BaseResultData<OrderResult>>(mContext) {
                @Override
                public void onSuccess(BaseResultData<OrderResult> result) {
                    final OrderResult orderResult = result.data;
                    if (orderResult.order_status == PayStatus.Unpaid) {
                        if (!retry() && !isReleased(loadView)) {
                            loadView.onFailed(PayStatus.Unpaid.getValue(), mContext.getString(R.string.pay_exception));
                        }
                    } else {
                        if (!isReleased(loadView)) {
                            if (orderResult.order_status == PayStatus.Success) {
                                loadView.onLoadData(orderResult);
                            } else {
                                loadView.onFailed(orderResult.order_status.getValue(), orderResult.description);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(final int errorCode, final String message) {
                    if (!retry() && !isReleased(loadView)) {
                        loadView.onFailed(errorCode, message);
                    }
                }

                private boolean retry() {
                    try {
                        if (times <= RETRY_TIMES) {
                            RxUtil.postOnIoThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadResult(times + 1);
                                }
                            }, 3000);
                            return true;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return false;
                }
            });
        }
    }

    public interface CheckResultProvider<T extends OrderInfo> {
        Observable<BaseResultData<OrderResult>> provideObservable(T orderInfo);
    }
}
