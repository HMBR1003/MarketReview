package org.baseballbaedal.baseballbaedal.MainFragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.baseballbaedal.baseballbaedal.MainActivity;
import org.baseballbaedal.baseballbaedal.MainFragment.Weather.WeatherActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.FragmentWeatherBinding;

/**
 * Created by Administrator on 2017-06-17-017.
 */

public class WeatherFragment extends Fragment{

    FragmentWeatherBinding binding;
    GridAdapter adapter;
    DisplayMetrics mMetrics;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        // 커스텀 어댑터 생성
        adapter = new GridAdapter (
                getActivity(),
                R.layout.item_change_col,       // GridView 항목의 레이아웃 row.xml
                img);
        binding.gridView.setAdapter(adapter);
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = null;
                String colname = null;
                //날씨 창 띄우기 동작
                switch (position) {
                    case 0:
                        city = "서울송파구잠실동";
                        colname = "잠실 야구장";
                        break;
                    case 1:
                        city = "서울구로구고척동";
                        colname = "고척 스카이돔";
                        break;
                    case 2:
                        city = "인천남구문학동";
                        colname = "SK행복드림구장";
                        break;
                    case 3:
                        city = "대전중구부사동";
                        colname = "한화 이글스파크";
                        break;
                    case 4:
                        city = "대구수성구연호동";
                        colname = "삼성 라이온즈파크";
                        break;
                    case 5:
                        city = "광주북구임동";
                        colname = "기아 챔피언스필드";
                        break;
                    case 6:
                        city = "부산동래구사직동";
                        colname = "사직 야구장";
                        break;
                    case 7:
                        city = "수원장안구조원동";
                        colname = "KT 위즈파크";
                        break;
                    case 8:
                        city = "창원마산회원구양덕동";
                        colname = "마산 야구장";
                        break;
                }
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                intent.putExtra("city", city);
                intent.putExtra("colname",colname);
                startActivity(intent);
            }
        });
    }
    class GridAdapter extends BaseAdapter {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_weather,container,false);
        View rootView = binding.getRoot();
        return rootView;
    }
}