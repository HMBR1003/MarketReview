package org.baseballbaedal.baseballbaedal.BusinessMan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityLogoViewBinding;

import java.io.FileNotFoundException;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class LogoViewActivity extends AppCompatActivity {

    ActivityLogoViewBinding binding;

    PhotoViewAttacher attacher;
    Uri uri;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_logo_view);
        dialog=new ProgressDialog(LogoViewActivity.this);

        //타이틀 설정
        binding.toolBar.setTitle("이미지 확대 보기");
        binding.toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolBar);
        //뒤로가기 버튼 만들기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        uri = intent.getParcelableExtra("imageUri");
        if(uri!=null) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                binding.imageView.setImageBitmap(bm);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "이미지 불러오기 오류", Toast.LENGTH_SHORT).show();
        }
        attacher = new PhotoViewAttacher(binding.imageView);
    }
    //상단 뒤로가기 버튼 기능 설정
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
