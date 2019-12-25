package com.zhiqi.campusassistant.core.news.entity;

import com.ming.base.bean.BaseJsonData;
import com.zhiqi.campusassistant.common.entity.BasePageData;

import java.util.List;

/**
 * Created by ming on 2017/3/12.
 */

public class NewsItem implements BaseJsonData {

    public List<NewsInfo> headline;

    public BasePageData<NewsInfo> general;
}
