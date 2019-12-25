package com.zhiqi.campusassistant.ui.launch.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ming.base.util.AppUtil;
import com.ming.base.util.DeviceUtil;
import com.ming.base.util.Log;
import com.ming.base.util.ViewUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.ui.widget.ViewPagerAdapter;

/**
 * Created by ming on 2017/3/27.
 * 导航页adapter
 */

public class NavigationAdapter extends ViewPagerAdapter {

    private static final int MAX_SIZE = 900;

    private LayoutInflater inflater;

    private Context mContext;

    private int statusHeight;

    private int imgSize;

    public NavigationAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        statusHeight = AppUtil.getStatusBarHeight(context);
        int screenHeight = DeviceUtil.getWindWidthAndHeight(mContext)[1] - AppUtil.getStatusBarHeight(mContext);
        imgSize = screenHeight > MAX_SIZE ? MAX_SIZE : screenHeight - 100;
        Log.i("NavigationAdapter", "imgSize:" + imgSize);
    }

    @SuppressLint("CheckResult")
    @Override
    public View instantiateItem(ViewGroup container, View itemView, int position) {
        if (itemView == null) {
            int bgRes = 0;
            int contentRes = 0;
            switch (position) {
                case 0:
                    itemView = inflater.inflate(R.layout.view_navigation_page1, container, false);
//                    itemView = inflater.inflate(R.layout.view_navigation_page1, container, false);
//                    bgRes = R.drawable.bg_nav_login;
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//                        // 避免与启动页的位置有误，因此需要减去status bar高度
//                        View view = itemView.findViewById(R.id.logo_layout);
//                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
//                        layoutParams.topMargin = layoutParams.topMargin - statusHeight;
//                        view.setLayoutParams(layoutParams);
//                    }
                    break;
                case 1:
                    itemView = inflater.inflate(R.layout.view_navigation_page2, container, false);
                    bgRes = R.drawable.bg_nav_leave;
                    contentRes = R.drawable.img_nav_leave_content;
                    TextView module = (TextView) itemView.findViewById(R.id.nav_module);
                    module.setText(R.string.nav_module_leave);
                    TextView tip = (TextView) itemView.findViewById(R.id.nav_tip);
                    tip.setText(R.string.nav_module_leave_tip);
                    break;
                case 2:
                    itemView = inflater.inflate(R.layout.view_navigation_page2, container, false);
                    bgRes = R.drawable.bg_nav_repair;
                    contentRes = R.drawable.img_nav_repair_content;
                    module = (TextView) itemView.findViewById(R.id.nav_module);
                    module.setText(R.string.nav_module_repair);
                    tip = (TextView) itemView.findViewById(R.id.nav_tip);
                    tip.setText(R.string.nav_module_repair_tip);
                    break;
                case 3:
                    itemView = inflater.inflate(R.layout.view_navigation_page2, container, false);
                    bgRes = R.drawable.bg_nav_course;
                    contentRes = R.drawable.img_nav_course_content;
                    module = (TextView) itemView.findViewById(R.id.nav_module);
                    module.setText(R.string.nav_module_course);
                    tip = (TextView) itemView.findViewById(R.id.nav_tip);
                    tip.setText(R.string.nav_module_course_tip);
                    break;
            }
            if (bgRes != 0) {
                ImageView imageView = itemView.findViewById(R.id.nav_bg);
                RequestBuilder<Drawable> builder = GlideApp.with(mContext)
                        .load(bgRes)
                        .skipMemoryCache(true)
                        .override(imgSize, imgSize);
                // 修改第一页加载效果
                if (position == 0) {
                    builder.listener(new LoadViewListener(itemView));
                }
                builder.into(imageView);

            }
            if (position != 0) {
                int size = imgSize - 100;
                if (contentRes != 0) {
                    ImageView imageView = itemView.findViewById(R.id.nav_module_img);
                    GlideApp.with(mContext)
                            .load(contentRes)
                            .skipMemoryCache(true)
                            .override(size, size)
                            .listener(new OversizeListener(imageView))
                            .into(imageView);
                }
            }
        }
        return itemView;
    }

    private class LoadViewListener implements RequestListener<Drawable> {

        private View itemView;

        public LoadViewListener(View itemView) {
            this.itemView = itemView;
            this.itemView.setVisibility(View.INVISIBLE);
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            itemView.setVisibility(View.VISIBLE);
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            itemView.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private class OversizeListener implements RequestListener<Drawable> {

        private ImageView imageView;

        public OversizeListener(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(final Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            ViewUtil.addOnGlobalLayoutListener(imageView, new Runnable() {
                @Override
                public void run() {
                    try {
                        double width = resource.getIntrinsicWidth();
                        double height = resource.getIntrinsicHeight();
                        double vHeight = imageView.getHeight();
                        int vWidth = (int) (width / height * vHeight);
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        params.width = vWidth;
                        imageView.setLayoutParams(params);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            return false;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
