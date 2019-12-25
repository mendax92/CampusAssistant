package com.ming.photopicker.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.Arrays;

/**
 * Created by ming on 2017/2/23.
 */

public class AnimationUtil {

    private static final String TAG = "AnimationUtil";

    public static void runEnterAnimation(final View targetView, final AnimationData animationData, final long duration) {
        if (targetView == null || animationData == null) {
            return;
        }
        if (targetView.getHeight() == 0) {
            ViewTreeObserver observer = targetView.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    targetView.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (targetView.getHeight() > 0) {
                        runEnterAnimation(targetView, animationData, duration);
                    }
                    return true;
                }
            });
        } else {
            int thumbnailLeft = animationData.startLeft;
            int thumbnailTop = animationData.startTop;
            int[] screenLocation = new int[2];
            targetView.getLocationOnScreen(screenLocation);

            int sourceWidth = animationData.sourceWidth;
            int sourceHeight = animationData.sourceHeight;

            thumbnailLeft = thumbnailLeft - screenLocation[0];
            thumbnailTop = thumbnailTop - screenLocation[1];
            // Set starting values for properties we're going to animate. These
            // values scale and position the full size version down to the thumbnail
            // size/location, from which we'll animate it back up
            ViewHelper.setPivotX(targetView, 0);
            ViewHelper.setPivotY(targetView, 0);
            ViewHelper.setScaleX(targetView, (float) sourceWidth / targetView.getWidth());
            ViewHelper.setScaleY(targetView, (float) sourceHeight / targetView.getHeight());
            ViewHelper.setTranslationX(targetView, thumbnailLeft);
            ViewHelper.setTranslationY(targetView, thumbnailTop);

            // Animate scale and translation to go from thumbnail to full size
            ViewPropertyAnimator.animate(targetView)
                    .setDuration(duration)
                    .scaleX(1)
                    .scaleY(1)
                    .translationX(0)
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator());

        }
    }


    public static void runExitAnimation(final View targetView, final AnimationData animationData, final long duration, final Runnable endAction) {
        if (targetView == null || animationData == null) {
            return;
        }
        if (targetView.getHeight() == 0) {
            ViewTreeObserver observer = targetView.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    targetView.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (targetView.getHeight() > 0) {
                        runExitAnimation(targetView, animationData, duration, endAction);
                    }
                    return true;
                }
            });
        } else {
            int thumbnailLeft = animationData.startLeft;
            int thumbnailTop = animationData.startTop;
            int[] screenLocation = new int[2];
            targetView.getLocationOnScreen(screenLocation);

            thumbnailLeft = thumbnailLeft - screenLocation[0];
            thumbnailTop = thumbnailTop - screenLocation[1];
            Log.i(TAG, "screenLocation:" + Arrays.toString(screenLocation));
            // Set starting values for properties we're going to animate. These
            // values scale and position the full size version down to the thumbnail
            // size/location, from which we'll animate it back up
            ViewPropertyAnimator.animate(targetView)
                    .setDuration(duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .scaleX((float) animationData.sourceWidth / targetView.getWidth())
                    .scaleY((float) animationData.sourceHeight / targetView.getHeight())
                    .translationX(thumbnailLeft)
                    .translationY(thumbnailTop)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            endAction.run();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });

        }
    }

    public static class AnimationData {
        public int startLeft;
        public int startTop;
        public int sourceWidth;
        public int sourceHeight;

        public AnimationData() {
        }

        public AnimationData(int startLeft, int startTop, int sourceWidth, int sourceHeight) {
            this.startLeft = startLeft;
            this.startTop = startTop;
            this.sourceWidth = sourceWidth;
            this.sourceHeight = sourceHeight;
        }
    }
}
