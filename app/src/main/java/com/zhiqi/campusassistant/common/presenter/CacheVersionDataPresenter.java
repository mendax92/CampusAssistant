package com.zhiqi.campusassistant.common.presenter;

import android.content.Context;

import com.ming.base.greendao.rx2.RxDao;
import com.ming.base.http.HttpManager;
import com.ming.base.rx2.SimpleObserver;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.entity.CacheData;
import com.zhiqi.campusassistant.common.entity.DataVersion;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.common.ui.view.ILoadView;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by ming on 2017/5/8.
 * 缓存版本数据presenter
 */

public abstract class CacheVersionDataPresenter<T extends DataVersion> extends CachePresenter {

    /**
     * 缓存最大时间，超时重新获取
     */
    private static final long CACHE_MAX_TIMEOUT = 24 * 60 * 60 * 1000;

    public CacheVersionDataPresenter(Context context, RxDao<CacheData, String> cacheDao) {
        super(context, cacheDao);
    }

    /**
     * 加载版本信息
     *
     * @param key
     * @param type
     * @param loadView
     */
    protected void loadVersionData(String key, final Type type, final ILoadView<T> loadView) {
        cacheDao.load(key)
                .flatMap(new Function<CacheData, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(CacheData cacheData) throws Exception {
                        T data = null;
                        if (cacheData != null) {
                            data = (T) cacheData.getData(type);
                        }
                        return Observable.just(data);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(SimpleObserver.create(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        //本地为空或者缓存超过24小时，则加载网络数据
                        if (t == null || System.currentTimeMillis() - t.cacheTime > CACHE_MAX_TIMEOUT) {
                            loadVersionDataFromNet(loadView);
                        } else if (!isReleased(loadView)) {
                            loadView.onLoadData(t);
                        }
                    }
                }));
    }

    /**
     * 从网络加载数据版本信息
     *
     * @param key
     * @param observable
     * @param loadView
     */
    protected void loadVersionDataFromNet(final String key, Observable<BaseResultData<T>> observable, final ILoadView<T> loadView) {
        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<T>>(mContext) {
            @Override
            public void onSuccess(BaseResultData<T> result) {
                if (!isReleased(loadView)) {
                    loadView.onLoadData(result.data);
                }
                if (result.data != null) {
                    result.data.cacheTime = System.currentTimeMillis();
                    cacheDao.insertOrReplace(new CacheData<T>(key, result.data))
                            .subscribe();
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (!isReleased(loadView)) {
                    loadView.onFailed(errorCode, message);
                }
            }
        });
    }

    /**
     * 加载版本信息
     *
     * @param loadView
     */
    public abstract void loadVersionData(ILoadView<T> loadView);

    /**
     * 从网络加载数据版本信息
     *
     * @param loadView
     */
    public abstract void loadVersionDataFromNet(ILoadView<T> loadView);
}
