package org.baseballbaedal.baseballbaedal.Order;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.bumptech.glide.Glide;


import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityImageZoomBinding;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageZoomActivity extends AppCompatActivity {

    PhotoViewAttacher attacher;
    ActivityImageZoomBinding imageZoomBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageZoomBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_zoom);

        Intent intent = getIntent();
        String stadium = intent.getStringExtra("stadium");

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        switch(stadium){
            case "잠실":
                Glide.with(this)
                        .load(R.drawable.jammain)
                        .into(imageZoomBinding.imageView);
                break;
            //다른야구장 좌석배치 추가할 경우 야구장이미지 추가
        }

        attacher = new PhotoViewAttacher(imageZoomBinding.imageView);
    }
}
