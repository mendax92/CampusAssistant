package com.zhiqi.campusassistant.core.message.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.ming.base.gson.AbsJsonDeserializer;
import com.ming.base.util.GsonUtils;
import com.ming.base.util.Log;
import com.zhiqi.campusassistant.core.course.entity.CourseInfo;
import com.zhiqi.campusassistant.core.leave.entity.LeaveInfo;
import com.zhiqi.campusassistant.core.news.entity.NewsInfo;
import com.zhiqi.campusassistant.core.repair.entity.RepairInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ming on 2016/12/20.
 * 消息模版转换适配器
 */

public class ModuleConverter extends AbsJsonDeserializer<ModuleInfo> implements JsonSerializer<ModuleInfo> {

    private static final String TAG = "ModuleConverter";

    public ModuleConverter() {
        addKey("data");
    }

    @Override
    public <C> C deserializeField(ModuleInfo object, String fieldName, String jsonValue, Type filedType) {
        Type type = getType(object.module_id);
        Log.i(TAG, "jsonValue:" + jsonValue + ", type:" + type);
        return GsonUtils.fromJson(jsonValue, type);
    }

    private Type getType(ModuleType moduleType) {
        if (moduleType == null) {
            return null;
        }
        switch (moduleType) {
            case LeaveApply:
            case LeaveApproval:
                return new TypeToken<ArrayList<LeaveInfo>>() {
                }.getType();
            case RepairApply:
            case RepairApproval:
                return new TypeToken<ArrayList<RepairInfo>>() {
                }.getType();
            case News:
                return new TypeToken<ArrayList<NewsInfo>>() {
                }.getType();
            case CourseSchedule:
                return new TypeToken<ArrayList<CourseInfo>>() {
                }.getType();
        }
        return null;
    }

    @Override
    public JsonElement serialize(ModuleInfo src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        Field[] fields = src.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    if ("data".equals(field.getName())) {
                        jsonObject.add(field.getName(), context.serialize(field.get(src), getType(src.module_id)));
                    } else {
                        jsonObject.add(field.getName(), context.serialize(field.get(src), field.getGenericType()));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return jsonObject;
    }
}
