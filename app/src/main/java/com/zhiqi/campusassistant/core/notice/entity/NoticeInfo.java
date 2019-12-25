package com.zhiqi.campusassistant.core.notice.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/5/3.
 * 通知公告信息
 */

public class NoticeInfo implements BaseJsonData {

    public int id;

    public String title;

    public String summary;

    public String detail_url;

    public String publish_time;
}
