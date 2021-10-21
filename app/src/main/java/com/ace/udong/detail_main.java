package com.ace.udong;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class detail_main extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView dateText, contentResult, titleDetail, starDetail; // 날짜확인 텍스트, 내용결과 텍스트
    Spinner timeSp, peopleSp, roomSp; // 스피너
    String[] timedata, peopledata, roomdata ; // 스피너에 담을 데이터 객체
    View dialogView;    // 다이얼로그뷰 -> 캘린더뷰
    Button btnDate, btnFinish, btnStarUp; // 캘린더호출 버튼, 예약 버튼
    String dateTotal, people, time, date, room; // 예약으로 보낼 데이터 객체
    MainDB mainDB;
    String titleDB, roomDB, contentDB;
    ImageView imgDetail;
    int imgDB, recommendDB, rcd;
    int[] productImg = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
            R.drawable.img6,
            R.drawable.img7,
            R.drawable.img8,
            R.drawable.img9,
            R.drawable.img10,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_main);
        getSupportActionBar().setTitle("상세 페이지");
        // 객체
        timeSp = findViewById(R.id.timeSp);
        peopleSp = findViewById(R.id.peopleSp);
        roomSp = findViewById(R.id.roomSp);
        dateText = findViewById(R.id.dateText);
        starDetail = findViewById(R.id.starDetail);
        contentResult = findViewById(R.id.contentResult);
        btnDate = findViewById(R.id.btnDate);
        btnFinish = findViewById(R.id.btnFinish);
        btnStarUp = findViewById(R.id.btnStarUp);
        imgDetail = findViewById(R.id.imgDetail);
        titleDetail = findViewById(R.id.titleDetail);

        //스피너 객체
        timeSp.setOnItemSelectedListener(this);
        peopleSp.setOnItemSelectedListener(this);
        roomSp.setOnItemSelectedListener(this);
        //값 받아 옴
        Intent intent = getIntent();
        int result = intent.getIntExtra("id", 0);
        String mId = intent.getStringExtra("mId");
        Log.d("re", "결과 "+result);
// --------------------------------------------------------------------------------------------------------
        //DB
        mainDB = new MainDB(this);
        SQLiteDatabase sqlDB = mainDB.getWritableDatabase();
        String sql = "select * from place where postid = '" + result + "'";
        Cursor cursor = sqlDB.rawQuery(sql, null);
        while (cursor.moveToNext()){
            titleDB = cursor.getString(1);
            imgDB = cursor.getInt(4);
            recommendDB = cursor.getInt(5);
//            roomDB = cursor.getString(8);
            contentDB = cursor.getString(3);
        }
        cursor.close();

        titleDetail.setText(titleDB);
        contentResult.setText(contentDB);
        starDetail.setText("추천수 : " + recommendDB);
        imgDetail.setImageResource(productImg[imgDB]);
// --------------------------------------------------------------------------------------------------------
        // 날짜
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // 다이얼로그 객체 inflate
                // 인플레이션
                dialogView =(View)View.inflate(detail_main.this, R.layout.calender, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(detail_main.this);
                dialog.setTitle("날짜 예약");
                dialog.setIcon(R.drawable.ic_launcher_foreground);
                // 다이얼로그에 들어갈 view
                //date
                CalendarView cv = dialogView.findViewById(R.id.calendarView1); //캘린더뷰 객체
                // 캘린더뷰 값 변경 및 저장.
                cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        dateTotal = year+"년 " + (month + 1) + "월 " + dayOfMonth + "일";
                    }
                });
                // 다이얼로그 확인 버튼. -> 누를시 텍스트뷰에 선택한 데이트가 set
                dialog.setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateText.setText(dateTotal);
                    } // date CLick
                });// date
                dialog.setView(dialogView);
                dialog.show();
            }
        });// dialog

        // 예약하기 버튼
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), time + "\n" + people + "\n" + room + "\n" + dateTotal + "\n" + mId, Toast.LENGTH_SHORT).show();
                // 추후 다음 예약액티비티로 값 전달.

                // intent 사용 예정.

            }
        });

        // 추천 버튼
        btnStarUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 추천 업데이트
                int resultF = recommendDB + 1;
                SQLiteDatabase sqlDB = mainDB.getWritableDatabase();
                String sql = "update place set recommend = '" + resultF + "'where postid='"+result+"'";
                sqlDB.execSQL(sql);
                Toast.makeText(getApplicationContext(), "추천이 완료되었습니다!", Toast.LENGTH_SHORT).show();
            // ---------------------------------------------------------------------------------------------------------------------------
               // 추천수 setText
                SQLiteDatabase sqlDB2 = mainDB.getWritableDatabase();
               String sql2 =  "select recommend from place where postid = '" + result + "'";
               Cursor cursor = sqlDB2.rawQuery(sql2, null);
                cursor.moveToFirst();
                rcd = cursor.getInt(cursor.getColumnIndex("recommend"));
                    Log.d("re", "결과1234: " + rcd);
                starDetail.setText("추천수 : " + rcd);
                cursor.close();
                sqlDB2.close();
            }
        });
        // ---------------------------------------------------------------------------------------------------------------------------------------------
        // 스피너객체 + 어댑터 = 스피너 내용.
        // 시간 선택
        timedata = new String[]{"시간 선택","10 ~ 11","11 ~ 12","12 ~ 13","13 ~ 14","14 ~ 15","15 ~ 16","16 ~ 17","17 ~ 18","18 ~ 19","19 ~ 20","20 ~ 21","21 ~ 22"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, timedata);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSp.setAdapter(timeAdapter);
        // 인원수 선택
        peopledata = new String[]{"인원수 선택","1명","2명","3명","4명","5명","6명","7명 이상"};
        ArrayAdapter<String> peopleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, peopledata);
        peopleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        peopleSp.setAdapter(peopleAdapter);
        // 방선택
        roomdata = new String[]{"방 선택", "A Room", "B Room", "C Room", "D Room", "E Room"};
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, roomdata);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSp.setAdapter(roomAdapter);
    } //oncreate
    // --------------------------------------------------------------------------------------------------------
    // ----------------------------------------------------class-----------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // 스피너 오버라이드
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // 스피너 선택 케이스.
       // 어떤 스피너를 선택했는지 알기 위해서 switch 문 사용. -> 사용 안할시 혼동.
        switch (parent.getId()){

            case R.id.roomSp:
                room = parent.getItemAtPosition(position).toString();
                break;


            case  R.id.peopleSp:
                people = parent.getItemAtPosition(position).toString();
                break;

            case R.id.timeSp:
                time = parent.getItemAtPosition(position).toString();
                break;

        }//switch
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}//class