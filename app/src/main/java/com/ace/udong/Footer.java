package com.ace.udong;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Footer extends AppCompatActivity {
    ImageView circleImageView;
    String mIdCheck, cIdCheck;
    int musicTool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footer);
        //액션바
        ActionBar ab = getSupportActionBar();
        ab.setTitle("우리 동네 합주실");

        //로그인한 회원 아이디 값 가져오기
        Intent intent = getIntent();
        String mId = intent.getStringExtra("mId");
        String cId = intent.getStringExtra("cId");

        //외부 레이아웃 파일 설정
        // 어디에 분리시켜놓은 layout 파일을 넣을지 결정!
        LinearLayout layout1 = findViewById(R.id.home);
        LinearLayout layout2 = findViewById(R.id.search);
        LinearLayout layout3 = findViewById(R.id.chat);
        LinearLayout layout4 = findViewById(R.id.mypage);




        View view1 = View.inflate(Footer.this, R.layout.mypage, null);
        layout4.addView(view1);

        // 탭호스트
        TabHost tabHost = findViewById(R.id.tabhost);
        tabHost.setup();

        //이미지뷰
        //1
        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.drawable.home);
        iv1.setLayoutParams(new ViewGroup.LayoutParams(370, 270));
        iv1.setPadding(50, 15, 80, 50);

        //2
        ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.drawable.search);
        iv2.setLayoutParams(new ViewGroup.LayoutParams(370, 270));
        iv2.setPadding(50, 15, 80, 50);
        //3
        ImageView iv3 = new ImageView(this);
        iv3.setImageResource(R.drawable.chat);
        iv3.setLayoutParams(new ViewGroup.LayoutParams(370, 270));
        iv3.setPadding(50, 15, 80, 50);
        //4
        ImageView iv4 = new ImageView(this);
        iv4.setImageResource(R.drawable.mypage);
        iv4.setLayoutParams(new ViewGroup.LayoutParams(370, 270));
        iv4.setPadding(50, 15, 80, 50);

        //각탭별 설정
        TabHost.TabSpec tabSpecHome = tabHost.newTabSpec("home").setIndicator(iv1);
        tabSpecHome.setContent(R.id.home);
        tabHost.addTab(tabSpecHome);

        TabHost.TabSpec tabSpecSearch = tabHost.newTabSpec("search").setIndicator(iv2);
        tabSpecSearch.setContent(R.id.search);
        tabHost.addTab(tabSpecSearch);

        TabHost.TabSpec tabSpecChat = tabHost.newTabSpec("chat").setIndicator(iv3);
        tabSpecChat.setContent(R.id.chat);
        tabHost.addTab(tabSpecChat);

        TabHost.TabSpec tabSpecMypage = tabHost.newTabSpec("mypage").setIndicator(iv4);
        tabSpecMypage.setContent(R.id.mypage);
        tabHost.addTab(tabSpecMypage);

        //setText로 웰컴메시지 띄우기
        TextView msg = layout4.findViewById(R.id.welcome);
        if (mId == null) {
            msg.setText(cId + "님 환영합니다");
        } else {
            msg.setText(mId + "님 환영합니다 ! ");
        }

        tabHost.setCurrentTab(0);

        Button moveInfo = findViewById(R.id.moveInfo);
        moveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Footer.this, Member_info.class);
                if (mId == null) {
                    intent.putExtra("cId", cId);
                } else {
                    intent.putExtra("mId", mId);
                }
                startActivity(intent);
            }
        });


        int[] instrument = {R.drawable.drum,
                            R.drawable.sing,
                            R.drawable.guitar,
                            R.drawable.piano
        };
        // 이미지뷰 가져오기
        circleImageView = layout4.findViewById(R.id.circleImageView);
        // 가져온 xml을 객체화
        View instrumentView = View.inflate(Footer.this, R.layout.instrument,null);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Footer.this);
                builder.setTitle("이미지를 선택하세요");
                ImageView drum = instrumentView.findViewById(R.id.imgDrum);
                ImageView sing = instrumentView.findViewById(R.id.imgSing);
                ImageView guitar = instrumentView.findViewById(R.id.imgGuitar);
                ImageView piano = instrumentView.findViewById(R.id.imgPiano);

                drum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicTool = 0;
                    }
                });
                sing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicTool = 1;
                    }
                });
                guitar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicTool = 2;
                    }
                });
                piano.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        musicTool = 3;
                    }
                });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 셋이미지
                        circleImageView.setImageResource(instrument[musicTool]);
                if (instrumentView.getParent() != null) {
                    ((ViewGroup) instrumentView.getParent()).removeView(instrumentView);
                }
                    }
                });
                builder.setView(instrumentView);
                builder.show();
            }
        });
        //로그아웃 처리
        Button btnLogout = layout4.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIdCheck = mId;
                cIdCheck = cId;
                if (mIdCheck == null) {
                    cIdCheck = null;
                    Intent logout = new Intent(Footer.this, Member_login.class);
                    startActivity(logout);
                } else {
                    mIdCheck = null;
                    Intent logout = new Intent(Footer.this, Member_login.class);
                    startActivity(logout);
                }
            }
        });


    } //oncreate
} // class

