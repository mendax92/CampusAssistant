package com.zhiqi.campusassistant.common.utils;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zhiqi.campusassistant.R;

/**
 * Created by minh on 18-2-7.
 * app简单对话框
 */

public class PromptDialogUtil {

    /**
     * 提醒框
     */
    public static void warn(Activity activity, @StringRes int msgRes) {
        warn(activity, activity.getString(msgRes), null);
    }

    /**
     * 提醒框
     */
    public static void warn(Activity activity, CharSequence msg) {
        warn(activity, msg, null);
    }

    public static void warn(Activity activity, CharSequence msg, MaterialDialog.SingleButtonCallback onPositive) {
        buildPromptDialog(activity, R.drawable.ic_tip_warnning, msg, true, activity.getString(R.string.common_confirm), onPositive)
                .build()
                .show();
    }

    public static void warn(Activity activity, CharSequence msg, CharSequence positiveTxt, MaterialDialog.SingleButtonCallback onPositive) {
        buildPromptDialog(activity, R.drawable.ic_tip_warnning, msg, true, positiveTxt, onPositive)
                .negativeColor(ContextCompat.getColor(activity, R.color.text_blue_color))
                .negativeText(R.string.common_cancel)
                .build()
                .show();
    }

    private static MaterialDialog.Builder buildPromptDialog(Activity activity, @DrawableRes int iconRes, CharSequence msg, boolean cancelable, CharSequence positiveTxt, MaterialDialog.SingleButtonCallback onPositive) {
        return buildDialog(activity, ContextCompat.getDrawable(activity, iconRes), msg, cancelable)
                .maxIconSize(activity.getResources().getDimensionPixelSize(R.dimen.common_ic_dialog_size))
                .positiveText(positiveTxt)
                .onPositive(onPositive);
    }

    private static MaterialDialog.Builder buildDialog(Activity activity, Drawable icon, CharSequence msg, boolean cancelable) {
        View rootView = LayoutInflater.from(activity).inflate(R.layout.view_prompt_dialog, null);
        ImageView iconView = rootView.findViewById(R.id.prompt_icon);
        if (icon != null) {
            iconView.setImageDrawable(icon);
            iconView.setVisibility(View.VISIBLE);
        } else {
            iconView.setVisibility(View.GONE);
        }
        TextView msgView = rootView.findViewById(R.id.prompt_msg);
        msgView.setText(msg);
        return new MaterialDialog.Builder(activity)
                .customView(rootView, true)
                .cancelable(cancelable);
    }
}
