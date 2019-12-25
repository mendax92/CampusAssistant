package com.zhiqi.campusassistant.core.lost.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ming.base.http.HttpManager;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.presenter.CacheVersionDataPresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.lost.api.LostFoundApiService;
import com.zhiqi.campusassistant.core.lost.entity.LostApplyType;
import com.zhiqi.campusassistant.core.lost.entity.LostInfo;
import com.zhiqi.campusassistant.core.lost.entity.LostTypeVersion;
import com.zhiqi.campusassistant.core.lost.entity.MyLostInfo;

import java.lang.reflect.Type;
import java.util.Locale;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/5/5.
 * 失物招领presenter
 */

public class LostFoundPresenter extends CacheVersionDataPresenter<LostTypeVersion> {

    private LostFoundApiService mApiService;

    private static final int DELETE = 1;


    public LostFoundPresenter(Context context, LostFoundApiService apiService, AppDaoSession daoSession) {
        super(context, daoSession.getRxDao(daoSession.getCacheDataDao()));
        mApiService = apiService;
    }

    /**
     * 查询失物招领信息
     *
     * @param lostType
     * @param page
     * @param loadView
     */
    public void getLostList(LostApplyType lostType, final int page, final ILoadView<BasePageData<LostInfo>> loadView) {
        final String cacheKey = String.format(Locale.getDefault(), CacheKey.LOST_LIST, lostType.getValue());
        Observable<BaseResultData<BasePageData<LostInfo>>> observable = mApiService.getLostList(lostType.getValue(), page, AppConfigs.DEFAULT_PAGE_SIZE);
        Type type = new TypeToken<BasePageData<LostInfo>>() {
        }.getType();
        handleCache(cacheKey, page != 1, type, observable, loadView);
    }

    /**
     * 获取我的失物招领
     *
     * @param page
     * @param loadView
     */
    public void getMyLostList(final int page, final ILoadView<BasePageData<MyLostInfo>> loadView) {
        Observable<BaseResultData<BasePageData<MyLostInfo>>> observable = mApiService.getMyLostList(page, AppConfigs.DEFAULT_PAGE_SIZE);
        Type type = new TypeToken<BasePageData<MyLostInfo>>() {
        }.getType();
        handleCache(CacheKey.LOST_MY_LIST, page != 1, type, observable, loadView);
    }

    @Override
    public void loadVersionData(ILoadView<LostTypeVersion> loadView) {
        loadVersionData(CacheKey.LOST_TYPE_VERSION, LostTypeVersion.class, loadView);
    }

    @Override
    public void loadVersionDataFromNet(ILoadView<LostTypeVersion> loadView) {
        Observable<BaseResultData<LostTypeVersion>> observable = mApiService.getLostType();
        loadVersionDataFromNet(CacheKey.LOST_TYPE_VERSION, observable, loadView);
    }

    /**
     * 删除失物招领
     *
     * @param id
     */
    public void deleteLost(int id, final IRequestView loadView) {
        Observable<BaseResultData> observable = mApiService.doAction(id, DELETE);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData>(mContext) {
            @Override
            public void onSuccess(BaseResultData result) {
                if (!isReleased(loadView)) {
                    loadView.onQuest(HttpErrorCode.SUCCESS, result.message);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onQuest(errorCode, message);
                }
            }
        });
    }
}
