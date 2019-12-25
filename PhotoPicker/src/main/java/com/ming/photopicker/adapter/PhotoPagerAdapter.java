package com.ming.photopicker.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ming.photopicker.R;
import com.ming.photopicker.utils.AndroidLifecycleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by donglua on 15/6/21.
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private List<String> paths = new ArrayList<>();
    private List<String> thumbnails = new ArrayList<>();
    private RequestManager mGlide;
    private RequestOptions requestOptions;

    public PhotoPagerAdapter(RequestManager glide, List<String> paths, List<String> thumbnails) {
        this.paths = paths;
        this.thumbnails = thumbnails;
        this.mGlide = glide;
        requestOptions = new RequestOptions()
                .fitCenter()
                .override(800, 800)
                .placeholder(R.drawable.photo_picker_ic_photo_black_48dp)
                .error(R.drawable.photo_picker_ic_broken_image_black_48dp);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Context context = container.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.photo_picker_item_pager, container, false);

        final ContentLoadingProgressBar loadingProgressBar = itemView.findViewById(R.id.iv_progress);
        loadingProgressBar.show();

        final ImageView imageView = itemView.findViewById(R.id.iv_pager);

        final String path = paths.get(position);

        boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);

        if (canLoadImage) {
            RequestBuilder<Drawable> thumbnailRequest = null;
            if (thumbnails != null && !thumbnails.isEmpty()) {
                thumbnailRequest = mGlide.load(thumbnails.get(position));
            }
            RequestBuilder<Drawable> requestBuilder = mGlide.load(path);
            if (thumbnailRequest != null) {
                requestBuilder = requestBuilder.thumbnail(thumbnailRequest);
            }
            requestBuilder
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loadingProgressBar.hide();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            loadingProgressBar.hide();
                            ViewGroup.LayoutParams params = imageView.getLayoutParams();
                            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                            imageView.setLayoutParams(params);
                            return false;
                        }
                    })
                    .into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing()) {
                        ((Activity) context).onBackPressed();
                    }
                }
            }
        });

        container.addView(itemView);

        return itemView;
    }


    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mGlide.clear((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
