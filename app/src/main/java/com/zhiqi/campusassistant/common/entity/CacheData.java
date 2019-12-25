package com.zhiqi.campusassistant.common.entity;

import android.text.TextUtils;

import com.ming.base.bean.BaseJsonData;
import com.ming.base.util.GsonUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.lang.reflect.Type;

/**
 * Created by ming on 2016/11/29.
 * 缓存数据
 */
@Entity
public class CacheData<T> implements BaseJsonData {

    @Id
    @Property(nameInDb = "cache_key")
    private String cacheKey;

    @Property(nameInDb = "cache_data")
    private String cacheData;

    @Property(nameInDb = "cache_time")
    private long cacheTime;

    @Transient
    private T data;

    @Generated(hash = 175014255)
    public CacheData(String cacheKey, String cacheData, long cacheTime) {
        this.cacheKey = cacheKey;
        this.cacheData = cacheData;
        this.cacheTime = cacheTime;
    }

    public CacheData(String cacheKey, T data) {
        this.cacheKey = cacheKey;
        this.cacheTime = System.currentTimeMillis();
        setData(data);
    }

    @Generated(hash = 1582791643)
    public CacheData() {
    }

    public String getCacheKey() {
        return this.cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheData() {
        return this.cacheData;
    }

    public void setCacheData(String cacheData) {
        this.cacheData = cacheData;
    }

    public T getData(Class<T> classOfT) {
        if (data == null && !TextUtils.isEmpty(cacheData)) {
            data = GsonUtils.fromJson(cacheData, classOfT);
        }
        return data;
    }

    public T getData(Type typeOfT) {
        if (data == null && !TextUtils.isEmpty(cacheData)) {
            data = GsonUtils.fromJson(cacheData, typeOfT);
        }
        return data;
    }

    public void setData(T data) {
        this.data = data;
        cacheData = GsonUtils.toJson(data);
    }

    public long getCacheTime() {
        return this.cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }
}
