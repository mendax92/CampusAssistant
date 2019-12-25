package com.zhiqi.campusassistant.ui.message.widget;

import android.content.Intent;
import android.view.View;

import com.ming.base.widget.CircleView;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.course.entity.CourseInfo;
import com.zhiqi.campusassistant.ui.course.activity.CourseDetailActivity;

/**
 * Created by ming on 2016/12/21.
 */

public class MsgCourseAdapter extends BaseQuickAdapter<CourseInfo> {

    private int[] itemsColors;

    public MsgCourseAdapter() {
        super(R.layout.item_msg_course, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseInfo item, int position) {
        CircleView circleView = helper.getView(R.id.msg_course_circle);
        if (itemsColors == null) {
            itemsColors = mContext.getResources().getIntArray(R.array.course_item_color);
        }
        int index = position % itemsColors.length;
        if (index >= itemsColors.length) {
            index = 0;
        }
        circleView.setCircleColor(itemsColors[index]);
        helper.setText(R.id.course_lesson, mContext.getString(R.string.course_lesson_detail, item.class_start, item.class_end));
        helper.setText(R.id.course_name, item.name);
        helper.setText(R.id.course_type, item.type_name);
        helper.setText(R.id.course_location, item.location);
        helper.getConvertView().setTag(R.id.msg_course_item, position);
        helper.getConvertView().setOnClickListener(itemClick);
    }

    private View.OnClickListener itemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag(R.id.msg_course_item);
            if (tag != null) {
                int position = (int) tag;
                CourseInfo info = getItem(position);
                if (info != null) {
                    Intent intent = new Intent(mContext, CourseDetailActivity.class);
                    intent.putExtra(AppConstant.EXTRA_COURSE_INFO, info);
                    mContext.startActivity(intent);
                }
            }
        }
    };
}
