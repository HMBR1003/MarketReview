package org.baseballbaedal.baseballbaedal.Order;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityOrderListBinding;

public class OrderListActivity extends NewActivity {

    private ActivityOrderListBinding binding;
    private LinearLayoutManager layoutManager;
    private boolean isBusiness;
    private OrderListAdapter adapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order_list);

        isBusiness = getIntent().getBooleanExtra("isBusiness",false);
        userId = getIntent().getStringExtra("userId");


        if(isBusiness) {
            binding.container.addView(getToolbar("주문받은 내역", true), 0);
        }else{
            binding.container.addView(getToolbar("주문 내역", true), 0);
        }

        //리사이클러뷰 설정
        layoutManager = new LinearLayoutManager(this);
        binding.orderListView.setLayoutManager(layoutManager);

        //어댑터 생성 후 리사이클러뷰에 붙이기
        adapter = new OrderListAdapter();
        binding.orderListView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    OrderData tmpData = data.getValue(OrderData.class);
                    adapter.add(new OrderAdapterItem(tmpData,data.getKey()));
//                    if(tmpData.getMarketId().equals(userId)){
//                        adapter.add(new OrderAdapterItem(tmpData,data.getKey()));
//                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
