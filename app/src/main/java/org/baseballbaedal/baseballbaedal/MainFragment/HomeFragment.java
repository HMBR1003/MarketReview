package org.baseballbaedal.baseballbaedal.MainFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import org.baseballbaedal.baseballbaedal.BusProvider;
import org.baseballbaedal.baseballbaedal.HeightEvent;
import org.baseballbaedal.baseballbaedal.MainActivity;
import org.baseballbaedal.baseballbaedal.Order.OrderActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.Test.DBTestActivity;
import org.baseballbaedal.baseballbaedal.databinding.FragmentHomeBinding;

/**
 * Created by Administrator on 2017-05-08.
 */

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    ViewPager viewPager;
    MainActivity mainActivity;
    double height;
    double width;
    int deliveryImageWidth;
    int deliveryImageHeight;
    int takeoutImageWidth;
    int takeoutimageHeight;
    int weatherImageWidth;
    int weatherImageHeight;
    int bottom;
    int leftRight;
    LinearLayout.LayoutParams layoutParams;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        mainActivity = (MainActivity) getActivity();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    public void init() {

//        binding.deliveryButton.setLayoutParams(new LinearLayout.LayoutParams(deliveryImageWidth,weatherImageHeight));
//        binding.weatherInfoButton.setLayoutParams(new LinearLayout.LayoutParams(weatherImageWidth,weatherImageHeight));
//        binding.takeoutButton.setLayoutParams(new LinearLayout.LayoutParams(takeoutImageWidth,takeoutimageHeight));

        layoutParams = new LinearLayout.LayoutParams(deliveryImageWidth,deliveryImageHeight);
        layoutParams.bottomMargin = bottom;
        binding.deliveryButton.setLayoutParams(layoutParams);


        layoutParams =new LinearLayout.LayoutParams(weatherImageWidth,weatherImageHeight);
        layoutParams.leftMargin = leftRight;
        binding.weatherInfoButton.setLayoutParams(layoutParams);

        layoutParams = new LinearLayout.LayoutParams(takeoutImageWidth,takeoutimageHeight);
        layoutParams.rightMargin = leftRight;
        binding.takeoutButton.setLayoutParams(layoutParams);

    }

    @Subscribe
    public void onPushEvent(HeightEvent heightEvent) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        deliveryImageHeight = (int)((height/1920)*660);
        deliveryImageWidth = (int)((width/1080)*965);
        takeoutimageHeight = (int)((height/1920)*650);
        takeoutImageWidth = (int)((width/1080)*539);
        weatherImageHeight = (int)((height/1920)*650);
        weatherImageWidth = (int)((width/1080)*398);
        bottom = (int)((height/1920)*28);
        leftRight = (int)((width/1080)*14);
        init();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentHomeBinding.bind(getView());

        Glide.with(this)
                .load(R.drawable.delivery)
                .into(binding.deliveryButton);
        Glide.with(this)
                .load(R.drawable.takeout)
                .into(binding.takeoutButton);
        Glide.with(this)
                .load(R.drawable.weather_info)
                .into(binding.weatherInfoButton);

        binding.deliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager = (ViewPager) mainActivity.findViewById(R.id.vp_horizontal_ntb);
                viewPager.setCurrentItem(1);
            }
        });
        binding.takeoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager = (ViewPager) mainActivity.findViewById(R.id.vp_horizontal_ntb);
                viewPager.setCurrentItem(2);
            }
        });
        binding.weatherInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager = (ViewPager) mainActivity.findViewById(R.id.vp_horizontal_ntb);
                viewPager.setCurrentItem(3);
            }
        });
    }
}
