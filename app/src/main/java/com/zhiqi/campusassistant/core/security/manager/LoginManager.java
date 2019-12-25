package com.zhiqi.campusassistant.core.security.manager;

import com.ming.base.greendao.rx2.RxDao;
import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.CacheData;
import com.zhiqi.campusassistant.common.utils.AppPreference;
import com.zhiqi.campusassistant.core.login.entity.LoginUser;
import com.zhiqi.campusassistant.dao.DaoSession;
import com.zhiqi.campusassistant.dao.LoginUserDao;

import java.util.List;

/**
 * Created by ming on 2016/11/21.
 * 登录管理，主要共享登录用户数据
 */

public class LoginManager {

    private static final String TAG = "LoginManager";

    private static LoginManager instance;

    private LoginUser mLoginUser;

    private AssistantApplication application;

    private LoginManager() {
        this.application = AssistantApplication.getInstance();
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            synchronized (LoginManager.class) {
                if (instance == null) {
                    instance = new LoginManager();
                }
            }
        }
        return instance;
    }

    public void init(DaoSession daoSession) {
        if (daoSession == null) {
            return;
        }
        LoginUserDao userDao = daoSession.getLoginUserDao();
        List<LoginUser> loginUsers = userDao.loadAll();
        if (loginUsers != null && !loginUsers.isEmpty()) {
            mLoginUser = loginUsers.get(0);
        }
    }

    public void onLogin(LoginUser loginUser) {
        this.mLoginUser = loginUser;
    }

    public LoginUser getLoginUser() {
        return mLoginUser;
    }

    public long getUserId() {
        return mLoginUser != null ? 0 : mLoginUser.getUser_id();
    }

    public void onLogout() {
        this.mLoginUser = null;
        AppDaoSession daoSession = application.getApplicationComponent().getDaoSession();
        if (daoSession != null) {
            RxDao<LoginUser, Long> userDao = daoSession.getRxDao(daoSession.getLoginUserDao());
            userDao.deleteAll().subscribe();
            RxDao<CacheData, String> cacheDao = daoSession.getRxDao(daoSession.getCacheDataDao());
            cacheDao.deleteAll().subscribe();
        }
    }

    public void changePhone(String phone) {
        if (this.mLoginUser != null) {
            this.mLoginUser.setPhone(phone);
            AppDaoSession daoSession = application.getApplicationComponent().getDaoSession();
            if (daoSession != null) {
                RxDao<LoginUser, Long> userDao = daoSession.getRxDao(daoSession.getLoginUserDao());
                userDao.update(mLoginUser).subscribe();
            }
            AppPreference.getInstance(application).setLoginAccount(phone);
        }
    }

    public boolean isLogin() {
        return mLoginUser != null;
    }
}
