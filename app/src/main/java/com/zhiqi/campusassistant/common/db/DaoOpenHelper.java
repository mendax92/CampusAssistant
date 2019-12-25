package com.zhiqi.campusassistant.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zhiqi.campusassistant.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ming on 2016/11/30.
 * 数据库升级管理
 */

public class DaoOpenHelper extends DaoMaster.OpenHelper {

    public DaoOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("DaoOpenHelper", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");

    }
}
