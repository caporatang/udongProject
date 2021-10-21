package com.ace.udong;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class insert_main extends AppCompatActivity {

    EditText etWriteTitle, etWriteLocation, etWriteContent, etWriteRoom, etWritePhoto;
    Button btnWriteBT;
    MainDB mainDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_main);
        // 액션바
        ActionBar ab = getSupportActionBar();
        ab.setTitle("글작성");
        //객체
        etWriteTitle = findViewById(R.id.etWriteTitle);
        etWriteContent = findViewById(R.id.etWriteContent);
        etWriteLocation = findViewById(R.id.etWriteLocation);
        etWritePhoto = findViewById(R.id.etWritePhoto);
        etWriteRoom = findViewById(R.id.etWriteRoom);
        btnWriteBT = findViewById(R.id.btnWriteBT);

        Intent intent = getIntent();
        String writer1 = intent.getStringExtra("cId");
        mainDB = new MainDB(this);



        btnWriteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title1 = etWriteTitle.getText().toString();
                String content1 = etWriteContent.getText().toString();
                String Location1 = etWriteLocation.getText().toString();
                String photo1 = etWritePhoto.getText().toString();
                String room1 = etWriteRoom.getText().toString();

//                String writer1 = "me";
//                int postid1 = 1;
                //intent
                Intent intent = new Intent(insert_main.this, main_main.class);
                int recommend1 = 0;
                try {
                // DB
                SQLiteDatabase sqlDB = mainDB.getWritableDatabase();
                sqlDB.execSQL("insert into place (title,writer,content,photo,recommend,location,room) values ('"+ title1 +"', '"+ writer1 +"','"+ content1 +"','"+ photo1 +"','"+ recommend1 +"','"+ Location1 +"','"+ room1 +"');");
                Log.d("DB" , "데이터 삽입 성공,,,!!!");
                sqlDB.close();
                Log.d("DB" , "데이터 베이스 closed,,,!!!");
                    Toast.makeText(getApplicationContext(),"글작성이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"에러가 발생했습니다! 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }//try/catch

            }
        });






    }// oncreate
}//class