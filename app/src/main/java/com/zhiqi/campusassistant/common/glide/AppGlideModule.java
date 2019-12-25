package com.zhiqi.campusassistant.common.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.zhiqi.campusassistant.app.AssistantApplication;

import java.io.InputStream;

/**
 * Created by ming on 2017/4/14.
 * glide配置
 */
@GlideModule
public class AppGlideModule extends com.bumptech.glide.module.AppGlideModule {

    private static final String CACHE_NAME = "images";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(AssistantApplication.getInstance(), CACHE_NAME, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE));
        builder.setDefaultRequestOptions(RequestOptions.fitCenterTransform());
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
