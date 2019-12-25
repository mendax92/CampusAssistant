package com.zhiqi.campusassistant.core.news.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.ming.base.http.HttpManager;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.entity.BasePageData;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheData;
import com.zhiqi.campusassistant.common.entity.CacheKey;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.presenter.CachePresenter;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;
import com.zhiqi.campusassistant.config.AppConfigs;
import com.zhiqi.campusassistant.core.news.api.NewsApiService;
import com.zhiqi.campusassistant.core.news.entity.CategoryInfo;
import com.zhiqi.campusassistant.core.news.entity.NewsInfo;
import com.zhiqi.campusassistant.core.news.entity.NewsItem;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by ming on 2017/3/12.
 * 新闻操作
 */

public class NewsPresenter extends CachePresenter {

    private NewsApiService mApiService;

    public NewsPresenter(Context context, NewsApiService apiService, AppDaoSession daoSession) {
        super(context, daoSession.getRxDao(daoSession.getCacheDataDao()));
        this.mApiService = apiService;
    }

    /**
     * 查询新闻栏目
     */
    public void queryNewsCategory(final ILoadView<List<CategoryInfo>> loadView) {
        Observable<BaseResultData<List<CategoryInfo>>> observable = mApiService.queryCategory();
        handleCache(CacheKey.NEWS_CATEGORY, new TypeToken<List<CategoryInfo>>() {
        }.getType(), observable, loadView);
    }

    /**
     * 查询新闻列表
     *
     * @param page
     * @param categoryId
     * @param loadView
     */
    public void queryNewsList(final int categoryId, final int page, final ILoadView<List<NewsInfo>> headView, final ILoadView<BasePageData<NewsInfo>> loadView) {
        Observable<BaseResultData<NewsItem>> observable = mApiService.queryNewsList(categoryId, page, AppConfigs.DEFAULT_PAGE_SIZE);
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<NewsItem>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<NewsItem> result) {
                if (!isReleased(loadView)) {
                    headView.onLoadData(result.data != null ? result.data.headline : null);
                    loadView.onLoadData(result.data != null ? result.data.general : null);
                }
                if (result.data != null && page == 1) {
                    String key = String.format(Locale.getDefault(), CacheKey.NEWS_PAGE_LIST, categoryId);
                    cacheDao.insertOrReplace(new CacheData<NewsItem>(key, result.data))
                            .subscribe();
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onFailed(errorCode, message);
                }
                if (page == 1) {
                    String key = String.format(Locale.getDefault(), CacheKey.NEWS_PAGE_LIST, categoryId);
                    cacheDao.load(key)
                            .map(new Function<CacheData, NewsItem>() {
                                @Override
                                public NewsItem apply(CacheData cacheData) throws Exception {
                                    NewsItem news = null;
                                    if (cacheData != null) {
                                        news = (NewsItem) cacheData.getData(new TypeToken<NewsItem>() {
                                        }.getType());
                                    }
                                    return news;
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<NewsItem>() {
                                @Override
                                public void accept(NewsItem newsItem) throws Exception {
                                    if (!isReleased(loadView) && newsItem != null) {
                                        headView.onLoadData(newsItem.headline);
                                        loadView.onLoadData(newsItem.general);
                                    }
                                }
                            });
                }
            }
        });
    }

}
