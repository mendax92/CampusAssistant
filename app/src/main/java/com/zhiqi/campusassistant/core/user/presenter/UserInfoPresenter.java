package com.zhiqi.campusassistant.core.user.presenter;

import android.content.Context;

import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.presenter.CachePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.core.user.api.UserInfoApiService;
import com.zhiqi.campusassistant.core.user.entity.ContactsList;
import com.zhiqi.campusassistant.core.user.entity.UserInfo;
import com.zhiqi.campusassistant.core.user.entity.UserRole;

import io.reactivex.Observable;

/**
 * Created by ming on 2016/11/29.
 * 联系人Presenter
 */
public class UserInfoPresenter extends CachePresenter {

    private UserInfoApiService mApiService;

    public UserInfoPresenter(Context context, UserInfoApiService apiService, AppDaoSession daoSession) {
        super(context, daoSession.getRxDao(daoSession.getCacheDataDao()));
        this.mApiService = apiService;
    }

    /**
     * 查找联系人列表
     *
     * @param departmentId
     * @param view
     */
    public void queryContactList(final int departmentId, final ILoadView<ContactsList> view) {
        Integer parentId = departmentId == 0 ? null : departmentId;
        Observable<BaseResultData<ContactsList>> observable = mApiService.queryContactsList(parentId);
        handleCache(CacheKey.CONTACTS_LIST, departmentId != 0, ContactsList.class, observable, view);
    }

    /**
     * 查询用户个人详细信息
     *
     * @param userId   用户id
     * @param userRole 当前登录角色
     */
    public void queryUserDetails(long userId, UserRole userRole, final ILoadView<UserInfo> view) {
        Observable<BaseResultData<UserInfo>> observable = mApiService.queryUserDetails(userId, userRole != null ? userRole.getValue() : 0);
        subscribe(observable, view);
    }

    /**
     * 搜索联系人
     *
     * @param keyWord 关键字
     * @param view
     */
    public void searchUsers(String keyWord, final ILoadView<ContactsList> view) {
        Observable<BaseResultData<ContactsList>> observable = mApiService.searchUsers(keyWord);
        subscribe(observable, view);
    }
}
