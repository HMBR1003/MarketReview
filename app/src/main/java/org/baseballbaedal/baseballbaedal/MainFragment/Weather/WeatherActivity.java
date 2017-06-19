package org.baseballbaedal.baseballbaedal.MainFragment.Weather;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.baseballbaedal.baseballbaedal.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {

    WeatherListViewAdapter adapter;
    DatabaseReference fireDB;
    Weather we;
    Dust dust;
    ValueEventListener listener;
    ProgressDialog progress;
    android.support.v7.widget.Toolbar toolbar;

    String city;
    String colname;

    TextView dateText, amTempText, amSkyText, pmTempText, pmSkyText, dustGradeText, dustValueText;
    ImageView pmSkyImage, amSkyImage;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        intent = getIntent();
        city = intent.getStringExtra("city");
        colname = intent.getStringExtra("colname");

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.weatherToolBar);
        //타이틀 설정
        toolbar.setTitle(colname+" 날씨");
        toolbar.setTitleTextColor(Color.WHITE);

        //뒤로가기 버튼 만들기
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String topAddress="";
        if(city.equals("서울송파구잠실동")) {
            topAddress = "서울 송파구 잠실동";
        }
        else if(city.equals("서울구로구고척동")){
            topAddress = "서울 구로구 고척동";
        }
        else if(city.equals("인천남구문학동")){
            topAddress = "인천 남구 문학동";
        }
        else if(city.equals("대전중구부사동")){
            topAddress = "대전 중구 부사동";
        }
        else if(city.equals("대구수성구연호동")){
            topAddress = "대구 수성구 연호동";
        }
        else if(city.equals("광주북구임동")){
            topAddress = "광주 북구 임동";
        }
        else if(city.equals("부산동래구사직동")){
            topAddress = "부산 동래구 사직동";
        }
        else if(city.equals("수원장안구조원동")){
            topAddress = "수원 장안구 조원동";
        }
        else if(city.equals("창원마산회원구양덕동")){
            topAddress = "창원 마산회원구 양덕동";
        }

        //상단에 주소 출력
        ((TextView)findViewById(R.id.weatherAddress)).setText(topAddress+"의 이번 주 날씨");

        ListView listView = (ListView)findViewById(R.id.listview1);
        dateText = (TextView)findViewById(R.id.dateText);
        amTempText = (TextView)findViewById(R.id.amTempText);
        amSkyImage = (ImageView)findViewById(R.id.amSkyImage);
        amSkyText = (TextView)findViewById(R.id.amSkyText);
        pmTempText = (TextView)findViewById(R.id.pmTempText);
        pmSkyImage = (ImageView)findViewById(R.id.pmSkyImage);
        pmSkyText = (TextView)findViewById(R.id.pmSkyText);
        dustGradeText = (TextView)findViewById(R.id.dustGradeText);
        dustValueText = (TextView)findViewById(R.id.dustValueText);
        fireDB = FirebaseDatabase.getInstance().getReference();
        we = new Weather();
        adapter = new WeatherListViewAdapter();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                int count = 1;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    we = data.getValue(Weather.class);
                    dust = data.getValue(Dust.class);
                    if(count == 1){
                        dateText.setText(we.date + "\n(오늘)");
                        amTempText.setText(we.am_temp+"°C");
                        amSkyImage.setImageDrawable(getWeatherImage(we.am_sky_code));
                        amSkyText.setText(we.am_sky_name);
                        pmTempText.setText(we.pm_temp+"°C");
                        pmSkyImage.setImageDrawable(getWeatherImage(we.pm_sky_code));
                        pmSkyText.setText(we.pm_sky_name);
                    }
                    if(dust.grade != null){
                        dustGradeText.setText(" "+dust.grade);
                        dustValueText.setText(" "+dust.value);
                    }
                    if(we.date != null && count != 1) {
                        adapter.addItem(we.date, we.am_temp+"°C", getWeatherImage(we.am_sky_code), we.am_sky_name, we.pm_temp+"°C",
                                getWeatherImage(we.pm_sky_code), we.pm_sky_name);
                    }
                    count++;
                }
                adapter.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        listView.setAdapter(adapter);
    }

    private Drawable getWeatherImage(String code) {
        switch(code){
            case "SKY_S00":
                return getResources().getDrawable(R.drawable._s00);
            case "SKY_S01":
                return getResources().getDrawable(R.drawable._s01);
            case "SKY_S02":
                return getResources().getDrawable(R.drawable._s02);
            case "SKY_S03":
                return getResources().getDrawable(R.drawable._s03);
            case "SKY_S04":
                return getResources().getDrawable(R.drawable._s04);
            case "SKY_S05":
                return getResources().getDrawable(R.drawable._s05);
            case "SKY_S06":
                return getResources().getDrawable(R.drawable._s06);
            case "SKY_S07":
                return getResources().getDrawable(R.drawable._s07);
            case "SKY_S08":
                return getResources().getDrawable(R.drawable._s08);
            case "SKY_S09":
                return getResources().getDrawable(R.drawable._s09);
            case "SKY_S10":
                return getResources().getDrawable(R.drawable._s10);
            case "SKY_S11":
                return getResources().getDrawable(R.drawable._s11);
            case "SKY_S12":
                return getResources().getDrawable(R.drawable._s12);
            case "SKY_S13":
                return getResources().getDrawable(R.drawable._s13);
            case "SKY_S14":
                return getResources().getDrawable(R.drawable._s14);
            case "SKY_W00":
                return getResources().getDrawable(R.drawable._w00);
            case "SKY_W01":
                return getResources().getDrawable(R.drawable._w01);
            case "SKY_W02":
                return getResources().getDrawable(R.drawable._w02);
            case "SKY_W03":
                return getResources().getDrawable(R.drawable._w03);
            case "SKY_W04":
                return getResources().getDrawable(R.drawable._w04);
            case "SKY_W07":
                return getResources().getDrawable(R.drawable._w07);
            case "SKY_W09":
                return getResources().getDrawable(R.drawable._w09);
            case "SKY_W10":
                return getResources().getDrawable(R.drawable._w10);
            case "SKY_W11":
                return getResources().getDrawable(R.drawable._w11);
            case "SKY_W12":
                return getResources().getDrawable(R.drawable._w12);
            case "SKY_W13":
                return getResources().getDrawable(R.drawable._w13);
            default:
                return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progress = new ProgressDialog(WeatherActivity.this);
        progress.setProgress(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("데이터를 불러오는중입니다.");
        progress.setCancelable(false);
        progress.show();

        if(city!=null) {
            fireDB.child("Weather").child(city).addValueEventListener(listener);
        }
        else{
            progress.dismiss();
            Toast.makeText(this, "데이터가 잘못 넘어왔습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireDB.removeEventListener(listener);
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