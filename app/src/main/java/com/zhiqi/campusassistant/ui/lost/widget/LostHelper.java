package com.zhiqi.campusassistant.ui.lost.widget;

import android.content.Context;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.lost.entity.LostApplyType;

/**
 * Created by ming on 2017/5/7.
 * 失物招领帮助类
 */

public class LostHelper {

    /**
     * 根据失物类型获取文字颜色
     *
     * @param type
     * @return
     */
    public static int getLostTextColor(Context context, int type) {
        int[] colors = context.getResources().getIntArray(R.array.lost_type_color);
        int color;
        if (type <= 0 || type >= colors.length - 1) {
            color = colors[colors.length - 1];
        } else {
            color = colors[type - 1];
        }
        return color;
    }

    public static String getLostText(Context context, LostApplyType lostApplyType) {
        int type = lostApplyType.getValue();
        String[] names = context.getResources().getStringArray(R.array.lost_type_name);
        String name;
        if (type <= 0 || type >= names.length - 1) {
            name = names[names.length - 1];
        } else {
            name = names[type - 1];
        }
        return name;
    }
}
