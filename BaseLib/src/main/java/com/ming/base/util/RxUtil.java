package com.ming.base.util;

import android.os.Looper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ming on 2016/10/8.
 */

public class RxUtil {

    /**
     * 倒计时
     *
     * @param time 总秒数
     * @return
     */
    public static Observable<Integer> countdown(int time) {
        if (time < 0) time = 0;
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime);

    }

    /**
     * 主线程操作
     *
     * @param runnable
     */
    public static Disposable postOnMainThread(Runnable runnable, long delay) {
        return AndroidSchedulers.mainThread().scheduleDirect(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public static Disposable postOnMainThread(Runnable runnable) {
        return AndroidSchedulers.mainThread().scheduleDirect(runnable);
    }

    /**
     * 是否为UI线程
     *
     * @return
     */
    public static boolean isUIThread() {
        long uiId = Looper.getMainLooper().getThread().getId();
        long cId = Thread.currentThread().getId();
        return uiId == cId;
    }

    /**
     * IO线程操作
     *
     * @param runnable
     */
    public static Disposable postOnIoThread(Runnable runnable) {
        return postOnIoThread(runnable, 0);
    }

    public static Disposable postOnIoThread(final Runnable runnable, long delay) {
        return Schedulers.io().scheduleDirect(runnable, delay, TimeUnit.MILLISECONDS);
    }
}
