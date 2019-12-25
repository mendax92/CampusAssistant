package com.zhiqi.campusassistant.core.payment.presenter;

import android.content.Context;

import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.core.payment.api.SelfPayApiService;
import com.zhiqi.campusassistant.core.payment.entity.ExpenseInfo;
import com.zhiqi.campusassistant.core.payment.entity.OrderDetail;
import com.zhiqi.campusassistant.core.payment.entity.OrderInfo;
import com.zhiqi.campusassistant.core.payment.entity.OrderResult;
import com.zhiqi.campusassistant.core.payment.entity.PayType;
import com.zhiqi.campusassistant.core.payment.entity.SelfPaidInfo;
import com.zhiqi.campusassistant.core.payment.entity.StudentExpense;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/7/23.
 * 自助缴费
 */

public class SelfPayPresenter extends BasePayPresenter {

    private SelfPayApiService mService;

    public SelfPayPresenter(Context context, SelfPayApiService service) {
        super(context);
        mService = service;
    }

    /**
     * 获取学生支付信息
     *
     * @param loadView
     */
    public void getStudentExpense(final int page, final ILoadView<BasePageData<StudentExpense>> loadView) {
        Observable<BaseResultData<BasePageData<StudentExpense>>> observable = mService.getStudentExpense(page, AppConfigs.DEFAULT_PAGE_SIZE);
        subscribe(observable, loadView);
    }

    /**
     * 加载消费支付消息
     *
     * @param expenseId
     * @param loadView
     */
    public void getPayInfo(long expenseId, final ILoadView<ExpenseInfo> loadView) {
        Observable<BaseResultData<ExpenseInfo>> observable = mService.getExpenseInfo(expenseId);
        subscribe(observable, loadView);
    }

    /**
     * 支付
     *
     * @param expenseId
     * @param payType
     * @param loadView
     */
    public boolean pay(long expenseId, PayType payType, ILoadView<OrderResult> loadView) {
        switch (payType) {
            case WECHAT:
                payWeChat(expenseId, loadView);
                return true;
        }
        return false;
    }

    private void payWeChat(final long expenseId, final ILoadView<OrderResult> loadView) {
        Observable<BaseResultData<OrderInfo>> orderObservable = mService.getOrderInfo(expenseId);
        Observable<BaseResultData<OrderResult>> payResultObservable = mService.getOrderResult(expenseId);
        payWeChat(orderObservable, payResultObservable, loadView);
    }

    /**
     * 获取缴费明细列表
     *
     * @param page
     * @param loadView
     */
    public void getPaidList(int page, final ILoadView<BasePageData<SelfPaidInfo>> loadView) {
        Observable<BaseResultData<BasePageData<SelfPaidInfo>>> observable = mService.getSelfPaidInfo(page, AppConfigs.DEFAULT_PAGE_SIZE);
        subscribe(observable, loadView);
    }

    /**
     * 获取支付详情
     *
     * @param orderId
     * @param loadView
     */
    public void getOrderDetail(long orderId, final ILoadView<OrderDetail> loadView) {
        Observable<BaseResultData<OrderDetail>> observable = mService.getOrderDetail(orderId);
        subscribe(observable, loadView);
    }
}
