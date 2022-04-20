package com.example.social_practice_activity.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.social_practice_activity.bean.myUser;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBHelper extends SQLiteOpenHelper{

    public static final String DB_DBNAME="activity_db";

    public static final String DB_TABLENAME="user_content";

    public static final int VERSION = 1;

    public static SQLiteDatabase dbInstance;

    private MyDBHelper myDBHelper;

    private StringBuffer tableCreate;

    private Context context;

    //数据库中创建一张Student表
    public static final String user_content = "create table if not exists user_content("
            + "id text primary key," + "password text);";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public void openDatabase() {
        if(dbInstance == null) {
            myDBHelper = new MyDBHelper(context,DB_DBNAME,VERSION);
            dbInstance = myDBHelper.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //创建表
        db.execSQL(user_content);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     *
     * @param user
     * @return
     */
    public long insert(myUser user)
    {
        ContentValues values = new ContentValues();
        values.put("id",  user.id);
        values.put("password", user.password);
        return dbInstance.insert(DB_TABLENAME, null, values);
    }

    /**
     *
     * @return list
     */

    @SuppressLint("Range")
    public ArrayList getAllUser() {
        ArrayList list = new ArrayList();
        Cursor cursor = null;
        cursor = dbInstance.query(DB_TABLENAME,
                new String[]{"id","password"},
                null,
                null,
                null,
                null,
                null);


        while(cursor.moveToNext()) {
            HashMap item = new HashMap();
            item.put("id", cursor.getString(cursor.getColumnIndex("id")));
            item.put("password", cursor.getString(cursor.getColumnIndex("password")));
            list.add(item);
        }

        return list;
    }

    public void delete(int _id) {
        dbInstance.delete(DB_TABLENAME, "id=?", new String[]{String.valueOf(_id)});
    }
    public void deleteAll() {
        dbInstance.delete(DB_TABLENAME, null, null);
    }

    public int getTotalCount() {
        Cursor cursor = dbInstance.query(DB_TABLENAME, new String[]{"count(*)"}, null, null, null, null, null);
        cursor.moveToNext();
        return cursor.getInt(0);
    }

    @SuppressLint("Range")
    public ArrayList<myUser> getAll()
    {
        ArrayList<myUser> list = new ArrayList();
        String sql = "select * from " + DB_TABLENAME;
        Cursor cursor = dbInstance.rawQuery(sql, null);
        while (cursor.moveToNext())
        {
            //HashMap item = new HashMap();
            myUser user = new myUser();
            user.id = cursor.getString(cursor.getColumnIndex("id"));
            user.password = cursor.getString(cursor.getColumnIndex("password"));
            list.add(user);
        }
        return list;

    }

    @SuppressLint("Range")
    public void backupData() {
        StringBuffer sqlBackup = new StringBuffer();
        Cursor cursor = null;
        cursor = dbInstance.query(DB_TABLENAME,
                new String[]{"id","password"},
                null,
                null,
                null,
                null,
                null);


        while(cursor.moveToNext()) {
            sqlBackup.append("insert into " + DB_TABLENAME + "(id,password,privacy)")
                    .append(" values ('")
                    .append(cursor.getString(cursor.getColumnIndex("id"))).append("','")
                    .append(cursor.getString(cursor.getColumnIndex("password"))).append("','")
                    .append(");").append("\n");
        }
        saveDataToFile(sqlBackup.toString());

    }


    private void saveDataToFile(String strData) {
        String fileName = "";
        fileName = "comm_data.bk";
        try {
            String SDPATH = Environment.getExternalStorageDirectory() + "/";
            File fileParentPath = new File(SDPATH + "zpContactData/");
            fileParentPath.mkdirs();
            File file = new File(SDPATH + "zpContactData/" + fileName);
            System.out.println("the file previous path = " + file.getAbsolutePath());

            file.createNewFile();
            System.out.println("the file next path = " + file.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(strData.getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void restoreData(String fileName) {
        try {
            String SDPATH = Environment.getExternalStorageDirectory() + "/";
            File file = null;
            if(fileName.endsWith(".bk")) {
                file = new File(SDPATH + "zpContactData/"+ fileName);
            } else {
                file = new File(SDPATH + "zpContactData/"+ fileName + ".bk");
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = "";
            while((str=br.readLine())!=null) {
                System.out.println(str);
                dbInstance.execSQL(str);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean findFile(String fileName) {
        String SDPATH = Environment.getExternalStorageDirectory() + "/";
        File file = null;
        if(fileName.endsWith(".bk")) {
            file = new File(SDPATH + "zpContact/"+fileName);
        } else {
            file = new File(SDPATH + "zpContact/"+fileName + ".bk");
        }

        if(file.exists()) {
            return true;
        } else {
            return false;
        }


    }


    class MyDBHelper extends SQLiteOpenHelper {

        public MyDBHelper(Context context, String name,
                          int version) {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            tableCreate = new StringBuffer();
            tableCreate.append("create table ")
                    .append(DB_TABLENAME)
                    .append(" (")
                    .append("id integer primary key autoincrement,")
                    .append("content text,")
                    .append("chatTime text,")
                    .append("isComeMsg text,")
                    .append("type text ")
                    .append(")");
            System.out.println(tableCreate.toString());
            db.execSQL(tableCreate.toString());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = "drop table if exists " + DB_TABLENAME;
            db.execSQL(sql);
            myDBHelper.onCreate(db);
        }

    }
}