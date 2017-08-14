package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuInfo;
import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityBasketBinding;

import info.hoang8f.widget.FButton;

public class BasketActivity extends NewActivity {
    public static final int BASKET_MAX_LENGTH = 30;

    ActivityBasketBinding basketBinding;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    DatabaseReference fireDB;

    ViewGroup headerView;
    ViewGroup footerView;
    TextView hapPriceText;
    TextView minPriceText;
    TextView marketName;
    ViewGroup[] view = new ViewGroup[BASKET_MAX_LENGTH];
    TextView[] tagText = new TextView[BASKET_MAX_LENGTH];
    ImageButton[] closeButton = new ImageButton[BASKET_MAX_LENGTH];
    TextView[] menuName = new TextView[BASKET_MAX_LENGTH];
    TextView[] menuPriceText = new TextView[BASKET_MAX_LENGTH];
    ImageView[] imageView = new ImageView[BASKET_MAX_LENGTH];
    TextView[] minHapPriceText = new TextView[BASKET_MAX_LENGTH];
    TextView[] countEdit = new TextView[BASKET_MAX_LENGTH];
    TextView[] optionText = new TextView[BASKET_MAX_LENGTH];
    Button[] plusButton = new Button[BASKET_MAX_LENGTH];
    Button[] minusButton = new Button[BASKET_MAX_LENGTH];

    MenuInfo[] menuList = new MenuInfo[BASKET_MAX_LENGTH];
    String[] menuKey = new String[BASKET_MAX_LENGTH];
    int[] menuAmount = new int[BASKET_MAX_LENGTH];
    boolean[] option1checked = new boolean[BASKET_MAX_LENGTH];
    boolean[] option2checked = new boolean[BASKET_MAX_LENGTH];
    boolean[] option3checked = new boolean[BASKET_MAX_LENGTH];
    boolean[] option4checked = new boolean[BASKET_MAX_LENGTH];
    boolean[] option5checked = new boolean[BASKET_MAX_LENGTH];
    StorageReference storageReference;
    String userID;
    int basketCount = 0;
    int hapPrice;
    LayoutInflater inflater;
    LinearLayout ll;
    FButton addMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basketBinding = DataBindingUtil.setContentView(this, R.layout.activity_basket);

        basketBinding.container.addView(getToolbar("장바구니", true), 0);


//        editor = shared.edit();
//
//        editor.putString("marketId", "slwVsecqtTO3RDjzPxBWrFekbEd2");
//        editor.putString("marketName", "치킨치킨");
//        editor.putString("minPrice", "10000");
//
//        editor.putString("menuKey0", "-KpEcsuBIg-8VmoqGl1f");
//        editor.putInt("menuAmount0", 2);
//        editor.putBoolean("option1checked0", false);
//        editor.putBoolean("option2checked0", false);
//        editor.putBoolean("option3checked0", false);
//        editor.putBoolean("option4checked0", false);
//        editor.putBoolean("option5checked0", false);
//
//        editor.putString("menuKey1", "-KpOEVQdssGBi5W5IHo5");
//        editor.putInt("menuAmount1", 1);
//        editor.putBoolean("option1checked1", true);
//        editor.putBoolean("option2checked1", true);
//        editor.putBoolean("option3checked1", true);
//        editor.putBoolean("option4checked1", false);
//        editor.putBoolean("option5checked1", false);
//        editor.commit();


        ll = new LinearLayout(this);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = (ViewGroup) inflater.inflate(R.layout.basket_end_layout, ll, false);

        ll = new LinearLayout(this);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = (ViewGroup) inflater.inflate(R.layout.basket_start_layout, ll, false);

        hapPriceText = (TextView) footerView.findViewById(R.id.hapPriceText);
        minPriceText = (TextView) footerView.findViewById(R.id.minPrice);
        marketName = (TextView) headerView.findViewById(R.id.marketName);

