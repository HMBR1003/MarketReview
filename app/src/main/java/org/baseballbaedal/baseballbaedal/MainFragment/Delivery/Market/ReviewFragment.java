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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.baseballbaedal.baseballbaedal.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReviewFragment extends ScrollTabHolderFragment {

    private static final String ARG_POSITION = "position";

    private ListView reviewListView;
    private TextView noReviewText;

    ReviewAdapter adapter;

    private int mPosition;
    String marketID;
    ValueEventListener listener;
    ReviewSave reviewSave;

    ArrayList<MarketReviewListItem> listItem;

    public static Fragment newInstance(int position, String marketId) {
        ReviewFragment f = new ReviewFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString("marketId", marketId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
        marketID = getArguments().getString("marketId");

//        mListItems = new ArrayList<String>();
//
//        for (int i = 1; i <= 100; i++) {
//            mListItems.add(i + ". item - currnet page: " + (mPosition + 1));
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review, null);

        listItem = new ArrayList<>();
        adapter = new ReviewAdapter(listItem);
        reviewListView = (ListView) v.findViewById(R.id.reviewListView);
        noReviewText = (TextView)v.findViewById(R.id.noReviewText);
        reviewListView.setAdapter(adapter);
        View placeHolderView = inflater.inflate(R.layout.view_header_review, reviewListView, false);
        placeHolderView.setBackgroundColor(0xFFFFFFFF);
        reviewListView.addHeaderView(placeHolderView);

        reviewListView.setOnScrollListener(new OnScroll());
        FirebaseDatabase.getInstance().getReference().child("review").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                boolean sw = false;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    reviewSave = data.getValue(ReviewSave.class);
                    if(reviewSave.marketID.equals(marketID)){
                        String reviewID = data.getKey();
                        adapter.addItem(reviewSave.userName,reviewSave.body,reviewSave.score,reviewID,reviewSave.imageBool,reviewSave.time);
                        adapter.notifyDataSetChanged();
                        sw = true;
                    }
                }
                if(!sw){
                    noReviewText.setVisibility(View.VISIBLE);
                    reviewListView.setVisibility(View.GONE);
                }
                Collections.sort(listItem,comparator);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        View placefooterView = inflater.inflate(R.layout.view_footer_placeholder, mListView, false);
//        placefooterView.setBackgroundColor(0xFFFFFFFF);
//        mListView.addFooterView(placefooterView);
        return v;
    }

    private Comparator<MarketReviewListItem> comparator = new Comparator<MarketReviewListItem>() {
        @Override
        public int compare(MarketReviewListItem o1, MarketReviewListItem o2) {
            int sor;
            if(Long.parseLong(o1.getTime()) < Long.parseLong(o2.getTime())){
                sor = 1;
            }else if(Long.parseLong(o1.getTime()) == Long.parseLong(o2.getTime())){
                sor = 0;
            }else{
                sor = -1;
            }
            return sor;
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, android.R.id.text1, mListItems));

        if (MarketInfoActivity.NEEDS_PROXY) {//in my moto phone(android 2.1),setOnScrollListener do not work well
            reviewListView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mScrollTabHolder != null)
                        mScrollTabHolder.onScroll(reviewListView, 0, 0, 0, mPosition);
                    return false;
                }
            });
        }
    }

    @Override
    public void adjustScroll(int scrollHeight,View view) {
        if (scrollHeight == 0 && reviewListView.getFirstVisiblePosition() >= 1) {
            return;
        }

        Log.d("테스트","dd");
        view.setTranslationY(Math.max(0,0));

        reviewListView.setSelection(1);
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