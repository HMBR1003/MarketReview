package org.baseballbaedal.baseballbaedal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.baseballbaedal.baseballbaedal.databinding.ActivityColSelectBinding;

public class ColSelectActivity extends NewActivity {


    SharedPreferences pref;
    //    GridAdapter adapter;
    int checkedItem = -1;
    long backTime;
    ActivityColSelectBinding binding;
    int img[] = {
            R.drawable.jamsil, R.drawable.skydom, R.drawable.munhak,
            R.drawable.eagles, R.drawable.lions, R.drawable.champions,
            R.drawable.giants, R.drawable.wizpark, R.drawable.masan
    };
    String colName[] = {
            "잠실 야구장(두산,LG)", "고척 스카이돔(넥센)", "SK 행복드림구장",
            "한화 이글스파크", "삼성 라이온즈파크", "기아 챔피언스필드",
            "사직 야구장(롯데)", "KT 위즈파크", "마산 야구장(NC)"
    };

    Intent intent;
    DisplayMetrics mMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_col_select);

        //타이틀 설정
        intent = getIntent();
        binding.container.addView(getToolbar("경기장 선택",!intent.getBooleanExtra("isFirst", true)),0);


        Glide.with(this)
                .load(img[0])
                .into(binding.col1);
        Glide.with(this)
                .load(img[1])
                .into(binding.col2);
        Glide.with(this)
                .load(img[2])
                .into(binding.col3);
        Glide.with(this)
                .load(img[3])
                .into(binding.col4);
        Glide.with(this)
                .load(img[4])
                .into(binding.col5);
        Glide.with(this)
                .load(img[5])
                .into(binding.col6);
        Glide.with(this)
                .load(img[6])
                .into(binding.col7);
        Glide.with(this)
                .load(img[7])
                .into(binding.col8);
        Glide.with(this)
                .load(img[8])
                .into(binding.col9);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.jamsil.setBackgroundColor(Color.rgb(255, 255, 255));

                binding.skydom.setBackgroundColor(Color.rgb(255, 255, 255));

                binding.munhak.setBackgroundColor(Color.rgb(255, 255, 255));

                binding.eagles.setBackgroundColor(Color.rgb(255, 255, 255));

                binding.lions.setBackgroundColor(Color.rgb(255, 255, 255));

                binding.champions.setBackgroundColor(Color.rgb(255, 255, 255));

                binding.giants.setBackgroundColor(Color.rgb(255, 255, 255));

                binding.wizpark.setBackgroundColor(Color.rgb(255, 255, 255));

                binding.masan.setBackgroundColor(Color.rgb(255, 255, 255));

                v.setBackgroundColor(getApplication().getResources().getColor(R.color.colorPrimary));
                switch (v.getId()) {
                    case R.id.jamsil:
                        checkedItem = 0;
                        break;
                    case R.id.skydom:
                        checkedItem = 1;
                        break;
                    case R.id.munhak:
                        checkedItem = 2;
                        break;
                    case R.id.eagles:
                        checkedItem = 3;
                        break;
                    case R.id.lions:
                        checkedItem = 4;
                        break;
                    case R.id.champions:
                        checkedItem = 5;
                        break;
                    case R.id.giants:
                        checkedItem = 6;
                        break;
                    case R.id.wizpark:
                        checkedItem = 7;
                        break;
                    case R.id.masan:
                        checkedItem = 8;
                        break;
                }
            }
        };
        binding.jamsil.setOnClickListener(listener);
        binding.skydom.setOnClickListener(listener);
        binding.munhak.setOnClickListener(listener);
        binding.eagles.setOnClickListener(listener);
        binding.lions.setOnClickListener(listener);
        binding.champions.setOnClickListener(listener);
        binding.giants.setOnClickListener(listener);
        binding.wizpark.setOnClickListener(listener);
        binding.masan.setOnClickListener(listener);


        binding.selectFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedItem == -1) {
                    Toast.makeText(ColSelectActivity.this, "경기장을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                pref = getSharedPreferences("selectedCol", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("selectedCol", checkedItem);
                editor.commit();
                setResult(RESULT_OK);
                Toast.makeText(ColSelectActivity.this, colName[checkedItem] + "을(를) 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (!intent.getBooleanExtra("isFirst", true)) {
            super.onBackPressed();
        } else {
            if (System.currentTimeMillis() - backTime < 2000) {
                ActivityCompat.finishAffinity(this);
                System.runFinalizersOnExit(true);
                System.exit(0);
            }
            Toast.makeText(this, "뒤로가기 키를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            backTime = System.currentTimeMillis();

        }
    }
}
