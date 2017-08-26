package org.baseballbaedal.baseballbaedal.Order;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityOrderBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Inflater;

import static org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.BasketActivity.BASKET_MAX_LENGTH;


public class OrderActivity extends NewActivity {
    public static final int REQUEST_CODE_SEATSELECT = 1;

    ActivityOrderBinding orderBinding;

    int buying = 0; // 1: 카드결제 2: 현금결제
    int colCheck;
    boolean isSetSeat = false;
    String marketId;
    Intent intent;
    AlertDialog orderDialog;

    String block = null;
    String row = null;
    String seatNum = null;
    String seat;
    int basketCount;
    boolean isBasket;
    SharedPreferences shared;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderBinding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        orderBinding.container.addView(getToolbar("주문하기", true), 0);

        orderBinding.orderButton.setButtonColor(getResources().getColor(R.color.buttonColor));
        orderBinding.orderButton.setCornerRadius(15);

        //현재 선택된 야구장 데이터 가져오기
        SharedPreferences colCheckpref = getSharedPreferences("selectedCol", MODE_PRIVATE);
        colCheck = colCheckpref.getInt("selectedCol", -1);
        switch (colCheck) {
            case 0:
                orderBinding.stadiumText.setText("잠실 야구장(두산,LG)");
                orderBinding.seatContainerJamsil.setVisibility(View.VISIBLE);
                break;
            case 1:
                orderBinding.stadiumText.setText("고척 스카이돔(넥센)");
                orderBinding.seatContainerEtc.setVisibility(View.VISIBLE);
                break;
            case 2:
                orderBinding.stadiumText.setText("SK 행복드림구장");
                orderBinding.seatContainerEtc.setVisibility(View.VISIBLE);
                break;
            case 3:
                orderBinding.stadiumText.setText("한화 이글스파크");
                orderBinding.seatContainerEtc.setVisibility(View.VISIBLE);
                break;
            case 4:
                orderBinding.stadiumText.setText("삼성 라이온즈파크");
                orderBinding.seatContainerEtc.setVisibility(View.VISIBLE);
                break;
            case 5:
                orderBinding.stadiumText.setText("기아 챔피언스필드");
                orderBinding.seatContainerEtc.setVisibility(View.VISIBLE);
                break;
            case 6:
                orderBinding.stadiumText.setText("사직 야구장(롯데)");
                orderBinding.seatContainerEtc.setVisibility(View.VISIBLE);
                break;
            case 7:
                orderBinding.stadiumText.setText("KT 위즈파크");
                orderBinding.seatContainerEtc.setVisibility(View.VISIBLE);
                break;
            case 8:
                orderBinding.stadiumText.setText("마산 야구장(NC)");
                orderBinding.seatContainerEtc.setVisibility(View.VISIBLE);
                break;
            default:
                orderBinding.stadiumText.setText("선택된 야구장이 없습니다.");
                break;
        }

        intent = getIntent();
        isBasket = intent.getBooleanExtra("isBasket", false);
        //즉시주문
        if (!isBasket) {
            marketId = intent.getStringExtra("marketId");
            String menuPrice = intent.getStringExtra("menuPrice");
            String menuName = intent.getStringExtra("menuName");
            int menuAmount = intent.getIntExtra("menuAmount", 1);
            String option1Name = intent.getStringExtra("option1Name");
            String option2Name = intent.getStringExtra("option2Name");
            String option3Name = intent.getStringExtra("option3Name");
            String option4Name = intent.getStringExtra("option4Name");
            String option5Name = intent.getStringExtra("option5Name");
            LinearLayout linearLayout = new LinearLayout(this);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.order_item, linearLayout, false);
            ((TextView) viewGroup.findViewById(R.id.menuName)).setText(menuName + " X " + menuAmount);
            ((TextView) viewGroup.findViewById(R.id.menuPrice)).setText(menuPrice + "원");
            orderBinding.finalPrice.setText(menuPrice + "원");

            boolean optionExist = false;
            String options = "";
            if (option1Name != null) {
                optionExist = true;
                options += option1Name + ", ";
            }
            if (option2Name != null) {
                optionExist = true;
                options += option2Name + ", ";
            }
            if (option3Name != null) {
                optionExist = true;
                options += option3Name + ", ";
            }
            if (option4Name != null) {
                optionExist = true;
                options += option4Name + ", ";
            }
            if (option5Name != null) {
                optionExist = true;
                options += option5Name + ", ";
            }

