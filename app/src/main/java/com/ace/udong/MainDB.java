package com.ace.udong;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MainDB extends SQLiteOpenHelper {


    public MainDB(@Nullable Context context) {
        super(context, "udong", null,1);
        Log.d("DB" , "생성자 호출함 !!!!");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE place (postid INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR(50), writer VARCHAR(30), content TEXT, photo INTEGER, recommend INTEGER, location VARCHAR(100), room VARCHAR(600));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL("");
    }
}//class
