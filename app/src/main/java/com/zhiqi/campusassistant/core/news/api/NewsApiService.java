package com.zhiqi.campusassistant.core.news.api;

import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.news.entity.CategoryInfo;
import com.zhiqi.campusassistant.core.news.entity.NewsItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ming on 2017/3/12.
 * 新闻API接口
 */

public interface NewsApiService {

    /**
     * 查询新闻栏目
     *
     * @return
     */
    @GET(HttpUrlConstant.QUERY_NEWS_CATEGORY)
    Observable<BaseResultData<List<CategoryInfo>>> queryCategory();

    /**
     * 查询新闻列表
     *
     * @return
     */
    @GET(HttpUrlConstant.QUERY_NEWS_LIST)
    Observable<BaseResultData<NewsItem>> queryNewsList(@Query("category") int categoryId, @Query("page_no") int pageNo, @Query("page_size") int pageSize);
}
