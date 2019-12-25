package com.zhiqi.campusassistant.core.user.entity;

import com.zhiqi.campusassistant.core.user.entity.UserRole;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by Edmin on 2016/11/6 0006.
 */


public class UserRoleConverter implements PropertyConverter<UserRole, Integer> {
    @Override
    public UserRole convertToEntityProperty(Integer databaseValue) {
        return UserRole.formatValue(databaseValue);
    }

    @Override
    public Integer convertToDatabaseValue(UserRole entityProperty) {
        return entityProperty.getValue();
    }
}
