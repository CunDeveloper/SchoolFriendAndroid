package com.nju.util;

/**
 * Created by xiaojuzhang on 2015/12/1.
 */
public class Constant {

    public static final String DB_NAME = "school_friend";
    public static final int MAX_BUFFER = 16384;
    public static final String BASE_URL = "http://114.215.105.94:8080/SchoolFriend";
    public static final String XUE_AUTH = "/XueXinAuthController";
    public static final String XUE_XIN_USERNAME = "username";
    public static final String XUE_XIN_PASSWORD ="password";
    public static final String  ANDROID_ID ="label_id";
    public static final String QUERY_ALL = "query_all";
    public static final String QUERY_OWN = "query_own";
    public static final String QUERY_ANOTHER = "query_another";
    public static final String USER_ID ="user_id";
    public static final String LABEL = "lable";
    public static final String PIC_SHARED_NAME="picSharedName";
    public static final String PERSON_CIRCLE_URL ="/UserContentContrller";
    public static final String PUBLISH_TEXT_URL ="/PublishTextController";
    public static final String USER_LOCATION ="user_location";
    public static final String PUBLISH_TEXT ="publish_text";
    public static final String SCHOOL_FRIEND_SHARED_PREFERENCE ="school_friend_shared_preferences";

    public static final String INSERT_INTO =" INSERT INTO ";
    public static final String DELETE_FROM = " DELETE FROM ";
    public static final String WHERE =" WHERE ";
    public static final String UPDATE ="UPDATE ";

    public static final String LEFT_BACKUT = "(";
    public static final String RIGHT_BACKUT = ")";
    public static final String COMMA_SEP = " ,";

    public static final int SAVE_CONTENT_MESG = 200;//保存缓存的信号量
    public static final int DELETE_CONTENT_MSG = 201;//删除缓存的信号量
    public static final int UPDATE_CONTENT_MSG = 202;//更新本地缓存的信号量

    public static final int MAX_SAVE_NUMBER = 250;//本地缓存的用户发表的最大纪录数
    public static final String MAX_ID_SAVE_CONTENT ="max_id_save_content";//用于纪录缓存在本地的content的最大id;

}
