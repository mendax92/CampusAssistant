package com.ming.base.rx2;

import com.ming.base.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ming on 2017/11/15.
 * 简单观察对象，可控制{@link Observable#empty()}null值回调
 */

public class SimpleObserver<T> implements Observer<T> {

    private static final String TAG = "SampleObserver";

    private Consumer<T> consumer;

    private boolean hasNext;

    private SimpleObserver(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T t) {
        hasNext = true;
        if (consumer != null) {
            try {
                consumer.accept(t);
            } catch (Throwable e) {
                Log.w(TAG, e);
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.w(TAG, e);
        onComplete();
    }

    @Override
    public void onComplete() {
        if (!hasNext) {
            onNext(null);
        }
    }

    /**
     * 创建observer
     */
    public static <T> SimpleObserver<T> create(Consumer<T> consumer) {
        return new SimpleObserver<>(consumer);
    }
}
