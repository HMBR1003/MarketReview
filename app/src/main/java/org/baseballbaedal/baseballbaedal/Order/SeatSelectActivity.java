package org.baseballbaedal.baseballbaedal.Order;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivitySeatSelectBinding;

import uk.co.senab.photoview.PhotoViewAttacher;

//좌석선택 액티비티
public class SeatSelectActivity extends NewActivity {

    public static final int NORMAL = 0;
    public static final int PREMIUM = 1;
    public static final int EXCITING1 = 2;
    public static final int EXCITING3 = 3;

    ActivitySeatSelectBinding selectBinding;
    PhotoViewAttacher attacher;

    int spinnerPosition;
    String premiumBlock = "";
    String block, raw, num;
    String blockNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectBinding = DataBindingUtil.setContentView(this, R.layout.activity_seat_select);
        selectBinding.container.addView(getToolbar("좌석 선택", true), 0);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.seat, android.R.layout.simple_spinner_item);
        selectBinding.spinner.setAdapter(adapter);

        selectBinding.spinner.setOnItemSelectedListener(itemSelectedListener);

        spinnerPosition = 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //스피너 리스너
    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case NORMAL:
                    selectBinding.blockLayout.setVisibility(View.VISIBLE);
                    imageGlide(R.drawable.jammain);
                    selectBinding.blockEdit.setText("");
                    selectBinding.rawEdit.setText("");
                    selectBinding.numEdit.setText("");
                    selectBinding.blockEdit.requestFocus();
                    spinnerPosition = NORMAL;
                    break;
                case PREMIUM:
                    selectBinding.blockLayout.setVisibility(View.GONE);
                    imageGlide(R.drawable.jampremium);
                    selectBinding.rawEdit.setText("");
                    selectBinding.numEdit.setText("");
                    selectBinding.rawEdit.requestFocus();
                    spinnerPosition = PREMIUM;
                    break;
                case EXCITING1:
                    selectBinding.blockLayout.setVisibility(View.GONE);
                    imageGlide(R.drawable.jamexciting1);
                    selectBinding.rawEdit.setText("");
                    selectBinding.numEdit.setText("");
                    selectBinding.rawEdit.requestFocus();
                    spinnerPosition = EXCITING1;
                    break;
                case EXCITING3:
                    selectBinding.blockLayout.setVisibility(View.GONE);
                    imageGlide(R.drawable.jamexciting3);
                    selectBinding.rawEdit.setText("");
                    selectBinding.numEdit.setText("");
                    selectBinding.rawEdit.requestFocus();
                    spinnerPosition = EXCITING3;
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //클릭 리스너
    public void OnClicked(View v) {
        switch (v.getId()) {
            case R.id.blockButton:

                blockNum = selectBinding.blockEdit.getText().toString();
                if (blockNum.length() != 0) {
                    switch (Integer.parseInt(blockNum)) {
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
                        default:
                            Toast.makeText(this, "번호를 다시 확인하여 주십시오.", Toast.LENGTH_SHORT).show();
                            selectBinding.blockEdit.setText("");
                            selectBinding.blockEdit.requestFocus();
                    }
                } else
                    Toast.makeText(this, "블록 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.selectButton:
                block = selectBinding.blockEdit.getText().toString();
                raw = selectBinding.rawEdit.getText().toString();
                num = selectBinding.numEdit.getText().toString();
                boolean blockbool = false;

                if (spinnerPosition == NORMAL) {
                    if (block.length() != 0 && raw.length() != 0 && num.length() != 0) {
                        for (int i = 101; i <= 122; i++)
                            if (Integer.parseInt(block) == i) {
                                blockbool = true;
                                break;
                            }
                        if (blockbool == false)
                            for (int i = 201; i <= 226; i++)
                                if (Integer.parseInt(block) == i) {
                                    blockbool = true;
                                    break;
                                }
                        if (blockbool == false)
                            for (int i = 301; i <= 334; i++)
                                if (Integer.parseInt(block) == i) {
                                    blockbool = true;
                                    break;
                                }
                        if (blockbool == false)
                            for (int i = 401; i <= 422; i++)
                                if (Integer.parseInt(block) == i) {
                                    blockbool = true;
                                    break;
                                }
                        if (blockbool == true) {
                            alertDialog(block, raw, num,true);
                        } else {
                            Toast.makeText(this, "블록 번호를 다시 확인하여 주십시오.", Toast.LENGTH_SHORT).show();
                            selectBinding.blockEdit.setText("");
                            selectBinding.blockEdit.requestFocus();
                        }
                    } else if (block.length() == 0) {
                        Toast.makeText(this, "블록 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                        selectBinding.blockEdit.requestFocus();
                    } else if (raw.length() == 0) {
                        Toast.makeText(this, "열 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                        selectBinding.rawEdit.requestFocus();
                    } else if (num.length() == 0) {
                        Toast.makeText(this, "좌석 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                        selectBinding.numEdit.requestFocus();
                    }
                } else {

                    switch (spinnerPosition) {
                        case PREMIUM:
                            if (raw.length() != 0 && num.length() != 0) {
                                premiumBlock = "PREMIUM";
                                alertDialog(premiumBlock, raw, num,false);
                            }  else if (raw.length() == 0) {
                                Toast.makeText(this, "열 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                                selectBinding.rawEdit.requestFocus();
                            } else if (num.length() == 0) {
                                Toast.makeText(this, "좌석 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                                selectBinding.numEdit.requestFocus();
                            }
                            break;
                        case EXCITING1:
                            if (raw.length() != 0 && num.length() != 0) {
                                premiumBlock = "1루 EXCITING";
                                alertDialog(premiumBlock, raw, num,false);
                            } else if (raw.length() == 0) {
                                Toast.makeText(this, "열 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                                selectBinding.rawEdit.requestFocus();
                            } else if (num.length() == 0) {
                                Toast.makeText(this, "좌석 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                                selectBinding.numEdit.requestFocus();
                            }

                            break;
                        case EXCITING3:
                            if (raw.length() != 0 && num.length() != 0) {
                                premiumBlock = "3루 EXCITING";
                                alertDialog(premiumBlock, raw, num,false);
                            } else if (raw.length() == 0) {
                                Toast.makeText(this, "열 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                                selectBinding.rawEdit.requestFocus();
                            } else if (num.length() == 0) {
                                Toast.makeText(this, "좌석 번호를 입력하여 주십시오.", Toast.LENGTH_SHORT).show();
                                selectBinding.numEdit.requestFocus();
                            }
                            break;
                    }
                }
                break;
            case R.id.imageview:
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.activity_image_zoom);
                ImageView imageView = (ImageView) dialog.findViewById(R.id.imageView);
                imageView.setImageResource(R.drawable.jammain);
                attacher = new PhotoViewAttacher(imageView);
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                dialog.getWindow().setAttributes(params);
                break;
        }
    }


    //좌석확인 다이알로그
    public void alertDialog(final String block, final String row, final String num, final boolean isNum) {
        final String seatString = "블럭 : " + block + "\n열 : " + raw + "\n좌석 : " + num;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("좌석확인");
        dialogBuilder.setMessage("좌석이 맞는지 확인하여 주십시오.\n" + seatString)
                .setCancelable(false)
                .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                        intent.putExtra("seat", seatString);
                        intent.putExtra("row",row);
                        intent.putExtra("seatNum",num);
                        if(isNum) {
                            intent.putExtra("numBlock", Integer.parseInt(block));
                        }
                        else {
                            intent.putExtra("stringBlock", block);
                        }
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    //이미지 글라이드
    public void imageGlide(int src) {
        Glide.with(this)
                .load(src)
                .into(selectBinding.imageview);
    }
}
