package com.zhiqi.campusassistant.core.usercenter.presenter;

import android.content.Context;

import com.ming.base.util.MD5Util;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.presenter.SimplePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.core.usercenter.api.SecurityApiService;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyFunction;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyInfo;
import com.zhiqi.campusassistant.core.usercenter.entity.VerifyResult;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/3/23.
 * 安全中心presenter
 */

public class SecurityPresenter extends SimplePresenter {

    private SecurityApiService mApiService;

    public SecurityPresenter(Context context, SecurityApiService apiService) {
        super(context);
        this.mApiService = apiService;
    }

    /**
     * 获取验证码
     *
     * @param areaCode
     * @param phone
     */
    public void getVerifyCode(String areaCode, String phone, VerifyFunction function, final ILoadView<VerifyInfo> view) {
        Observable<BaseResultData<VerifyInfo>> observable = mApiService.getVerifyCode(areaCode, phone, function.getValue());
        subscribe(observable, view);
    }

    /**
     * 校验验证码
     *
     * @param verifyCode
     * @param loadView
     */
    public void checkVerifyCode(String verifyCode, String requestId, final ILoadView<VerifyResult> loadView) {
        Observable<BaseResultData<VerifyResult>> observable = mApiService.checkVerifyCode(verifyCode, requestId);
        subscribe(observable, loadView);
    }

    /**
     * 重置密码
     *
     * @param resetToken
     * @param password
     * @param view
     */
    public void resetPassword(String resetToken, String password, final IRequestView view) {
        password = MD5Util.getMD5Value(password);
        Observable<BaseResultData> observable = mApiService.resetPassword(resetToken, password);
        requestSimple(observable, view);
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @param view
     */
    public void changePassword(String oldPwd, String newPwd, final IRequestView view) {
        oldPwd = MD5Util.getMD5Value(oldPwd);
        newPwd = MD5Util.getMD5Value(newPwd);
        Observable<BaseResultData> observable = mApiService.changePassword(oldPwd, newPwd);
        requestSimple(observable, view);
    }

    /**
     * 修改支付密码
     *
     * @param oldPwd
     * @param newPwd
     * @param view
     */
    public void changePayPassword(String oldPwd, String newPwd, final IRequestView view) {
        oldPwd = MD5Util.getMD5Value(oldPwd);
        newPwd = MD5Util.getMD5Value(newPwd);
        Observable<BaseResultData> observable = mApiService.changePayPassword(oldPwd, newPwd);
        requestSimple(observable, view);
    }

    /**
     * 重置支付密码
     *
     * @param password
     * @param view
     */
    public void resetPayPassword(String resetToken, String password, final IRequestView view) {
        password = MD5Util.getMD5Value(password);
        Observable<BaseResultData> observable = mApiService.resetPayPassword(resetToken, password);
        requestSimple(observable, view);
    }
}
