package org.baseballbaedal.baseballbaedal.MainFragment.Delivery;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.baseballbaedal.baseballbaedal.BaseActivity;
import org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.MarketInfoActivity;
import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityMarketListBinding;

import dmax.dialog.SpotsDialog;


public class MarketListActivity extends NewActivity {

    ActivityMarketListBinding binding;
    ValueEventListener listener;
    DatabaseReference fireDB;
    MarketListAdapter adapter;
    MarketList market;
    int colCheck;
    SpotsDialog dialog;

    ItemClickListener itemClickListener;
    int menuCode;
    String userID;  //사업자아이디

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_market_list);

        SharedPreferences colCheckpref = getSharedPreferences("selectedCol", MODE_PRIVATE);
        colCheck = colCheckpref.getInt("selectedCol", -1);

        itemClickListener = new ItemClickListener();
        fireDB = FirebaseDatabase.getInstance().getReference();
        adapter = new MarketListAdapter();

        dialog = new SpotsDialog(MarketListActivity.this, "데이터를 불러오는 중입니다...", R.style.ProgressBar);
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    finish();
                }
                return true;
            }
        });
        dialog.show();

        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(itemClickListener);

        //메뉴데이터 가져오기
        Intent intent = getIntent();
        String menu = intent.getStringExtra("menu");
        //치킨 1, 피자 2, 햄버거 3, 족발 4, 테이크아웃 5, 기타 6
        switch (menu) {
            case "치킨":
                menuCode = 1;
                break;
            case "피자":
                menuCode = 2;
                break;
            case "햄버거":
                menuCode = 3;
                break;
            case "족발":
                menuCode = 4;
                break;
            case "기타":
                menuCode = 5;
                break;
        }


        //상단 툴바 설정
        binding.container.addView(getToolbar(menu,true),0);

        listener = new ValueEventListener() {       //마켓리스트 추출
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    market = data.getValue(MarketList.class);

                    if (market.handleFood == menuCode) {
                        if (market.selectedCol == colCheck + 1) {
                            userID = data.getKey();
                            String address1 = market.marketAddress1.substring(7);
                            adapter.addItem(userID, address1 + "\n" + market.marketAddress2, market.marketName, " " + market.marketTel, market.minPrice, market.aTime);
                            Log.d("handle", data.child("handleFood").getValue().toString());
                        }
                    }
                }
                if(adapter.getCount()>0){
                    binding.noMarketContainer.setVisibility(View.GONE);
                    binding.marketContainer.setVisibility(View.VISIBLE);
                }
                else{
                    binding.noMarketContainer.setVisibility(View.VISIBLE);
                    binding.marketContainer.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireDB.child("market").removeEventListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fireDB.child("market").addValueEventListener(listener);
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MarketListItem item = (MarketListItem) adapter.getItem(position);

            Intent intent = new Intent(getApplicationContext(), MarketInfoActivity.class);
            intent.putExtra("uid", item.getMarketUserID());
            startActivity(intent);
        }
    }
}
