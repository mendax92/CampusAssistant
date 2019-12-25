package com.zhiqi.campusassistant.ui.news.widget;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.widget.ViewPagerAdapter;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.core.news.entity.NewsInfo;
import com.zhiqi.campusassistant.ui.web.activity.WebActivity;

import java.util.List;

/**
 * Created by ming on 2017/3/12.
 * 头部新闻
 */

public class HeadNewsPageAdapter extends ViewPagerAdapter {

    private static final String TAG = "HeadNewsPageAdapter";

    private Context mContext;

    private List<NewsInfo> data;

    public HeadNewsPageAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<NewsInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public View instantiateItem(ViewGroup container, View itemView, int position) {
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_news_picture, container, false);
        }
        if (data != null && position < data.size()) {
            ImageView img = (ImageView) itemView.findViewById(R.id.new_pic);
            NewsInfo item = data.get(position);
            TextView textView = (TextView) itemView.findViewById(R.id.title);
            textView.setText(item.title);
            GlideApp.with(mContext)
                    .load(item.thumbnails)
                    .placeholder(R.drawable.ic_img_big_default)
                    .into(img);
            itemView.setTag(R.id.news_item, item.url);
            itemView.setOnClickListener(itemClick);
        }
        return itemView;
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
    public int getCount() {
        return data == null ? 0 : data.size();
    }
}
