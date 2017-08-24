package org.baseballbaedal.baseballbaedal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ssomai.android.scalablelayout.ScalableLayout;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2017-07-31-031.
 */

public class NewActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setToolbar(android.support.v7.widget.Toolbar toolbar, String title, int color,boolean isBack){
        //타이틀 설정
        toolbar.setTitle(title);
        //타이틀 색 설정
        toolbar.setTitleTextColor(color);
        //툴바 붙이기
        setSupportActionBar(toolbar);
        //뒤로가기 버튼 만들기
        if(isBack) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public View getToolbar(String title, boolean isBack){
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_actionbar, null);

        //타이틀 설정
        ((TextView)view.findViewById(R.id.title)).setText(title);

        //뒤로가기 버튼 만들기
        if(isBack) {
            (view.findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        else{
            (view.findViewById(R.id.btnBack)).setVisibility(View.GONE);
            ((ScalableLayout)view.findViewById(R.id.titleScal)).moveChildView((view.findViewById(R.id.title)),50,0);
        }
        return view;
    }

    //뒤로가기 버튼 기능 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    protected void attachBaseContext(Context newBase) {
//        //글꼴 설정하기 위한 구문
//        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
//    }
}
