package org.baseballbaedal.baseballbaedal.Order;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityOrderListBinding;

public class OrderListActivity extends NewActivity {

    ActivityOrderListBinding binding;
    boolean isBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_list);

        isBusiness = getIntent().getBooleanExtra("isBusiness",false);

        //타이틀 설정 임시로 true
        isBusiness=true;

        if(isBusiness) {
            binding.container.addView(getToolbar("주문받은 내역", true), 0);
        }else{
            binding.container.addView(getToolbar("주문 내역", true), 0);
        }

        FirebaseDatabase.getInstance().getReference().child("dd").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
