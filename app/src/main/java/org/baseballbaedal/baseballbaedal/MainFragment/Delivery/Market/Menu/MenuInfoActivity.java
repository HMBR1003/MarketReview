package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.Menu;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityMenuInfoBinding;

public class MenuInfoActivity extends NewActivity {

    ActivityMenuInfoBinding binding;

    String aTime;
    String menuKey;
    String marketName;
    String marketId;
    String menuName;
    String menuExplain;
    String menuPrice;
    String option1Name;
    String option1Price;
    String option2Name;
    String option2Price;
    String option3Name;
    String option3Price;
    String option4Name;
    String option4Price;
    String option5Name;
    String option5Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_menu_info);

        Intent intent = getIntent();
        aTime = intent.getStringExtra("aTime");
        menuKey = intent.getStringExtra("menuKey");
        marketName = intent.getStringExtra("marketName");
        marketId = intent.getStringExtra("marketId");
        menuName = intent.getStringExtra("menuName");
        menuExplain = intent.getStringExtra("menuExplain");
        menuPrice = intent.getStringExtra("menuPrice");
        option1Name = intent.getStringExtra("option1Name");
        option1Price = intent.getStringExtra("option1Price");
        option2Name = intent.getStringExtra("option2Name");
        option2Price = intent.getStringExtra("option2Price");
        option3Name = intent.getStringExtra("option3Name");
        option3Price = intent.getStringExtra("option3Price");
        option4Name = intent.getStringExtra("option4Name");
        option4Price = intent.getStringExtra("option4Price");
        option5Name = intent.getStringExtra("option5Name");
        option5Price = intent.getStringExtra("option5Price");


        binding.container.addView(getToolbar(marketName,true),0);
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(marketId).child("menu").child(menuKey + ".jpg");
        try {
            Glide
                    .with(MenuInfoActivity.this)
                    .using(new FirebaseImageLoader())
                    .load(ref)
                    .signature(new StringSignature(aTime)) //이미지저장시간
                    .placeholder(R.drawable.jamsil)
                    .thumbnail(0.1f)
                    .crossFade()
                    .into(binding.menuImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.menuName.setText(menuName);
        binding.menuExplain.setText(menuExplain);
        binding.menuPrice.setText(menuPrice);


        if(option1Name!=null){
            binding.allOptionContainer.setVisibility(View.VISIBLE);
            binding.commonContainer.setVisibility(View.GONE);
            binding.amountContainer.setVisibility(View.GONE);
            binding.amountLine.setVisibility(View.GONE);

            binding.optionContainer1.setVisibility(View.VISIBLE);
            binding.optionName1.setText(option1Name);
            binding.optionPrice1.setText(option1Price);

            if(option2Name!=null){
                binding.optionContainer2.setVisibility(View.VISIBLE);
                binding.optionName2.setText(option2Name);
                binding.optionPrice2.setText(option2Price);

                if(option3Name!=null){
                    binding.optionContainer3.setVisibility(View.VISIBLE);
                    binding.optionName3.setText(option3Name);
                    binding.optionPrice3.setText(option3Price);

                    if(option4Name!=null){
                        binding.optionContainer4.setVisibility(View.VISIBLE);
                        binding.optionName4.setText(option4Name);
                        binding.optionPrice4.setText(option4Price);

                        if(option5Name!=null){
                            binding.optionContainer5.setVisibility(View.VISIBLE);
                            binding.optionName5.setText(option5Name);
                            binding.optionPrice5.setText(option5Price);
                        }
                    }
                }
            }

        }

    }
}
