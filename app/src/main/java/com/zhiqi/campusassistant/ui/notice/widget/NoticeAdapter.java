package com.zhiqi.campusassistant.ui.notice.widget;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.notice.entity.NoticeInfo;
import com.zhiqi.campusassistant.ui.web.activity.WebActivity;

/**
 * Created by ming on 2017/5/3.
 * 通知公告
 */

public class NoticeAdapter extends BaseQuickAdapter<NoticeInfo> {

    public NoticeAdapter() {
        super(R.layout.item_notice, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeInfo item, int position) {
        helper.setText(R.id.notice_title, item.title);
        helper.setText(R.id.notice_summary, item.summary);
        helper.setText(R.id.notice_time, item.publish_time);
        helper.getConvertView().setTag(R.id.notice_item, position);
        helper.getConvertView().setOnClickListener(itemListener);
    }

    private View.OnClickListener itemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag(R.id.notice_item);
            if (tag != null && tag instanceof Integer) {
                int position = (int) tag;
                NoticeInfo info = getItem(position);
                if (!TextUtils.isEmpty(info.detail_url)) {
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra(AppConstant.EXTRA_URL, info.detail_url);
                    mContext.startActivity(intent);
                }
            }
        }
    };
}
