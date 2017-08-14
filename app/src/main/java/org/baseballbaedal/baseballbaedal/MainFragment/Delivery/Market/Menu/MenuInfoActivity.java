package org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.Menu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.baseballbaedal.baseballbaedal.LoginActivity;
import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.Order.OrderActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityMenuInfoBinding;

import static org.baseballbaedal.baseballbaedal.MainActivity.isBusiness;

public class MenuInfoActivity extends NewActivity {

    ActivityMenuInfoBinding binding;

    String aTime;
    String menuKey;
    String marketName;
    String marketId;
    String menuName;
    String menuExplain;
    int menuPrice;
    String option1Name;
    int option1Price;
    int checkOption1Price;
    boolean option1Checked;
    String option2Name;
    int option2Price;
    int checkOption2Price;
    boolean option2Checked;
    String option3Name;
    int option3Price;
    int checkOption3Price;
    boolean option3Checked;
    String option4Name;
    int option4Price;
    int checkOption4Price;
    boolean option4Checked;
    String option5Name;
    int option5Price;
    int checkOption5Price;
    boolean option5Checked;
    String minPrice;

    int totalPrice;
    AlertDialog dialog;

    int foodCount = 1;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_info);
        shared = getSharedPreferences("basket", MODE_PRIVATE);
        Intent intent = getIntent();
        aTime = intent.getStringExtra("aTime");
        menuKey = intent.getStringExtra("menuKey");
        marketName = intent.getStringExtra("marketName");
        marketId = intent.getStringExtra("marketId");
        minPrice = intent.getStringExtra("minPrice");
        menuName = intent.getStringExtra("menuName");
        menuExplain = intent.getStringExtra("menuExplain");
        menuPrice = Integer.parseInt(intent.getStringExtra("menuPrice"));
        option1Name = intent.getStringExtra("option1Name");
        if (option1Name != null) {
            option1Price = Integer.parseInt(intent.getStringExtra("option1Price"));

            binding.allOptionContainer.setVisibility(View.VISIBLE);
            binding.commonContainer.setVisibility(View.GONE);
            binding.amountContainer.setVisibility(View.GONE);
            binding.amountLine.setVisibility(View.GONE);

            binding.optionContainer1.setVisibility(View.VISIBLE);
            binding.optionName1.setText(option1Name);
            binding.optionPrice1.setText(numToWon(option1Price) + "원");

            option2Name = intent.getStringExtra("option2Name");
            if (option2Name != null) {
                option2Price = Integer.parseInt(intent.getStringExtra("option2Price"));
                binding.optionContainer2.setVisibility(View.VISIBLE);
                binding.optionName2.setText(option2Name);
                binding.optionPrice2.setText(numToWon(option2Price) + "원");

                option3Name = intent.getStringExtra("option3Name");
                if (option3Name != null) {
                    option3Price = Integer.parseInt(intent.getStringExtra("option3Price"));
                    binding.optionContainer3.setVisibility(View.VISIBLE);
                    binding.optionName3.setText(option3Name);
                    binding.optionPrice3.setText(numToWon(option3Price) + "원");

                    option4Name = intent.getStringExtra("option4Name");
                    if (option4Name != null) {
                        option4Price = Integer.parseInt(intent.getStringExtra("option4Price"));
                        binding.optionContainer4.setVisibility(View.VISIBLE);
                        binding.optionName4.setText(option4Name);
                        binding.optionPrice4.setText(numToWon(option4Price) + "원");

                        option5Name = intent.getStringExtra("option5Name");
                        if (option5Name != null) {
                            option5Price = Integer.parseInt(intent.getStringExtra("option5Price"));
                            binding.optionContainer5.setVisibility(View.VISIBLE);
                            binding.optionName5.setText(option5Name);
                            binding.optionPrice5.setText(numToWon(option5Price) + "원");
                        }
                    }
                }
            }
        }
        if(intent.getBooleanExtra("isTakeout",false)){
            binding.buttonContainer.setVisibility(View.GONE);
        }


        binding.container.addView(getToolbar(marketName, true), 0);
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(marketId).child("menu").child(menuKey + ".jpg");
        try {
            Glide
                    .with(MenuInfoActivity.this)
                    .using(new FirebaseImageLoader())
                    .load(ref)
                    .signature(new StringSignature(aTime)) //이미지저장시간
                    .placeholder(R.drawable.jamsil)
                    .thumbnail(0.1f)
                    .crossFade()
                    .into(binding.menuImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.menuName.setText(menuName);
        binding.menuExplain.setText(menuExplain);
        binding.menuPrice.setText(numToWon(menuPrice) + "원");
        binding.minPrice1.setText(numToWon(Integer.parseInt(minPrice)) + "원");
        binding.minPrice2.setText(numToWon(Integer.parseInt(minPrice)) + "원");
        binding.totalPrice1.setText(numToWon(menuPrice) + "원");
        binding.totalPrice2.setText(numToWon(menuPrice) + "원");


        checkOption1Price = 0;
        checkOption2Price = 0;
        checkOption3Price = 0;
        checkOption4Price = 0;
        checkOption5Price = 0;

        binding.optionCheck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    checkOption1Price = option1Price;
                    option1Checked = true;
                } else {
                    checkOption1Price = 0;
                    option1Checked = false;
                }

                calculatePrice();
            }
        });

        binding.optionCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    checkOption2Price = option2Price;
                    option2Checked = true;
                } else {
                    checkOption2Price = 0;
                    option2Checked = false;
                }

                calculatePrice();
            }
        });

        binding.optionCheck3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    checkOption3Price = option3Price;
                    option3Checked = true;
                } else {
                    checkOption3Price = 0;
                    option3Checked = false;
                }

                calculatePrice();
            }
        });

        binding.optionCheck4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    checkOption4Price = option4Price;
                    option4Checked = true;
                } else {
                    checkOption4Price = 0;
                    option4Checked = false;
                }

                calculatePrice();
            }
        });

        binding.optionCheck5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    checkOption5Price = option5Price;
                    option5Checked = true;
                } else {
                    checkOption5Price = 0;
                    option5Checked = false;
                }

                calculatePrice();
            }
        });

        binding.plusButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodCount++;
                binding.menuAmount1.setText(foodCount + "");
                binding.menuAmount2.setText(foodCount + "");
                calculatePrice();
            }
        });

        binding.plusButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodCount++;
                binding.menuAmount1.setText(foodCount + "");
                binding.menuAmount2.setText(foodCount + "");
                calculatePrice();
            }
        });

        binding.minusButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodCount > 1)
                    foodCount--;
                binding.menuAmount1.setText(foodCount + "");
                binding.menuAmount2.setText(foodCount + "");
                calculatePrice();
            }
        });

        binding.minusButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodCount > 1)
                    foodCount--;
                binding.menuAmount1.setText(foodCount + "");
                binding.menuAmount2.setText(foodCount + "");
                calculatePrice();
            }
        });
        binding.basketButton.setButtonColor(getResources().getColor(R.color.buttonColor));
        binding.basketButton.setCornerRadius(15);
        binding.basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences.Editor editor = shared.edit();
                String marketId1 = shared.getString("marketId", null);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if(isBusiness==0||isBusiness==1)
                    {
                        //장바구니에 항목이 존재할 때
                        if (marketId1 != null) {

                            //현재 장바구니에 있는 음식점과 다른 음식점일 경우
                            if (!marketId1.equals(marketId)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MenuInfoActivity.this);
                                builder.setTitle("장바구니 알림");
                                builder.setMessage("장바구니에는 한 음식점의 메뉴만 담을 수 있습니다.\n확인을 누르시면 이전에 담은 메뉴가 초기화됩니다.\n담으시겠습니까?");
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        shared = getSharedPreferences("basket", MODE_PRIVATE);
                                        shared.edit().clear().apply();
                                        editor.putString("marketId", marketId);
                                        editor.putString("marketName", marketName);
                                        editor.putString("minPrice", minPrice);
                                        editor.putString("menuKey0", menuKey);
                                        editor.putInt("menuAmount0", foodCount);
                                        editor.putBoolean("option1checked0", option1Checked);
                                        editor.putBoolean("option2checked0", option2Checked);
                                        editor.putBoolean("option3checked0", option3Checked);
                                        editor.putBoolean("option4checked0", option4Checked);
                                        editor.putBoolean("option5checked0", option5Checked);
                                        editor.putInt("basketCount", 1);
                                        editor.commit();
                                        Toast.makeText(MenuInfoActivity.this, "장바구니에 담았습니다.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                dialog = builder.create();
                                dialog.setCancelable(false);
                                dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface arg0, int keyCode,
                                                         KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                                            dialog.dismiss();
                                        }
                                        return true;
                                    }
                                });
                                dialog.show();
                            }

                            //같은 음식점일 경우
                            else {
                                int index = shared.getInt("basketCount", 0);
                                if (index < 30) {
                                    boolean isExist = false;
                                    int check = 0;
                                    int menuAmount;
                                    boolean option1checked;
                                    boolean option2checked;
                                    boolean option3checked;
                                    boolean option4checked;
                                    boolean option5checked;
                                    for (int i = 0; i < 30; i++) {
                                        if (shared.getString("menuKey" + i, "").equals(menuKey)) {

                                            Log.d("같은메뉴", i + "번째");
                                            option1checked = shared.getBoolean("option1checked" + i, false);
                                            option2checked = shared.getBoolean("option2checked" + i, false);
                                            option3checked = shared.getBoolean("option3checked" + i, false);
                                            option4checked = shared.getBoolean("option4checked" + i, false);
                                            option5checked = shared.getBoolean("option5checked" + i, false);

                                            if (option1checked == option1Checked && option2checked == option2Checked && option3checked == option3Checked && option4checked == option4Checked && option5checked == option5Checked) {
                                                menuAmount = shared.getInt("menuAmount" + i, 1);
                                                editor.putInt("menuAmount" + i, menuAmount + 1);
                                                editor.commit();
                                                isExist = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!isExist) {
                                        editor.putString("menuKey" + index, menuKey);
                                        editor.putInt("menuAmount" + index, foodCount);
                                        editor.putBoolean("option1checked" + index, option1Checked);
                                        editor.putBoolean("option2checked" + index, option2Checked);
                                        editor.putBoolean("option3checked" + index, option3Checked);
                                        editor.putBoolean("option4checked" + index, option4Checked);
                                        editor.putBoolean("option5checked" + index, option5Checked);
                                        editor.putInt("basketCount", index + 1);
                                        editor.commit();
                                    }
                                } else {
                                    Toast.makeText(MenuInfoActivity.this, "장바구니 최대 개수를 초과했습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                Toast.makeText(MenuInfoActivity.this, "장바구니에 담았습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        //장바구니에 항목이 없을 경우
                        else {

                            editor.putString("marketId", marketId);
                            editor.putString("marketName", marketName);
                            editor.putString("minPrice", minPrice);

                            editor.putString("menuKey0", menuKey);

                            editor.putInt("menuAmount0", foodCount);
                            editor.putBoolean("option1checked0", option1Checked);
                            editor.putBoolean("option2checked0", option2Checked);
                            editor.putBoolean("option3checked0", option3Checked);
                            editor.putBoolean("option4checked0", option4Checked);
                            editor.putBoolean("option5checked0", option5Checked);
                            editor.putInt("basketCount", 1);
                            editor.commit();

                            Toast.makeText(MenuInfoActivity.this, "장바구니에 담았습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    //사장 고객인 경우
                    else{
                        Toast.makeText(MenuInfoActivity.this, "사업자 고객은 주문을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    pleaseLogin();
                }


            }
        });
        binding.nowOrder.setButtonColor(getResources().getColor(R.color.buttonColor));
        binding.nowOrder.setCornerRadius(15);
        binding.nowOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if(isBusiness==0||isBusiness==1){
                        if(totalPrice<Integer.parseInt(minPrice)){
                            Toast.makeText(MenuInfoActivity.this, "주문하시는 금액이 최소 주문 금액보다 작습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                    else {
                        Toast.makeText(MenuInfoActivity.this, "사업자 고객은 주문을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    pleaseLogin();
                }
            }
        });
    }

    //로그인 해달라는 창을 띄우는 메서드
    public void pleaseLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuInfoActivity.this);
        builder.setTitle("알림");
        builder.setMessage("먼저 로그인을 해주세요");
        builder.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MenuInfoActivity.this, LoginActivity.class);
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

    public void calculatePrice() {
        totalPrice = (menuPrice + checkOption1Price + checkOption2Price + checkOption3Price + checkOption4Price + checkOption5Price) * foodCount;
        binding.totalPrice1.setText(numToWon(totalPrice) + "원");
        binding.totalPrice2.setText(numToWon(totalPrice) + "원");
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