        init();
        basketBinding.orderButton.setButtonColor(getResources().getColor(R.color.buttonColor));
        basketBinding.orderButton.setCornerRadius(15);
        basketBinding.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void init() {
        basketBinding.basketLayout.removeAllViews();

        shared = getSharedPreferences("basket", MODE_PRIVATE);

        userID = shared.getString("marketId", "");
        marketName.setText(shared.getString("marketName", null));
        minPriceText.setText(numToWon(Integer.parseInt(shared.getString("minPrice", "0"))) + "원");
        for (int i = 0; i < BASKET_MAX_LENGTH; i++) {
            menuKey[i] = shared.getString("menuKey" + i, null);
            menuAmount[i] = shared.getInt("menuAmount" + i, 13);
            option1checked[i] = shared.getBoolean("option1checked" + i, false);
            option2checked[i] = shared.getBoolean("option2checked" + i, false);
            option3checked[i] = shared.getBoolean("option3checked" + i, false);
            option4checked[i] = shared.getBoolean("option4checked" + i, false);
            option5checked[i] = shared.getBoolean("option5checked" + i, false);
        }
        emptyCheck();


        fireDB = FirebaseDatabase.getInstance().getReference().child("market").child(userID).child("menu");
        fireDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    menuList[count] = data.getValue(MenuInfo.class);
                    menuList[count].menuKey = data.getKey();
                    count++;
                }

                basketBinding.basketLayout.addView(headerView);


                for (int i = 0; menuKey[i] != null; i++) {
                    for (int j = 0; j < count; j++) {
                        if (menuKey[i].equals(menuList[j].menuKey)) {
                            basketCreate(menuList[j], menuAmount[i], option1checked[i], option2checked[i], option3checked[i], option4checked[i], option5checked[i]);
                        }
                    }
                }

                //총 가격 표시해줄 푸터뷰
                basketBinding.basketLayout.addView(footerView);
                addMenuButton = (FButton)findViewById(R.id.addMenuButton);
                addMenuButton.setButtonColor(getResources().getColor(R.color.buttonColor));
                addMenuButton.setCornerRadius(15);
                addMenuButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MarketInfoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("uid", userID);
                        intent.putExtra("isTakeout", false);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //장바구니 항목 삭제 버튼
    View.OnClickListener closeListener = new View.OnClickListener() {   //닫기버튼
        @Override
        public void onClick(View v) {
            Object object = v.getTag();
            for (int i = 0; i < basketCount; i++) {
                if (object.equals(tagText[i].getTag())) {
                    alertDialog(i);
                    break;
                }
            }
        }
    };

    //주문하기 버튼
    public void OnClicked(View v) {
        switch (v.getId()) {
            case R.id.orderButton:  //주문하기버튼
//                for (int i = 0; i < BASKET_MAX_LENGTH; i++) {
//                    Log.d("menu" + i, menu[i]);
//                }
                break;
        }
    }

    public void emptyCheck() {
        boolean isEmpty = true;
        for (int i = 0; i < BASKET_MAX_LENGTH; i++) {
            menuKey[i] = shared.getString("menuKey" + i, null);
            if (menuKey[i] != null) {
                isEmpty = false;
                break;
            }
        }
        if (isEmpty) {
            basketBinding.commonContainer.setVisibility(View.GONE);
            basketBinding.emptyContainer.setVisibility(View.VISIBLE);
            shared.edit().remove("marketId").apply();
            shared.edit().remove("marketName").apply();
            shared.edit().remove("minPrice").apply();
        }
    }

    public void alertDialog(final int num) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("이 항목을 삭제하시겠습니까?")
                .setCancelable(false)
                .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        basketBinding.basketLayout.removeView(view[num]);
                        int mPrice = Integer.parseInt(minHapPriceText[num].getText().toString().replaceAll(",", ""));
                        int totalPrice = Integer.parseInt(hapPriceText.getText().toString().replaceAll(",", "").replaceAll("원", "")) - mPrice;
                        hapPriceText.setText(numToWon(totalPrice) + "원");
//                        shared.edit().remove("menuKey" + num).apply();
//                        shared.edit().remove("menuAmount" + num).apply();
//                        shared.edit().remove("option1checked" + num).apply();
//                        shared.edit().remove("option2checked" + num).apply();
//                        shared.edit().remove("option3checked" + num).apply();
//                        shared.edit().remove("option4checked" + num).apply();
//                        shared.edit().remove("option5checked" + num).apply();

