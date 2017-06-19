package org.baseballbaedal.baseballbaedal;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.baseballbaedal.baseballbaedal.databinding.ActivityMarketDetailViewBinding;

public class MarketDetailViewActivity extends AppCompatActivity {

    ActivityMarketDetailViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_market_detail_view);


    }
}
