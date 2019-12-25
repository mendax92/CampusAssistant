package com.zhiqi.campusassistant.ui.message.widget;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ming.base.util.Log;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.ming.base.widget.recyclerView.decoration.HorizontalDividerItemDecoration;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.core.message.entity.ModuleInfo;
import com.zhiqi.campusassistant.ui.leave.widget.LeaveApplyAdapter;
import com.zhiqi.campusassistant.ui.leave.widget.LeaveApprovalAdapter;
import com.zhiqi.campusassistant.ui.news.widget.NewsItemAdapter;
import com.zhiqi.campusassistant.ui.repair.widget.RepairApplyAdapter;
import com.zhiqi.campusassistant.ui.repair.widget.RepairApprovalAdapter;

/**
 * Created by ming on 2016/12/20.
 * 消息列表
 */

public class MessageAdapter extends BaseQuickAdapter<ModuleInfo> {

    private static final String TAG = "MessageAdapter";

    private RecyclerView.ItemDecoration defaultItemDecoration;

    private RecyclerView.ItemDecoration marginItemDecoration;

    public MessageAdapter() {
        super(R.layout.item_msg_module, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, ModuleInfo item, int position) {
        if (item != null && item.module_id != null) {
            Log.i(TAG, "convert type:" + item.module_id + ", position:" + position);
            fillTop(helper, item);
            BaseQuickAdapter<?> adapter = getItemAdapter(item);
            fillAdapter((RecyclerView) helper.getView(R.id.msg_data), adapter, item);
        }
    }

    private BaseQuickAdapter getItemAdapter(ModuleInfo item) {
        if (defaultItemDecoration == null) {
            defaultItemDecoration = new HorizontalDividerItemDecoration.Builder(mContext).build();
        }
        if (marginItemDecoration == null) {
            marginItemDecoration = new HorizontalDividerItemDecoration.Builder(mContext)
                    .backgroundResource(R.color.white)
                    .margin(mContext.getResources().getDimensionPixelSize(R.dimen.common_margin_s), 0)
                    .build();
        }
        BaseQuickAdapter<?> adapter = null;
        switch (item.module_id) {
            case LeaveApply:
                adapter = new LeaveApplyAdapter();
                break;
            case LeaveApproval:
                adapter = new LeaveApprovalAdapter();
                break;
            case RepairApply:
                adapter = new RepairApplyAdapter();
                break;
            case RepairApproval:
                adapter = new RepairApprovalAdapter();
                break;
            case News:
                adapter = new NewsItemAdapter();
                break;
            case CourseSchedule:
                adapter = new MsgCourseAdapter();
                break;
        }
        return adapter;
    }

    private <T extends BaseQuickAdapter> void fillAdapter(RecyclerView recyclerView, T adapter, ModuleInfo item) {
        if (adapter == null) {
            return;
        }
        try {
            Log.i(TAG, "type:" + item.module_id);
            ViewCompat.setNestedScrollingEnabled(recyclerView, false);
            LinearLayoutManager layoutManager = recyclerView.getLayoutManager() == null ? new LinearLayoutManager(mContext) : (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.setLayoutManager(layoutManager);
            // swapAdapter避免adapter错误导致异常
            recyclerView.setAdapter(adapter);
            MessageEmptyView emptyView = new MessageEmptyView(mContext);
            adapter.setEmptyView(emptyView);
            recyclerView.removeItemDecoration(defaultItemDecoration);
            recyclerView.removeItemDecoration(marginItemDecoration);
            switch (item.module_id) {
                case LeaveApply:
                    recyclerView.addItemDecoration(marginItemDecoration);
                    emptyView.setEmptyTip(R.string.leave_empty_apply);
                    break;
                case LeaveApproval:
                    recyclerView.addItemDecoration(defaultItemDecoration);
                    emptyView.setEmptyTip(R.string.leave_empty_approval);
                    break;
                case RepairApply:
                    recyclerView.addItemDecoration(marginItemDecoration);
                    emptyView.setEmptyTip(R.string.repair_empty_apply);
                    break;
                case RepairApproval:
                    recyclerView.addItemDecoration(defaultItemDecoration);
                    emptyView.setEmptyTip(R.string.repair_empty_approval);
                    break;
                case News:
                    recyclerView.addItemDecoration(defaultItemDecoration);
                    emptyView.setEmptyTip(R.string.news_empty_information);
                    break;
                case CourseSchedule:
                    recyclerView.addItemDecoration(marginItemDecoration);
                    emptyView.setEmptyTip(R.string.course_no_schedule);
                    break;
            }
            adapter.setNewData(item.data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fillTop(BaseViewHolder helper, ModuleInfo item) {
        helper.addOnClickListener(R.id.msg_module_top);
        helper.setVisible(R.id.msg_module_time, false);
        helper.setText(R.id.msg_module_title, item.module_name);
        helper.setText(R.id.msg_module_sub, item.update_time);
        switch (item.module_id) {
            case LeaveApply:
                helper.setImageResource(R.id.msg_module_icon, R.drawable.ic_app_leave_apply);
                break;
            case LeaveApproval:
                helper.setImageResource(R.id.msg_module_icon, R.drawable.ic_app_leave_approval);
                break;
            case RepairApply:
                helper.setImageResource(R.id.msg_module_icon, R.drawable.ic_app_repair_apply);
                break;
            case RepairApproval:
                helper.setImageResource(R.id.msg_module_icon, R.drawable.ic_app_repair_approval);
                break;
            case News:
                helper.setImageResource(R.id.msg_module_icon, R.drawable.ic_app_information);
                break;
            case CourseSchedule:
                helper.setImageResource(R.id.msg_module_icon, R.drawable.ic_app_course_schedule);
                TextView timeView = helper.getView(R.id.msg_module_time);
                timeView.setVisibility(View.VISIBLE);
                timeView.setText(item.datetime);
                helper.setText(R.id.msg_module_sub, mContext.getString(R.string.course_week, item.weeks));
                break;
        }
    }
}
