package com.zhiqi.campusassistant.common.entity;

import com.ming.base.bean.BaseJsonData;

import java.util.List;

/**
 * Created by ming on 2017/1/14.
 * 分页基础类
 */

public class BasePageData<T> implements BaseJsonData {

    public int record_count;

    public boolean lastpage;

    public int total_page;

    public int page_no;

    public int page_size;

    public List<T> list;

}
