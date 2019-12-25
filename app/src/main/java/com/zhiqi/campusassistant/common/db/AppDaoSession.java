package com.zhiqi.campusassistant.common.db;

import com.ming.base.greendao.rx2.RxDao;
import com.zhiqi.campusassistant.dao.DaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ming on 17-11-16.
 * app dao会话管理
 */

public class AppDaoSession extends DaoSession {

    private final Map<Class<?>, RxDao<?, ?>> entityRxDao;

    public AppDaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
        this(db, type, daoConfigMap, null);
    }

    public AppDaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap, Scheduler scheduler) {
        super(db, type, daoConfigMap);
        entityRxDao = new HashMap<>();
    }

    private RxDao<?, ?> newRxDao(AbstractDao<?, ?> dao, Scheduler scheduler) {
        return new RxDao<>(dao, scheduler == null ? Schedulers.io() : scheduler);
    }

    /**
     * 获取rx dao
     */
    @SuppressWarnings("unchecked")
    public <T, K> RxDao<T, K> getRxDao(AbstractDao<T, K> dao, Scheduler scheduler) {
        RxDao<T, K> rxDao = (RxDao<T, K>) entityRxDao.get(dao.getClass());
        if (rxDao == null) {
            rxDao = (RxDao<T, K>) newRxDao(dao, scheduler);
            entityRxDao.put(dao.getClass(), rxDao);
        }
        return rxDao;
    }

    /**
     * 获取rx dao
     */
    public <T, K> RxDao<T, K> getRxDao(AbstractDao<T, K> dao) {
        return getRxDao(dao, null);
    }

}
