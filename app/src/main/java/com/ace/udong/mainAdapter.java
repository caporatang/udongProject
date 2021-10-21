package com.ace.udong;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class mainAdapter extends BaseAdapter {

    Context context;

    mainAdapter(Context c) {
        context = c;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        // 반복되는 View 객체 생성 해줘야함. --> 추후 DB 내용 넣어야함.
        // 디자인 뷰 inflate
        View listmain = View.inflate(context, R.layout.listmain, null);

        ImageView imageList = listmain.findViewById(R.id.imageList);      //이미지
        TextView textTitle = listmain.findViewById(R.id.textTitle);       //제목
        TextView textLocation = listmain.findViewById(R.id.textLocation); //지역
        TextView starText = listmain.findViewById(R.id.starText);         //평점

        // set 부분
//        imageList.setImageResource(productImg[index]);
//        textTitle.setText(title[index]);
//        textLocation.setText(location[index]);
//        starText.setText(star[index]);


        return listmain;
    } // getView
}//class