                        int index = shared.getInt("basketCount",0);
//                        shared.edit().putInt("basketCount",0).apply();
                        for (int i = num; shared.getString("menuKey" + (i + 1), null) != null; i++) {
                            shared.edit().putString("menuKey" + i, shared.getString("menuKey" + (i + 1), null)).apply();
                            shared.edit().putInt("menuAmount" + i, shared.getInt("menuAmount" + (i + 1), 0)).apply();
                            shared.edit().putBoolean("option1checked" + i, shared.getBoolean("option1checked" + (i + 1), false)).apply();
                            shared.edit().putBoolean("option2checked" + i, shared.getBoolean("option2checked" + (i + 1), false)).apply();
                            shared.edit().putBoolean("option3checked" + i, shared.getBoolean("option3checked" + (i + 1), false)).apply();
                            shared.edit().putBoolean("option4checked" + i, shared.getBoolean("option4checked" + (i + 1), false)).apply();
                            shared.edit().putBoolean("option5checked" + i, shared.getBoolean("option5checked" + (i + 1), false)).apply();
//                            editor = shared.edit();
//                            editor.putString("menuKey" + (i - 1), shared.getString("menuKey" + i, null));
//                            editor.putInt("menuAmount" + (i - 1), shared.getInt("menuAmount" + i, 0));
//                            editor.putBoolean("option1checked" + (i - 1), shared.getBoolean("option1checked" + i, false));
//                            editor.putBoolean("option2checked" + (i - 1), shared.getBoolean("option2checked" + i, false));
//                            editor.putBoolean("option3checked" + (i - 1), shared.getBoolean("option3checked" + i, false));
//                            editor.putBoolean("option4checked" + (i - 1), shared.getBoolean("option4checked" + i, false));
//                            editor.putBoolean("option5checked" + (i - 1), shared.getBoolean("option5checked" + i, false));
//                            editor.commit();
                        }
                        shared.edit().putInt("basketCount", index-1).apply();
                        shared.edit().remove("menuKey" + (index-1)).apply();
                        shared.edit().remove("menuAmount" + (index-1)).apply();
                        shared.edit().remove("option1checked" + (index-1)).apply();
                        shared.edit().remove("option2checked" + (index-1)).apply();
                        shared.edit().remove("option3checked" + (index-1)).apply();
                        shared.edit().remove("option4checked" + (index-1)).apply();
                        shared.edit().remove("option5checked" + (index-1)).apply();

