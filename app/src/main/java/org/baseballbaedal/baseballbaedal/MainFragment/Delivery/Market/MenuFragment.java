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

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuInfo;
import org.baseballbaedal.baseballbaedal.LoginActivity;
import org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.Menu.MenuInfoActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.FragmentMenuBinding;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static org.baseballbaedal.baseballbaedal.MainActivity.isBusiness;
import static org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.MarketInfoActivity.marketName;
import static org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.MarketInfoActivity.marketTel;
import static org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.MarketInfoActivity.minPrice;


public class MenuFragment extends ScrollTabHolderFragment {
    private FragmentMenuBinding binding;

    MenuAdapter adapter;

    private int mPosition;
    String marketId;
    ValueEventListener listener;
    int imageWidth;
    boolean isTakeout;

    GridViewWithHeaderAndFooter menuListView;
    SharedPreferences shared;

    public static Fragment newInstance(int position, String marketId, int imageWidth, boolean isTakeout) {
        MenuFragment f = new MenuFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putString("marketId", marketId);
        b.putInt("imageWidth", imageWidth);
        b.putBoolean("isTakeout",isTakeout);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("position");
        marketId = getArguments().getString("marketId");
        imageWidth = getArguments().getInt("imageWidth");
        isTakeout = getArguments().getBoolean("isTakeout");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu, container, false);

        menuListView = (GridViewWithHeaderAndFooter) rootView.findViewById(R.id.menuListView);

        adapter = new MenuAdapter(getContext(), imageWidth);

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
        int index = shared.getInt("basketCount", 0);
        if(index>0) {
            binding.cartButton.setText("장바구니로 이동(" + index + ")");
        }
        else{
            binding.cartButton.setText("장바구니로 이동");
        }
        super.onResume();
    }

    public String numToWon(int num) {
        String tmp = num + "";
        String won;
        if (tmp.length() > 3) {
            int a = tmp.length() % 3;
            int b = tmp.length() / 3;
            if (a != 0) {
                String first = tmp.substring(0, a);
                won = first;
                for (int i = 0; i < b; i++) {
                    won = won + "," + tmp.substring(a, a + 3);
                    a = a + 3;
                }
            } else {
                a = 3;
                String first = tmp.substring(0, a);
                won = first;
                for (int i = 0; i < b - 1; i++) {
                    won = won + "," + tmp.substring(a, a + 3);
                    a = a + 3;
                }
            }
        } else {
            won = tmp;
        }
        return won;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentMenuBinding.bind(getView());
        shared = getActivity().getSharedPreferences("basket", MODE_PRIVATE);

        menuListView.setOnScrollListener(new OnScroll());
        menuListView.setAdapter(adapter);
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MenuInfoActivity.class);
                intent.putExtra("isTakeout",isTakeout);
                intent.putExtra("marketName",marketName);
                intent.putExtra("marketId",marketId);
                intent.putExtra("minPrice",minPrice);
                intent.putExtra("menuName", adapter.getInfo(position).menuName);
                intent.putExtra("menuExplain", adapter.getInfo(position).menuExplain);
                intent.putExtra("menuPrice",adapter.getInfo(position).menuPrice);
                intent.putExtra("menuKey", adapter.getMenuKey(position));
                intent.putExtra("aTime", adapter.getInfo(position).aTime);
                if (adapter.getInfo(position).option1Name != null) {
                    intent.putExtra("option1Name", adapter.getInfo(position).option1Name);
                    intent.putExtra("option1Price",adapter.getInfo(position).option1Price);
                    if (adapter.getInfo(position).option2Name != null) {
                        intent.putExtra("option2Name", adapter.getInfo(position).option2Name);
                        intent.putExtra("option2Price", adapter.getInfo(position).option2Price);
                        if (adapter.getInfo(position).option3Name != null) {
                            intent.putExtra("option3Name", adapter.getInfo(position).option3Name);
                            intent.putExtra("option3Price",adapter.getInfo(position).option3Price);
                            if (adapter.getInfo(position).option4Name != null) {
                                intent.putExtra("option4Name", adapter.getInfo(position).option4Name);
                                intent.putExtra("option4Price", adapter.getInfo(position).option4Price);
                                if (adapter.getInfo(position).option5Name != null) {
                                    intent.putExtra("option5Name", adapter.getInfo(position).option5Name);
                                    intent.putExtra("option5Price", adapter.getInfo(position).option5Price);
                                }
                            }
                        }
                    }
                }
                startActivity(intent);
            }
        });

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    binding.noMenuContainer.setVisibility(View.GONE);
                    menuListView.setVisibility(View.VISIBLE);

                    adapter.clear();
                    int i = 0;
                    MenuInfo menuInfo;
                    MenuItem items[] = new MenuItem[(int) dataSnapshot.getChildrenCount()];
                    MenuInfo infos[] = new MenuInfo[(int) dataSnapshot.getChildrenCount()];
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        menuInfo = data.getValue(MenuInfo.class);
                        items[i] = new MenuItem(menuInfo.aTime, menuInfo.menuName, numToWon(Integer.parseInt(menuInfo.menuPrice)) + "원", data.getKey(), marketId, menuInfo.isMain, menuInfo.aseq);
                        infos[i++] = menuInfo;
                    }
                    //메뉴 순서대로 리스트에 추가
                    for (int iii = 1; iii < dataSnapshot.getChildrenCount() + 1; iii++) {
                        for (int jjj = 0; jjj < dataSnapshot.getChildrenCount(); jjj++) {
                            if (items[jjj].aseq == iii) {
                                adapter.add(items[jjj]);
                                adapter.addInfo(infos[jjj]);
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
        binding.callButton.setButtonColor(getResources().getColor(R.color.buttonColor));
        binding.callButton.setCornerRadius(15);
        binding.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + marketTel));
                startActivity(intent);
            }
        });

        binding.cartButton.setButtonColor(getResources().getColor(R.color.buttonColor));
        binding.cartButton.setCornerRadius(15);
        binding.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (isBusiness == 0 || isBusiness == 1) {
                        Intent intent = new Intent(getActivity(), BasketActivity.class);
                        intent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "사업자 고객은 주문을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    pleaseLogin();
                }
            }
        });

        if(isTakeout){
            binding.buttonContainer.setVisibility(View.GONE);
        }


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

    //로그인 해달라는 창을 띄우는 메서드
    public void pleaseLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("알림");
        builder.setMessage("먼저 로그인을 해주세요");
        builder.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog loginDialog;
        loginDialog = builder.create();
        loginDialog.setCancelable(false);
        loginDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    loginDialog.dismiss();
                }
                return true;
            }
        });
        loginDialog.show();
    }
    @Override
    public void adjustScroll(int scrollHeight, View view) {
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