package com.zhiqi.campusassistant.core.upload.api;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by ming on 2017/2/26.
 * 上传接口
 */

public interface UploadApiService {

    /**
     * 上传文件
     *
     * @param url   上传服务器地址
     * @param parts http内容（包括文件和参数）
     * @return
     */
    @Multipart
    @POST
    Observable<Response<ResponseBody>> uploadFile(@Url String url, @PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> parts);

    /**
     * 上传文件
     *
     * @param url    上传服务器地址
     * @param params 文本
     * @param file   文件
     * @return
     */
    @Multipart
    @POST
    Observable<Response<ResponseBody>> uploadFile(@Url String url, @PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);
}
