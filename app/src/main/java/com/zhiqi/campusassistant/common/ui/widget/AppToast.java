package com.zhiqi.campusassistant.common.ui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiqi.campusassistant.R;

import butterknife.ButterKnife;


public class AppToast {

    public static class AppsToast extends Toast {

        private View rootView;

        private AppsToast(Context context) {
            super(context);
        }

        @Override
        public void setText(int resId) {
            if (rootView != null) {
                TextView contentView = ButterKnife.findById(rootView, R.id.toast_message);
                if (contentView != null) {
                    contentView.setText(resId);
                }
            }
        }

        @Override
        public void setText(CharSequence s) {
            if (rootView != null) {
                TextView contentView = ButterKnife.findById(rootView, R.id.toast_message);
                if (contentView != null) {
                    contentView.setText(s);
                }
            }
        }

        @Override
        public void setView(View view) {
            super.setView(view);
            rootView = view;
        }

        public void setIconRes(int iconRes) {
            if (rootView != null) {
                ImageView iconView = ButterKnife.findById(rootView, R.id.toast_img);
                if (iconView != null) {
                    iconView.setImageDrawable(iconView.getResources().getDrawable(iconRes));
                }
            }
        }
    }

    public static class Builder {
        private Context context;
        private View contentView;
        protected CharSequence text;
        private int duration;
        protected int iconRes = 0;
        private int gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        private int xOffset = 0;
        private int yOffset = 0;
        private int horizontalMargin = 0;
        private int verticalMargin = 0;

        public Builder(Context context) {
            this.context = context;
            this.yOffset = context.getResources().getDimensionPixelSize(
                    R.dimen.toast_y_offset);
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setText(CharSequence text) {
            this.text = text;
            return this;
        }

        public Builder setText(int textId) {
            this.text = context.getString(textId);
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setIconRes(int iconRes) {
            this.iconRes = iconRes;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setXOffset(int xOffset) {
            this.xOffset = xOffset;
            return this;
        }

        public Builder setYOffset(int yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder setVerticalMargin(int verticalMargin) {
            this.verticalMargin = verticalMargin;
            return this;
        }

        public Builder setHorizontalMargin(int horizontalMargin) {
            this.horizontalMargin = horizontalMargin;
            return this;
        }

        public AppsToast create() {

            View toastRootView;

            if (contentView == null) {
                LayoutInflater inflate = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                toastRootView = inflate.inflate(R.layout.view_app_toast, null);
                TextView contentView = ButterKnife.findById(toastRootView, R.id.toast_message);
                ImageView iconImgView = ButterKnife.findById(toastRootView, R.id.toast_img);

                if (iconRes > 0) {
                    if (iconRes < 0) {
                        iconImgView.setVisibility(View.GONE);
                    } else {
                        iconImgView.setVisibility(View.VISIBLE);
                        iconImgView.setImageResource(iconRes);
                    }
                }

                contentView.setText(text);
            } else {
                toastRootView = contentView;
            }

            AppsToast toast = new AppsToast(context);
            toast.setDuration(duration);
            toast.setMargin(horizontalMargin, verticalMargin);
            toast.setGravity(gravity, xOffset, yOffset);
            toast.setView(toastRootView);

            return toast;
        }

        public AppsToast show() {
            AppsToast toast = create();
            toast.show();
            return toast;
        }
    }

    /**
     * 显示自定义的Toast
     *
     * @param context  上下文
     * @param iconRes  上方icon图片资源
     * @param content  下方文字内容
     * @param duration 显示时长
     * @return
     */
    public static android.widget.Toast showToast(Context context, int iconRes, CharSequence content, int duration) {
        Builder toastBuilder = new Builder(context);
        toastBuilder.setDuration(duration).setText(content).setIconRes(iconRes);
        return toastBuilder.show();
    }


    /**
     * 显示自定义Toast
     *
     * @param context    上下文
     * @param iconRes    图标
     * @param contentRes 文字
     * @param duration   时长
     * @return
     */
    public static android.widget.Toast showToast(Context context, int iconRes, int contentRes, int duration) {
        String content = context.getString(contentRes);
        return showToast(context, iconRes, content, duration);
    }

    /**
     * 显示自定义Toast
     *
     * @param context    上下文
     * @param contentRes 文字
     * @param duration   时长
     * @return
     */
    public static android.widget.Toast showToast(Context context, int contentRes, int duration) {
        String content = context.getString(contentRes);
        return showToast(context, -1, content, duration);
    }

    /**
     * 显示自定义Toast
     *
     * @param context  上下文
     * @param content  文字
     * @param duration 时长
     * @return
     */
    public static android.widget.Toast showToast(Context context, String content, int duration) {
        return showToast(context, -1, content, duration);
    }


    /**
     * 显示自定义的Toast,显示位置在屏幕正中间
     *
     * @param context  上下文
     * @param iconRes  上方icon图片资源
     * @param content  下方文字内容
     * @param duration 显示时长
     * @return
     */
    public static android.widget.Toast showCenterToast(Context context, int iconRes, CharSequence content, int duration) {
        Builder toastBuilder = new Builder(context);
        toastBuilder.setDuration(duration).setText(content).setIconRes(iconRes).setGravity(Gravity.CENTER).setXOffset(0).setYOffset(0);
        return toastBuilder.show();
    }


    /**
     * 显示自定义Toast,显示位置在屏幕正中间
     *
     * @param context    上下文
     * @param iconRes    图标
     * @param contentRes 文字
     * @param duration   时长
     * @return
     */
    public static android.widget.Toast showCenterToast(Context context, int iconRes, int contentRes, int duration) {
        String content = context.getString(contentRes);
        return showCenterToast(context, iconRes, content, duration);
    }

    /**
     * 显示自定义Toast,显示位置在屏幕正中间
     *
     * @param context    上下文
     * @param contentRes 文字
     * @param duration   时长
     * @return
     */
    public static android.widget.Toast showCenterToast(Context context, int contentRes, int duration) {
        String content = context.getString(contentRes);
        return showCenterToast(context, -1, content, duration);
    }

    /**
     * 显示自定义Toast,显示位置在屏幕正中间
     *
     * @param context  上下文
     * @param content  文字
     * @param duration 时长
     * @return
     */
    public static android.widget.Toast showCenterToast(Context context, String content, int duration) {
        return showCenterToast(context, -1, content, duration);
    }

}
