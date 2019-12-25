package com.zhiqi.campusassistant.core.upload;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.google.gson.reflect.TypeToken;
import com.ming.base.http.HttpManager;
import com.ming.base.util.FileUtil;
import com.ming.base.util.GsonUtils;
import com.ming.base.util.Log;
import com.ming.base.util.RxUtil;
import com.zhiqi.campusassistant.R;
import com.zhiqi.campusassistant.common.entity.BaseResultData;
import com.zhiqi.campusassistant.common.http.OnHttpFilterCallback;
import com.zhiqi.campusassistant.config.HttpErrorCode;
import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;
import com.zhiqi.campusassistant.core.upload.model.UploadSingleTask;
import com.zhiqi.campusassistant.core.upload.model.UploadStatus;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;
import com.zhiqi.campusassistant.core.upload.presenter.UploadPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ming on 2017/5/10.
 * 上传服务
 */

public class BaseUploadServer {

    private static final String TAG = "UploadServer";

    private Context mContext;

    private UploadPresenter mUploadPresenter;

    private IApiServiceProvider apiServiceProvider;

    public BaseUploadServer(Context context, UploadPresenter uploadPresenter, IApiServiceProvider apiServiceProvider) {
        this.mContext = context.getApplicationContext();
        this.mUploadPresenter = uploadPresenter;
        this.apiServiceProvider = apiServiceProvider;
    }

    public <T extends BaseUploadBean> void deleteTask(UploadTask<T> task) {
        mUploadPresenter.deleteTask(task);
    }

    /**
     * 上传文件
     *
     * @param request
     * @param isUpdate 是否为更新数据
     * @param listener
     * @param <T>
     */
    public <T extends BaseUploadBean> void upload(T request, boolean isUpdate, OnUploadListener<T> listener) {
        Observable<UploadTask<T>> observable = mUploadPresenter.uploadImage(request);
        subscribeUpload(observable, isUpdate, listener);
    }

    public <T extends BaseUploadBean> void retryUpload(UploadTask<T> task, boolean isUpdate, OnUploadListener<T> listener) {
        Observable<UploadTask<T>> observable = mUploadPresenter.retryUpload(task);
        subscribeUpload(observable, isUpdate, listener);
    }

    /**
     * 处理上传subscribe
     *
     * @param observable
     */
    private <T extends BaseUploadBean> void subscribeUpload(Observable<UploadTask<T>> observable, final boolean isUpdate, final OnUploadListener<T> listener) {
        observable.subscribeOn(Schedulers.io())
                .subscribe(new Observer<UploadTask<T>>() {
                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(UploadTask<T> uploadTask) {
                        Log.i(TAG, "uploadTask status:" + uploadTask.getStatus());
                        if (UploadStatus.Success == uploadTask.getStatus()) {
                            afterUpload(uploadTask, isUpdate, listener);
                        } else if (UploadStatus.Failed == uploadTask.getStatus()) {
                            doOnFailed(listener, uploadTask, HttpErrorCode.ERROR_EXCEPTION, mContext.getString(R.string.upload_failed_tip));
                        }
                    }
                });
    }

    /**
     * 上传之后处理
     *
     * @param task
     */
    private <T extends BaseUploadBean> void afterUpload(final UploadTask<T> task, final boolean isUpdate, final OnUploadListener<T> listener) {
        if (task == null) {
            return;
        }
        final BaseUploadBean uploadBean = task.getEntity();
        try {
            uploadBean.setUploadFiles(getUploadServerFile(task, uploadBean.getUploadFiles()));
        } catch (Exception ex) {
            ex.printStackTrace();
            doOnFailed(listener, task, HttpErrorCode.ERROR_EXCEPTION, mContext.getString(R.string.upload_failed_tip));
            return;
        }
        Observable<BaseResultData<List<String>>> observable = getApiObserver(uploadBean, isUpdate);

        HttpManager.subscribe(observable, new OnHttpFilterCallback<BaseResultData<List<String>>>(mContext) {

            @Override
            public void onSuccess(BaseResultData<List<String>> result) {
                Log.i(TAG, "success");
                mUploadPresenter.deleteTask(task);
                doOnSuccess(listener, task, result.message);
            }

            @Override
            public void onFailure(BaseResultData<List<String>> result) {
                if (HttpErrorCode.ERROR_UPLOAD_FILE_NOT_EXIST == result.error_code && whenServerFileNotExist(task, listener, result)) {
                    return;
                }
                doOnFailed(listener, task, result.error_code, result.message);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                Log.i(TAG, message);
                doOnFailed(listener, task, errorCode, message);
            }
        });
    }