                        Toast.makeText(BasketActivity.this, "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        emptyCheck();
                    }
                })
                .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void basketCreate(MenuInfo menuList, int amount, boolean option1, boolean option2, boolean option3, boolean option4, boolean option5) {   //메뉴 불러오기
        ll = new LinearLayout(this);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view[basketCount] = (ViewGroup) inflater.inflate(R.layout.basket_layout, ll, false);
        tagText[basketCount] = (TextView) view[basketCount].findViewById(R.id.tagText); //번지수 기억 텍스트
        tagText[basketCount].setTag(basketCount);
        closeButton[basketCount] = (ImageButton) view[basketCount].findViewById(R.id.closeButton);  //닫기버튼
        closeButton[basketCount].setOnClickListener(closeListener);
        closeButton[basketCount].setTag(basketCount);
        menuPriceText[basketCount] = (TextView) view[basketCount].findViewById(R.id.menuPriceText); //메뉴가격
        menuPriceText[basketCount].setText(numToWon(Integer.parseInt(menuList.menuPrice)));
        menuName[basketCount] = (TextView) view[basketCount].findViewById(R.id.menuName);   //메뉴이름
        menuName[basketCount].setText(menuList.menuName);
        imageView[basketCount] = (ImageView) view[basketCount].findViewById(R.id.imageView);    //메뉴이미지
        storageReference = FirebaseStorage.getInstance().getReference().child("market").child(userID).child("menu").child(menuList.menuKey + ".jpg");
        try {
            Glide
                    .with(BasketActivity.this)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .signature(new StringSignature(menuList.aTime)) //이미지저장시간
                    .placeholder(R.drawable.jamsil)
                    .thumbnail(0.1f)
                    .crossFade()
                    .into(imageView[basketCount]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        countEdit[basketCount] = (TextView) view[basketCount].findViewById(R.id.countEdit);     //메뉴당 주문 갯수
        countEdit[basketCount].setText(amount + "");
        plusButton[basketCount] = (Button) view[basketCount].findViewById(R.id.plusButton); //+ 버튼
        plusButton[basketCount].setTag(basketCount);
        plusButton[basketCount].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                int tmp = Integer.parseInt(countEdit[position].getText().toString());
                tmp = tmp + 1;
                countEdit[position].setText((tmp) + "");
                updatePrice(position, tmp, true);
            }
        });
        minusButton[basketCount] = (Button) view[basketCount].findViewById(R.id.minusButton); //- 버튼
        minusButton[basketCount].setTag(basketCount);
        minusButton[basketCount].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                int tmp = Integer.parseInt(countEdit[position].getText().toString());
                if (tmp > 1) {
                    tmp = tmp - 1;
                    countEdit[position].setText((tmp) + "");
                    updatePrice(position, tmp, false);
                }
            }
        });

        minHapPriceText[basketCount] = (TextView) view[basketCount].findViewById(R.id.minHapPriceText);  //메뉴당 가격 소계
        //(메뉴가격 + 옵션1 + 옵션2 + 옵션3 + 옵션4 + 옵션5) * 수량
        int p = Integer.parseInt(menuList.menuPrice);
        if (option1) {
            p += Integer.parseInt(menuList.option1Price);
        }
        if (option2) {
            p += Integer.parseInt(menuList.option2Price);
        }
        if (option3) {
            p += Integer.parseInt(menuList.option3Price);
        }
        if (option4) {
            p += Integer.parseInt(menuList.option4Price);
        }
        if (option5) {
            p += Integer.parseInt(menuList.option5Price);
        }


        minHapPriceText[basketCount].setText(numToWon(p * amount));
        minHapPriceText[basketCount].setTag(p);

        int price = p * amount;    //소계
        hapPrice = Integer.parseInt(hapPriceText.getText().toString().replaceAll(",", "").replaceAll("원", "")) + price;   //합계금액
        hapPriceText.setText(numToWon(hapPrice) + "원");

        optionText[basketCount] = (TextView) view[basketCount].findViewById(R.id.optionText);   //옵션텍스트

        if (option1 || option2 || option3 || option4 || option5) {
            String option = "";
            if (option1) {
                option += menuList.option1Name + "(+" + numToWon(Integer.parseInt(menuList.option1Price)) + "원)\n";
            }
            if (option2) {
                option += menuList.option2Name + "(+" + numToWon(Integer.parseInt(menuList.option2Price)) + "원)\n";
            }
            if (option3) {
                option += menuList.option3Name + "(+" + numToWon(Integer.parseInt(menuList.option3Price)) + "원)\n";
            }
            if (option4) {
                option += menuList.option4Name + "(+" + numToWon(Integer.parseInt(menuList.option4Price)) + "원)\n";
            }
            if (option5) {
                option += menuList.option5Name + "(+" + numToWon(Integer.parseInt(menuList.option5Price)) + "원)\n";
            }
            optionText[basketCount].setText(option);
        }

        basketBinding.basketLayout.addView(view[basketCount]);

        basketCount++;
    }

    public void updatePrice(int position, int amount, boolean type) {
        int totalPrice;
        int price = (int) minHapPriceText[position].getTag();
        minHapPriceText[position].setText(numToWon(price * amount));
        if (type) {   //플러스
            totalPrice = Integer.parseInt(hapPriceText.getText().toString().replaceAll(",", "").replaceAll("원", "")) + price;
        } else {       //마이너스
            totalPrice = Integer.parseInt(hapPriceText.getText().toString().replaceAll(",", "").replaceAll("원", "")) - price;
        }
        hapPriceText.setText(numToWon(totalPrice) + "원");
        shared.edit().putInt("menuAmount" + position, amount).apply();
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
}

