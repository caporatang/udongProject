package com.ace.udong;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class UdongDB extends SQLiteOpenHelper {

    // SQLiteOpenHelper를 상속한 클래스 생성
    public UdongDB(@Nullable Context context) {
        super(context, "udong", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 재정의한 메서드로 테이블을 생성한다
        db.execSQL("CREATE TABLE member (name varchar(10), id varchar(30), pw varchar(30), tel varchar(11), email varchar(100), company varchar(100));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
