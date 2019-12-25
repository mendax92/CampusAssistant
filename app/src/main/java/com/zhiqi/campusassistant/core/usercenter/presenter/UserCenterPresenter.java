package com.zhiqi.campusassistant.core.usercenter.presenter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.glide.GlideApp;
import com.zhiqi.campusassistant.common.presenter.SimplePresenter;
import com.zhiqi.campusassistant.common.ui.view.IRequestView;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.usercenter.api.UserCenterApiService;
import com.zhiqi.campusassistant.core.usercenter.entity.FeedbackRequest;
import com.zhiqi.campusassistant.dao.CacheDataDao;
import com.zhiqi.campusassistant.dao.DaoSession;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ming on 2017/3/17.
 * 用户反馈
 */

public class UserCenterPresenter extends SimplePresenter {

    private UserCenterApiService mApiService;

    private CacheDataDao cacheDataDao;

    public UserCenterPresenter(Context context, UserCenterApiService apiService, DaoSession daoSession) {
        super(context);
        this.mApiService = apiService;
        this.cacheDataDao = daoSession.getCacheDataDao();
    }

    /**
     * 用户反馈
     *
     * @param request
     */
    public void requestFeedback(final FeedbackRequest request, final IRequestView requestView) {
        Observable<BaseResultData> observable = mApiService.feedback(request);
        requestSimple(observable, requestView);
    }

    /**
     * 清除缓存数据
     *
     * @param view
     */
    public void clearCache(final IRequestView view) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                File file = GlideApp.getPhotoCacheDir(mContext);
                if (file != null && file.exists() && file.listFiles() != null) {
                    GlideApp.get(mContext).clearDiskCache();
                }
                cacheDataDao.deleteAll();
                emitter.onNext(true);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (!isReleased(view)) {
                            view.onQuest(HttpErrorCode.ERROR_EXCEPTION, mContext.getString(R.string.user_setting_clear_failed));
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aVoid) {
                        if (!isReleased(view)) {
                            view.onQuest(HttpErrorCode.SUCCESS, mContext.getString(R.string.user_setting_clear_success));
                        }
                    }
                });
    }
}
