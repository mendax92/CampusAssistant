package com.zhiqi.campusassistant.app.dagger.module;

import android.database.sqlite.SQLiteDatabase;

import com.zhiqi.campusassistant.app.AssistantApplication;
import com.zhiqi.campusassistant.common.db.AppDaoSession;
import com.zhiqi.campusassistant.common.db.DbManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ming on 2016/9/13.
 */
@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public DbManager provideDbManager(AssistantApplication context) {
        return new DbManager(context);
    }

    @Provides
    @Singleton
    public AppDaoSession provideDaoSession(DbManager dbManager) {
        return dbManager.getDaoSession();
    }

    @Provides
    @Singleton
    public SQLiteDatabase provideSQLiteDatabase(DbManager dbManager) {
        return dbManager.getSQLiteDatabase();
    }
}
