package com.zhiqi.campusassistant.common.db;

import android.database.sqlite.SQLiteDatabase;

import com.zhiqi.campusassistant.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

/**
 * Created by ming on 17-11-16.
 * app dao master管理
 */

public class AppDaoMaster extends DaoMaster {

    public AppDaoMaster(SQLiteDatabase db) {
        super(db);
    }

    public AppDaoMaster(Database db) {
        super(db);
    }

    @Override
    public AppDaoSession newSession() {
        return new AppDaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    @Override
    public AppDaoSession newSession(IdentityScopeType type) {
        return new AppDaoSession(db, type, daoConfigMap);
    }
}
