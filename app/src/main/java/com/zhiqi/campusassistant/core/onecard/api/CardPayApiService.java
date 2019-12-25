package com.zhiqi.campusassistant.core.onecard.api;

import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.app.entity.BannerInfos;
import com.zhiqi.campusassistant.core.onecard.entity.CardBalanceInfo;
import com.zhiqi.campusassistant.core.onecard.entity.CardOrderDetail;
import com.zhiqi.campusassistant.core.onecard.entity.CardOrderInfo;
import com.zhiqi.campusassistant.core.onecard.entity.CardPayRequest;
import com.zhiqi.campusassistant.core.onecard.entity.CardQrCodeInfo;
import com.zhiqi.campusassistant.core.onecard.entity.CardTopUpInfo;
import com.zhiqi.campusassistant.core.payment.entity.OrderResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ming on 2018/1/21.
 * 一卡通支付API接口
 */

public interface CardPayApiService {

    /**
     * 加载余额
     */
    @GET(HttpUrlConstant.GET_CARD_BALANCE)
    Observable<BaseResultData<CardBalanceInfo>> getBalanceInfo();

    /**
     * 加载充值信息
     */
    @GET(HttpUrlConstant.LOAD_CARD_TOP_UP)
    Observable<BaseResultData<CardTopUpInfo>> loadTopUpInfo();

    /**
     * 获取校园通支付信息
     *
     * @param request 支付请求信息
     */
    @POST(HttpUrlConstant.LOAD_CARD_ORDER)
    Observable<BaseResultData<CardOrderInfo>> getCardOrderInfo(@Body CardPayRequest request);

    /**
     * 检查支付结果
     *
     * @param orderId 订单ID
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.CHECK_ORDER_STATUS)
    Observable<BaseResultData<OrderResult>> checkOrderResult(@Field("order_id") long orderId);

    /**
     * 获取订单详情
     *
     * @param orderId 订单ID
     */
    @GET(HttpUrlConstant.GET_CARD_ORDER_DETAIL)
    Observable<BaseResultData<CardOrderDetail>> getOrderDetail(@Query("order_id") long orderId, @Query("order_no") String orderNo);

    /**
     * 获取订单列表
     */
    @GET(HttpUrlConstant.GET_CARD_ORDER_LIST)
    Observable<BaseResultData<BasePageData<CardOrderDetail>>> getOrderList(@Query("page_no") int page, @Query("page_size") int pageSize);

    /**
     * 获取校园卡二维码信息
     */
    @GET(HttpUrlConstant.GET_CARD_QR_CODE)
    Observable<BaseResultData<CardQrCodeInfo>> getQrCodeInfo(@Query("code_num") int codeNum);

    /**
     * 开启校园卡支付功能
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.OPEN_CARD_PAYMENT)
    Observable<BaseResultData> openPayment(@Field("is_open") int isOpen, @Field("password") String password);

    /**
     * 获取banner
     *
     * @return
     */
    @GET(HttpUrlConstant.ADVERTISE_GETADVER)
    Observable<BaseResultData<List<BannerInfos.BannerInfo>>> getAdver(@Query("flag")int  flag);
}
