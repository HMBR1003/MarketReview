package org.baseballbaedal.baseballbaedal.BusinessMan;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityMenuAddBinding;

import java.io.File;

public class MenuAddActivity extends AppCompatActivity {
    private static final int REQUEST_CROP = 6000;
    private static final int GET_MARKET_IMAGE = 7000 ;

    ActivityMenuAddBinding binding;
    int optionIndex = 0;
    AlertDialog exitDialog;
    AlertDialog checkDialog;
    AlertDialog submitDialog;
    InputMethodManager imm;
    DatabaseReference myRef;
    String uid;
    Uri sendUri;
    private Uri tempImageUri;
    private Uri imageCropUri;
    Bitmap bitmap = null;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            binding.explainLimit.setText(binding.menuExplain.length()+" / 80자");

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_add);
        //타이틀 설정
        binding.toolBar.setTitle("메뉴 추가");
        binding.toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolBar);
        //뒤로가기 버튼 만들기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //키보드 자동 올리기 관련
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //메뉴설명란 입력문자 수 읽어오기
        binding.menuExplain.addTextChangedListener(watcher);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //이미지뷰 클릭설정
        binding.menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zoomIntent = new Intent(MenuAddActivity.this, LogoViewActivity.class);
                zoomIntent.putExtra("imageUri",sendUri);
                startActivity(zoomIntent);
            }
        });
        binding.loadMenuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GET_MARKET_IMAGE);
            }
        });

        //옵션 추가하는 버튼 기능 설정
        binding.optionAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (optionIndex) {
                    case 0:
                        binding.optionSet1.setVisibility(View.VISIBLE);
                        //포커스 주기
                        binding.optionName1.requestFocus();
                        //키보드 보이기
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        optionIndex++;
                        break;
                    case 1:
                        binding.optionSet2.setVisibility(View.VISIBLE);
                        //포커스 주기
                        binding.optionName2.requestFocus();
                        //키보드 보이기
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        optionIndex++;
                        break;
                    case 2:
                        binding.optionSet3.setVisibility(View.VISIBLE);
                        //포커스 주기
                        binding.optionName3.requestFocus();
                        //키보드 보이기
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        optionIndex++;
                        break;
                    case 3:
                        binding.optionSet4.setVisibility(View.VISIBLE);
                        //포커스 주기
                        binding.optionName4.requestFocus();
                        //키보드 보이기
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        optionIndex++;
                        break;
                    case 4:
                        binding.optionSet5.setVisibility(View.VISIBLE);
                        //포커스 주기
                        binding.optionName5.requestFocus();
                        //키보드 보이기
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        optionIndex++;
                        break;
                    default:
                        Toast.makeText(MenuAddActivity.this, "옵션은 5개까지만 추가 가능합니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        //옵션 삭제하는 버튼 기능 설정
        binding.optionRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionIndex == 0) {
                    Toast.makeText(MenuAddActivity.this, "삭제할 옵션이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuAddActivity.this);
                    builder.setTitle("옵션 삭제 확인");
                    builder.setMessage(optionIndex + "번 옵션을 삭제하시겠습니까? 작성한 내용이 사라집니다.");
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (optionIndex) {
                                case 5:
                                    binding.optionSet5.setVisibility(View.GONE);
                                    binding.optionName5.setText(null);
                                    binding.optionPrice5.setText(null);
                                    optionIndex--;
                                    break;
                                case 4:
                                    binding.optionSet4.setVisibility(View.GONE);
                                    binding.optionName4.setText(null);
                                    binding.optionPrice4.setText(null);
                                    optionIndex--;
                                    break;
                                case 3:
                                    binding.optionSet3.setVisibility(View.GONE);
                                    binding.optionName3.setText(null);
                                    binding.optionPrice3.setText(null);
                                    optionIndex--;
                                    break;
                                case 2:
                                    binding.optionSet2.setVisibility(View.GONE);
                                    binding.optionName2.setText(null);
                                    binding.optionPrice2.setText(null);
                                    optionIndex--;
                                    break;
                                case 1:
                                    binding.optionSet1.setVisibility(View.GONE);
                                    binding.optionName1.setText(null);
                                    binding.optionPrice1.setText(null);
                                    optionIndex--;
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    checkDialog = builder.create();
                    checkDialog.setCancelable(false);
                    checkDialog.setOnKeyListener(new Dialog.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface arg0, int keyCode,
                                             KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                checkDialog.dismiss();
                            }
                            return true;
                        }
                    });
                    checkDialog.show();
                }
            }
        });


        myRef = FirebaseDatabase.getInstance().getReference();

        binding.menuSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textCheck()){
                    submit();
                }
            }
        });
    }

    public boolean textCheck(){
        if(binding.menuName.length()<=0){
            Toast.makeText(this, "메뉴 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(binding.menuPrice.length()<=0){
            Toast.makeText(this, "메뉴 가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(binding.menuExplain.length()<=0){
            Toast.makeText(this, "메뉴 설명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(optionIndex>0){
            switch (optionIndex){
                case 1:
                    if(binding.optionName1.length()<=0||binding.optionPrice1.length()<=0) {
                        Toast.makeText(this, "추가되어진 옵션 항목을 다 채워주세요", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case 2:
                    if(binding.optionName1.length()<=0||binding.optionPrice1.length()<=0||binding.optionName2.length()<=0||binding.optionPrice2.length()<=0) {
                        Toast.makeText(this, "추가되어진 옵션 항목을 다 채워주세요", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case 3:
                    if(binding.optionName1.length()<=0||binding.optionPrice1.length()<=0||binding.optionName2.length()<=0||binding.optionPrice2.length()<=0||binding.optionName3.length()<=0||binding.optionPrice3.length()<=0) {
                        Toast.makeText(this, "추가되어진 옵션 항목을 다 채워주세요", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case 4:
                    if(binding.optionName1.length()<=0||binding.optionPrice1.length()<=0||binding.optionName2.length()<=0||binding.optionPrice2.length()<=0||binding.optionName3.length()<=0||binding.optionPrice3.length()<=0||binding.optionName4.length()<=0||binding.optionPrice4.length()<=0) {
                        Toast.makeText(this, "추가되어진 옵션 항목을 다 채워주세요", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case 5:
                    if(binding.optionName1.length()<=0||binding.optionPrice1.length()<=0||binding.optionName2.length()<=0||binding.optionPrice2.length()<=0||binding.optionName3.length()<=0||binding.optionPrice3.length()<=0||binding.optionName4.length()<=0||binding.optionPrice4.length()<=0||binding.optionName5.length()<=0||binding.optionPrice5.length()<=0) {
                        Toast.makeText(this, "추가되어진 옵션 항목을 다 채워주세요", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                default:
                    Toast.makeText(this, "텍스트 체크 오류", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        return true;
    }

    public  void submit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuAddActivity.this);
        builder.setTitle("메뉴 추가 확인");
        builder.setMessage("작성하신 내용대로 메뉴를 추가하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String key = myRef.child("market").child(uid).child("menu").push().getKey();
                myRef.child("market").child(uid).child("menu").child(key).child("menuName").setValue(binding.menuName.getText().toString());
                myRef.child("market").child(uid).child("menu").child(key).child("menuPrice").setValue(binding.menuPrice.getText().toString());
                myRef.child("market").child(uid).child("menu").child(key).child("menuExplain").setValue(binding.menuExplain.getText().toString());
                if(optionIndex==1||optionIndex==2||optionIndex==3||optionIndex==4||optionIndex==5) {
                    myRef.child("market").child(uid).child("menu").child(key).child("optionName1").setValue(binding.optionName1.getText().toString());
                    myRef.child("market").child(uid).child("menu").child(key).child("optionPrice1").setValue(binding.optionPrice1.getText().toString());
                }
                if(optionIndex==2||optionIndex==3||optionIndex==4||optionIndex==5){
                    myRef.child("market").child(uid).child("menu").child(key).child("optionName2").setValue(binding.optionName2.getText().toString());
                    myRef.child("market").child(uid).child("menu").child(key).child("optionPrice2").setValue(binding.optionPrice2.getText().toString());
                }
                if(optionIndex==3||optionIndex==4||optionIndex==5){
                    myRef.child("market").child(uid).child("menu").child(key).child("optionName3").setValue(binding.optionName3.getText().toString());
                    myRef.child("market").child(uid).child("menu").child(key).child("optionPrice3").setValue(binding.optionPrice3.getText().toString());
                }
                if(optionIndex==4||optionIndex==5){
                    myRef.child("market").child(uid).child("menu").child(key).child("optionName4").setValue(binding.optionName4.getText().toString());
                    myRef.child("market").child(uid).child("menu").child(key).child("optionPrice4").setValue(binding.optionPrice4.getText().toString());
                }
                if(optionIndex==5){
                    myRef.child("market").child(uid).child("menu").child(key).child("optionName5").setValue(binding.optionName5.getText().toString());
                    myRef.child("market").child(uid).child("menu").child(key).child("optionPrice5").setValue(binding.optionPrice5.getText().toString());
                }
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        submitDialog = builder.create();
        submitDialog.setCancelable(false);
        submitDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    submitDialog.dismiss();
                }
                return true;
            }
        });
        submitDialog.show();
    }
    private File getTempFile(){
        File file = new File( Environment.getExternalStorageDirectory(), "tmpImage.jpg" );
        try{
            file.createNewFile();
        }
        catch( Exception e ){
            Log.e("파일 생성", "실패" );
        }
        return file;
    }
    public void cropImage() {

        tempImageUri = Uri.fromFile(getTempFile());

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageCropUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행
        startActivityForResult(intent, REQUEST_CROP);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //매장 대표사진 설정에서 결과를 받아왔을 경우
        if(requestCode==GET_MARKET_IMAGE&&resultCode==RESULT_OK) {
            imageCropUri = data.getData(); //인텐트에서 이미지에 대한 데이터 추출
            cropImage();
        }
        else if(requestCode==REQUEST_CROP&&resultCode==RESULT_OK){
            File tempFile = getTempFile();
            if (tempFile.exists()) {
                bitmap = BitmapFactory.decodeFile(tempFile.toString());
                binding.menuImageViewContainer.setVisibility(View.VISIBLE);
                binding.menuTextViewContainer.setVisibility(View.INVISIBLE);
                binding.menuImageView.setImageBitmap(bitmap);
                sendUri = Uri.fromFile(getTempFile());
            }
        }
        else if(requestCode==REQUEST_CROP&&resultCode!=RESULT_OK){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, GET_MARKET_IMAGE);
        }
    }

    //뒤로가기 버튼 기능 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuAddActivity.this);
        builder.setTitle("종료 확인");
        builder.setMessage("나가시겠습니까? 작성중이던 내용이 사라집니다.");
        builder.setPositiveButton("확인(나가기)", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        exitDialog = builder.create();
        exitDialog.setCancelable(false);
        exitDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    exitDialog.dismiss();
                }
                return true;
            }
        });
        exitDialog.show();
    }
}
