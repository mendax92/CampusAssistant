package com.zhiqi.campusassistant.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhiqi.campusassistant.dao.DaoMaster;
import com.zhiqi.campusassistant.dao.DaoSession;

/**
 * Created by ming on 2016/9/13.
 * 数据库管理
 */
public class DbManager {

    private AppDaoSession mDaoSession;

    private SQLiteDatabase db;

    public DbManager(Context context) {
        DaoOpenHelper mHelper = new DaoOpenHelper(context.getApplicationContext(), "campus_assistant");
        db = mHelper.getWritableDatabase();
        AppDaoMaster mDaoMaster = new AppDaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public AppDaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return db;
    }
}
