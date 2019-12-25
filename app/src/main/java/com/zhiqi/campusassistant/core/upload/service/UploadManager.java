package com.zhiqi.campusassistant.core.upload.service;

import android.text.TextUtils;

import com.ming.base.util.FileUtil;
import com.ming.base.util.Log;
import com.zhiqi.campusassistant.core.upload.api.UploadApiService;
import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;
import com.zhiqi.campusassistant.core.upload.model.UploadSingleTask;
import com.zhiqi.campusassistant.core.upload.model.UploadStatus;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;

import org.reactivestreams.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by ming on 2017/2/27.
 * 上传管理
 */

public class UploadManager {

    private static final String TAG = "UploadManager";

    private static final int RETRY_TIMES = 3;

    private static final Object LOCK = new Object();

    private UploadApiService mApiService;

    private List<UploadStep> uploadSteps;

    private Map<String, List<ObserverInfo<?>>> uploadingObservers = new HashMap<>();

    public UploadManager(UploadApiService apiService) {
        this.mApiService = apiService;
        uploadSteps = new ArrayList<>();
        uploadSteps.add(analyzeStep);
        uploadSteps.add(uploadStep);
    }

    public <T extends BaseUploadBean> Observable<UploadTask<T>> upload(final UploadTask<T> task) {
        return Observable.create(new ObservableOnSubscribe<UploadTask<T>>() {
            @Override
            public void subscribe(ObservableEmitter<UploadTask<T>> emitter) throws Exception {
                for (UploadStep step : uploadSteps) {
                    step.onNext(task, emitter);
                }
            }
        });
    }

    private UploadStep analyzeStep = new UploadStep() {
        @Override
        public <T extends BaseUploadBean> void onNext(UploadTask<T> task, ObservableEmitter<? super UploadTask<T>> emitter) {
            task.setStatus(UploadStatus.Waiting);
            List<UploadSingleTask> singleTasks = task.getSingleTasks();
            long size = 0;
            Iterator<UploadSingleTask> iterator = singleTasks.iterator();
            int successSize = 0;
            while (iterator.hasNext()) {
                UploadSingleTask singleTask = iterator.next();
                if (UploadStatus.Success == singleTask.getStatus()) {
                    size += singleTask.getLength();
                    successSize++;
                    continue;
                }
                long fileLen = FileUtil.getFileSize(singleTask.getFilePath());
                if (fileLen <= 0) {
                    iterator.remove();
                    continue;
                }
                singleTask.setLength(fileLen);
                singleTask.setMd5(FileUtil.getMd5(singleTask.getFilePath()));
                size += fileLen;
            }
            task.setLength(size);
            emitter.onNext(task);
            if (successSize == singleTasks.size()) {
                task.setStatus(UploadStatus.Success);
                emitter.onNext(task);
                emitter.onComplete();
            }
        }
    };

