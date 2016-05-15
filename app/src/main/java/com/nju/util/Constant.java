package com.nju.util;

/**
 * Created by xiaojuzhang on 2015/12/1.
 */
public class Constant {

    public static final String DB_NAME = "school_friend";
    public static final int MAX_BUFFER = 16384;
    public static final String BASE_URL = "http://115.159.186.158:8080/SchoolFriend";
    public static final String XUE_AUTH = BASE_URL + "/XueXinAuthController";
    public static final String XUE_XIN_USERNAME = "username";
    public static final String XUE_XIN_PASSWORD = "password";
    public static final String XUE_XIN_CAPTCHA = "captcha";
    public static final String ANDROID_ID = "label_id";
    public static final String QUERY_ALL = "query_all";
    public static final String QUERY_OWN = "query_own";
    public static final String QUERY_ANOTHER = "query_another";
    public static final String USER_ID = "user_id";
    public static final String LABEL = "lable";
    public static final String CONTENT = "content";
    public static final String PIC_SHARED_NAME = "picSharedName";
    public static final String PERSON_CIRCLE_URL = "/UserContentContrller";
    public static final String PUBLISH_TEXT_URL = "/PublishTextController";
    public static final String PUBLISH_TEXT_WITH_PIC_URL = "/UserPublishContentController?school=lanzhouuniversity";
    public static final String USER_LOCATION = "user_location";
    public static final String PUBLISH_TEXT = "publish_text";
    public static final String SCHOOL_FRIEND_SHARED_PREFERENCE = "school_friend_shared_preferences";
    public static final String XUE_XIN_INFO = "xue_xin_info";
    public static final String XUE_XIN_USERNAME_OR_PASS_ERROR = "xue_xin_user_or_pass_error";
    public static final String INSERT_INTO = " INSERT INTO ";
    public static final String DELETE_FROM = " DELETE FROM ";
    public static final String WHERE = " WHERE ";
    public static final String UPDATE = "UPDATE ";

    public static final String LEFT_BACKUT = "(";
    public static final String RIGHT_BACKUT = ")";
    public static final String COMMA_SEP = " ,";

    public static final int SAVE_CONTENT_MESG = 200;//保存缓存的信号量
    public static final int DELETE_CONTENT_MSG = 201;//删除缓存的信号量
    public static final int UPDATE_CONTENT_MSG = 202;//更新本地缓存的信号量
    public static final String HTTP_ERROR = "内部服务器出错";
    public static final String HTTP_URL_ERROR = "请求网关错误";
    public static final int MAX_SAVE_NUMBER = 250;//本地缓存的用户发表的最大纪录数
    public static final String MAX_ID_SAVE_CONTENT = "max_id_save_content";//用于纪录缓存在本地的content的最大id;

    public static final String EXCEPTION = "exception";
    public static final String INFO = "info";
    public static final String CODE = "code";
    public static final String MSG = "msg";


    public static final int OK = 200;
    public static final long TODAY_TIME = 86400000;
    public static final long YESTERDAY_TIME = 172800000;
    public static final String YESTERDAY = "昨天";
    public static final String TODAY = "今天";
    public static final String TITLE = "title";
    public static final String WHO_SCAN = "whoScan";
    public static final String VOICE_TYPE = "voiceType";
    public static final String AUTHORIZATION = "authorization";
    public static final String FILE = "file";
    public static final String PROBLEM = "problem";
    public static final String DESCRIPTION = "description";
    public static final String UNKNOWN_CHARACTER = "???";
    public static final String UTF_8 = "UTF-8";
    public static final String TYPE = "type";
    public static final String LOCATION = "location";
    public static final String MONTH = "月";
    public static final String DD = "dd";
    public static final String MM = "MM";
    public static final String ALL = "所有";

