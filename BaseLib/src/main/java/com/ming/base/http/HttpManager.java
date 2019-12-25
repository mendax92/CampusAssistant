package com.ming.base.http;

import android.text.TextUtils;

import com.ming.base.http.interfaces.IDataParser;
import com.ming.base.http.interfaces.OnHttpCallback;
import com.ming.base.util.GsonUtils;
import com.ming.base.util.Log;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by Edmin on 2016/9/4
 * http观察者管理
 */
public class HttpManager {

    private static final String TAG = "HttpManager";

    /**
     * 观察者线程分发
     *
     * @param observable retrofit返回的observable
     * @param callback   http回调
     * @param parser     解析器
     * @param <Data>
     * @param <Result>
     */
    public static <Data, Result> void subscribe(final Observable<Data> observable, final OnHttpCallback<Result> callback, final IDataParser<Data, Result> parser, boolean postMainThread) {
        if (observable == null) {
            return;
        }
        Observable<Result> obser = observable.subscribeOn(Schedulers.io())
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Data>>() {
                    @Override
                    public ObservableSource<? extends Data> apply(Throwable throwable) throws Exception {
                        try {
                            if (throwable instanceof HttpException) {
                                HttpException exception = (HttpException) throwable;
                                String json = exception.response().errorBody().string();
                                if (!TextUtils.isEmpty(json)) {
                                    Type type = getType(observable);
                                    if (type == null) {
                                        if (parser == null) {
                                            type = getType(callback);
                                        } else {
                                            type = getType(parser);
                                        }
                                    }
                                    Log.i(TAG, "onErrorResumeNext json:" + json);
                                    if (type != null) {
                                        Data data = GsonUtils.fromJson(json, type);
                                        return Observable.just(data);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return Observable.error(throwable);
                    }
                })
                .map(new Function<Data, Result>() {
                    @Override
                    public Result apply(Data data) throws Exception {
                        Log.i(TAG, "http map thread " + Thread.currentThread().getId());
                        Result result = null;
                        try {
                            if (parser != null) {
                                result = parser.parseData(data);
                            } else {
                                result = (Result) data;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return result;
                    }
                });
        if (postMainThread) {
            obser = obser.observeOn(AndroidSchedulers.mainThread());
        }
        obser.subscribe(new Observer<Result>() {
            @Override
            public void onComplete() {
                Log.i(TAG, "Http onCompleted.");
            }

            @Override
            public void onError(Throwable e) {
                try {
                    Log.w(TAG, "Http onError", e);
                    if (callback != null) {
                        callback.onFailure(e);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result data) {
                try {
                    Log.i(TAG, "Http onNext data:" + data);
                    callback.onFinished(data);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static <Data, Result> void subscribe(final Observable<Data> observable, final OnHttpCallback<Result> callback, final IDataParser<Data, Result> parser) {
        subscribe(observable, callback, parser, true);
    }

    public static <Result> void subscribe(Observable<Result> observable, final OnHttpCallback<Result> listener, boolean postMainThread) {
        subscribe(observable, listener, null, postMainThread);
    }

    public static <Result> void subscribe(Observable<Result> observable, final OnHttpCallback<Result> listener) {
        subscribe(observable, listener, true);
    }

    private static Type getType(Object object) {
        Type type = null;
        try {
            Type classSuperclass = object.getClass().getGenericSuperclass();
            if (classSuperclass != null) {
                Type[] types = ((ParameterizedType) classSuperclass).getActualTypeArguments();
                if (types != null) {
                    type = types[0];
                }
            }
            Log.i(TAG, object.getClass() + " Arguments type:" + type);
        } catch (Exception ex) {
        }
        return type;
    }
}
