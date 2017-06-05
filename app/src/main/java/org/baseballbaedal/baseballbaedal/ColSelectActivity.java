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
import android.widget.TextView;
import android.widget.Toast;

import org.baseballbaedal.baseballbaedal.databinding.ActivityColSelectBinding;

public class ColSelectActivity extends AppCompatActivity {


    SharedPreferences pref;
    GridAdapter adapter;
    int checkedItem=-1;
    long backTime;
    ActivityColSelectBinding binding;
    int img[] = {
            R.drawable.jamsil,R.drawable.jamsil,R.drawable.jamsil,
            R.drawable.jamsil,R.drawable.jamsil,R.drawable.jamsil,
            R.drawable.jamsil,R.drawable.jamsil,R.drawable.jamsil
    };
    String colName[] = {
            "잠실 야구장(두산,LG)","고척 스카이돔(넥센)","SK 행복드림구장",
            "한화 이글스파크","삼성 라이온즈파크","기아 챔피언스필드",
            "사직 야구장(롯데)","KT 위즈파크","마산 야구장(NC)"
    };
    LinearLayout linearLayout[];
    Intent intent;
    int oldPosition=0;

    DisplayMetrics mMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_col_select);

        //타이틀 설정
        binding.toolBar.setTitle("경기장 선택");
        binding.toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolBar);
        intent = getIntent();
        if(!intent.getBooleanExtra("isFirst",true)){
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

        // 커스텀 아답타 생성
        adapter = new GridAdapter (
                getApplicationContext(),
                R.layout.item_change_col,       // GridView 항목의 레이아웃 row.xml
                img);
        binding.gridView.setAdapter(adapter);
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkedItem = position;
                if(oldPosition < parent.getChildCount())
                    parent.getChildAt(oldPosition).setBackgroundColor(Color.rgb(255,255,255));
                oldPosition=position;
                view.setBackgroundColor(getApplication().getResources().getColor(R.color.colorPrimary));
            }
        });

        binding.selectFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedItem==-1){
                    Toast.makeText(ColSelectActivity.this, "경기장을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                pref = getSharedPreferences("selectedCol", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("selectedCol", checkedItem);
                editor.commit();
                setResult(RESULT_OK);
                Toast.makeText(ColSelectActivity.this, colName[checkedItem]+"을(를) 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    class GridAdapter extends BaseAdapter{
        Context context;
        int layout;
        int img[];
        LayoutInflater inf;
        View view[]=new View[9];

        public GridAdapter(Context context, int layout, int[] img) {
            this.context = context;
            this.layout = layout;
            this.img = img;
            inf = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return view[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int rowWidth = (mMetrics.widthPixels) / 3;

            if(convertView==null) {
                view[position] = convertView;
                view[position] = inf.inflate(layout, null);
                ImageView iv = (ImageView) view[position].findViewById(R.id.colImage);
                TextView tv = (TextView) view[position].findViewById(R.id.colText);
                iv.setImageResource(img[position]);
                tv.setText(colName[position]);
                view[position].setLayoutParams(new GridView.LayoutParams(rowWidth, rowWidth));
            }
            return view[position];
        }
    }

    @Override
    public void onBackPressed() {
        if(!intent.getBooleanExtra("isFirst",true)){
            super.onBackPressed();
        }
        else{
            if(System.currentTimeMillis()-backTime<2000){
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
