package com.ming.base.util;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by ming on 2016/12/1.
 */

public class ViewUtil {


    public static void addOnGlobalLayoutListener(final View view, final Runnable runnable) {
        if (view.getWidth() > 0) {
            runnable.run();
            return;
        }
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                runnable.run();
            }
        });
    }
}
