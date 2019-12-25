package com.zhiqi.campusassistant.core.upload.service;

import android.media.MediaMetadataRetriever;
import android.webkit.MimeTypeMap;

import com.ming.base.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by ming on 2017/2/27.
 * 表单上传辅助类
 */
public class PartGenerator {

    private PartGenerator() {
        throw new IllegalStateException("No instance");
    }

    private static final String MULTIPART_FORM_DATA = MultipartBody.FORM.toString();


    public static Map<String, RequestBody> createPartFromMap(Map<String, String> param) {
        if (param == null) {
            return null;
        }
        Map<String, RequestBody> parts = new HashMap<>();
        Set<Map.Entry<String, String>> entries = param.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            parts.put(entry.getKey(), createPartFromString(entry.getValue()));
        }
        return parts;
    }

    /**
     * 提交表单请求时<b>MediaType</b>类型为'multipart/form-data'的{@link RequestBody}对象
     *
     * @param value 请求参数(key-value)中的值
     * @return multipart/form-data类型的RequestBoy
     */
    public static RequestBody createPartFromString(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    /**
     * 提交表单请求时<b>MediaType</b>类型为'multipart/form-data'的{@link MultipartBody.Part}对象
     *
     * @param key  请求参数(key-value)中的键
     * @param file 请求参数(key-value)中的值
     * @return multipart/form-data类型的MultipartBody.Part
     */
    public static MultipartBody.Part createPartFromFile(String key, File file) {

        return createPartFromFile(MediaType.parse(getMimeType(file.getAbsolutePath())), key, file);
    }

    /**
     * 提交表单请求时所需的{@link MultipartBody.Part}对象,需要传入正确的MediaType,如"image/*"或者"multipart/form-data"
     *
     * @param mediaType MediaType类型
     * @param key       请求参数(key-value)中的键
     * @param file      请求参数(key-value)中的值
     * @return 自定义MediaType类型的MultipartBody.Part
     */
    public static MultipartBody.Part createPartFromFile(MediaType mediaType, String key, File file) {

        RequestBody requestFile = RequestBody.create(mediaType, file);

        return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
    }

    private static String getMimeTypeFromSuffix(String filePath) {
        String suffix = FileUtil.getFileExtension(filePath);
        if (suffix == null) {
            return MULTIPART_FORM_DATA;
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return MULTIPART_FORM_DATA;
    }


    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = null;
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (Exception e) {
            }
            if (mime == null) {
                mime = getMimeTypeFromSuffix(filePath);
            }
        }
        return mime;
    }
}