            if (!optionExist) {
                ((TextView) viewGroup.findViewById(R.id.options)).setText("없음");
            } else {
                options = options.substring(0, options.length() - 2);
                ((TextView) viewGroup.findViewById(R.id.options)).append(options);
            }

            orderBinding.orderContainer.addView(viewGroup);
        }

        //장바구니
        else {
            shared = getSharedPreferences("basket", MODE_PRIVATE);

            basketCount = shared.getInt("basketCount", 1);
            //intent.getIntExtra("basketCount", 1);
            String totalPrice = shared.getString("totalPrice", "");
            //intent.getStringExtra("totalPrice");


//            String[] menuKey = new String[basketCount];
//            String[] menuName = new String[basketCount];
//            int[] menuAmount = new int[basketCount];
//            boolean[] option1checked = new boolean[basketCount];
//            boolean[] option2checked = new boolean[basketCount];
//            boolean[] option3checked = new boolean[basketCount];
//            boolean[] option4checked = new boolean[basketCount];
//            boolean[] option5checked = new boolean[basketCount];
//            String[] option1Name = new String[basketCount];
//            String[] option2Name = new String[basketCount];
//            String[] option3Name = new String[basketCount];
//            String[] option4Name = new String[basketCount];
//            String[] option5Name = new String[basketCount];
//            String basketPrice[] = new String[basketCount];
//            for (int i = 0; i < basketCount; i++) {
//                menuKey[i] = shared.getString("menuKey" + i, null);
//                menuAmount[i] = shared.getInt("menuAmount" + i, 1);
//                menuName[i] = shared.getString("menuName" + i, null);
//                option1checked[i] = shared.getBoolean("option1checked" + i, false);
//                option2checked[i] = shared.getBoolean("option2checked" + i, false);
//                option3checked[i] = shared.getBoolean("option3checked" + i, false);
//                option4checked[i] = shared.getBoolean("option4checked" + i, false);
//                option5checked[i] = shared.getBoolean("option5checked" + i, false);
//                option1Name[i] = shared.getString("option1Name" + i, null);
//                option2Name[i] = shared.getString("option2Name" + i, null);
//                option3Name[i] = shared.getString("option3Name" + i, null);
//                option4Name[i] = shared.getString("option4Name" + i, null);
//                option5Name[i] = shared.getString("option5Name" + i, null);
//                intent.getStringExtra("basketPrice" + i);
//            }


            for (int i = 0; i < basketCount; i++) {
                LinearLayout linearLayout = new LinearLayout(this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.order_item, linearLayout, false);
                ((TextView) viewGroup.findViewById(R.id.menuName)).setText(/* intent.getStringExtra("menuName"+i)*/shared.getString("menuName" + i, "") + " X " + shared.getInt("menuAmount" + i, 1));
                ((TextView) viewGroup.findViewById(R.id.menuPrice)).setText(shared.getString("basketPrice" + i, ""));

                boolean optionExist = false;
                String options = "";
                if (shared.getBoolean("option1checked" + i, false)) {
                    optionExist = true;
                    options += shared.getString("option1Name" + i, null) + ", ";
                }
                if (shared.getBoolean("option2checked" + i, false)) {
                    optionExist = true;
                    options += shared.getString("option2Name" + i, null) + ", ";
                }
                if (shared.getBoolean("option3checked" + i, false)) {
                    optionExist = true;
                    options += shared.getString("option3Name" + i, null) + ", ";
                }
                if (shared.getBoolean("option4checked" + i, false)) {
                    optionExist = true;
                    options += shared.getString("option4Name" + i, null) + ", ";
                }
                if (shared.getBoolean("option5checked" + i, false)) {
                    optionExist = true;
                    options += shared.getString("option5Name" + i, null) + ", ";
                }

                if (!optionExist) {
                    ((TextView) viewGroup.findViewById(R.id.options)).setText("없음");
                } else {
                    options = options.substring(0, options.length() - 2);
                    ((TextView) viewGroup.findViewById(R.id.options)).append(options);
                }
                orderBinding.orderContainer.addView(viewGroup);
            }
            orderBinding.finalPrice.setText(totalPrice);


//            LinearLayout linearLayout = new LinearLayout(this);
//            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.order_item, linearLayout, false);
//            ((TextView) viewGroup.findViewById(R.id.menuName)).setText(menuName + " X " + menuAmount);
//            ((TextView) viewGroup.findViewById(R.id.menuPrice)).setText(menuPrice + "원");
//            orderBinding.finalPrice.setText(menuPrice + "원");
//            boolean optionExist = false;
//            String options = "";
//            if (option1Name != null) {
//                optionExist = true;
//                options += option1Name + ", ";
//            }
//            if (option2Name != null) {
//                optionExist = true;
//                options += option2Name + ", ";
//            }
//            if (option3Name != null) {
//                optionExist = true;
//                options += option3Name + ", ";
//            }
//            if (option4Name != null) {
//                optionExist = true;
//                options += option4Name + ", ";
//            }
//            if (option5Name != null) {
//                optionExist = true;
//                options += option5Name + ", ";
//            }
//
//            if (!optionExist) {
//                ((TextView) viewGroup.findViewById(R.id.options)).setText("없음");
//            } else {
//                options = options.substring(0, options.length() - 2);
//                ((TextView) viewGroup.findViewById(R.id.options)).append(options);
//            }
//
//            orderBinding.orderContainer.addView(viewGroup);
        }

    }

    public void OnClicked(View v) {
        switch (v.getId()) {
            case R.id.seatButton://좌석선택
//                //야구장에 따라 좌석선택창 띄우기 (미구현)
//                switch(colCheck){
//                    case 0:
//                        intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
//                        startActivityForResult(intent,REQUEST_CODE_SEATSELECT);
//                        break;
//                    case 1:
//                        intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
//                        startActivityForResult(intent,REQUEST_CODE_SEATSELECT);
//                        break;
//                    case 2:
//                        intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
//                        startActivityForResult(intent,REQUEST_CODE_SEATSELECT);
//                        break;
//                    case 3:
//                        intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
//                        startActivityForResult(intent,REQUEST_CODE_SEATSELECT);
//                        break;
//                    case 4:
//                        intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
//                        startActivityForResult(intent,REQUEST_CODE_SEATSELECT);
//                        break;
//                    case 5:
//                        intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
//                        startActivityForResult(intent,REQUEST_CODE_SEATSELECT);
//                        break;
//                    case 6:
//                        intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
//                        startActivityForResult(intent,REQUEST_CODE_SEATSELECT);
//                        break;
//                    case 7:
//                        intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
//                        startActivityForResult(intent,REQUEST_CODE_SEATSELECT);
//                        break;
//                    case 8:
//                        intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
//                        startActivityForResult(intent,REQUEST_CODE_SEATSELECT);
//                        break;
//                    default:
//                        Toast.makeText(this, "야구장을 선택하여 주십시오.", Toast.LENGTH_SHORT).show();
//                }
                Intent intent;
                intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SEATSELECT);
                break;
            case R.id.orderButton://주문완료
                if ((isSetSeat || (orderBinding.blockNum.length() > 0 && orderBinding.rowNum.length() > 0 && orderBinding.seatNum.length() > 0)) && orderBinding.telEdit.length() > 0) {
                    switch (buying) {
                        case 0:
                            Toast.makeText(this, "결제방식을 선택해 주세요", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            if (colCheck != 0) {
                                block = orderBinding.blockNum.getText().toString();
                                row = orderBinding.rowNum.getText().toString();
                                seatNum = orderBinding.seatNum.getText().toString();
                                seat = "블럭 : " + block + "\n열 : " + row + "\n좌석 : " + seatNum;
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                            builder.setTitle("주문 확인");
                            builder.setMessage("이대로 주문하시겠습니까?\n좌석이 맞는 지 한번 더 확인해주세요\n" + seat);
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //데이터베이스 참조
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("order").push();

                                    //좌석 정보 넣기
                                    ref.child("row").setValue(row);
                                    ref.child("seatNum").setValue(seatNum);
                                    ref.child("block").setValue(block);

                                    //아이디 넣기
                                    ref.child("marketId").setValue(marketId);
                                    ref.child("userId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    //주문 정보 넣기
                                    //장바구니 주문
                                    if (isBasket) {
                                        ref.child("count").setValue(basketCount);
                                        ref.child("totalPrice").setValue(shared.getString("totalPrice", "0원"));
                                        for (int i = 0; i < basketCount; i++) {
                                            ref.child("menuName" + i).setValue(shared.getString("menuName" + i, ""));
                                            ref.child("menuAmount" + i).setValue(shared.getInt("menuAmount" + i, 1));
                                            ref.child("basketPrice" + i).setValue(shared.getString("basketPrice" + i, ""));
                                            if (shared.getBoolean("option1checked" + i, false)) {
                                                ref.child("option1Name" + i).setValue(shared.getString("option1Name" + i, null));
                                                ref.child("option1Price" + i).setValue(shared.getString("option1Price" + i, null));
                                            }
                                            if (shared.getBoolean("option2checked" + i, false)) {
                                                ref.child("option2Name" + i).setValue(shared.getString("option2Name" + i, null));
                                                ref.child("option2Price" + i).setValue(shared.getString("option2Price" + i, null));
                                            }
                                            if (shared.getBoolean("option3checked" + i, false)) {
                                                ref.child("option3Name" + i).setValue(shared.getString("option3Name" + i, null));
                                                ref.child("option3Price" + i).setValue(shared.getString("option3Price" + i, null));
                                            }
                                            if (shared.getBoolean("option4checked" + i, false)) {
                                                ref.child("option4Name" + i).setValue(shared.getString("option4Name" + i, null));
                                                ref.child("option4Price" + i).setValue(shared.getString("option4Price" + i, null));
                                            }
                                            if (shared.getBoolean("option5checked" + i, false)) {
                                                ref.child("option5Name" + i).setValue(shared.getString("option5Name" + i, null));
                                                ref.child("option5Price" + i).setValue(shared.getString("option5Price" + i, null));
                                            }


                                        }
                                    }
                                    //즉시 주문
                                    else{

                                    }
                                    if(buying==1){
                                        ref.child("pay").setValue("카드 결제");
                                    }
                                    else if(buying==2){
                                        ref.child("pay").setValue("현금 결제");
                                    }

                                    ref.child("userName").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                                    ref.child("userTel").setValue(orderBinding.telEdit.getText().toString());
                                    ref.child("userMemo").setValue(orderBinding.memoEdit.getText().toString());
                                    ref.child("date").setValue(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis())));

                                    shared.edit().clear().apply();
                                    Toast.makeText(getApplicationContext(), "주문이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            orderDialog = builder.create();
                            orderDialog.setCancelable(false);
                            orderDialog.setOnKeyListener(new Dialog.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface arg0, int keyCode,
                                                     KeyEvent event) {
                                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                                        orderDialog.dismiss();
                                    }
                                    return true;
                                }
                            });
                            orderDialog.show();

                            break;
                    }
                } else if (!(isSetSeat || (orderBinding.blockNum.length() > 0 && orderBinding.rowNum.length() > 0 && orderBinding.seatNum.length() > 0))) {
                    Toast.makeText(this, "좌석을 선택하거나 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else if (orderBinding.telEdit.length() == 0) {
                    Toast.makeText(this, "핸드폰 번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cardBuyText:  //카드결제
                orderBinding.moneyBuyText.setBackgroundResource(R.color.white);
                orderBinding.cardBuyText.setBackgroundResource(R.color.buttonColor);
                orderBinding.cardBuyText.setTextColor(getResources().getColor(R.color.white));
                orderBinding.moneyBuyText.setTextColor(getResources().getColor(R.color.darkgray));
                buying = 1;
                break;
            case R.id.moneyBuyText: //현금결제
                orderBinding.moneyBuyText.setBackgroundResource(R.color.buttonColor);
                orderBinding.cardBuyText.setBackgroundResource(R.color.white);
                orderBinding.moneyBuyText.setTextColor(getResources().getColor(R.color.white));
                orderBinding.cardBuyText.setTextColor(getResources().getColor(R.color.darkgray));
                buying = 2;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SEATSELECT:
                if (resultCode == RESULT_OK) {
                    seat = data.getStringExtra("seat");
                    row = data.getStringExtra("row");
                    seatNum = data.getStringExtra("seatNum");
                    int blockNum = data.getIntExtra("numBlock", 0);
                    String blockString = data.getStringExtra("stringBlock");
                    orderBinding.seatText.setText(seat);
                    isSetSeat = true;
                    orderBinding.seatContainerJamsil.setVisibility(View.VISIBLE);
//                    orderBinding.seatText.setVisibility(View.VISIBLE);
//                    orderBinding.seatImage.setVisibility(View.VISIBLE);
                    if (blockNum != 0) {
                        switch (blockNum) {
                            case 334:
                                imageGlide(R.drawable.seat334);
                                break;
                            case 333:
                                imageGlide(R.drawable.seat333);
                                break;
                            case 332:
                                imageGlide(R.drawable.seat332);
                                break;
                            case 331:
                                imageGlide(R.drawable.seat331);
                                break;
                            case 330:
                                imageGlide(R.drawable.seat330);
                                break;
                            case 329:
                                imageGlide(R.drawable.seat329);
                                break;
                            case 328:
                                imageGlide(R.drawable.seat328);
                                break;
                            case 327:
                                imageGlide(R.drawable.seat327);
                                break;
                            case 326:
                                imageGlide(R.drawable.seat326);
                                break;
                            case 325:
                                imageGlide(R.drawable.seat325);
                                break;
                            case 324:
                                imageGlide(R.drawable.seat324);
                                break;
                            case 323:
                                imageGlide(R.drawable.seat323);
                                break;
                            case 322:
                                imageGlide(R.drawable.seat322);
                                break;
                            case 321:
                                imageGlide(R.drawable.seat321);
                                break;
                            case 320:
                                imageGlide(R.drawable.seat320);
                                break;
                            case 319:
                                imageGlide(R.drawable.seat319);
                                break;
                            case 318:
                                imageGlide(R.drawable.seat318);
                                break;
                            case 317:
                                imageGlide(R.drawable.seat317);
                                break;
                            case 316:
                                imageGlide(R.drawable.seat316);
                                break;
                            case 315:
                                imageGlide(R.drawable.seat315);
                                break;
                            case 314:
                                imageGlide(R.drawable.seat314);
                                break;
                            case 313:
                                imageGlide(R.drawable.seat313);
                                break;
                            case 312:
                                imageGlide(R.drawable.seat312);
                                break;
                            case 311:
                                imageGlide(R.drawable.seat311);
                                break;
                            case 310:
                                imageGlide(R.drawable.seat310);
                                break;
                            case 309:
                                imageGlide(R.drawable.seat309);
                                break;
                            case 308:
                                imageGlide(R.drawable.seat308);
                                break;
                            case 307:
                                imageGlide(R.drawable.seat307);
                                break;
                            case 306:
                                imageGlide(R.drawable.seat306);
                                break;
                            case 305:
                                imageGlide(R.drawable.seat305);
                                break;
                            case 304:
                                imageGlide(R.drawable.seat304);
                                break;
                            case 303:
                                imageGlide(R.drawable.seat303);
                                break;
                            case 302:
                                imageGlide(R.drawable.seat302);
                                break;
                            case 301:
                                imageGlide(R.drawable.seat301);
                                break;
                            case 201:
                                imageGlide(R.drawable.seat201);
                                break;
                            case 202:
                                imageGlide(R.drawable.seat202);
                                break;
                            case 203:
                                imageGlide(R.drawable.seat203);
                                break;
                            case 204:
                                imageGlide(R.drawable.seat204);
                                break;
                            case 205:
                                imageGlide(R.drawable.seat205);
                                break;
                            case 206:
                                imageGlide(R.drawable.seat206);
                                break;
                            case 207:
                                imageGlide(R.drawable.seat207);
                                break;
                            case 208:
                                imageGlide(R.drawable.seat208);
                                break;
                            case 209:
                                imageGlide(R.drawable.seat209);
                                break;
                            case 210:
                                imageGlide(R.drawable.seat210);
                                break;
                            case 211:
                                imageGlide(R.drawable.seat211);
                                break;
                            case 212:
                                imageGlide(R.drawable.seat212);
                                break;
                            case 213:
                                imageGlide(R.drawable.seat213);
                                break;
                            case 214:
                                imageGlide(R.drawable.seat214);
                                break;
                            case 215:
                                imageGlide(R.drawable.seat215);
                                break;
                            case 216:
                                imageGlide(R.drawable.seat216);
                                break;
                            case 217:
                                imageGlide(R.drawable.seat217);
                                break;
                            case 218:
                                imageGlide(R.drawable.seat218);
                                break;
                            case 219:
                                imageGlide(R.drawable.seat219);
                                break;
                            case 220:
                                imageGlide(R.drawable.seat220);
                                break;
                            case 221:
                                imageGlide(R.drawable.seat221);
                                break;
                            case 222:
                                imageGlide(R.drawable.seat222);
                                break;
                            case 223:
                                imageGlide(R.drawable.seat223);
                                break;
                            case 224:
                                imageGlide(R.drawable.seat224);
                                break;
                            case 225:
                                imageGlide(R.drawable.seat225);
                                break;
                            case 226:
                                imageGlide(R.drawable.seat226);
                                break;
                            case 101:
                                imageGlide(R.drawable.seat101);
                                break;
                            case 102:
                                imageGlide(R.drawable.seat102);
                                break;
                            case 103:
                                imageGlide(R.drawable.seat103);
                                break;
                            case 104:
                                imageGlide(R.drawable.seat104);
                                break;
                            case 105:
                                imageGlide(R.drawable.seat105);
                                break;
                            case 106:
                                imageGlide(R.drawable.seat106);
                                break;
                            case 107:
                                imageGlide(R.drawable.seat107);
                                break;
                            case 108:
                                imageGlide(R.drawable.seat108);
                                break;
                            case 109:
                                imageGlide(R.drawable.seat109);
                                break;
                            case 110:
                                imageGlide(R.drawable.seat110);
                                break;
                            case 111:
                                imageGlide(R.drawable.seat111);
                                break;
                            case 112:
                                imageGlide(R.drawable.seat112);
                                break;
                            case 113:
                                imageGlide(R.drawable.seat113);
                                break;
                            case 114:
                                imageGlide(R.drawable.seat114);
                                break;
                            case 115:
                                imageGlide(R.drawable.seat115);
                                break;
                            case 116:
                                imageGlide(R.drawable.seat116);
                                break;
                            case 117:
                                imageGlide(R.drawable.seat117);
                                break;
                            case 118:
                                imageGlide(R.drawable.seat118);
                                break;
                            case 119:
                                imageGlide(R.drawable.seat119);
                                break;
                            case 120:
                                imageGlide(R.drawable.seat120);
                                break;
                            case 121:
                                imageGlide(R.drawable.seat121);
                                break;
                            case 122:
                                imageGlide(R.drawable.seat122);
                                break;
                            case 401:
                                imageGlide(R.drawable.seat401);
                                break;
                            case 402:
                                imageGlide(R.drawable.seat402);
                                break;
                            case 403:
                                imageGlide(R.drawable.seat403);
                                break;
                            case 404:
                                imageGlide(R.drawable.seat404);
                                break;
                            case 405:
                                imageGlide(R.drawable.seat405);
                                break;
                            case 406:
                                imageGlide(R.drawable.seat406);
                                break;
                            case 407:
                                imageGlide(R.drawable.seat407);
                                break;
                            case 408:
                                imageGlide(R.drawable.seat408);
                                break;
                            case 409:
                                imageGlide(R.drawable.seat409);
                                break;
                            case 410:
                                imageGlide(R.drawable.seat410);
                                break;
                            case 411:
                                imageGlide(R.drawable.seat411);
                                break;
                            case 412:
                                imageGlide(R.drawable.seat412);
                                break;
                            case 413:
                                imageGlide(R.drawable.seat413);
                                break;
                            case 414:
                                imageGlide(R.drawable.seat414);
                                break;
                            case 415:
                                imageGlide(R.drawable.seat415);
                                break;
                            case 416:
                                imageGlide(R.drawable.seat416);
                                break;
                            case 417:
                                imageGlide(R.drawable.seat417);
                                break;
                            case 418:
                                imageGlide(R.drawable.seat418);
                                break;
                            case 419:
                                imageGlide(R.drawable.seat419);
                                break;
                            case 420:
                                imageGlide(R.drawable.seat420);
                                break;
                            case 421:
                                imageGlide(R.drawable.seat421);
                                break;
                            case 422:
                                imageGlide(R.drawable.seat422);
                                break;
                        }

                        block = blockNum + "";
                    } else {
                        if (blockString.equals("PREMIUM")) {
                            imageGlide(R.drawable.jampremium);
                        } else if (blockString.equals("1루 EXCITING")) {
                            imageGlide(R.drawable.jamexciting1);
                        } else if (blockString.equals("3루 EXCITING")) {
                            imageGlide(R.drawable.jamexciting3);
                        }

                        block = blockString;
                    }
                }
        }
    }

    //이미지 글라이드
    public void imageGlide(int src) {
        Glide.with(this)
                .load(src)
                .into(orderBinding.seatImage);
    }
}
