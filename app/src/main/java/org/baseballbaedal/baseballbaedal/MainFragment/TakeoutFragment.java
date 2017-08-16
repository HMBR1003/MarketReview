package org.baseballbaedal.baseballbaedal.MainFragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.MarketInfoActivity;
import org.baseballbaedal.baseballbaedal.MainFragment.Delivery.MarketList;
import org.baseballbaedal.baseballbaedal.MainFragment.Delivery.MarketListAdapter;
import org.baseballbaedal.baseballbaedal.MainFragment.Delivery.MarketListItem;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.FragmentTakeoutBinding;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017-05-08.
 */

public class TakeoutFragment extends Fragment {

    FragmentTakeoutBinding binding;
    ValueEventListener listener;
    DatabaseReference fireDB;
    MarketListAdapter adapter;
    MarketList market;
    int colCheck;

    ItemClickListener itemClickListener;
    int menuCode=5;
    String userID;  //사업자아이디

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_takeout,container,false);
        return rootView;
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        BusProvider.getInstance().register(this);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        BusProvider.getInstance().unregister(this);
//    }
//
//    @Subscribe
//    public void onPushEvent(HeightEvent heightEvent) {
//        height = heightEvent.getHeight();
//        width = heightEvent.getWidth();
//        containerHeight = height / 3;
//        imageWidth = (width / 100) * 37;
//        init();
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentTakeoutBinding.bind(getView());

        SharedPreferences colCheckpref = getActivity().getSharedPreferences("selectedCol", MODE_PRIVATE);
        colCheck = colCheckpref.getInt("selectedCol", -1);
        itemClickListener = new ItemClickListener();
        fireDB = FirebaseDatabase.getInstance().getReference();
        adapter = new MarketListAdapter();
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(itemClickListener);

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
                            adapter.addItem(userID, address1 + "\n" + market.marketAddress2, market.marketName, " " + market.marketTel, market.minPrice, market.aTime,true);
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        fireDB.child("market").removeEventListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        fireDB.child("market").addValueEventListener(listener);
    }

    public class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MarketListItem item = (MarketListItem) adapter.getItem(position);

            Intent intent = new Intent(getActivity(), MarketInfoActivity.class);
            intent.putExtra("uid", item.getMarketUserID());
            intent.putExtra("isTakeout", true);
            startActivity(intent);
        }
    }
}
