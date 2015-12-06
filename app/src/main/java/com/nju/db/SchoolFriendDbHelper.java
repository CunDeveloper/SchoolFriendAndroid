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
//    private static final String SQL_CREATE_AREA =
//            CREATE_TABLE+ AreaEntry.TABLE_NAME + " (" +
//                    AreaEntry.COLUMN_NAME_ID + PRIMARY_KEY +COMMA_SEP+
//                    AreaEntry.COLUMN_NAME_AREA_ID + TEXT_TYPE + COMMA_SEP +
//                    AreaEntry.COLUMN_NAME_AREA + TEXT_TYPE + COMMA_SEP +
//                    AreaEntry.COLUMN_NAME_CITY_ID + TEXT_TYPE  +
//              " )";
//    private static final String SQL_CREATE_CITY =
//            CREATE_TABLE+ CityEntry.TABLE_NAME + " (" +
//                    CityEntry.COLUMN_NAME_CID + PRIMARY_KEY +COMMA_SEP+
//                    CityEntry.COLUMN_NAME_CITY_ID + TEXT_TYPE + COMMA_SEP +
//                    CityEntry.COLUMN_NAME_CITY + TEXT_TYPE + COMMA_SEP +
//                    CityEntry.COLUMN_NAME_PROVINCE_ID + TEXT_TYPE  +
//                    " )";
    private static final String SQL_CREATE_COLLEGE =
            CREATE_TABLE+ CollegeEntry.TABLE_NAME + " (" +
                    CollegeEntry.COLUMN_NAME_CO_ID + PRIMARY_KEY +COMMA_SEP+
                    CollegeEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CollegeEntry.COLUMN_NAME_PROVINCE_ID + INTEGER_TYPE +
                    " )";

    private static final String SQL_CREATE_PROVINCE =
            CREATE_TABLE+ ProvinceEntry.TABLE_NAME + " (" +
                    ProvinceEntry.COLUMN_NAME_P_ID + PRIMARY_KEY +COMMA_SEP+
                    ProvinceEntry.COLUMN_NAME_PROVINCE_ID + INTEGER_TYPE + COMMA_SEP +
                    ProvinceEntry.COLUMN_NAME_P_NAME + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_SCHOOL =
            CREATE_TABLE+ SchoolEntry.TABLE_NAME + " (" +
                    SchoolEntry.COLUMN_NAME_SC_ID + PRIMARY_KEY +COMMA_SEP+
                    SchoolEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    SchoolEntry.COLUMN_NAME_COLLEGE_ID + INTEGER_TYPE +
                    " )";

//    private static final String SQL_DELETE_AREA =
//            DROP_TABLE + AreaEntry.TABLE_NAME;
//    private static final String SQL_DELETE_CITY =
//            DROP_TABLE + CityEntry.TABLE_NAME;
    private static final String SQL_DELETE_COLLEGE =
            DROP_TABLE + CollegeEntry.TABLE_NAME;
    private static final String SQL_DELETE_PROVINCE =
            DROP_TABLE + ProvinceEntry.TABLE_NAME;
    private static final String SQL_DELETE_SCHOOL =
            DROP_TABLE + SchoolEntry.TABLE_NAME;


    public SchoolFriendDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COLLEGE);
        db.execSQL(SQL_CREATE_PROVINCE);
        db.execSQL(SQL_CREATE_SCHOOL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();
        db.execSQL(SQL_DELETE_COLLEGE);
        db.execSQL(SQL_DELETE_PROVINCE);
        db.execSQL(SQL_DELETE_SCHOOL);
        db.endTransaction();
        onCreate(db);
    }
}
