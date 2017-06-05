package org.baseballbaedal.baseballbaedal.BusinessMan;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityMenuManageBinding;

public class MenuManageActivity extends AppCompatActivity {

    ActivityMenuManageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_manage);

        //타이틀 설정
        binding.toolBar.setTitle("메뉴 관리");
        binding.toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolBar);
        //뒤로가기 버튼 만들기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.addMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MenuAddActivity.class);
                startActivity(intent);
            }
        });
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
