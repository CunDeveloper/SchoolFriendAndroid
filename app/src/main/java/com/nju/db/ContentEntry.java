package com.nju.db;

import android.provider.BaseColumns;

/**
 * Created by xiaojuzhang on 2015/12/14.
 */
public final class ContentEntry implements BaseColumns {
    public static final String TABLE_NAME = "content";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_TEXT = "text";
    public static final String  COLUMN_NAME_USER_ID ="user_id";
    public static final String COLUMN_NAME_IS_CONTAIN_IMAGE = "is_contain_image";
    public static final String COLUMN_NAME_DATE ="date";
}
