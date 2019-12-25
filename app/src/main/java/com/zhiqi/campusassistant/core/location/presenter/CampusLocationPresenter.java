package com.zhiqi.campusassistant.core.location.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.presenter.CachePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.core.location.api.CampusLocationApiService;
import com.zhiqi.campusassistant.core.location.entity.CampusLocationInfo;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/8/20.
 * 学校定位presenter
 */

public class CampusLocationPresenter extends CachePresenter {

    private CampusLocationApiService mApiService;

    public CampusLocationPresenter(Context context, CampusLocationApiService apiService, AppDaoSession daoSession) {
        super(context, daoSession.getRxDao(daoSession.getCacheDataDao()));
        this.mApiService = apiService;
    }

    /**
     * 获取校园定位
     *
     * @param loadView
     */
    public void getCampusLocations(ILoadView<ArrayList<CampusLocationInfo>> loadView) {
        Observable<BaseResultData<ArrayList<CampusLocationInfo>>> observable = mApiService.getCampusLocationList();
        handleCache(CacheKey.CAMPUS_LOCATION, new TypeToken<ArrayList<CampusLocationInfo>>() {
        }.getType(), observable, loadView);
    }
}
