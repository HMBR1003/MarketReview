package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.Menu;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityMenuInfoBinding;

public class MenuInfoActivity extends AppCompatActivity {

    ActivityMenuInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_menu_info);
    }
}
