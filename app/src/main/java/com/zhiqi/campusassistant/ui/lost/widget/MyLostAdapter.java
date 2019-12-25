package com.zhiqi.campusassistant.ui.lost.widget;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.entity.ImageData;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.widget.ImageAdapter;
import com.zhiqi.campusassistant.core.lost.entity.MyLostInfo;

/**
 * Created by ming on 2017/5/8.
 * 我的失物招领adapter
 */

public class MyLostAdapter extends BaseQuickAdapter<MyLostInfo> {

    public MyLostAdapter() {
        super(R.layout.item_lost_my, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyLostInfo item, int position) {
        helper.setText(R.id.month, item.publish_month);
        helper.setText(R.id.day, item.publish_day);
        helper.setText(R.id.content, item.content);
        helper.setText(R.id.lost_type, item.type_name);
        TextView lostName = helper.getView(R.id.type_name);
        lostName.setText(item.lost_name);
        lostName.setTextColor(LostHelper.getLostTextColor(mContext, item.lost_type));
        RecyclerView lostImgs = helper.getView(R.id.lost_img);
        if (item.images == null || item.images.isEmpty()) {
            lostImgs.setVisibility(View.GONE);
        } else {
            lostImgs.setVisibility(View.VISIBLE);
            ImageAdapter imageAdapter = new ImageAdapter(mContext, null);

            ViewPreloadSizeProvider<ImageData> preloadSizeProvider = new ViewPreloadSizeProvider<>();
            RecyclerViewPreloader<ImageData> preloader =
                    new RecyclerViewPreloader<>(GlideApp.with(mContext), imageAdapter, preloadSizeProvider, 5);
            lostImgs.addOnScrollListener(preloader);

            lostImgs.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            lostImgs.removeItemDecoration(itemDecoration);
            lostImgs.addItemDecoration(itemDecoration);
            lostImgs.setAdapter(imageAdapter);
            imageAdapter.setNewData(item.images);
        }
        helper.addOnClickListener(R.id.delete);
    }

    private RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int left = 0;
            if (position > 0) {
                left = mContext.getResources().getDimensionPixelSize(R.dimen.common_margin_xs);
            }
            outRect.set(left, 0, 0, 0);
        }
    };
}
