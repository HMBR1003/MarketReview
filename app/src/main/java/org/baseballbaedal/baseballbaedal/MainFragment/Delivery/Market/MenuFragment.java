//package org.androidtown.markettest;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.widget.AbsListView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.ScrollView;
//
//import java.util.ArrayList;
//
///**
// * Created by Administrator on 2017-07-24.
// */
//
//public class MenuFragment extends ScrollTabHolderFragment{
//
//    private static final String ARG_POSITION = "position";
//    private ScrollView scrollView;
////    private ListView mListView;
////    private ArrayList<String> mListItems;
//
//    private int mPosition;
//    public static Fragment newInstance(int position) {
//        MenuFragment f = new MenuFragment();
//        Bundle b = new Bundle();
//        b.putInt(ARG_POSITION, position);
//        f.setArguments(b);
//        return f;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPosition = getArguments().getInt(ARG_POSITION);
//
////        //리스트에 들어갈 아이템 세팅
////        mListItems = new ArrayList<String>();
////        for (int i = 1; i <= 100; i++) {
////            mListItems.add(i + ". item - currnet page: " + (mPosition + 1));
////        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_menu, null);
//
//        scrollView = (ScrollView)v.findViewById(R.id.menuScrollView);
//        //리스트 할당
////        mListView = (ListView) v.findViewById(R.id.listView);
////
////        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
////        placeHolderView.setBackgroundColor(0xFFFFFFFF);
////        mListView.addHeaderView(placeHolderView);
//
//        return v;
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                mScrollTabHolder.onScroll(scrollView, 0,0,0, mPosition);
//            }
//        });
//
////        //리스트뷰에 스크롤 리스너 부착
////        mListView.setOnScrollListener(new MenuFragment.OnScroll());
////
////        //리스트뷰에 어댑터 부착
////        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, android.R.id.text1, mListItems));
////
////        if(MainActivity.NEEDS_PROXY){//in my moto phone(android 2.1),setOnScrollListener do not work well
////            mListView.setOnTouchListener(new View.OnTouchListener() {
////                @Override
////                public boolean onTouch(View v, MotionEvent event) {
////                    if (mScrollTabHolder != null)
////                        mScrollTabHolder.onScroll(mListView, 0, 0, 0, mPosition);
////                    return false;
////                }
////            });
////        }
//    }
//
//    @Override
//    public void adjustScroll(int scrollHeight) {
////        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
////            return;
////        }
////
////        mListView.setSelectionFromTop(1, scrollHeight);
//
//    }
//
//    public class OnScroll implements AbsListView.OnScrollListener {
//
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem,
//                             int visibleItemCount, int totalItemCount) {
//            if (mScrollTabHolder != null)
//                mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
//        }
//
//    }
//
//
//    @Override
//    public void onScroll(View view, int firstVisibleItem,
//                         int visibleItemCount, int totalItemCount, int pagePosition) {
//    }
//}
package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuInfo;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.FragmentMenuBinding;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

import static org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.MarketInfoActivity.marketTel;


public class MenuFragment extends ScrollTabHolderFragment {
    private FragmentMenuBinding binding;

    MenuAdapter adapter;

    private int mPosition;
    String marketId;
    ValueEventListener listener;
    int imageWidth;

    GridViewWithHeaderAndFooter menuListView;

    public static Fragment newInstance(int position, String marketId, int imageWidth) {
        MenuFragment f = new MenuFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putString("marketId", marketId);
        b.putInt("imageWidth",imageWidth);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("position");
        marketId = getArguments().getString("marketId");
        imageWidth = getArguments().getInt("imageWidth");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_menu,container,false);

        menuListView = (GridViewWithHeaderAndFooter)rootView.findViewById(R.id.menuListView);

        adapter = new MenuAdapter(getContext(),imageWidth);

        View placeHolderView = inflater.inflate(R.layout.view_header_menu, menuListView, false);
        placeHolderView.setBackgroundColor(0xFFFFFFFF);
        menuListView.addHeaderView(placeHolderView);

        View placefooterView = inflater.inflate(R.layout.view_footer_menu, menuListView, false);
        placefooterView.setBackgroundColor(0xFFFFFFFF);
        menuListView.addFooterView(placefooterView);
        return rootView;
    }

    @Override
    public void onStop() {
        FirebaseDatabase.getInstance().getReference().child("market").child(marketId).child("menu").removeEventListener(listener);
        super.onStop();
    }

    @Override
    public void onResume() {
        FirebaseDatabase.getInstance().getReference().child("market").child(marketId).child("menu").addValueEventListener(listener);
        super.onResume();
    }

    public String numToWon(int num){
        String tmp = num+"";
        String won;
        if(tmp.length()>3){
            int a = tmp.length()%3;
            int b = tmp.length()/3;
            if(a!=0) {
                String first = tmp.substring(0, a);
                won = first;
                for(int i =0; i<b; i++){
                    won = won+","+ tmp.substring(a,a+3);
                    a=a+3;
                }
            }
            else{
                a=3;
                String first = tmp.substring(0, a);
                won = first;
                for(int i =0; i<b-1; i++){
                    won = won+","+ tmp.substring(a,a+3);
                    a=a+3;
                }
            }
        }
        else{
            won = tmp;
        }
        return won;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentMenuBinding.bind(getView());

        menuListView.setOnScrollListener(new OnScroll());
        menuListView.setAdapter(adapter);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0) {
                    binding.noMenuContainer.setVisibility(View.GONE);
                    menuListView.setVisibility(View.VISIBLE);

                    adapter.clear();
                    int i = 0;
                    MenuInfo menuInfo;
                    MenuItem items[] = new MenuItem[(int) dataSnapshot.getChildrenCount()];
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        menuInfo = data.getValue(MenuInfo.class);
                        items[i++] = new MenuItem(menuInfo.aTime, menuInfo.menuName, numToWon(Integer.parseInt(menuInfo.menuPrice))+"원", data.getKey(), marketId, menuInfo.isMain, menuInfo.aseq);
                    }
                    //메뉴 순서대로 리스트에 추가
                    for (int iii = 1; iii < dataSnapshot.getChildrenCount() + 1; iii++) {
                        for (int jjj = 0; jjj < dataSnapshot.getChildrenCount(); jjj++) {
                            if (items[jjj].aseq == iii) {
                                adapter.add(items[jjj]);
                                break;
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    binding.noMenuContainer.setVisibility(View.VISIBLE);
                    menuListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        binding.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+marketTel));
                startActivity(intent);
            }
        });


//        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, android.R.id.text1, mListItems));

        if (MarketInfoActivity.NEEDS_PROXY) {//in my moto phone(android 2.1),setOnScrollListener do not work well
            menuListView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mScrollTabHolder != null)
                        mScrollTabHolder.onScroll(menuListView, 0, 0, 0, mPosition);
                    return false;
                }
            });
        }
    }

    @Override
    public void adjustScroll(int scrollHeight,View view) {
        if (scrollHeight == 0 && menuListView.getFirstVisiblePosition() >= 1) {
            return;
        }

        menuListView.setSelection(1);
//
//        mListView.setSelectionFromTop(1, scrollHeight);

    }

    public class OnScroll implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (mScrollTabHolder != null)
                mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
        }

    }


    @Override
    public void onScroll(View view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount, int pagePosition) {
    }

}