    private UploadStep uploadStep = new UploadStep() {
        @Override
        public <T extends BaseUploadBean> void onNext(final UploadTask<T> task, final ObservableEmitter<? super UploadTask<T>> emitter) {
            if (UploadStatus.Success == task.getStatus()) {
                return;
            }
            List<UploadSingleTask> singleTasks = task.getSingleTasks();
            Map<String, RequestBody> params = PartGenerator.createPartFromMap(task.getParams());
            List<MultipartBody.Part> bodies = new ArrayList<>();
            for (UploadSingleTask singleTask : singleTasks) {
                if (UploadStatus.Success == singleTask.getStatus()) {
                    Log.i(TAG, "file is uploaded, path:" + singleTask.getFilePath());
                    continue;
                }
                task.setStatus(UploadStatus.Uploading);
                File file = new File(singleTask.getFilePath());
                MultipartBody.Part body = null;
                if (file.exists()) {
                    String uploadKey = TextUtils.isEmpty(task.getUploadKey()) ? "files" : task.getUploadKey();
                    body = PartGenerator.createPartFromFile(uploadKey, file);
                    bodies.add(body);
                    if (!task.getWholeCommit()) {
                        final String key = getKey(singleTask, task);
                        boolean hasExist = false;
                        synchronized (LOCK) {
                            Log.i(TAG, "key:" + key);
                            List<ObserverInfo<?>> infos = uploadingObservers.get(key);
                            if (infos != null) {
                                infos.add(new ObserverInfo<T>(singleTask, emitter));
                                Log.i(TAG, "key is exist, key:" + key);
                                hasExist = true;
                            } else {
                                infos = new ArrayList<>();
                                infos.add(new ObserverInfo<T>(singleTask, emitter));
                                uploadingObservers.put(key, infos);
                                Log.i(TAG, "uploadingObservers:" + uploadingObservers);
                            }
                        }
                        if (hasExist) {
                            emitter.onNext(task);
                            continue;
                        }
                        mApiService.uploadFile(task.getUrl(), params, body)
                                .retry(RETRY_TIMES)
                                .subscribeOn(Schedulers.io())
                                .subscribe(new UploadingObserver<T>(task, key));
                    }
                }
            }
            if (bodies.isEmpty()) {
                emitter.onError(new NullPointerException("File is null."));
                return;
            }
            if (task.getWholeCommit()) {
                Observable<Response<ResponseBody>> observable = mApiService.uploadFile(task.getUrl(), params, bodies);
                observable.subscribe(new Observer<Response<ResponseBody>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        task.setStatus(UploadStatus.Uploading);
                        emitter.onNext(task);
                    }

                    @Override
                    public void onComplete() {
                        emitter.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        task.setStatus(UploadStatus.Failed);
                        emitter.onError(e);
                    }

                    @Override
                    public void onNext(Response<ResponseBody> body) {
                        Log.i(TAG, "http result:" + body);
                        task.setStatus(UploadStatus.Success);
                        task.setResult(parseBody(body));
                        emitter.onNext(task);
                    }
                });
            }
        }
    };

    private String getKey(UploadSingleTask singleTask, UploadTask task) {
        if (singleTask == null) {
            return null;
        }
        return (singleTask.getMd5() == null ? "" : singleTask.getMd5()) + "&" + task.getUrl();
    }

    private class UploadingObserver<T extends BaseUploadBean> implements Observer<Response<ResponseBody>> {

        private UploadTask<T> task;

        private String key;

        UploadingObserver(UploadTask<T> task, String key) {
            this.task = task;
            this.key = key;
        }

        @Override
        public void onSubscribe(Disposable d) {
            List<ObserverInfo<?>> infos = uploadingObservers.get(key);
            if (infos != null && !infos.isEmpty()) {
                for (ObserverInfo<?> info : infos) {
                    ObserverInfo<T> temp = (ObserverInfo<T>) info;
                    temp.singleTask.setStatus(UploadStatus.Uploading);
                    temp.observable.onNext(task);
                }
            }
        }

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            trigger(UploadStatus.Failed, null);
            e.printStackTrace();
        }

        @Override
        public void onNext(Response<ResponseBody> body) {
            Log.i(TAG, "onNext");
            trigger(UploadStatus.Success, body);
        }

        private void trigger(UploadStatus status, Response<ResponseBody> response) {
            Log.i(TAG, "status:" + status + ", result:" + response);
            String result = parseBody(response);
            List<ObserverInfo<?>> infos = uploadingObservers.get(key);
            if (infos != null && !infos.isEmpty()) {
                for (ObserverInfo info : infos) {
                    if (UploadStatus.Failed == status) {
                        task.setStatus(status);
                    }
                    info.singleTask.setStatus(status);
                    info.singleTask.setResult(result);
                    List<UploadSingleTask> singleTasks = task.getSingleTasks();
                    int completedSize = 0;
                    int successSize = 0;
                    long progress = 0;
                    for (UploadSingleTask singleTask : singleTasks) {
                        if (UploadStatus.Success == singleTask.getStatus()) {
                            completedSize++;
                            successSize++;
                            progress += singleTask.getLength();
                            uploadingObservers.remove(getKey(singleTask, task));
                        } else if (UploadStatus.Failed == singleTask.getStatus()) {
                            completedSize++;
                            uploadingObservers.remove(getKey(singleTask, task));
                        }
                    }
                    Log.i(TAG, "progress:" + progress + ", task length:" + task.getLength());
                    task.setProgress(progress);
                    if (successSize == singleTasks.size()) {
                        task.setStatus(UploadStatus.Success);
                    }
                    if (completedSize == singleTasks.size()) {
                        info.observable.onNext(task);
                    }
                    if (completedSize == singleTasks.size()) {
                        info.observable.onComplete();
                    }
                }
            }
            synchronized (LOCK) {
                uploadingObservers.remove(key);
            }
        }
    }

    private String parseBody(Response<ResponseBody> response) {
        if (response == null) {
            return null;
        }
        String result = null;
        ResponseBody body = response.body();
        if (body != null) {
            try {
                result = new String(body.bytes());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (result == null && response.errorBody() != null) {
            try {
                result = new String(response.errorBody().bytes());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private class ObserverInfo<T extends BaseUploadBean> {
        private UploadSingleTask singleTask;

        private ObservableEmitter<? super UploadTask<T>> observable;

        private ObserverInfo(UploadSingleTask singleTask, ObservableEmitter<? super UploadTask<T>> observable) {
            this.singleTask = singleTask;
            this.observable = observable;
        }
    }

}
