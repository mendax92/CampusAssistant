package com.zhiqi.campusassistant.core.news.entity;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;

/**
 * Created by ming on 2016/12/22.
 * 校园资讯
 */

public class NewsInfo implements BaseJsonData {

    public int id;

    public int category_id;

    public NewsType show_type;

    public String title;

    public String thumbnails;

    public String summary;

    public String publish_time;

    public String source;

    public String url;

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
