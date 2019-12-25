package com.zhiqi.campusassistant.core.app.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ming.base.greendao.rx2.RxDao;
import com.ming.base.http.HttpManager;
import com.ming.base.rx2.SimpleObserver;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.presenter.BasePresenter;
import com.zhiqi.campusassistant.common.ui.view.AppILoadView;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.core.app.api.AppApiService;
import com.zhiqi.campusassistant.core.app.entity.AppInfo;
import com.zhiqi.campusassistant.core.app.entity.BannerInfos;
import com.zhiqi.campusassistant.core.app.entity.CheckAppGoWhere;
import com.zhiqi.campusassistant.core.app.entity.ModuleCategory;

import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by ming on 2017/3/11.
 * 应用presenter
 */

public class AppPresenter extends BasePresenter {

    private static final long MAX_TIME_CACHE = 5 * 60 * 1000;
    private Context mContext;
    private AppApiService mApiService;
    private RxDao<CacheData, String> mDao;
    private AppILoadView mAppILoadView;

    public AppPresenter(Context context, AppApiService apiService, AppDaoSession daoSession) {
        this.mContext = context.getApplicationContext();
        this.mApiService = apiService;
        this.mDao = daoSession.getRxDao(daoSession.getCacheDataDao());
    }

    public void setAppILoadView(AppILoadView mAppILoadView) {
        this.mAppILoadView = mAppILoadView;
    }

    public void queryAppList(final ILoadView<List<ModuleCategory>> loadView) {
        mDao.load(CacheKey.APP_LIST)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(SimpleObserver.create(new Consumer<CacheData>() {
                    @Override
                    public void accept(CacheData cacheData) throws Exception {
                        List<ModuleCategory> data = null;
                        //本地为空或者缓存超过5分钟，则加载网络数据
                        if (cacheData != null) {
                            data = (List<ModuleCategory>) cacheData.getData(new TypeToken<List<ModuleCategory>>() {
                            }.getType());
                        }
                        if (data == null || System.currentTimeMillis() - cacheData.getCacheTime() >= MAX_TIME_CACHE) {
                            queryAppListFromNet(loadView, data);
                        } else if (!isReleased(loadView)) {
                            loadView.onLoadData(checkModuleData(data));
                        }
                    }
                }));
    }

    private void queryAppListFromNet(final ILoadView<List<ModuleCategory>> loadView, final List<ModuleCategory> cache) {
        Observable<BaseResultData<List<ModuleCategory>>> observable = mApiService.queryAppList();
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<List<ModuleCategory>>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<List<ModuleCategory>> result) {
                if (result != null && result.data != null && result.data.size() > 0) {
                    getAdver(loadView, result);
                } else {
                    if (!isReleased(loadView)) {
                        if (cache != null) {
                            loadView.onLoadData(checkModuleData(cache));
                        }
                    }
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onFailed(errorCode, message);
                    if (cache != null) {
                        loadView.onLoadData(checkModuleData(cache));
                    }
                }
            }
        });
    }

    private void getAdver(final ILoadView<List<ModuleCategory>> loadView, final BaseResultData<List<ModuleCategory>> result) {
        Observable<BaseResultData<List<BannerInfos.BannerInfo>>> observable = mApiService.getAdver(BannerInfos.flag_app_index);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<List<BannerInfos.BannerInfo>>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<List<BannerInfos.BannerInfo>> data) {
                result.data.get(0).setData(data.data);
                if (!isReleased(loadView)) {
                    loadView.onLoadData(checkModuleData(result.data));
                }
                if (result.data != null) {
                    CacheData<List<ModuleCategory>> cacheData = new CacheData<>(CacheKey.APP_LIST, result.data);
                    cacheData.setCacheTime(System.currentTimeMillis());
                    mDao.insertOrReplace(cacheData).subscribe();
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onFailed(errorCode, message);
                    if (result != null) {
                        loadView.onLoadData(checkModuleData(result.data));
                    }
                }
            }
        });
    }

    private List<ModuleCategory> checkModuleData(List<ModuleCategory> data) {
        if (data != null && !data.isEmpty()) {
            Iterator<ModuleCategory> moduleIterator = data.iterator();
            while (moduleIterator.hasNext()) {
                ModuleCategory category = moduleIterator.next();
                List<AppInfo> appInfos = category.list;
                if (appInfos != null && !appInfos.isEmpty()) {
                    Iterator<AppInfo> iterator = appInfos.iterator();
                    while (iterator.hasNext()) {
                        AppInfo info = iterator.next();
                        if (info == null || !info.is_active) {
                            iterator.remove();
                        }
                    }
                }
                if (appInfos == null || appInfos.isEmpty()) {
                    moduleIterator.remove();
                }
            }
        }
        return data;
    }

    public void checkAppToWhere(String detail_information) {
        Observable<BaseResultData<CheckAppGoWhere>> observable = mApiService.checkAppToWhere(detail_information);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<CheckAppGoWhere>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<CheckAppGoWhere> data) {
                if (mAppILoadView != null) {
                    mAppILoadView.checkAppToWhere(data);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (mAppILoadView != null) {
                    mAppILoadView.onFailed(errorCode, message);
                }
            }
        });
    }
}
