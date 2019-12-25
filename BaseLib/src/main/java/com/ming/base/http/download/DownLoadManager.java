package com.ming.base.http.download;

import android.text.TextUtils;

import com.ming.base.http.client.LongRetrofitFactory;
import com.ming.base.http.download.api.DownloadApiService;
import com.ming.base.util.FileUtil;
import com.ming.base.util.Log;
import com.ming.base.util.MD5Util;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;


public class DownLoadManager {

    private static final String TAG = "DownLoadManager";

    private static String ENDPOINT = "http://example.com/api/";

    private static DownLoadManager instance;

    private Retrofit retrofit;

    private DownloadApiService apiService;

    public static DownLoadManager getInstance() {
        if (instance == null) {
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    instance = new DownLoadManager();
                }
            }
        }
        return instance;
    }

    private DownLoadManager() {

    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    private DownloadApiService getDownloadApiService() {
        if (apiService == null) {
            synchronized (DownLoadManager.class) {
                if (apiService == null) {
                    if (retrofit == null) {
                        retrofit = LongRetrofitFactory.getRetrofitClient(ENDPOINT).getRetrofit();
                    }
                    apiService = retrofit.create(DownloadApiService.class);
                }
            }
        }
        return apiService;
    }

    public Flowable<DownLoadBean> download(final String downloadUrl, final String filePath) {
        return download(downloadUrl, filePath, false);
    }

    /**
     * 下载文件
     *
     * @param downloadUrl   url
     * @param filePath      下载文件路径（完整路径，包括文件名，如果存在将会被覆盖）
     * @param supportResume 是否支持断点续传
     * @return
     */
    public Flowable<DownLoadBean> download(final String downloadUrl, final String filePath, boolean supportResume) {
        final DownLoadBean downLoadBean = new DownLoadBean(downloadUrl, filePath);
        return download(downLoadBean, supportResume);
    }

    public Flowable<DownLoadBean> download(final DownLoadBean downLoadBean) {
        return download(downLoadBean, false);
    }

    /**
     * 下载文件
     *
     * @param downLoadBean  下载信息
     * @param supportResume 是否支持断点续传
     * @return
     */
    public Flowable<DownLoadBean> download(final DownLoadBean downLoadBean, final boolean supportResume) {

        if (TextUtils.isEmpty(downLoadBean.downloadUrl) || TextUtils.isEmpty(downLoadBean.filePath)) {
            return Flowable.error(new IllegalArgumentException("DownLoadBean is illegal,downloadUrl or filePath is empty!"));
        }

        return Flowable.create(new FlowableOnSubscribe<DownLoadBean>() {
            @Override
            public void subscribe(FlowableEmitter<DownLoadBean> e) throws Exception {
                try {
                    doDownload(downLoadBean, supportResume, e);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    e.onError(ex);
                }
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io());
    }

    private void doDownload(DownLoadBean downLoadBean, boolean supportResume, FlowableEmitter<DownLoadBean> emitter) throws Exception {
        if (!supportResume) {
            File file = new File(downLoadBean.filePath);
            if (file.exists()) {
                file.delete();
            }
        } else {
            File file = new File(downLoadBean.filePath);
            File parent = file.getParentFile();
            File tempFile = new File(parent, MD5Util.getMD5Value(downLoadBean.downloadUrl));
            Log.i(TAG, "tempFile:" + tempFile);
            downLoadBean.tempFile = tempFile.getAbsolutePath();
        }
        downLoadBean.status = DownloadStatus.Waiting;
        if (supportResume && !TextUtils.isEmpty(downLoadBean.tempFile)) {
            downLoadBean.downloadSize = FileUtil.getFileSize(downLoadBean.tempFile);
            Log.i(TAG, "downloadSize:" + downLoadBean.downloadSize);
        }
        emitter.onNext(downLoadBean);
        Call<ResponseBody> call;
        if (supportResume) {
            Log.i(TAG, "downloadSize:" + downLoadBean.downloadSize);
            call = getDownloadApiService().download("bytes=" + downLoadBean.downloadSize + "-", downLoadBean.downloadUrl);
        } else {
            call = getDownloadApiService().download(downLoadBean.downloadUrl);
        }
        saveFile(downLoadBean, call.execute().body(), emitter);
    }

    /**
     * 保存下载文件
     *
     * @param downLoadBean
     * @param resp
     * @param emitter
     */
    private void saveFile(DownLoadBean downLoadBean, ResponseBody resp, FlowableEmitter<DownLoadBean> emitter) throws Exception {
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            int readLen;
            long downloadSize = downLoadBean.downloadSize;
            long contentLength = resp.contentLength();
            int progress = 0;
            byte[] buffer = new byte[1024];
            inputStream = resp.byteStream();
            long fileLength = downloadSize + contentLength;
            downLoadBean.fileSize = fileLength;
            Log.i(TAG, "fileLength:" + fileLength);
            String filePath = TextUtils.isEmpty(downLoadBean.tempFile) ? downLoadBean.filePath : downLoadBean.tempFile;
            File file = new File(filePath);
            if (!file.exists()) {
                FileUtil.createFile(filePath);
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(downloadSize);
            downLoadBean.status = DownloadStatus.Prepare;
            emitter.onNext(downLoadBean);
            long postDataTime = System.currentTimeMillis();
            while ((readLen = inputStream.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, readLen);
                downloadSize += readLen;
                downLoadBean.status = DownloadStatus.Downloading;
                downLoadBean.downloadSize = downloadSize;
                int p = downLoadBean.getProgress();
                if (p != progress) {
                    progress = p;
                    long now = System.currentTimeMillis();
                    if (now - postDataTime > 1000) {
                        postDataTime = now;
                        emitter.onNext(downLoadBean);
                    }
                }
            }
            Log.i(TAG, "Normal download onNext!");
            if (!filePath.equals(downLoadBean.filePath)) {
                Log.i(TAG, "rename file:" + filePath);
                FileUtil.deleteFile(downLoadBean.filePath);
                FileUtil.renameFile(filePath, downLoadBean.filePath);
                Log.i(TAG, "after rename file:" + downLoadBean.filePath);
            }
            Log.i(TAG, "Normal download completed!");
            downLoadBean.status = DownloadStatus.Finish;
            downLoadBean.downloadSize = downloadSize;
            emitter.onNext(downLoadBean);
            emitter.onComplete();
            Log.i(TAG, "Normal download onCompleted!");
        } finally {
            closeQuietly(inputStream);
            closeQuietly(resp);
            closeQuietly(randomAccessFile);
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
