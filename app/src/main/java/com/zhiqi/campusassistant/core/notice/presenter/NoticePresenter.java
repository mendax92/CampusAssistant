package com.zhiqi.campusassistant.core.notice.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.presenter.CachePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.core.notice.api.NoticeApiService;
import com.zhiqi.campusassistant.core.notice.entity.NoticeInfo;

import java.lang.reflect.Type;

import io.reactivex.Observable;

/**
 * Created by ming on 2017/5/3.
 * 通知公告presenter
 */

public class NoticePresenter extends CachePresenter {

    private NoticeApiService mApiService;

    public NoticePresenter(Context context, NoticeApiService apiService, AppDaoSession daoSession) {
        super(context, daoSession.getRxDao(daoSession.getCacheDataDao()));
        mApiService = apiService;
    }

    public void queryNoticeList(final int page, final ILoadView<BasePageData<NoticeInfo>> loadView) {
        Observable<BaseResultData<BasePageData<NoticeInfo>>> observable = mApiService.getNoticeList(page, AppConfigs.DEFAULT_PAGE_SIZE);
        Type type = new TypeToken<BasePageData<NoticeInfo>>() {
        }.getType();
        handleCache(CacheKey.NOTICE_LIST, page != 1, type, observable, loadView);
    }
}
