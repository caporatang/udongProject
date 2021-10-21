package com.ace.udong;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Member_login extends AppCompatActivity {
    //아래에서 넓은 범위에서 사용할 전역변수 선언
    Button btnJoin, btnLogin;
    EditText id, password;
    UdongDB udongDB;
    String idResult;
    String pwResult;
    String companyNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_login);

        //데이터베이스 객체 생성
        udongDB = new UdongDB(this);

        btnJoin = findViewById(R.id.btnJoin); // 가입버튼
        btnLogin = findViewById(R.id.btnLogin); // 로그인 버튼
        id = findViewById(R.id.id); // 로그인창 아이디 에디트텍스트
        password = findViewById(R.id.password); // 로그인창 비밀번호 에디트 텍스트

        // 회원 가입 페이지로 이동
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Member_login.this, Memer_join.class);
                startActivity(intent);
            }
        }); // 회원가입 버튼

        // 데이터베이스 검색을 통한 로그인 처리
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // db객체 생성
                SQLiteDatabase udong = udongDB.getWritableDatabase();
                String loginId = id.getText().toString();
                String loginPass = password.getText().toString();

                // 아이디 비밀번호 검색
                String sql = "select id, pw, company from member where id = '" + loginId + "'";
                Cursor cursor = udong.rawQuery(sql, null);
                // db에서 입력한 값 가져와서 검색
                while (cursor.moveToNext()) {
                    idResult = cursor.getString(0);
                    pwResult = cursor.getString(1);
                    companyNum = cursor.getString(2);
                }// while end
                // EditText에서 가져온 값과 DB에서 가져온 결과를 비교하여 처리
                if (loginId.equals(idResult) && loginPass.equals(pwResult)) {
                    // db에서 사업자번호 컬럼에 있는 값이 null이면, 개인회원이다
                    if (companyNum.equals("null")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Member_login.this);
                        builder.setTitle("우 동");
                        builder.setMessage(idResult + "님 환영합니다 \n연습실을 찾으러 떠나요 !");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(Member_login.this, main_main.class);
                                // 개인회원은 mId로 intent에 등록하고 다른 페이지로 넘겨준다
                                // 인텐트에 등록 값 넘겨주기
                                intent.putExtra("mId", idResult);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    } else if (loginId.equals(idResult) && loginPass.equals(pwResult)) {
                        // 가입할때 사업자번호 컬럼이 null이 아니면 기업회원이다.
                        if (companyNum != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Member_login.this);
                            builder.setTitle("우 동");
                            builder.setMessage("환영합니다 " + idResult + "님 기업회원이시네요 ! \n우동을 통해 빠르게 홍보 해보세요!");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 기업회원은 cId로 intent에 등록하여 메인 페이지에 넘겨준다
                                    Intent intent = new Intent(Member_login.this, main_main.class);
                                    intent.putExtra("cId", idResult);
                                    startActivity(intent);
                                }
                            });
                            builder.show();
                        }
                    } // else if end >> 컬럼에 값이 있으면 기업회원
                } // 사업자회원, 개인회원 구분 if end
                else { // 위에 조건이 맞지 않으면 아이디 비밀번호를 확인 메세지를 띄워줌
                    AlertDialog.Builder builder = new AlertDialog.Builder(Member_login.this);
                    builder.setTitle("우 동");
                    builder.setMessage("아이디와 비밀번호가 맞지 않아요\n확인 해주세요!");
                    builder.setPositiveButton("확인", null);
                    builder.show();
                } // 아이디와 비밀번호가 틀렸을 경우
                cursor.close(); // 검색종료
                udong.close();
            }
        });
    }// onCreate
} // class