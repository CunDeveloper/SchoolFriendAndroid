package com.nju.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchoolFriendGson {

    private static SchoolFriendGson friendGson = null;
    private Gson gson;

    private SchoolFriendGson() {
        gson = new Gson();
    }

    public static SchoolFriendGson newInstance() {
        if (friendGson == null) {
            friendGson = new SchoolFriendGson();
        }
        return friendGson;
    }

    public <T, V> String toJson(Map<T, V> map) {
        return gson.toJson(map);
    }

    public <T> String toJson(List<T> list) {
        return gson.toJson(list);
    }

    public <T> String toJson(T t) {
        return gson.toJson(t);
    }

    public <T> ArrayList<T> fromJsonToList(String json, final Class clazz) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, clazz);
        return gson.fromJson(json, type);
    }

    public <K, V> Map<K, V> fromJsonToMap(String json) {
        Type dataSetMapType = new TypeToken<Map<K, V>>() {
        }.getType();
        return gson.fromJson(json, dataSetMapType);
    }

}
