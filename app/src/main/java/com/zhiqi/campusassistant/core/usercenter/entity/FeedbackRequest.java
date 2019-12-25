package com.zhiqi.campusassistant.core.usercenter.entity;

import com.ming.base.bean.BaseJsonData;

/**
 * Created by ming on 2017/3/17.
 */

public class FeedbackRequest implements BaseJsonData {

    public static final int TYPE_OTHERS = 0;

    public static final int TYPE_SUGGEST = 1;

    public static final int TYPE_APP_ERROR = 2;

    public int type;

    public String content;

    public String contact_info;

}
