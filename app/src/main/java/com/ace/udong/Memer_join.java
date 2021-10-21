package com.ace.udong;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Memer_join extends AppCompatActivity {
    //전역변수 선언
    Button btnJoinConfirm;
    EditText joinCompanyNum, joinName, joinId, joinEmail, joinPw, joinPwConfirm, joinTel;
    CheckBox memberCheck, companyCheck, lastCheck;
    UdongDB udongDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memer_join);
        setTitle("회원가입");
        btnJoinConfirm = findViewById(R.id.btnJoinConfirm); // 회원 가입 버튼

        joinCompanyNum = findViewById(R.id.joinCompanyNum); // 사업자 번호
        joinId = findViewById(R.id.joinId); // 가입 아이디
        joinName = findViewById(R.id.joinName); // 가입 이름
        joinEmail = findViewById(R.id.joinEmail); // 가입 이메일
        joinPw = findViewById(R.id.joinPw); // 가입 비밀번호
        joinPwConfirm = findViewById(R.id.joinPwConfirm); // 가입 비밀번호 중복확인
        joinTel = findViewById(R.id.joinTel); // 가입 전화번호

        memberCheck = findViewById(R.id.memberCheck); // 개인회원 체크박스
        companyCheck = findViewById(R.id.companyCheck); // 기업회원 체크박스
        lastCheck = findViewById(R.id.lastCheck); // 약관동의

        udongDB = new UdongDB(this);

        // 개인회원 기업회원 체크박스가 둘다 체크 되지 않은경우
        // 사업자번호 입력칸을 입력되지 않게 막는다.
        if (memberCheck.isChecked() == false && companyCheck.isChecked() == false) {
            joinCompanyNum.setEnabled(false);
        }

        //개인회원 체크박스
        memberCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 개인회원 체크박스 클릭했을 경우 사업자회원 입력을 막음
                // 사업자번호 입력 번호 EditText 비활성화
                if (memberCheck.isChecked() == true) {
                    companyCheck.setEnabled(false);
                    joinCompanyNum.setEnabled(false);
                } else {
                    // 개인회원 체크가 안되있을경우
                    companyCheck.setEnabled(true);
                    // 아무것도 체크가 안된 상태이므로 사업자  번호
                    // EditText 입력을 막는다.
                    joinCompanyNum.setEnabled(false);
                }
            }
        }); // 개인회원 체크박스 end

        // 기업회원 체크박스
        companyCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 기업회원 체크박스를 체크
                if (companyCheck.isChecked() == false) {
                    // 체크하지 않은 경우, 사업자번호를 막음
                    joinCompanyNum.setEnabled(false);
                    // 개인회원 체크박스는 살린다
                    memberCheck.setEnabled(true);
                } else {
                    // 사업자번호 입력칸을 열어준다
                    joinCompanyNum.setEnabled(true);
                    // 체크한 경우에 개인회원 체크박스를 막고
                    memberCheck.setEnabled(false);
                }
            }
        }); // 기업회원 체크박스 end


        btnJoinConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 에디트 텍스트에 입력값을 가져온다
                String pwJoin = joinPw.getText().toString();
                String confirmPw = joinPwConfirm.getText().toString();
                String name = joinName.getText().toString();
                String id = joinId.getText().toString();
                String email = joinEmail.getText().toString();
                String tel = joinTel.getText().toString();

                 // 입력칸이 null이나 체크하지 않은 경우에 가입되지 않게 처리
                if (companyCheck.isChecked() == false && memberCheck.isChecked() == false) {
                    Toast.makeText(getApplicationContext(), "개인기업, 기업회원을 선택 해주세요", Toast.LENGTH_SHORT).show();
                } // 개인 , 기업회원 둘다 체크 안하고 가입할시 토스트
                if (lastCheck.isChecked() == false) {
                    Toast.makeText(getApplicationContext(), "약관에 동의 해주세요", Toast.LENGTH_SHORT).show();
                } // 약관동의 체크박스 확인 토스트

                // 비밀번호가 다를 경우 가입이 되지않고, 포커스를 주고 키보드 올라오게 하기.
                if (!pwJoin.equals(confirmPw)) {
                    joinPw.requestFocus();
                    InputMethodManager key = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    key.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인 해주세요", Toast.LENGTH_SHORT).show();
                }

                // 공백체크
                if (name.equals("") && id.equals("") && email.equals("") &&
                        pwJoin.equals("") && confirmPw.equals("") && tel.equals("")
                ) {
                    Toast.makeText(getApplicationContext(), "빈칸을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (id.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (email.equals("") || !email.contains("@")) {
                    joinEmail.requestFocus();
                    InputMethodManager key = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    key.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    Toast.makeText(getApplicationContext(), "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else if (pwJoin.equals("") || joinPw.length() < 8) {
                    joinPw.requestFocus();
                    InputMethodManager key = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    key.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else if (confirmPw.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else if (tel.equals("") || tel.contains("-")) {
                    joinTel.requestFocus();
                    InputMethodManager key = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    key.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    Toast.makeText(getApplicationContext(), "전화번호를 확인해주세요", Toast.LENGTH_SHORT).show();

                    // 기업회원 체크박스가 체크 되어있고, 비밀번호 중복확인을 했을 경우에
                    // 기업회원이므로 사업자 번호까지 추가해서 db에 insert 한다
                } else if (companyCheck.isChecked() == true && pwJoin.equals(confirmPw)) {
                    // 위에서 작성한 값들이 빈칸이나 맞게 입력한 경우에 sqlite에 insert 처리
                    SQLiteDatabase udong = udongDB.getWritableDatabase();
                    String dbId = joinId.getText().toString();
                    String dbPw = joinPw.getText().toString();
                    String dbEmail = joinEmail.getText().toString();
                    String dbTel = joinTel.getText().toString();
                    String dbName = joinName.getText().toString();
                    String dbCompanyNum = joinCompanyNum.getText().toString();
                    udong.execSQL("insert into member values ('" + dbName + "', '" + dbId + "', '" + dbPw + "','" + dbTel + "','" + dbEmail + "', '" + dbCompanyNum + "');");
                    AlertDialog.Builder builder = new AlertDialog.Builder(Memer_join.this);
                    builder.setTitle("우 동");
                    builder.setMessage("환영합니다 우동으로 떠나요 !");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //db에 insert처리하고 Alert다이얼로그에서 확인을 누를 경우에 로그인 화면으로 이동
                            Intent intent = new Intent(Memer_join.this, Member_login.class);
                            startActivity(intent);
                        }
                    });
                    builder.create().show();
                    udong.close();
                    //개인회원 체크박스 체크, 비밀번호 중복확인을 패스하는 경우
                } else if (memberCheck.isChecked() == true && pwJoin.equals(confirmPw)) {
                    SQLiteDatabase udong = udongDB.getWritableDatabase();
                    String dbId = joinId.getText().toString();
                    String dbPw = joinPw.getText().toString();
                    String dbEmail = joinEmail.getText().toString();
                    String dbTel = joinTel.getText().toString();
                    String dbName = joinName.getText().toString();
                    //기업회원이 아니므로 사업자번호 text는 null처리 한다
                    udong.execSQL("insert into member values ('" + dbName + "', '" + dbId + "', '" + dbPw + "','" + dbTel + "','" + dbEmail + "', '" + null + "');");
                    AlertDialog.Builder builder = new AlertDialog.Builder(Memer_join.this);
                    builder.setTitle("우 동");
                    builder.setMessage("연습실을 찾으러 떠나요!");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // db에 isnert처리하고 로그인 화면으로 이동
                            Intent intent = new Intent(Memer_join.this, Member_login.class);
                            startActivity(intent);
                        }
                    });
                    builder.create().show();
                    udong.close();
                }


            } // on click
        }); // btnJoinConfirm end


    }
}