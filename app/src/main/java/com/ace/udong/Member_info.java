package com.ace.udong;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Member_info extends AppCompatActivity {
    String mId, cId;
    Button btnUpdate, btnDelete;
    TextView textId;
    EditText textName, textTel, textEmail;
    UdongDB udongDB;

    String nameResult, idResult, emailResult, telResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);
        setTitle("내 정보");

        //인텐트 받기
        Intent intent = getIntent();
        mId = intent.getStringExtra("mId");
        if (mId == null) { // 일반회원으로 넘어온 값이 null이면
            cId = intent.getStringExtra("cId"); // 기업회원으로 구분
        }

        btnUpdate = findViewById(R.id.btnUpdate); // 개인정보 수정 버튼
        btnDelete = findViewById(R.id.btnDelete); // 개인정보 수정 버튼
        textId = findViewById(R.id.textId); // 아이디 텍스트뷰 아이디는 pk로 수정 불가
        textName = findViewById(R.id.textName); // 이름
        textTel = findViewById(R.id.textTel); // 전화번호
        textEmail = findViewById(R.id.textEmail); // email

        // udongDB 객체 생성
        udongDB = new UdongDB(this);

        // mId가 null이면 기업회원이다
        if (mId == null) {
            SQLiteDatabase udong = udongDB.getWritableDatabase();
            // 기업회원이면 cId값으로 데이터를 읽어온다
            String sql = "select name, id, email, tel from member where id = '" + cId + "'";
            Cursor cursor = udong.rawQuery(sql, null);
            //검색
            while (cursor.moveToNext()) {
                nameResult = cursor.getString(0);
                idResult = cursor.getString(1);
                emailResult = cursor.getString(2);
                telResult = cursor.getString(3);
            }// while end
            cursor.close();
        } else {
            //mId가 null이 아니면 일반회원이다
            SQLiteDatabase udong = udongDB.getWritableDatabase();
            // mId값으로 데이터베이스에서 검색해온다
            String sql = "select name, id, email, tel from member where id = '" + mId + "'";
            Cursor cursor = udong.rawQuery(sql, null);
            //검색
            while (cursor.moveToNext()) {
                nameResult = cursor.getString(0);
                idResult = cursor.getString(1);
                emailResult = cursor.getString(2);
                telResult = cursor.getString(3);
            }// while end
            cursor.close();
        }
        // 검색해온 값들을 setText해준다
        textId.setText(idResult);
        textName.setText(nameResult);
        textTel.setText(telResult);
        textEmail.setText(emailResult);

        // 개인정보 수정처리
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Member_info.this);
                builder.setTitle("우 동");
                builder.setMessage("개인정보를 수정 하시겠습니까?");
                builder.setNegativeButton("취소", null);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 확인 버튼을 눌렀을때 처리
                        if (mId == null) { // mId에 값이 없으면 기업회원
                            String updateName1 = textName.getText().toString();
                            String updateTel1 = textTel.getText().toString();
                            String updateEmail1 = textEmail.getText().toString();
                            Toast.makeText(getApplicationContext(), "값은" + updateName1 + updateTel1 + updateEmail1, Toast.LENGTH_LONG).show();
                            SQLiteDatabase udong = udongDB.getWritableDatabase();
                            udong.execSQL("update member set name = '" + updateName1 + "', tel = '" + updateTel1 + "', email = '" + updateEmail1 + "' where id = '" + cId + "';");
                            // 업데이트 버튼을 클릭하고 db처리가 되면 수정하고 메인으로 cId값을 가지고 이동
                            Intent updateMove = new Intent(Member_info.this, main_main.class);
                            updateMove.putExtra("cId", idResult);
                            startActivity(updateMove);
                            udong.close();
                        }// if
                        else { // 값이 있으면 일반회원 처리
                            String updateName1 = textName.getText().toString();
                            String updateTel1 = textTel.getText().toString();
                            String updateEmail1 = textEmail.getText().toString();
                            SQLiteDatabase udong = udongDB.getWritableDatabase();
                            udong.execSQL("update member set name = '" + updateName1 + "', tel = '" + updateTel1 + "', email = '" + updateEmail1 + "' where id = '" + mId + "';");
                            // 업데이트 버튼을 클릭하고 db처리가 되면 수정하고 메인으로 mId값을 가지고 이동
                            Intent updateMove = new Intent(Member_info.this, main_main.class);
                            updateMove.putExtra("mId", idResult);
                            startActivity(updateMove);
                            udong.close();
                        } // else
                    }
                }); // positive button
                builder.show();
            }
        }); // update btn end

        // 탈퇴(삭제)처리
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Member_info.this);
                builder.setTitle("우 동");
                builder.setMessage("탈퇴 하시겠습니까?");
                builder.setNegativeButton("취소", null);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // mId가 null이면 기업회원이므로
                        if (mId == null) {
                            SQLiteDatabase udong = udongDB.getWritableDatabase();
                            //cId 값으로 탈퇴처리
                            udong.execSQL("DELETE FROM member WHERE id = '" + cId + "';");
                            udong.close();
                            // 탈퇴하면 로그인페이지로 이동
                            Intent intent = new Intent(Member_info.this, Member_login.class);
                            startActivity(intent);
                        } else {
                            SQLiteDatabase udong = udongDB.getWritableDatabase();
                            // mId 값으로 탈퇴처리
                            udong.execSQL("DELETE FROM member WHERE id = '" + mId + "';");
                            udong.close();
                            // 탈퇴하면 로그인페이지로 이동
                            Intent intent = new Intent(Member_info.this, Member_login.class);
                            startActivity(intent);
                        } // else
                    }
                });
                builder.show();
            } //onClick
        }); // btnDelete
    } // onCreate
} // class