    public static final String WIFI = "wifi";
    public static final String MOBILE = "mobile";
    public static final String OTHER = "other";
    public static final String RECOMMEND_COLLECT = "recommendCollect";
    public static final String RECOMMEND = "recommend";
    public static final String ALUMNI_VOICE = "alumniVoice";
    public static final String MAJOR_ASK = "majorAsk";
    public static final String DELETE_TABLE_DATA = "DELETE * FROM ";
    public static final String[] EMPTY = {};
    public static final int MAX_LIST_NUMBER = 100;
    public static final String RECOMMEND_ID = "recommendID";
    public static final String UNDERGRADUATE = "本科";
    public static final String MASTER = "硕士";
    public static final String DOCTOR = "博士";
    public static final String DEGREE = "degree";
    public static final String WORK_TYP = "workType";
    public static final String INTERN = "实习";
    public static final String ALL_JOG = "社招";
    public static final String SCHOOL_JOG = "校招";

    public static final int LIMIT = 200;
    public static final String DYNAMIC_PRE_ID = "dynamicPreId";
    public static final String DYNAMIC_NEXT_ID = "dynamicNextId";
    public static final String VOICE_PRE_ID = "voicePreId";
    public static final String VOICE_NEXT_ID = "voiceNextId";
    public static final String MY_VOICE_PRE_ID = "myVoicePreId";
    public static final String MY_VOICE_NEXT_ID = "myVoiceNextId";
    public static final String ASK_PRE_ID = "askPreId";
    public static final String ASK_NEXT_ID = "askNextId";
    public static final String MY_ASK_PRE_ID = "myAskPreId";
    public static final String MY_ASK_NEXT_ID = "myAskNextId";
    public static final String RECOMMEND_PRE_ID = "recommendPreId";
    public static final String RECOMMEND_NEXT_ID = "recommendNextId";
    public static final String MY_RECOMMEND_PRE_ID = "recommendPreId";
    public static final String MY_RECOMMEND_NEXT_ID = "recommendNextId";
    public static final String PRE = "pre";
    public static final String NEXT = "next";
    public static final String DEGREES = "degress";
    public static final String ASK_LABEL = "askLabel";
    public static final String VOICE_LABEL = "voiceLabel";
    public static final String COLLEGES = "colleges";
    public static final String EMAIL = "email";
    public static final String A_LABEL = "label";
    public static final String EDIT = "编辑";
    public static final String PLEASE_LABEL_CONTENT = "请输入标签内容";
    public static final String ALL_SACN = "所有校友可见";
    public static final String ONLY_ME_SCAN = "仅自己可见";
    public static final String SAME_YEAR_COLLEAGE = "同届同一学院可见";
    public static final String SAME_COLLEGE_NOT_YEAR = "本学院其他届可见";
    public static final String SAME_NOT_YEAR_NOT_COLLEGE = "其他届其他学院可见";
    public static final String SAME_YEAR_NOT_COLLEGE = "本届其他学院可见";

    public static final String OK_MSG = "\"OK\"";
    public static final String PUBLISH_OK = "发布成功";
    public static final String PUBLISH_ERREOR = "发表失败";
    public static final String IN_LOCATION = "所在位置";
    public static final String ALUMNI_DYNAMIC = "alumniDynamic";
    public static final String MY_DYNAMIC_PRE_ID = "myDynamicPreId";
    public static final String MY_DYNAMIC_NEXT_ID = "myDynamicNextId";
    public static final String REPLAY = "回复";
    public static final String PRAISE_OK = "点赞成功";
    public static final String COMMENT_OK = "评论成功";
    public static final String DELETE = "删除";
    public static final int MAX_ROW = 50;
    public static final String AUTHOR_ID = "authorId";
    public static final String ALUMNI_PUBLIC_PIC_ROOT = "校友圈";
    public static final String EMPTY_STR = "";
    public static final String COLLECT = "收藏";
    public static final String COMPLAIN = "投诉";
    public static final String PERSON_VOICE = "我的心声";
    public static final String LEVEL_ALL = "所有";
}
