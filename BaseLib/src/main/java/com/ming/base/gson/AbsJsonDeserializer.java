package com.ming.base.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.ming.base.util.GsonUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ming on 2017/1/7.
 * Json反序列基础类
 */

public abstract class AbsJsonDeserializer<T> implements JsonDeserializer<T> {

    /**
     * 序列换关键字
     */
    private Set<String> serializeKey = new HashSet<>();

    public void addKey(String key) {
        serializeKey.add(key);
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        TypeToken typeToken = TypeToken.get(typeOfT);
        Class<?> raw = typeToken.getRawType();
        JsonObject jsonObject = json.getAsJsonObject();
        Field[] fields = raw.getDeclaredFields();
        ObjectConstructor<?> constructor = GsonUtils.getObjectConstructor(typeToken);
        T result = (T) constructor.construct();
        try {
            List<FieldInfo> infos = new ArrayList<>();
            Map<String, Field> fieldMap = new HashMap<>();
            for (Field field : fields) {
                fieldMap.put(field.getName(), field);
            }
            Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
            Iterator<Map.Entry<String, JsonElement>> iterator = entries.iterator();
            for (; iterator.hasNext(); ) {
                Map.Entry<String, JsonElement> entry = iterator.next();
                String property = entry.getKey();
                JsonElement element = entry.getValue();
                Field field = fieldMap.get(property);
                if (field == null) {
                    field = findField(property, raw.getSuperclass());
                }
                if (field != null) {
                    boolean deserialize = excludeField(field, false);
                    if (!deserialize) {
                        continue;
                    }
                    field.setAccessible(true);
                    // 获取字段类型
                    Type fieldType = $Gson$Types.resolve(typeToken.getType(), raw, field.getGenericType());
                    String fieldName = field.getName();
                    // 如果含有该关键字则自己进行序列化
                    if (serializeKey.contains(fieldName)) {
                        try {
                            FieldInfo fieldInfo = new FieldInfo();
                            fieldInfo.field = field;
                            fieldInfo.fieldName = fieldName;
                            fieldInfo.fieldType = fieldType;
                            if (element != null) {
                                fieldInfo.jsonValue = GsonUtils.getString(element);
                            }
                            infos.add(fieldInfo);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        continue;
                    }
                    // 否则默认进行反序列化
                    Object value = context.deserialize(element, fieldType);
                    if (value != null) {
                        field.set(result, value);
                    }
                }
            }
            if (!infos.isEmpty()) {
                for (FieldInfo filedInfo : infos) {
                    Object obj = null;
                    try {
                        obj = deserializeField(result, filedInfo.fieldName, filedInfo.jsonValue, filedInfo.fieldType);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    filedInfo.field.set(result, obj);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private Field findField(String name, Class<?> cls) {
        Field field = null;
        try {
            field = cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
        }
        if (field == null && !cls.equals(Object.class)) {
            return findField(name, cls.getSuperclass());
        }
        return field;
    }

    /**
     * 反序列化字段
     *
     * @param object    序列换对象
     * @param fieldName 字段名称
     * @param jsonValue 对应json数据
     * @param filedType 字段类型
     * @param <C>       序列化类型
     * @return
     */
    public abstract <C> C deserializeField(T object, String fieldName, String jsonValue, Type filedType);

    /**
     * 是否可以反序列化
     *
     * @param f         字段
     * @param serialize 是否序列化
     * @return
     */
    protected static boolean excludeField(Field f, boolean serialize) {
        return !Excluder.DEFAULT.excludeClass(f.getType(), serialize) && !Excluder.DEFAULT.excludeField(f, serialize);
    }

    private class FieldInfo {
        private String fieldName;
        private String jsonValue;
        private Type fieldType;
        private Field field;
    }
}
