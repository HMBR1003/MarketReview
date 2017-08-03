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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.baseballbaedal.baseballbaedal.MainActivity;
import org.baseballbaedal.baseballbaedal.MainFragment.Weather.WeatherActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.FragmentWeatherBinding;

/**
 * Created by Administrator on 2017-06-17-017.
 */

public class WeatherFragment extends Fragment {

    FragmentWeatherBinding binding;
    //    GridAdapter adapter;
    DisplayMetrics mMetrics;

    int img[] = {
            R.drawable.jamsil,R.drawable.skydom,R.drawable.munhak,
            R.drawable.eagles,R.drawable.lions,R.drawable.champions,
            R.drawable.giants,R.drawable.wizpark,R.drawable.masan
    };
    String colName[] = {
            "잠실 야구장(두산,LG)", "고척 스카이돔(넥센)", "SK 행복드림구장",
            "한화 이글스파크", "삼성 라이온즈파크", "기아 챔피언스필드",
            "사직 야구장(롯데)", "KT 위즈파크", "마산 야구장(NC)"
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        Glide.with(this)
                .load(R.drawable.weather_tab)
                .into(binding.weatherTab);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = null;
                String colname = null;
                switch (v.getId()) {
                    case R.id.jamsil:
                        city = "서울송파구잠실동";
                        colname = "잠실 야구장";
                        break;
                    case R.id.skydom:
                        city = "서울구로구고척동";
                        colname = "고척 스카이돔";
                        break;
                    case R.id.munhak:
                        city = "인천남구문학동";
                        colname = "SK행복드림구장";
                        break;
                    case R.id.eagles:
                        city = "대전중구부사동";
                        colname = "한화 이글스파크";
                        break;
                    case R.id.lions:
                        city = "대구수성구연호동";
                        colname = "삼성 라이온즈파크";
                        break;
                    case  R.id.champions:
                        city = "광주북구임동";
                        colname = "기아 챔피언스필드";
                        break;
                    case R.id.giants:
                        city = "부산동래구사직동";
                        colname = "사직 야구장";
                        break;
                    case  R.id.wizpark:
                        city = "수원장안구조원동";
                        colname = "KT 위즈파크";
                        break;
                    case R.id.masan:
                        city = "창원마산회원구양덕동";
                        colname = "마산 야구장";
                        break;
                }
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                intent.putExtra("city", city);
                intent.putExtra("colname", colname);
                startActivity(intent);
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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }
}