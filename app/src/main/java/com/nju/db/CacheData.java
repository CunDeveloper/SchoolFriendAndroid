package com.nju.db;

import android.util.LruCache;

/**
 * Created by cun on 2016/3/29.
 */
public class CacheData {
    private static final int SIZE = 4 * 1024 * 1024;
    private static LruCache<String,Object> cache;
    static {
        if (cache == null) {
            cache = new LruCache<>(SIZE);
        }
    }

    public static void save(Object object){
        cache.put("myCollect",object);
    }

    public static Object get(String key){
        return cache.get(key);
    }

}
