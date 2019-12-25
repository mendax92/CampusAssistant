package com.ming.base.http.download.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by ming on 2017/1/7.
 * 下载API
 */

public interface DownloadApiService {

    @GET
    @Streaming
    Call<ResponseBody> download(@Header("Range") String start, @Url String url);

    @GET
    @Streaming
    Call<ResponseBody> download(@Url String url);
}