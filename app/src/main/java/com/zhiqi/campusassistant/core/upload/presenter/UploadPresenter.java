package com.zhiqi.campusassistant.core.upload.presenter;

import android.webkit.URLUtil;

import com.ming.base.util.BitmapUtil;
import com.ming.base.util.FileUtil;
import com.ming.base.util.Log;
import com.zhiqi.campusassistant.common.presenter.BasePresenter;
import com.zhiqi.campusassistant.config.AppConstant;
import com.zhiqi.campusassistant.config.AppPathConfig;
import com.zhiqi.campusassistant.config.HttpUrlConstant;
import com.zhiqi.campusassistant.core.upload.entity.BaseUploadBean;
import com.zhiqi.campusassistant.core.upload.model.UploadSingleTask;
import com.zhiqi.campusassistant.core.upload.model.UploadStatus;
import com.zhiqi.campusassistant.core.upload.model.UploadTask;
import com.zhiqi.campusassistant.core.upload.service.UploadManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by ming on 2017/2/26.
 * 上传presenter
 */

public class UploadPresenter extends BasePresenter {

    private static final String TAG = "UploadPresenter";

    private static final String PARAM_FILE_CATEGORY = "fileCategory";

    private static final long MAX_FILE_LENGTH = 200 * 1024;

    private static final long MAX_IMAGE_SIZE = 1080;

    private UploadManager manager;

    public UploadPresenter(UploadManager manager) {
        this.manager = manager;
    }

    /**
     * 上传文件，将会裁剪图片
     *
     * @param uploadBean
     * @param <T>
     */
    public <T extends BaseUploadBean> Observable<UploadTask<T>> uploadImage(final T uploadBean) {
        if (uploadBean == null || uploadBean.getUploadFiles() == null || uploadBean.getUploadFiles().isEmpty()) {
            return null;
        }
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                Log.i(TAG, "compress " + Thread.currentThread().getId());
                List<String> uploadFiles = uploadBean.getUploadFiles();
                String filePath;
                for (int i = 0; i < uploadFiles.size(); i++) {
                    filePath = uploadFiles.get(i);
                    if (URLUtil.isNetworkUrl(filePath)) {
                        continue;
                    }
                    uploadFiles.set(i, compressImage(filePath));
                }
                Log.i(TAG, "uploadFiles :" + uploadFiles);
                emitter.onNext(uploadBean);
                emitter.onComplete();
            }
        })
                .flatMap(new Function<T, ObservableSource<UploadTask<T>>>() {
                    @Override
                    public ObservableSource<UploadTask<T>> apply(T t) throws Exception {
                        return upload(t);
                    }
                });

    }

    /**
     * 上传
     *
     * @param uploadBean
     * @param <T>
     */
    public <T extends BaseUploadBean> Observable<UploadTask<T>> upload(final T uploadBean) {
        if (uploadBean == null || uploadBean.getUploadFiles() == null || uploadBean.getUploadFiles().isEmpty()) {
            return null;
        }
        return Observable.just(uploadBean)
                .map(new Function<T, UploadTask<T>>() {
                    @Override
                    public UploadTask<T> apply(T t) throws Exception {
                        Log.i(TAG, "transform " + Thread.currentThread().getId());
                        return transformTask(t);
                    }
                })
                .flatMap(new Function<UploadTask<T>, Observable<UploadTask<T>>>() {
                    @Override
                    public Observable<UploadTask<T>> apply(UploadTask<T> task) throws Exception {
                        Log.i(TAG, "upload " + Thread.currentThread().getId());
                        return readyUpload(task);
                    }
                });
        //.subscribeOn(Schedulers.io());
    }

    public <T extends BaseUploadBean> Observable<UploadTask<T>> retryUpload(UploadTask<T> task) {
        return readyUpload(task);
    }

    private <T extends BaseUploadBean> Observable<UploadTask<T>> readyUpload(final UploadTask<T> task) {
        List<UploadSingleTask> singleTasks = task.getSingleTasks();
        if (singleTasks == null || singleTasks.isEmpty()) {
            return Observable.create(new ObservableOnSubscribe<UploadTask<T>>() {
                @Override
                public void subscribe(ObservableEmitter<UploadTask<T>> emitter) throws Exception {
                    task.setStatus(UploadStatus.Success);
                    emitter.onNext(task);
                    emitter.onComplete();
                }
            });
        } else {
            return manager.upload(task);
        }
    }

    private <T extends BaseUploadBean> UploadTask<T> transformTask(T uploadBean) {
        List<String> uploadFiles = uploadBean.getUploadFiles();
        if (uploadFiles == null || uploadFiles.isEmpty()) {
            return null;
        }
        UploadTask<T> task = new UploadTask<>();
        task.setUploadKey(AppConstant.UPLOAD_KEY);
        task.setEntity(uploadBean);
        task.setUrl(HttpUrlConstant.BASE_URL + HttpUrlConstant.UPLOAD_FILE_URL);
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_FILE_CATEGORY, uploadBean.getUploadType().getValue());
        task.setParams(params);
        List<UploadSingleTask> singleTasks = new ArrayList<>();
        for (String filePath : uploadFiles) {
            if (URLUtil.isNetworkUrl(filePath)) {
                continue;
            }
            UploadSingleTask singleTask = new UploadSingleTask();
            singleTask.setFilePath(filePath);
            singleTasks.add(singleTask);
        }
        task.setSingleTasks(singleTasks);
        return task;
    }

    /**
     * 删除任务
     *
     * @param task
     */
    public <T extends BaseUploadBean> void deleteTask(UploadTask<T> task) {
        if (task == null) {
            return;
        }
        List<UploadSingleTask> singleTasks = task.getSingleTasks();
        if (singleTasks != null) {
            String filePath;
            for (UploadSingleTask singleTask : singleTasks) {
                filePath = singleTask.getFilePath();
                Log.i(TAG, "filePath:" + filePath);
                if (filePath != null && FileUtil.getFolderName(filePath).contains(AppPathConfig.APP_CACHE_IMAGE)) {
                    FileUtil.deleteFile(filePath);
                }
            }
        }
    }

    /**
     * 裁剪图片
     *
     * @param filePath
     * @return
     */
    private String compressImage(String filePath) {
        String newFilePath = filePath;
        try {
            long fileSize = FileUtil.getFileSize(filePath);
            if (fileSize > MAX_FILE_LENGTH) {
                synchronized (UploadPresenter.class) {
                    newFilePath = AppPathConfig.APP_CACHE_IMAGE + File.separatorChar +
                            System.currentTimeMillis() + FileUtil.FILE_EXTENSION_SEPARATOR + FileUtil.getFileExtension(filePath);
                }
                Log.i(TAG, " Old file size : " + FileUtil.getFileSize(filePath));
                BitmapUtil.generateThumb(filePath, newFilePath, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE, MAX_FILE_LENGTH);
                Log.i(TAG, " New file size : " + FileUtil.getFileSize(newFilePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "newFilePath:" + newFilePath);
        return newFilePath;
    }
}
