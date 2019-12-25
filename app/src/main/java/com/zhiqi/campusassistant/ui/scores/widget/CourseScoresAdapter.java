package com.zhiqi.campusassistant.ui.scores.widget;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.scores.entity.CourseScoreInfo;

/**
 * Created by ming on 2017/5/12.
 * 课程成绩adapter
 */

public class CourseScoresAdapter extends BaseQuickAdapter<CourseScoreInfo> {
    public CourseScoresAdapter() {
        super(R.layout.item_scores_of_course, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseScoreInfo item, int position) {
        helper.setText(R.id.course_name, item.course_name);
        helper.setText(R.id.score, item.score);
        helper.setText(R.id.credit, item.credits);
    }
}
