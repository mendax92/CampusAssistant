package com.zhiqi.campusassistant.core.entrance.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.presenter.CachePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.core.entrance.api.EntranceApiService;
import com.zhiqi.campusassistant.core.entrance.entity.EntranceInfo;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/8/16.
 * 报到指南
 */

public class EntrancePresenter extends CachePresenter {

    private EntranceApiService mApiService;

    public EntrancePresenter(Context context, EntranceApiService apiService, AppDaoSession daoSession) {
        super(context, daoSession.getRxDao(daoSession.getCacheDataDao()));
        this.mApiService = apiService;
    }

    /**
     * 获取报到指南列表
     *
     * @param loadView
     */
    public void getEntranceList(final ILoadView<List<EntranceInfo>> loadView) {
        Observable<BaseResultData<List<EntranceInfo>>> observable = mApiService.getEntranceList();
        handleCache(CacheKey.ENTRANCE_LIST, new TypeToken<List<EntranceInfo>>() {
        }.getType(), observable, loadView);
    }
}
