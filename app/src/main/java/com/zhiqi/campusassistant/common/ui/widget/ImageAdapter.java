package com.zhiqi.campusassistant.common.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ming.base.util.Log;
import com.ming.base.widget.recyclerView.BaseQuickAdapter;
import com.ming.base.widget.recyclerView.BaseViewHolder;
import com.ming.photopicker.PhotoPreview;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.entity.ImageData;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.glide.GlideRequest;
import com.zhiqi.campusassistant.common.glide.GlideRequests;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ming on 2017/2/6.
 */

public class ImageAdapter extends BaseQuickAdapter<ImageData> implements ListPreloader.PreloadModelProvider<ImageData> {

    private GlideRequests glide;
    private GlideRequest<Drawable> fullRequest;
    private GlideRequest<Drawable> thumbRequest;

    public ImageAdapter(Context context, List<ImageData> data) {
        super(R.layout.item_image, data);
        this.glide = GlideApp.with(context);
        int size = context.getResources().getDimensionPixelSize(R.dimen.common_choose_img_size);
        fullRequest = glide.asDrawable()
                .centerCrop()
                .placeholder(R.drawable.ic_img_square_default);
        thumbRequest = glide.asDrawable()
                .override(size)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade());
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageData item, int position) {
        ImageView imageView = helper.getView(R.id.image);
        imageView.setOnClickListener(clickListener);
        imageView.setTag(R.id.image, position);
        fullRequest.load(item.origin)
                .thumbnail(TextUtils.isEmpty(item.thumbnail) ? null : thumbRequest.load(item.thumbnail))
                .into(imageView);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.image);
            PhotoPreview.builder()
                    .setPhotos(getOrigin())
                    .setThumbnails(getThumbnails())
                    .setCurrentItem(position)
                    .setShowDeleteButton(false)
                    .start((Activity) mContext);
        }
    };

    public ArrayList<String> getOrigin() {
        List<ImageData> data = getData();
        ArrayList<String> list = new ArrayList<>();
        for (ImageData itemData : data) {
            if (!TextUtils.isEmpty(itemData.origin)) {
                list.add(itemData.origin);
            }
        }
        return list;
    }

    public ArrayList<String> getThumbnails() {
        List<ImageData> data = getData();
        ArrayList<String> list = new ArrayList<>();
        for (ImageData itemData : data) {
            if (!TextUtils.isEmpty(itemData.origin)) {
                list.add(itemData.thumbnail);
            }
        }
        Log.i(TAG, "list:" + list);
        return list;
    }

    @NonNull
    @Override
    public List<ImageData> getPreloadItems(int position) {
        return mData.subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder getPreloadRequestBuilder(ImageData item) {
        return fullRequest.load(item.origin)
                .thumbnail(TextUtils.isEmpty(item.thumbnail) ? null : thumbRequest.load(item.thumbnail));
    }
}
