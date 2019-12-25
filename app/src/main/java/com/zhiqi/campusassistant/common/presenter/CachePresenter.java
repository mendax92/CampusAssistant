package com.zhiqi.campusassistant.common.presenter;

import android.content.Context;

import com.ming.base.greendao.rx2.RxDao;
import com.ming.base.http.HttpManager;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheData;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by ming on 2017/5/5.
 * 缓存presenter
 */

public class CachePresenter extends SimplePresenter {

    protected RxDao<CacheData, String> cacheDao;

    public CachePresenter(Context context, RxDao<CacheData, String> cacheDao) {
        super(context);
        this.cacheDao = cacheDao;
    }

    protected <T> void handleCache(final String cacheKey, final boolean skipCache, final Type resultType, Observable<BaseResultData<T>> observable, final ILoadView<T> loadView) {
        if (skipCache) {
            subscribe(observable, loadView);
        } else {
            handleCache(cacheKey, resultType, observable, loadView);
        }
    }

    /**
     * 处理缓存
     *
     * @param cacheKey
     * @param observable
     * @param loadView
     * @param <T>
     */
    protected <T> void handleCache(final String cacheKey, final Type resultType, Observable<BaseResultData<T>> observable, final ILoadView<T> loadView) {
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<T>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<T> result) {
                if (!isReleased(loadView)) {
                    loadView.onLoadData(result.data);
                }
                if (result.data != null) {
                    cacheDao.insertOrReplace(new CacheData<T>(cacheKey, result.data))
                            .subscribe();
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onFailed(errorCode, message);
                }
                cacheDao.load(cacheKey)
                        .flatMap(new Function<CacheData, ObservableSource<T>>() {
                            @Override
                            public ObservableSource<T> apply(CacheData cacheData) throws Exception {
                                T data = null;
                                if (cacheData != null) {
                                    data = (T) cacheData.getData(resultType);
                                }
                                return Observable.just(data);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<T>() {
                            @Override
                            public void accept(T cacheData) throws Exception {
                                if (!isReleased(loadView)) {
                                    loadView.onLoadData(cacheData);
                                }
                            }
                        });
            }
        });
    }
}
