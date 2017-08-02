package org.baseballbaedal.baseballbaedal.BusinessMan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.baseballbaedal.baseballbaedal.BaseActivity;
import org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuAddActivity;
import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityBusinessSignupBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class BusinessSignupActivity extends NewActivity {
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private static final int GET_MARKET_IMAGE = 7000;
    private static final int REQUEST_CROP = 6000;

    String email;
    String name;
    String uid;
    int isBusiness;     //사업자 구분하기 위해 선언
    int handleFood = 0;
    int selectedCol = 0;
    int selectedPay = 0;
    int selectedBeer= 0;
    ActivityBusinessSignupBinding dataBinding;  //데이터 바인딩
    DatabaseReference myRef;
    File tempFile;

    private Uri tempImageUri;
    private Uri imageCropUri;
    Uri sendUri;
    String sendUrl;

    Bitmap bitmap = null;
    SpotsDialog dialog;
    AlertDialog checkDialog;
    AlertDialog submitDialog;
    String isMarket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_business_signup);

        //저장소 권한 묻기
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(BusinessSignupActivity.this, "저장소 권한이 없어 취소되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        new TedPermission(getApplicationContext())
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("매장 대표 로고 사진 업로드를 위해 저장소 권한이 필요합니다.")
                .setRationaleConfirmText("확인")
                .setDeniedMessage("설정으로 가셔서 저장소 권한을 허용해주세요.")
                .setDeniedCloseButtonText("닫기")
                .setGotoSettingButtonText("설정하러 가기")
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

        //주소찾기 란 입력하지 못하게 하기 위해 설정
        dataBinding.marketAddress1.setInputType(0);
        dataBinding.marketAddress1.setFocusable(false);
        dataBinding.marketAddress1.setClickable(false);

        //주소찾기 버튼 클릭 시 Daum에서 지원하는 주소찾기 창을 띄움
        dataBinding.searchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessSignupActivity.this, AddressWebViewActivity.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            }
        });


        //uid,사업자여부,이메일,이름 가져오기
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        isBusiness = intent.getIntExtra("isBusiness", -1);
        email = intent.getStringExtra("email");
        name = intent.getStringExtra("name");
        //임시파일 생성
        tempFile = getTempFile();
