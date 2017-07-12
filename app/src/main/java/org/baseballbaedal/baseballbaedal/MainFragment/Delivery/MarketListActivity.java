package org.baseballbaedal.baseballbaedal.MainFragment.Delivery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.baseballbaedal.baseballbaedal.R;


public class MarketListActivity extends AppCompatActivity {

    ValueEventListener listener;
    DatabaseReference fireDB;
    MarketListAdapter adapter;
    MarketList market;
    int colCheck;
    ProgressDialog progress;
    ListView listView;

    int menuCode;
    String userID;  //사업자아이디

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_list);

        SharedPreferences colCheckpref = getSharedPreferences("selectedCol", MODE_PRIVATE);
        colCheck = colCheckpref.getInt("selectedCol", -1);
        switch (colCheck) {
            case 0:
                Toast.makeText(this, "잠실 야구장(두산,LG)", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "고척 스카이돔(넥센)", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "SK 행복드림구장", Toast.LENGTH_SHORT).show();

                break;
            case 3:
                Toast.makeText(this, "한화 이글스파크", Toast.LENGTH_SHORT).show();

                break;
            case 4:
                Toast.makeText(this, "삼성 라이온즈파크", Toast.LENGTH_SHORT).show();

                break;
            case 5:
                Toast.makeText(this, "기아 챔피언스필드", Toast.LENGTH_SHORT).show();

                break;
            case 6:
                Toast.makeText(this, "사직 야구장(롯데)", Toast.LENGTH_SHORT).show();

                break;
            case 7:
                Toast.makeText(this, "KT 위즈파크", Toast.LENGTH_SHORT).show();

                break;
            case 8:
                Toast.makeText(this, "마산 야구장(NC)", Toast.LENGTH_SHORT).show();

                break;
            default:
                Toast.makeText(this, "선택된 야구장이 없습니다.", Toast.LENGTH_SHORT).show();

                break;
        }


        listView = (ListView) findViewById(R.id.listView);
        fireDB = FirebaseDatabase.getInstance().getReference();
        adapter = new MarketListAdapter();

        progress = new ProgressDialog(MarketListActivity.this);
        progress.setProgress(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("데이터를 불러오는중입니다.");
        progress.setCancelable(false);
        progress.show();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ItemClickListener());

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(menu);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        listener = new ValueEventListener() {       //마켓리스트 추출
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    market = data.getValue(MarketList.class);

                    if (market.handleFood == menuCode) {
                        if(market.selectedCol == colCheck+1) {
                            userID = data.getKey();
                            String address1 = market.marketAddress1.substring(7);
                            adapter.addItem(userID, " " + address1 + " " + market.marketAddress2, market.marketName, " " + market.marketTel);
                            Log.d("handle", data.child("handleFood").getValue().toString());
                            adapter.notifyDataSetChanged();
                        }
                    }
                    progress.dismiss();
                }
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

//            Intent intent = new Intent(getApplicationContext(), MenuListActivity.class);
//            intent.putExtra("id",item.getMarketUserID());
//            intent.putExtra("name",item.getMarketName());
//            startActivity(intent);
        }
    }
}
