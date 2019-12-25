package com.zhiqi.campusassistant.app.dagger.presenter;

import android.content.Context;

import com.ming.base.util.Log;
import com.ming.pay.IPayService;
import com.ming.pay.PayPlatform;
import com.ming.pay.PayServiceFactory;
import com.zhiqi.campusassistant.common.http.CloupusApiAdapter;
import com.zhiqi.campusassistant.common.http.LongApiAdapter;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.core.appsetting.presenter.AppUpgradePresenter;
import com.zhiqi.campusassistant.core.jpush.api.JPushApiService;
import com.zhiqi.campusassistant.core.jpush.service.JPushManager;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;
import com.zhiqi.campusassistant.dao.DaoSession;

import yixiaotong.android.client.meallibrary.LeXiaoTongMealSDK;

/**
 * Created by ming on 2016/11/21.
 * 初始化管理
 */

public class InitManager {

    private static final String TAG = "InitManager";

    private Context mContext;

    private DaoSession mDaoSession;

    private CloupusApiAdapter cloupusApiAdapter;
    private LongApiAdapter longApiAdapter;

    private AppUpgradePresenter settingPresenter;

    public InitManager(Context context, DaoSession daoSession, AppUpgradePresenter settingPresenter, CloupusApiAdapter cloupusApiAdapter, LongApiAdapter longApiAdapter) {
        this.mContext = context;
        this.mDaoSession = daoSession;
        this.settingPresenter = settingPresenter;
        this.cloupusApiAdapter = cloupusApiAdapter;
        this.longApiAdapter = longApiAdapter;
        Log.i(TAG, "mContext:" + mContext + ", mDaoSession:" + mDaoSession);
    }

    public void initData(JPushApiService pushApiService) {
        Log.i(TAG, "Start initData.");
        // db
        LoginManager.getInstance().init(mDaoSession);

        // jpush
        Log.i(TAG, "init JPush.");
        JPushManager.init(mContext, pushApiService);

        // 微信支付
        Log.i(TAG, "init Wechat pay");
        IPayService wechatPayService = PayServiceFactory.getPayService(PayPlatform.WECHAT);
        wechatPayService.init(mContext, AppConfigs.APP_ID_WECHAT);

        // 支付宝
        Log.i(TAG, "init Alipay");
        IPayService alipayService = PayServiceFactory.getPayService(PayPlatform.WECHAT);
        alipayService.init(mContext, AppConfigs.APP_ID_ALIPAY);

        // 第三方
        Log.i(TAG, "int le xiao tong meal sdk.");
        LeXiaoTongMealSDK.getInstance().init(mContext);

        Log.i(TAG, "End initData.");
    }

    /**
     * 检测更新
     */
    public void checkUpgrade() {
        settingPresenter.checkUpgrade();
    }

    /**
     * 取消所有http请求
     */
    public void cancelHttpRequest() {
        if (cloupusApiAdapter != null) {
            cloupusApiAdapter.cancelAll();
        }
        if (longApiAdapter != null) {
            longApiAdapter.cancelAll();
        }
    }
}
