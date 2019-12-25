package com.zhiqi.campusassistant.core.usercenter.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyInfo;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by ming on 2017/3/23.
 * 安全中心API
 */

public interface SecurityApiService {

    /**
     * 获取验证码
     *
     * @param areaCode
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.GET_VERIFY_CODE)
    Observable<BaseResultData<VerifyInfo>> getVerifyCode(@Field("area_code") String areaCode, @Field("phone") String phone, @Field("function_id") int verifyFunction);

    /**
     * 校验验证码
     *
     * @return
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.CHECK_VERIFY_CODE)
    Observable<BaseResultData<VerifyResult>> checkVerifyCode(@Field("code") String code, @Field("request_id") String request_id);


    /**
     * 重置密码
     *
     * @param resetToken
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.RESET_PASSWORD)
    Observable<BaseResultData> resetPassword(@Field("reset_token") String resetToken, @Field("new_password") String password);

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.CHANGE_PASSWORD)
    Observable<BaseResultData> changePassword(@Field("old_password") String oldPwd, @Field("new_password") String newPwd);

    /**
     * 修改支付密码
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.CHANGE_PAY_PASSWORD)
    Observable<BaseResultData> changePayPassword(@Field("old_password") String oldPwd, @Field("new_password") String newPwd);

    /**
     * 重置密码
     *
     * @param resetToken
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST(HttpUrlConstant.RESET_PAY_PASSWORD)
    Observable<BaseResultData> resetPayPassword(@Field("reset_token") String resetToken, @Field("new_password") String password);
}
