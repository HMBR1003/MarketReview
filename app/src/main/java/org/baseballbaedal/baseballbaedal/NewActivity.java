package org.baseballbaedal.baseballbaedal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Administrator on 2017-07-31-031.
 */

public class NewActivity extends BaseActivity {

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
}
