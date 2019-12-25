package com.zhiqi.campusassistant.ui.main.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ming.base.util.TimeUtil;
import com.ming.base.util.ViewUtil;
import com.ming.base.widget.TwoDimensionalLayout;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.course.entity.UICourseData;
import com.zhiqi.campusassistant.core.course.entity.UICourseInfo;

import java.util.Calendar;

/**
 * Created by ming on 2016/12/5.
 */

public class CourseAdapter extends TwoDimensionalLayout.BaseTableAdapter<UICourseInfo> {

    private Context mContext;

    private long thisMonday;

    private UICourseData courseData;

    private int headHeight;

    private View firstHeader;

    public CourseAdapter() {
        this.thisMonday = TimeUtil.getFirstDayOfWeek(System.currentTimeMillis());
        setColumnHeaderItemType(R.layout.view_course_column_header);
        addItemType(1, R.layout.item_course);
    }

    @Override
    public int getContentViewType(UICourseInfo item) {
        return 1;
    }

    @Override
    public void convertContentView(BaseViewHolder helper, UICourseInfo item) {
        if (item.item != null) {
            String txt = !TextUtils.isEmpty(item.item.location) ?
                    mContext.getString(R.string.course_show, item.item.name, item.item.location) : item.item.name;
            TextView textView = helper.getView(R.id.course_text);
            textView.setText(txt);
            switch (item.yPos) {
                case 5:
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    helper.getConvertView().setBackgroundResource(R.drawable.course_bg_sat);
                    break;
                case 6:
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    helper.getConvertView().setBackgroundResource(R.drawable.course_bg_sun);
                    break;
                default:
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.text_main_color));
                    helper.getConvertView().setBackgroundResource(R.drawable.course_bg_normal);
                    break;
            }
        }
    }

    @Override
    public void convertColumnHeadView(int row, BaseViewHolder helper) {
        helper.setText(R.id.course_lesson, String.valueOf(row + 1));
    }

    @Override
    public View getRowHeadView(int column, View view, ViewGroup parent) {
        View contentView = view;
        HeaderViewHolder holder = null;
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (view == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.view_course_row_header, parent, false);
            holder = new HeaderViewHolder();
            holder.contentView = contentView;
            holder.tipText = (TextView) contentView.findViewById(R.id.course_date);
            holder.titleText = (TextView) contentView.findViewById(R.id.course_week);
            contentView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) view.getTag();
        }
        ViewGroup.LayoutParams params = holder.contentView.getLayoutParams();
        if (params != null) {
            params.width = column == 0 ? mContext.getResources().getDimensionPixelSize(R.dimen.course_list_header_column_width) : ViewGroup.LayoutParams.MATCH_PARENT;
            holder.contentView.setLayoutParams(params);
        }
        long monday = courseData == null ? thisMonday : courseData.mondayTime;
        long time = TimeUtil.getAfterDayTime(monday, column - 1);
        if (column == 0) {
            holder.tipText.setVisibility(View.GONE);
            holder.titleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.common_text_xs));
            holder.titleText.setText(mContext.getString(R.string.course_month, TimeUtil.getField(time, Calendar.MONTH)));
            firstHeader = contentView;
        } else {
            holder.tipText.setVisibility(View.VISIBLE);
            holder.tipText.setText(String.valueOf(TimeUtil.getField(time, Calendar.DAY_OF_MONTH)));
            holder.titleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.common_text_s));
            holder.titleText.setText(TimeUtil.getWeekOfChinese(time, mContext.getString(R.string.course_day_week)));
            if (TimeUtil.isSameDay(System.currentTimeMillis(), time)) {
                int whiteColor = ContextCompat.getColor(mContext, R.color.white);
                holder.titleText.setTextColor(whiteColor);
                holder.tipText.setTextColor(whiteColor);
                holder.contentView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.common_orange));
            } else {
                holder.titleText.setTextColor(ContextCompat.getColor(mContext, R.color.text_main_color));
                holder.tipText.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray_dark_color));
                holder.contentView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.windowBackground));
            }
            if (headHeight == 0) {
                final View itemView = contentView;
                ViewUtil.addOnGlobalLayoutListener(contentView, new Runnable() {
                    @Override
                    public void run() {
                        if (headHeight == 0) {
                            headHeight = itemView.getHeight();
                            ViewGroup.LayoutParams params = firstHeader.getLayoutParams();
                            params.height = headHeight;
                            firstHeader.setLayoutParams(params);
                        }
                    }
                });
            }
        }
        return contentView;
    }

    public void setCourseData(UICourseData courseData) {
        this.courseData = courseData;
        headHeight = 0;
        setData(courseData != null ? courseData.courseInfos : null);
    }

    private class HeaderViewHolder {
        private View contentView;
        private TextView tipText;
        private TextView titleText;
    }
}
