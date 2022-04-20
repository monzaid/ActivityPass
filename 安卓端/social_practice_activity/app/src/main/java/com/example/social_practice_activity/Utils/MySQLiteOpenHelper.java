package com.example.social_practice_activity.Utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库的名称
     */
    private static final String DATABASE_NAME = "UserDB.db";

    /**
     * 数据库的版本号，以后要升级数据库，修改版本号为 +1 即可
     */
    private static final int DATABASE_VERSION = 2;

    private static MySQLiteOpenHelper instance;

    /**
     * 单例模式
     * @param context 传入上下文
     * @return 返回MySQLiteOpenHelper对象
     */
    public static MySQLiteOpenHelper getInstance(Context context) {
        if (null == instance) {
            synchronized (MySQLiteOpenHelper.class) {
                if (null == instance) {
                    instance = new MySQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
                }
            }
        }
        return instance;
    }

    // 构造方法不对外暴露
    private MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 构造方法不对外暴露
    private MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    // 初始化操作，会执行onCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建一个 student_table表
        db.execSQL("create table if not exists user_table(id text primary key, password text);");
    }

    // 用于升级数据库，当Version 变动了，就会调用onUpgrade方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("alter table student_table add age integer null");
    }
}
