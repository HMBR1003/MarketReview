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

public class ColSelectActivity extends AppCompatActivity {


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
        binding.toolBar.setTitle("경기장 선택");
        binding.toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolBar);
        intent = getIntent();
        if (!intent.getBooleanExtra("isFirst", true)) {
            //뒤로가기 버튼 만들기
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //경기장 선택 완료 시 동작 버튼
//        binding.selectFinish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                setResult(RESULT_OK);
//                finish();
//            }
//        });

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        int rowWidth = (mMetrics.widthPixels) / 3;
        binding.col1.setLayoutParams(new TableRow.LayoutParams(rowWidth, rowWidth));
        binding.col2.setLayoutParams(new TableRow.LayoutParams(rowWidth, rowWidth));
        binding.col3.setLayoutParams(new TableRow.LayoutParams(rowWidth, rowWidth));
        binding.col4.setLayoutParams(new TableRow.LayoutParams(rowWidth, rowWidth));
        binding.col5.setLayoutParams(new TableRow.LayoutParams(rowWidth, rowWidth));
        binding.col6.setLayoutParams(new TableRow.LayoutParams(rowWidth, rowWidth));
        binding.col7.setLayoutParams(new TableRow.LayoutParams(rowWidth, rowWidth));
        binding.col8.setLayoutParams(new TableRow.LayoutParams(rowWidth, rowWidth));
        binding.col9.setLayoutParams(new TableRow.LayoutParams(rowWidth, rowWidth));
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
                binding.jamsilText.setTextColor(Color.BLACK);

                binding.skydom.setBackgroundColor(Color.rgb(255, 255, 255));
                binding.skydomText.setTextColor(Color.BLACK);

                binding.munhak.setBackgroundColor(Color.rgb(255, 255, 255));
                binding.munhakText.setTextColor(Color.BLACK);

                binding.eagles.setBackgroundColor(Color.rgb(255, 255, 255));
                binding.eaglesText.setTextColor(Color.BLACK);

                binding.lions.setBackgroundColor(Color.rgb(255, 255, 255));
                binding.lionsText.setTextColor(Color.BLACK);

                binding.champions.setBackgroundColor(Color.rgb(255, 255, 255));
                binding.championsText.setTextColor(Color.BLACK);

                binding.giants.setBackgroundColor(Color.rgb(255, 255, 255));
                binding.giantsText.setTextColor(Color.BLACK);

                binding.wizpark.setBackgroundColor(Color.rgb(255, 255, 255));
                binding.wizparkText.setTextColor(Color.BLACK);

                binding.masan.setBackgroundColor(Color.rgb(255, 255, 255));
                binding.masanText.setTextColor(Color.BLACK);

                v.setBackgroundColor(getApplication().getResources().getColor(R.color.colorPrimary));
                switch (v.getId()){
                    case R.id.jamsil:
                        checkedItem = 0;
                        binding.jamsilText.setTextColor(Color.WHITE);
                        break;
                    case R.id.skydom:
                        checkedItem = 1;
                        binding.skydomText.setTextColor(Color.WHITE);
                        break;
                    case R.id.munhak:
                        checkedItem = 2;
                        binding.munhakText.setTextColor(Color.WHITE);
                        break;
                    case R.id.eagles:
                        checkedItem = 3;
                        binding.eaglesText.setTextColor(Color.WHITE);
                        break;
                    case R.id.lions:
                        checkedItem = 4;
                        binding.lionsText.setTextColor(Color.WHITE);
                        break;
                    case R.id.champions:
                        checkedItem = 5;
                        binding.championsText.setTextColor(Color.WHITE);
                        break;
                    case R.id.giants:
                        checkedItem = 6;
                        binding.giantsText.setTextColor(Color.WHITE);
                        break;
                    case R.id.wizpark:
                        checkedItem = 7;
                        binding.wizparkText.setTextColor(Color.WHITE);
                        break;
                    case R.id.masan:
                        checkedItem = 8;
                        binding.masanText.setTextColor(Color.WHITE);
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


//        // 커스텀 아답타 생성
//        adapter = new GridAdapter(
//                getApplicationContext(),
//                R.layout.item_change_col,       // GridView 항목의 레이아웃 row.xml
//                img);
//        binding.gridView.setAdapter(adapter);
//        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                checkedItem = position;
//                if(oldPosition < parent.getChildCount()) {
//                    parent.getChildAt(oldPosition).setBackgroundColor(Color.rgb(255, 255, 255));
//                    ((TextView) parent.getChildAt(oldPosition).findViewById(R.id.colText)).setTextColor(Color.BLACK);
//                }
//                oldPosition=position;
//                view.setBackgroundColor(getApplication().getResources().getColor(R.color.colorPrimary));
//                ((TextView)view.findViewById(R.id.colText)).setTextColor(Color.WHITE);
//            }
//        });

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

//    class GridAdapter extends BaseAdapter {
//        Context context;
//        int layout;
//        int img[];
//        LayoutInflater inf;
//        View view[] = new View[9];
//
//        public GridAdapter(Context context, int layout, int[] img) {
//            this.context = context;
//            this.layout = layout;
//            this.img = img;
//            inf = (LayoutInflater) context.getSystemService
//                    (Context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        @Override
//        public int getCount() {
//            return 9;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return view[position];
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            int rowWidth = (mMetrics.widthPixels) / 3;
//
//            if (convertView == null) {
//                view[position] = convertView;
//                view[position] = inf.inflate(layout, null);
//                ImageView iv = (ImageView) view[position].findViewById(R.id.colImage);
//                TextView tv = (TextView) view[position].findViewById(R.id.colText);
//                Glide.with(context)
//                        .load(img[position])
//                        .into(iv);
//                tv.setText(colName[position]);
//                view[position].setLayoutParams(new GridView.LayoutParams(rowWidth, rowWidth));
//            }
//            return view[position];
//        }
//    }

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
