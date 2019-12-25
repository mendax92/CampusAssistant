package com.zhiqi.campusassistant.core.login.presenter;

import android.content.Context;

import com.ming.base.greendao.rx2.RxDao;
import com.ming.base.http.HttpManager;
import com.ming.base.util.MD5Util;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.presenter.BasePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.common.utils.AppPreference;
import com.zhiqi.campusassistant.core.jpush.service.JPushManager;
import com.zhiqi.campusassistant.core.login.api.LoginApiService;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.core.security.manager.LoginManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by Edmin on 2016/11/5 0005.
 * 登录
 */

public class LoginPresenter extends BasePresenter {

    private Context mContext;

    private LoginApiService mApiService;

    private RxDao<LoginUser, Long> mUserDao;

    public LoginPresenter(Context context, LoginApiService apiService, AppDaoSession daoSession) {
        this.mContext = context;
        this.mApiService = apiService;
        this.mUserDao = daoSession.getRxDao(daoSession.getLoginUserDao());
    }

    /**
     * 登录接口
     *
     * @param account
     * @param password
     * @param loadView
     */
    public void login(final String account, String password, final ILoadView<LoginUser> loadView) {
        password = MD5Util.getMD5Value(password);
        Observable<BaseResultData<LoginUser>> observable = mApiService.login(account, password);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<LoginUser>>(mContext) {
            @Override
            public void onSuccess(final BaseResultData<LoginUser> result) {
                final LoginUser user = result.data;
                mUserDao.insertOrReplace(user)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LoginUser>() {
                            @Override
                            public void accept(LoginUser loginUser) throws Exception {
                                LoginManager.getInstance().onLogin(loginUser);
                                AppPreference.getInstance(mContext).setLoginAccount(account);
                                JPushManager.getInstance().reportRegistrationId();
                                if (!isReleased(loadView)) {
                                    loadView.onLoadData(loginUser);
                                }
                            }
                        });
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
     * 退出
     *
     * @param view
     */
    public void logout(final IRequestView view) {
        Observable<BaseResultData> observable = mApiService.logout();
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData>(mContext) {
            @Override
            public void onSuccess(BaseResultData result) {
                LoginManager.getInstance().onLogout();
                if (!isReleased(view)) {
                    view.onQuest(result.error_code, result.message);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(view)) {
                    view.onQuest(errorCode, message);
                }
            }
        });
    }
}
