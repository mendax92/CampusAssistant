package com.zhiqi.campusassistant.core.payment.api;

import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.payment.entity.ExpenseInfo;
import com.zhiqi.campusassistant.core.payment.entity.OrderDetail;
import com.zhiqi.campusassistant.core.payment.entity.OrderInfo;
import com.zhiqi.campusassistant.core.payment.entity.OrderResult;
import com.zhiqi.campusassistant.core.payment.entity.SelfPaidInfo;
import com.zhiqi.campusassistant.core.payment.entity.StudentExpense;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ming on 2017/7/23.
 */

public interface SelfPayApiService {

    /**
     * 获取学生支付列表
     *
     * @return
     */
    @GET(HttpUrlConstant.GET_STUDENT_EXPENSE)
    Observable<BaseResultData<BasePageData<StudentExpense>>> getStudentExpense(@Query("page_no") int page, @Query("page_size") int pageSize);

    /**
     * 获取支付信息
     *
     * @param expenseId
     * @return
     */
    @GET(HttpUrlConstant.GET_EXPENSE_INFO)
    Observable<BaseResultData<ExpenseInfo>> getExpenseInfo(@Query("expense_id") long expenseId);

    /**
     * 获取订单信息
     *
     * @param expenseId
     * @return
     */
    @GET(HttpUrlConstant.GET_STUDENT_ORDER_INFO)
    Observable<BaseResultData<OrderInfo>> getOrderInfo(@Query("expense_id") long expenseId);

    /**
     * 获取订单结果
     *
     * @param expenseId
     * @return
     */
    @GET(HttpUrlConstant.GET_STUDENT_ORDER_RESULT)
    Observable<BaseResultData<OrderResult>> getOrderResult(@Query("expense_id") long expenseId);

    /**
     * 获取自助缴费完成列表
     *
     * @return
     */
    @GET(HttpUrlConstant.GET_SELF_ORDER_LIST)
    Observable<BaseResultData<BasePageData<SelfPaidInfo>>> getSelfPaidInfo(@Query("page_no") int page, @Query("page_size") int pageSize);

    /**
     * 获取支付详情
     *
     * @param orderId
     * @return
     */
    @GET(HttpUrlConstant.GET_ORDER_DETAIL)
    Observable<BaseResultData<OrderDetail>> getOrderDetail(@Query("order_id") long orderId);

}
