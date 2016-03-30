package com.nju.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nju.util.Constant;

/**
 * Created by xiaojuzhang on 2015/12/1.
 */
public class SchoolFriendDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = Constant.DB_NAME;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String PRIMARY_KEY = " INTEGER PRIMARY KEY";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    private static final String SQL_CREATE_CONTENT =
            CREATE_TABLE + ContentEntry.TABLE_NAME + " (" +
                    ContentEntry.COLUMN_NAME_ID + INTEGER_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_IS_CONTAIN_IMAGE + INTEGER_TYPE + COMMA_SEP +
                    ContentEntry.COLUMN_NAME_USER_ID + INTEGER_TYPE +
                    " )";
    private static final String SQL_CREATE_COLLEGE =
            CREATE_TABLE + CollegeEntry.TABLE_NAME + " (" +
                    CollegeEntry.COLUMN_NAME_CO_ID + PRIMARY_KEY + COMMA_SEP +
                    CollegeEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CollegeEntry.COLUMN_NAME_PROVINCE_ID + INTEGER_TYPE +
                    " )";
    private static final String SQL_CREATE_PROVINCE =
            CREATE_TABLE + ProvinceEntry.TABLE_NAME + " (" +
                    ProvinceEntry.COLUMN_NAME_P_ID + PRIMARY_KEY + COMMA_SEP +
                    ProvinceEntry.COLUMN_NAME_PROVINCE_ID + INTEGER_TYPE + COMMA_SEP +
                    ProvinceEntry.COLUMN_NAME_P_NAME + TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_SCHOOL =
            CREATE_TABLE + SchoolEntry.TABLE_NAME + " (" +
                    SchoolEntry.COLUMN_NAME_SC_ID + PRIMARY_KEY + COMMA_SEP +
                    SchoolEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    SchoolEntry.COLUMN_NAME_COLLEGE_ID + INTEGER_TYPE +
                    " )";
    private static final String SQL_CREATE_RECOMMEND_COLLECT =
            CREATE_TABLE + RecommendCollectEntity.TABLE_NAME + " (" +
                    RecommendCollectEntity.ID + PRIMARY_KEY + COMMA_SEP +
                    RecommendCollectEntity.CONTENT +TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_ALUMNI_VOICE_COLLECT =
            CREATE_TABLE + AlumniVoiceCollectEntity.TABLE_NAME + " (" +
                    AlumniVoiceCollectEntity.ID + PRIMARY_KEY + COMMA_SEP +
                    AlumniVoiceCollectEntity.CONTENT +TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_MAJOR_ASK_COLLECT =
            CREATE_TABLE + MajorAskCollectEntity.TABLE_NAME + " (" +
                    MajorAskCollectEntity.ID + PRIMARY_KEY + COMMA_SEP +
                    MajorAskCollectEntity.CONTENT +TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_RECOMMEND_WORK =
            CREATE_TABLE + RecommendEntity.TABLE_NAME + " (" +
                    RecommendEntity.ID + PRIMARY_KEY + COMMA_SEP +
                    RecommendEntity.CONTENT +TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_ALUMNI_VOICE =
            CREATE_TABLE + AlumniVoiceEntity.TABLE_NAME + " (" +
                    AlumniVoiceEntity.ID + PRIMARY_KEY + COMMA_SEP +
                    AlumniVoiceEntity.CONTENT +TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_MAJOR_ASK=
            CREATE_TABLE + MajorAskEntity.TABLE_NAME + " (" +
                    MajorAskEntity.ID + PRIMARY_KEY + COMMA_SEP +
                    MajorAskEntity.CONTENT +TEXT_TYPE +
                    " )";


    private static final String SQL_DELETE_COLLEGE =
            DROP_TABLE + CollegeEntry.TABLE_NAME;
    private static final String SQL_DELETE_PROVINCE =
            DROP_TABLE + ProvinceEntry.TABLE_NAME;
    private static final String SQL_DELETE_SCHOOL =
            DROP_TABLE + SchoolEntry.TABLE_NAME;
    private static final String SQL_DELETE_CONTENT =
            DROP_TABLE + ContentEntry.TABLE_NAME;
    private static final String SQL_DELETE_RECOMMEND_COLLECT =
            DROP_TABLE + RecommendCollectEntity.TABLE_NAME;
    private static final String SQL_DELETE_ALUMNI_VOICE_COLLECT =
            DROP_TABLE + AlumniVoiceCollectEntity.TABLE_NAME;
    private static final String SQL_DELETE_MAJOR_ASK_COLLECT =
            DROP_TABLE + MajorAskCollectEntity.TABLE_NAME;
    private static final String SQL_DELETE_RECOMMEND_WORK =
            DROP_TABLE + RecommendEntity.TABLE_NAME;
    private static final String SQL_DELETE_ALUMNI_VOICE =
            DROP_TABLE + AlumniVoiceEntity.TABLE_NAME;
    private static final String SQL_DELETE_MAJOR_ASK =
            DROP_TABLE + MajorAskEntity.TABLE_NAME;


    private static SchoolFriendDbHelper dbHelper;


    private SchoolFriendDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SchoolFriendDbHelper newInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new SchoolFriendDbHelper(context.getApplicationContext());
        }
        return dbHelper;
    }

    public static SchoolFriendDbHelper newInstance() {
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COLLEGE);
        db.execSQL(SQL_CREATE_PROVINCE);
        db.execSQL(SQL_CREATE_SCHOOL);
        db.execSQL(SQL_CREATE_CONTENT);
        db.execSQL(SQL_CREATE_RECOMMEND_COLLECT);
        db.execSQL(SQL_CREATE_ALUMNI_VOICE_COLLECT);
        db.execSQL(SQL_CREATE_MAJOR_ASK_COLLECT);
        db.execSQL(SQL_CREATE_RECOMMEND_WORK);
        db.execSQL(SQL_CREATE_ALUMNI_VOICE);
        db.execSQL(SQL_CREATE_MAJOR_ASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_COLLEGE);
        db.execSQL(SQL_DELETE_PROVINCE);
        db.execSQL(SQL_DELETE_SCHOOL);
        db.execSQL(SQL_DELETE_CONTENT);
        db.execSQL(SQL_DELETE_RECOMMEND_COLLECT);
        db.execSQL(SQL_DELETE_ALUMNI_VOICE_COLLECT);
        db.execSQL(SQL_DELETE_MAJOR_ASK_COLLECT);
        db.execSQL(SQL_DELETE_RECOMMEND_WORK);
        db.execSQL(SQL_DELETE_ALUMNI_VOICE);
        db.execSQL(SQL_DELETE_MAJOR_ASK);
        onCreate(db);
    }
}