    private <T extends BaseUploadBean> boolean whenServerFileNotExist(final UploadTask<T> task, final OnUploadListener<T> listener, final BaseResultData<List<String>> result) {
        List<String> failed = result.data;
        if (failed != null && task.getSingleTasks() != null) {
            boolean retry = false;
            for (String failFile : failed) {
                if (TextUtils.isEmpty(failFile) || URLUtil.isNetworkUrl(failFile)) {
                    continue;
                }
                for (UploadSingleTask singleTask : task.getSingleTasks()) {
                    Map<String, String> singleResult = getResult(singleTask.getResult());
                    if (singleResult != null) {
                        for (String key : singleResult.keySet()) {
                            String serverFile = singleResult.get(key);
                            if (failFile.equals(serverFile)) {
                                retry = true;
                                singleTask.setStatus(UploadStatus.Failed);
                                break;
                            }
                        }
                    }
                }
            }
            if (retry) {
                doOnFailed(listener, task, result.error_code, result.message);
                return true;
            }
            //ToastUtil.show(mContext, result.message);
        }
        return false;
    }

    private <T extends BaseUploadBean> Observable<BaseResultData<List<String>>> getApiObserver(T request, boolean isUpdate) {
        return apiServiceProvider.provideApiObserver(request, isUpdate);
    }

    /**
     * 填充上传文件
     *
     * @param task
     * @param uploads
     * @return
     */
    private <T extends BaseUploadBean> List<String> getUploadServerFile(UploadTask<T> task, List<String> uploads) {
        List<String> files = uploads;
        Log.i(TAG, "files:" + files);
        Map<String, String> resultList = null;
        if (task.getWholeCommit() && !TextUtils.isEmpty(task.getResult())) {
            resultList = getResult(task.getResult());
        } else {
            List<UploadSingleTask> singleTasks = task.getSingleTasks();
            if (singleTasks != null) {
                resultList = new HashMap<>();
                for (UploadSingleTask singleTask : singleTasks) {
                    Log.i(TAG, "singleTask result:" + singleTask.getResult());
                    Map<String, String> singleResult = getResult(singleTask.getResult());
                    if (singleResult != null) {
                        resultList.putAll(singleResult);
                    }
                }
            }
        }
        if (resultList != null) {
            List<String> list = new ArrayList<>(uploads);
            for (int i = 0; i < list.size(); i++) {
                String file = list.get(i);
                if (!TextUtils.isEmpty(file) && !URLUtil.isNetworkUrl(file) && !resultList.containsValue(file)) {
                    String md5 = FileUtil.getMd5(file);
                    Log.i(TAG, "md5:" + md5);
                    uploads.set(i, resultList.get(md5));
                }
            }
        }
        Log.i(TAG, "files:" + files);
        return files;
    }

    private <T extends BaseUploadBean> void doOnSuccess(final OnUploadListener<T> listener, final UploadTask<T> task, final String message) {
        Log.i(TAG, "doOnSuccess");
        if (listener != null) {
            if (RxUtil.isUIThread()) {
                listener.onSuccess(task, message);
            } else {
                RxUtil.postOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(task, message);
                    }
                });
            }
        }
    }

    /**
     * 失败操作
     *
     * @param msg
     */
    private <T extends BaseUploadBean> void doOnFailed(final OnUploadListener<T> listener, final UploadTask<T> task, final int errorCode, final String msg) {
        Log.i(TAG, "doOnFailed");
        if (listener != null) {
            if (RxUtil.isUIThread()) {
                listener.onFailed(task, errorCode, msg);
            } else {
                RxUtil.postOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailed(task, errorCode, msg);
                    }
                });
            }
        }
    }

    private Map<String, String> getResult(String resultString) {
        BaseResultData<Map<String, String>> result = GsonUtils.fromJson(resultString,
                new TypeToken<BaseResultData<Map<String, String>>>() {
                }.getType());
        return result == null || result.data == null ? null : result.data;
    }


}
