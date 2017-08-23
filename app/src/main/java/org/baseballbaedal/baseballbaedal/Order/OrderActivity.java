package org.baseballbaedal.baseballbaedal.Order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityOrderBinding;


public class OrderActivity extends NewActivity {
    public static final int REQUEST_CODE_SEATSELECT = 1;

    ActivityOrderBinding orderBinding;

    int buying = 0; // 1: 카드결제 2: 현금결제
    int colCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderBinding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        orderBinding.container.addView(getToolbar("주문하기", true), 0);

        orderBinding.orderButton.setButtonColor(getResources().getColor(R.color.buttonColor));
        orderBinding.orderButton.setCornerRadius(15);
        orderBinding.seatButton.setButtonColor(getResources().getColor(R.color.buttonColor));
        orderBinding.seatButton.setCornerRadius(15);


        //현재 선택된 야구장 데이터 가져오기
        SharedPreferences colCheckpref = getSharedPreferences("selectedCol", MODE_PRIVATE);
        colCheck = colCheckpref.getInt("selectedCol", -1);
        switch (colCheck) {
            case 0:
                orderBinding.stadiumText.setText("잠실 야구장(두산,LG)");
                break;
            case 1:
                orderBinding.stadiumText.setText("고척 스카이돔(넥센)");
                break;
            case 2:
                orderBinding.stadiumText.setText("SK 행복드림구장");
                break;
            case 3:
                orderBinding.stadiumText.setText("한화 이글스파크");
                break;
            case 4:
                orderBinding.stadiumText.setText("삼성 라이온즈파크");
                break;
            case 5:
                orderBinding.stadiumText.setText("기아 챔피언스필드");
                break;
            case 6:
                orderBinding.stadiumText.setText("사직 야구장(롯데)");
                break;
            case 7:
                orderBinding.stadiumText.setText("KT 위즈파크");
                break;
            case 8:
                orderBinding.stadiumText.setText("마산 야구장(NC)");
                break;
            default:
                orderBinding.stadiumText.setText("선택된 야구장이 없습니다.");
                break;
        }

    }

    public void OnClicked(View v) {
        switch (v.getId()) {
            case R.id.seatButton://좌석선택
                Intent intent;
                //야구장에 따라 좌석선택창 띄우기 (미구현)
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
                intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SEATSELECT);
                break;
            case R.id.orderButton://주문완료
                switch (buying) {
                    case 1:
                        Toast.makeText(this, "카드결제", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(this, "현금결제", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "결제방식 선택하셈", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cardBuyText:  //카드결제
                orderBinding.moneyBuyText.setBackgroundResource(R.drawable.border_background_white_bottom);
                orderBinding.cardBuyText.setBackgroundResource(R.drawable.border_background_blue_top);
                buying = 1;
                break;
            case R.id.moneyBuyText: //현금결제
                orderBinding.moneyBuyText.setBackgroundResource(R.drawable.border_background_blue_bottom);
                orderBinding.cardBuyText.setBackgroundResource(R.drawable.border_background_white_top);
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
                    String seat = data.getStringExtra("seat");
                    int blockNum = data.getIntExtra("block", 0);
                    orderBinding.seatText.setText(seat);
                    orderBinding.seatContainerJamsil.setVisibility(View.VISIBLE);
//                    orderBinding.seatText.setVisibility(View.VISIBLE);
//                    orderBinding.seatImage.setVisibility(View.VISIBLE);
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