//        dataBinding.toolBar.setBackgroundColor(getResources().getColor(R.color.ThemeColor));
        //유저가 고객이고 사업자 등록신청을 하는 경우 화면 설정


        if (isBusiness == 0) {
            //상단 툴바 설정
            setToolbar(dataBinding.toolBar,"사업자 신규등록 신청",Color.WHITE,true);
            dataBinding.inputBusinessInfo.setText("사업자로 신청할 정보를 입력해 주세요");
            dataBinding.businessSubmit.setText("작성 완료");
            isMarket = "tmp";
        }

        //사업자 등록신청 승인을 기다리는 고객의 경우 화면 설정(임시 데이터베이스에 저장된 내용을 불러와서 세팅함)
        else if (isBusiness == 1) {
            //상단 툴바 설정
            setToolbar(dataBinding.toolBar,"사업자 신청정보 수정",Color.WHITE,true);
            dataBinding.inputBusinessInfo.setText("신청한 정보를 수정합니다.");
            dataBinding.businessSubmit.setText("수정 완료");
            isMarket = "tmp";
            //데이터베이스에 저장된 데이터 가져오기 함수
            loadData();
        }

        //사업자 승인이 난 고객의 경우 화면 설정(확정 데이터베이스에 저장된 내용을 불러와서 세팅함)
        else if (isBusiness == 2) {
            //상단 툴바 설정
            setToolbar(dataBinding.toolBar,"매장 정보 수정",Color.WHITE,true);
            dataBinding.inputBusinessInfo.setText("등록된 사업자 정보를 수정합니다.");
            dataBinding.businessSubmit.setText("수정 완료");
            isMarket = "market";
            //데이터베이스에 저장된 데이터 가져오기 함수
            loadData();
        }

        //인텐트 값이 제대로 넘어오지 않았을 경우
        else {
            Toast.makeText(this, "사업자 여부 불러오기 오류", Toast.LENGTH_SHORT).show();
            finish();
        }

        //대표 로고 사진 불러오기 버튼 동작 설정
        dataBinding.loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //갤러리에서 이미지 불러오기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GET_MARKET_IMAGE);
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, GET_MARKET_IMAGE);
            }
        });

        //취급음식 스피너
        ArrayAdapter handleFoodAdapter = ArrayAdapter.createFromResource(this, R.array.handleFood, android.R.layout.simple_spinner_item);
        handleFoodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataBinding.handleFoodSpinner.setAdapter(handleFoodAdapter);
        dataBinding.handleFoodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleFood = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //경기장 선택 스피너
        ArrayAdapter colAdapter = ArrayAdapter.createFromResource(this, R.array.col, android.R.layout.simple_spinner_item);
        colAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataBinding.colSpinner.setAdapter(colAdapter);
        dataBinding.colSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCol = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //결제 방법스피너
        ArrayAdapter payAdapter = ArrayAdapter.createFromResource(this, R.array.pay, android.R.layout.simple_spinner_item);
        payAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataBinding.paySpinner.setAdapter(payAdapter);
        dataBinding.paySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPay = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //맥주 배달 여부 스피너
        ArrayAdapter beerAdapter = ArrayAdapter.createFromResource(this, R.array.beer, android.R.layout.simple_spinner_item);
        beerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataBinding.beerSpinner.setAdapter(beerAdapter);
        dataBinding.beerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBeer = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //이미지뷰 클릭 시 확대해서 보여주는 창 띄우기
        dataBinding.marketImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zoomIntent = new Intent(BusinessSignupActivity.this, LogoViewActivity.class);
                zoomIntent.putExtra("imageUri", sendUri);
                startActivity(zoomIntent);
            }
        });

        //뒤로가기 버튼 만들기
        setSupportActionBar(dataBinding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //하단의 신청 완료 버튼을 눌렀을 때의 동작 설정
        dataBinding.businessSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //작성하지 않은 부분이 있나 확인
                if (textCheck()) {
                    //디비와 저장소에 데이터를 넣는 함수 실행
                    submitAlert();
                }
            }
        });
    }

    //빈공간 체크 함수
    public boolean textCheck() {
        if (dataBinding.sanghoName.length() <= 0) {
            Toast.makeText(this, "상호명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataBinding.manName.length() <= 0) {
            Toast.makeText(this, "사업자명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataBinding.businessRegisterNum.length() <= 0) {
            Toast.makeText(this, "사업자 등록번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataBinding.manTel.length() <= 0) {
            Toast.makeText(this, "사업자 전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataBinding.marketName.length() <= 0) {
            Toast.makeText(this, "매장명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedCol == 0) {
            Toast.makeText(this, "근처 경기장을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (handleFood == 0) {
            Toast.makeText(this, "취급 음식을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataBinding.marketAddress1.length() <= 0) {
            Toast.makeText(this, "주소를 검색하여 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataBinding.marketAddress2.length() <= 0) {
            Toast.makeText(this, "상세 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataBinding.marketTel.length() <= 0) {
            Toast.makeText(this, "매장 전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (dataBinding.minPrice.length() <= 0) {
            Toast.makeText(this, "최소 주문가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedPay == 0) {
            Toast.makeText(this, "결제 방법을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (selectedBeer == 0) {
            Toast.makeText(this, "맥주 배달 여부를 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bitmap == null) {
            Toast.makeText(this, "매장 사진을 등록해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //유저에게 최종확인후 데이터 넣는 작업을 하는 함수
    public void submitAlert() {
        //확인창 만들기
        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessSignupActivity.this);
        builder.setTitle("제출 확인");
        builder.setMessage("이대로 제출하시겠습니까?");
        //확인 버튼설정 및 버튼을 눌렀을 때 동작 설정
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                try {
                    if (!(isBusiness == 0 || isBusiness == 1 || isBusiness == 2)) {
                        Toast.makeText(BusinessSignupActivity.this, "데이터 저장 시 사업자 여부 데이터 오류", Toast.LENGTH_SHORT).show();
                    } else {
                        myRef = FirebaseDatabase.getInstance().getReference();
                        //데이터베이스 초기화
                        myRef = FirebaseDatabase.getInstance().getReference();
                        myRef.child(isMarket).child(uid).child("accountEmail").setValue(email);
                        myRef.child(isMarket).child(uid).child("accountName").setValue(name);

                        myRef.child(isMarket).child(uid).child("sanghoName").setValue(dataBinding.sanghoName.getText().toString());
                        myRef.child(isMarket).child(uid).child("manName").setValue(dataBinding.manName.getText().toString());
                        myRef.child(isMarket).child(uid).child("manTel").setValue(dataBinding.manTel.getText().toString());
                        myRef.child(isMarket).child(uid).child("businessRegisterNum").setValue(dataBinding.businessRegisterNum.getText().toString());
                        myRef.child(isMarket).child(uid).child("marketName").setValue(dataBinding.marketName.getText().toString());
                        myRef.child(isMarket).child(uid).child("selectedCol").setValue(selectedCol);
                        myRef.child(isMarket).child(uid).child("selectedPay").setValue(selectedPay);
                        myRef.child(isMarket).child(uid).child("selectedBeer").setValue(selectedBeer);
                        myRef.child(isMarket).child(uid).child("handleFood").setValue(handleFood);
                        myRef.child(isMarket).child(uid).child("marketAddress1").setValue(dataBinding.marketAddress1.getText().toString());
                        myRef.child(isMarket).child(uid).child("marketAddress2").setValue(dataBinding.marketAddress2.getText().toString());
                        myRef.child(isMarket).child(uid).child("marketTel").setValue(dataBinding.marketTel.getText().toString());
                        myRef.child(isMarket).child(uid).child("minPrice").setValue(dataBinding.minPrice.getText().toString());
                        if (isBusiness == 0 || isBusiness == 1) {
                            myRef.child("users").child(uid).child("isBusiness(0(not),1(applying),2(finish))").setValue(1);
                        }
                        uploadImage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //닫기
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //확인창 생성하기
        submitDialog = builder.create();
        //취소 불가능 하게하기
        submitDialog.setCancelable(false);
        //뒤로가기 키 눌렀을 때는 사라지게 하기
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
        //확인창 보여주기
        submitDialog.show();
    }

    //주소찾기나 사진 불러오기 결과를 받아왔을 때의 동작 설정
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //주소찾기 화면에서 결과를 받아왔을 경우
        if (requestCode == SEARCH_ADDRESS_ACTIVITY && resultCode == RESULT_OK) {
            dataBinding.marketAddress1.setText(data.getStringExtra("data"));   //인텐트로 받아온 주소값을 텍스트에 설정한다
        }
        //매장 대표사진 설정에서 결과를 받아왔을 경우
        else if (requestCode == GET_MARKET_IMAGE && resultCode == RESULT_OK) {
            //인텐트에서 이미지에 대한 데이터 추출
            imageCropUri = data.getData();
            //크롭하는 화면 띄움
            cropImage();
        }
        //이미지를 크롭한 뒤의 동작
        else if (requestCode == REQUEST_CROP && resultCode == RESULT_OK) {

            //크롭에 성공하여 이미지가 존재하면
            if (tempFile.exists()) {
                //이미지뷰에 이미지를 세팅하는 작업을 함
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(tempFile.getPath(), options);
                int imageWidth = options.outWidth;

                if (imageWidth > 1000 && imageWidth < 2000) {
                    options.inSampleSize = 2;
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);
                } else if (imageWidth >= 2000 && imageWidth < 3000) {
                    options.inSampleSize = 4;
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);
                } else if (imageWidth >= 3000) {
                    options.inSampleSize = 6;
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);
                }else{
                    bitmap = BitmapFactory.decodeFile(tempFile.toString());
                }

                dataBinding.imageViewContainer.setVisibility(View.VISIBLE);
                dataBinding.textViewContainer.setVisibility(View.INVISIBLE);
                dataBinding.marketImageView.setImageBitmap(bitmap);

                //크게보기 했을 때 보내줄 uri에 값을 저장
                sendUri = Uri.fromFile(tempFile);
            }
        }
        //크롭하다가 취소했을 경우 다시 갤러리선택화면을 띄움
        else if (requestCode == REQUEST_CROP && resultCode != RESULT_OK) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, GET_MARKET_IMAGE);
//            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(i, GET_MARKET_IMAGE);
        }
    }

    //크롭한 이미지를 저장하고 불러올때 쓰일 함수
    private File getTempFile() {
        File file = new File(getExternalCacheDir(), "marketTmpImage.jpg");
        try {
            file.createNewFile();
        } catch (Exception e) {
            Log.e("파일 생성", "실패");
        }
        return file;
    }

    //이미지 크롭함수
    public void cropImage() {
        tempImageUri = Uri.fromFile(tempFile);

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


    //데이터베이스에서 데이터를 로드해서 세팅해주는 함수
    public void loadData() {
        //데이터 불러오는 중이라고 알림창 띄우기
        dialog = new SpotsDialog(BusinessSignupActivity.this, "데이터를 불러오는 중입니다...", R.style.ProgressBar);
        dialog.setCancelable(false);
        dialog.show();

        //데이터베이스 초기화
        myRef = FirebaseDatabase.getInstance().getReference();

//        final ImageViewTarget<GlideDrawable> target = new ImageViewTarget<GlideDrawable>(dataBinding.marketImageView) {
//            @Override
//            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                super.onResourceReady(resource, glideAnimation);
//                dialog.dismiss();
//            }
//
//            @Override
//            protected void setResource(GlideDrawable resource) {
//
//            }
//        };
        //데이터 불러와서 화면에 세팅하기
        myRef.child(isMarket).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    dataBinding.imageViewContainer.setVisibility(View.VISIBLE);
                    dataBinding.textViewContainer.setVisibility(View.INVISIBLE);

                    final MarketInfo data = dataSnapshot.getValue(MarketInfo.class);

                    dataBinding.sanghoName.setText(data.sanghoName);
                    dataBinding.manName.setText(data.manName);
                    dataBinding.businessRegisterNum.setText(data.businessRegisterNum);
                    dataBinding.manTel.setText(data.manTel);
                    dataBinding.marketName.setText(data.marketName);
                    dataBinding.handleFoodSpinner.setSelection((int) data.handleFood);
                    handleFood = (int) data.handleFood;
                    dataBinding.colSpinner.setSelection((int) data.selectedCol);
                    selectedCol = (int) data.selectedCol;
                    dataBinding.paySpinner.setSelection((int) data.selectedPay);
                    selectedPay = (int) data.selectedPay;
                    dataBinding.beerSpinner.setSelection((int) data.selectedBeer);
                    selectedBeer = (int) data.selectedBeer;
                    dataBinding.marketAddress1.setText(data.marketAddress1);
                    dataBinding.marketAddress2.setText(data.marketAddress2);
                    dataBinding.marketTel.setText(data.marketTel);
                    dataBinding.minPrice.setText(data.minPrice);
                    StorageReference ref = FirebaseStorage.getInstance().getReference().child(isMarket).child(uid).child(uid + ".jpg");
                    try {
                        Glide
                                .with(BusinessSignupActivity.this)
                                .using(new FirebaseImageLoader())
                                .load(ref)
                                .listener(new RequestListener<StorageReference, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        Toast.makeText(BusinessSignupActivity.this, "저장소 이미지 가져오기 실패", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        bitmap = ((GlideBitmapDrawable) resource).getBitmap();
                                        try {
                                            FileOutputStream out = new FileOutputStream(tempFile.getPath());
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                            out.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        sendUri = Uri.fromFile(tempFile);
                                        dialog.dismiss();
                                        return false;
                                    }
                                })
                                .signature(new StringSignature(data.aTime)) //이미지저장시간
                                .placeholder(R.drawable.jamsil)
                                .thumbnail(0.1f)
                                .crossFade()
                                .into(dataBinding.marketImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            sendUri = uri;
//                            marketImageURL = String.valueOf(uri);
//                            Log.d("다운로드 URL", marketImageURL);
                    //피카소를 이용하여 저장소에 저장된 사진을 url로 이미지뷰에 연결하기
//                            Glide.with(getApplicationContext())
//                                    .load(marketImageURL)
//                                    .listener(new RequestListener<String, GlideDrawable>() {
//                                        @Override
//                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                            return false;
//                                        }
//                                    })
//                                    .listener(new RequestListener<Drawable>() {
//                                        @Override
//                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                                        }
//                                    })
//                                    .into(dataBinding.marketImageView);
//                            Picasso.with(getApplicationContext())
//                                    .load(marketImageURL)
//                                    .fit()
//                                    .centerInside()
//                                    .into(dataBinding.marketImageView, new Callback.EmptyCallback() {
//                                        @Override
//                                        public void onSuccess() {
//                                            BitmapDrawable d = (BitmapDrawable) dataBinding.marketImageView.getDrawable();
//                                            bitmap = d.getBitmap();
//                                            try {
//                                                FileOutputStream out = new FileOutputStream(tempFile.getPath());
//                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                                                out.close();
//                                            } catch (FileNotFoundException e) {
//                                                e.printStackTrace();
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//                                            sendUri = Uri.fromFile(tempFile);
//                                            dialog.dismiss();
//                                        }
//
//                                        @Override
//                                        public void onError() {
//                                            super.onError();
//                                            Toast.makeText(BusinessSignupActivity.this, "이미지 표시 에러", Toast.LENGTH_SHORT).show();
//                                            dialog.dismiss();
//                                        }
//                                    });
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            Toast.makeText(BusinessSignupActivity.this, "저장소 이미지 주소 가져오기 실패", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    });
//                    Toast.makeText(BusinessSignupActivity.this, "데이터 가져오기 성공", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(BusinessSignupActivity.this, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(BusinessSignupActivity.this, "데이터 가져오기 실패 에러 내용 : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessSignupActivity.this);
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

    public void uploadImage() {
        //데이터 저장하는 중이라고 알림창 띄우기
        dialog = new SpotsDialog(BusinessSignupActivity.this, "데이터를 저장하는 중입니다...", R.style.ProgressBar);
        dialog.setCancelable(false);
        dialog.show();

        try {

            //실제로 이미지가 저장될 곳의 참조
            StorageReference saveRef = FirebaseStorage.getInstance().getReference().child(isMarket).child(uid).child(uid + ".jpg");

            //비트맵을 jpg로 변환시켜서 변수에 저장
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            //jpg형식으로 저장된 변수를 저장소에 업로드하는 함수
            UploadTask uploadTask = saveRef.putBytes(data);
            //성공했을 시와 실패했을 시를 받아오는 리스너 부착
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    dialog.dismiss();
                    Toast.makeText(BusinessSignupActivity.this, "제출 실패.", Toast.LENGTH_SHORT).show();
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myRef.child(isMarket).child(uid).child("aTime").setValue(System.currentTimeMillis() + "");
                    dialog.dismiss();
                    if (isBusiness != 2)
                        setResult(RESULT_OK);
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

