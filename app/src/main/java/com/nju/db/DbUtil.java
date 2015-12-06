package com.nju.db;

import android.content.Context;

/**
 * Created by xiaojuzhang on 2015/12/1.
 */
public class DbUtil {

    private static SchoolFriendDbHelper mDbHelper = null;
    private DbUtil(Context context){
        mDbHelper = new SchoolFriendDbHelper(context);
    }

    public static SchoolFriendDbHelper newInstance(Context context) {
        if (mDbHelper == null) {
            new DbUtil(context);
        }
        return mDbHelper;
    }
}
