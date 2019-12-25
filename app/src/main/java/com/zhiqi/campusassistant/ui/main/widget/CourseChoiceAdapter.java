package com.zhiqi.campusassistant.ui.main.widget;

import android.widget.RadioButton;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;

/**
 * Created by ming on 2016/12/15.
 */

public class CourseChoiceAdapter extends BaseQuickAdapter<Integer> {

    private int checked;

    private int currentWeek;

    public CourseChoiceAdapter() {
        super(R.layout.item_course_choice_week, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item, int position) {
        RadioButton button = helper.getView(R.id.choice_item);
        String text = mContext.getResources().getString(item == currentWeek ? R.string.course_current_week : R.string.course_week, item);
        button.setText(text);
        button.setChecked(item == checked);
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setChecked(int item) {
        this.checked = item;
    }
}
