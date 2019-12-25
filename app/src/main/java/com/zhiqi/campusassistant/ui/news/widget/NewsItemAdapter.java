package com.zhiqi.campusassistant.ui.news.widget;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ming.base.widget.recyclerView.BaseMultiItemQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.news.entity.NewsInfo;
import com.zhiqi.campusassistant.ui.web.activity.WebActivity;

/**
 * Created by ming on 2016/12/23.
 */

public class NewsItemAdapter extends BaseMultiItemQuickAdapter<NewsInfo> {

    public NewsItemAdapter() {
        super(null);
        addItemType(1, R.layout.item_news_picture);
        addItemType(2, R.layout.item_news_title);
        addItemType(3, R.layout.item_news_fix);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsInfo item, int position) {
        helper.setText(R.id.title, item.title);
        int type = getItemViewType(item);
        switch (type) {
            case 1:
                GlideApp.with(mContext)
                        .load(item.thumbnails)
                        .placeholder(R.drawable.ic_img_big_default)
                        .into((ImageView) helper.getView(R.id.new_pic));
                break;
            case 2:
                TextView subTitle = helper.getView(R.id.sub_title);
                if (!TextUtils.isEmpty(item.summary)) {
                    subTitle.setVisibility(View.VISIBLE);
                    subTitle.setText(item.summary);
                } else {
                    subTitle.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.source)) {
                    helper.setText(R.id.describe, mContext.getString(R.string.news_describe, item.publish_time, item.source));
                } else {
                    helper.setText(R.id.describe, item.publish_time);
                }
                break;
            case 3:
                subTitle = helper.getView(R.id.sub_title);
                if (!TextUtils.isEmpty(item.summary)) {
                    subTitle.setVisibility(View.VISIBLE);
                    subTitle.setText(item.summary);
                } else {
                    subTitle.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.source)) {
                    helper.setText(R.id.describe, mContext.getString(R.string.news_describe, item.publish_time, item.source));
                } else {
                    helper.setText(R.id.describe, item.publish_time);
                }
                GlideApp.with(mContext)
                        .load(item.thumbnails)
                        .placeholder(R.drawable.ic_img_square_default)
                        .into((ImageView) helper.getView(R.id.new_pic));
                break;
        }
        helper.getConvertView().setTag(R.id.news_item, item.url);
        helper.getConvertView().setOnClickListener(itemClick);
    }

    private View.OnClickListener itemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag(R.id.news_item);
            if (tag != null) {
                String url = (String) tag;
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(AppConstant.EXTRA_URL, url);
                mContext.startActivity(intent);
            }
        }
    };

    @Override
    protected int getItemViewType(NewsInfo item) {
        if (item.show_type == null) {
            return 0;
        }
        switch (item.show_type) {
            case Picture:
                return 1;
            case Title:
                return 2;
            case Fix:
                return 3;
        }
        return 0;
    }
}
