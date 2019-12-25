package com.zhiqi.campusassistant.core.message.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.presenter.CachePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.core.message.api.MessageApiService;
import com.zhiqi.campusassistant.core.message.entity.ModuleInfo;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ming on 2016/12/20.
 * 消息提供者
 */

public class MessagePresenter extends CachePresenter {

    private MessageApiService mApiService;

    public MessagePresenter(Context context, MessageApiService apiService, AppDaoSession daoSession) {
        super(context, daoSession.getRxDao(daoSession.getCacheDataDao()));
        this.mApiService = apiService;
    }

    public void queryMessageList(final ILoadView<List<ModuleInfo>> view) {
        Observable<BaseResultData<List<ModuleInfo>>> observable = mApiService.queryMessageList();
        handleCache(CacheKey.MESSAGE_MODULE_LIST, new TypeToken<List<ModuleInfo>>() {
        }.getType(), observable, view);
    }
}
