package com.zhiqi.campusassistant.core.user.entity;

import com.zhiqi.campusassistant.core.user.entity.Gender;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by Edmin on 2016/11/6 0006.
 */

public class GenderConverter implements PropertyConverter<Gender, Integer> {
    @Override
    public Gender convertToEntityProperty(Integer databaseValue) {
        return Gender.formatValue(databaseValue);
    }

    @Override
    public Integer convertToDatabaseValue(Gender entityProperty) {
        return entityProperty.getValue();
    }
}
