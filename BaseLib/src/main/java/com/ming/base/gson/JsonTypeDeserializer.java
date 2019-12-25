package com.ming.base.gson;

import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.ming.base.util.GsonUtils;

import java.lang.reflect.Type;

/**
 * Created by ming on 2017/8/23.
 * json多态反序列化
 */

public class JsonTypeDeserializer<T> extends AbsJsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        TypeToken typeToken = TypeToken.get(typeOfT);
        Class<?> raw = typeToken.getRawType();
        // 获取JsonTypeInfo注解
        JsonTypeInfo typeInfo = raw.getAnnotation(JsonTypeInfo.class);
        if (typeInfo != null) {
            // 得到SubType注解数组
            SubType[] types = typeInfo.subType();
            if (types.length > 0) {
                // 获取节点名
                String property = typeInfo.property();
                if (!TextUtils.isEmpty(property)) {
                    JsonObject jsonObject = json.getAsJsonObject();
                    // 获取节点值
                    JsonElement element = jsonObject.get(property);
                    if (element != null) {
                        String jsonValue = GsonUtils.getString(element);
                        if (!TextUtils.isEmpty(jsonValue)) {
                            // 遍历子类型注解，找到则反序列
                            for (SubType type : types) {
                                if (jsonValue.equals(type.property())) {
                                    if (!raw.isAssignableFrom(type.value())) {
                                        throw new JsonParseException("SubType " + type.value() + " is not sub type from " + raw);
                                    }
                                    return context.deserialize(json, type.value());
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.deserialize(json, typeOfT, context);
    }

    @Override
    public <C> C deserializeField(T object, String fieldName, String jsonValue, Type filedType) {
        return null;
    }
}
