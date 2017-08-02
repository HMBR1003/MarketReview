package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import org.baseballbaedal.baseballbaedal.BusinessMan.BusinessSignupActivity;
import org.baseballbaedal.baseballbaedal.BusinessMan.LogoViewActivity;
import org.baseballbaedal.baseballbaedal.Manifest;
import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityMenuAddBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class MenuAddActivity extends NewActivity {
    private static final int REQUEST_CROP = 6000;
    private static final int GET_MARKET_IMAGE = 7000;

    ActivityMenuAddBinding binding;
    int optionIndex = 0;
    AlertDialog exitDialog;
    AlertDialog checkDialog;
    AlertDialog submitDialog;
    SpotsDialog uploadDialog;
    SpotsDialog loadDialog;
    InputMethodManager imm;
    DatabaseReference myRef;
    String uid;
    String menuKey;
    Uri sendUri;
    private Uri tempImageUri;
    private Uri imageCropUri;
    Bitmap bitmap = null;
    boolean isEdit;
    File tempFile;

    int menuCount;


    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            binding.explainLimit.setText(binding.menuExplain.length() + " / 50자");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_add);

        //저장소 권한 묻기
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MenuAddActivity.this, "저장소 권한이 없어 메뉴를 추가할 수 없습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        new TedPermission(getApplicationContext())
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("메뉴 사진 업로드를 위해 저장소 권한이 필요합니다.")
                .setRationaleConfirmText("확인")
                .setDeniedMessage("설정으로 가셔서 저장소 권한을 허용해주세요.")
                .setDeniedCloseButtonText("닫기")
                .setGotoSettingButtonText("설정하러 가기")
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();


        myRef = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tempFile = getTempFile();

        binding.menuExplain.setHorizontallyScrolling(false);
        binding.menuExplain.setLines(5);

        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("isEdit", false);
        menuCount = intent.getIntExtra("menuCount", 0);

        if (isEdit) {
            menuKey = intent.getStringExtra("menuId");
            //상단 툴바 설정
            setToolbar(binding.toolBar, "메뉴 수정", Color.WHITE, true);
            loadData();
        } else {
            if (menuCount == -1) {
                Toast.makeText(this, "메뉴 개수가 잘못 넘어왔습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            //상단 툴바 설정
            setToolbar(binding.toolBar, "메뉴 추가", Color.WHITE, true);
        }


        //키보드 자동 올리기 관련
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //메뉴설명란 입력문자 수 읽어오기
        binding.menuExplain.addTextChangedListener(watcher);

        //이미지뷰 클릭설정
        binding.menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zoomIntent = new Intent(MenuAddActivity.this, LogoViewActivity.class);
                zoomIntent.putExtra("imageUri", sendUri);
                startActivity(zoomIntent);
            }
        });

        //메뉴 이미지 불러오기 버튼
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

        binding.menuSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textCheck()) {
                    submit();
                }
            }
        });
    }

    public boolean textCheck() {
        if (bitmap == null) {
            Toast.makeText(this, "사진을 등록해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.menuName.length() <= 0) {
            Toast.makeText(this, "메뉴 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.menuPrice.length() <= 0) {
            Toast.makeText(this, "메뉴 가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.menuExplain.length() <= 0) {
            Toast.makeText(this, "메뉴 설명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (optionIndex > 0) {
            switch (optionIndex) {
                case 1:
                    if (binding.optionName1.length() <= 0 || binding.optionPrice1.length() <= 0) {
                        Toast.makeText(this, "추가되어진 옵션 항목을 다 채워주세요", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case 2:
                    if (binding.optionName1.length() <= 0 || binding.optionPrice1.length() <= 0 || binding.optionName2.length() <= 0 || binding.optionPrice2.length() <= 0) {
                        Toast.makeText(this, "추가되어진 옵션 항목을 다 채워주세요", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case 3:
                    if (binding.optionName1.length() <= 0 || binding.optionPrice1.length() <= 0 || binding.optionName2.length() <= 0 || binding.optionPrice2.length() <= 0 || binding.optionName3.length() <= 0 || binding.optionPrice3.length() <= 0) {
                        Toast.makeText(this, "추가되어진 옵션 항목을 다 채워주세요", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case 4:
                    if (binding.optionName1.length() <= 0 || binding.optionPrice1.length() <= 0 || binding.optionName2.length() <= 0 || binding.optionPrice2.length() <= 0 || binding.optionName3.length() <= 0 || binding.optionPrice3.length() <= 0 || binding.optionName4.length() <= 0 || binding.optionPrice4.length() <= 0) {
                        Toast.makeText(this, "추가되어진 옵션 항목을 다 채워주세요", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    break;
                case 5:
                    if (binding.optionName1.length() <= 0 || binding.optionPrice1.length() <= 0 || binding.optionName2.length() <= 0 || binding.optionPrice2.length() <= 0 || binding.optionName3.length() <= 0 || binding.optionPrice3.length() <= 0 || binding.optionName4.length() <= 0 || binding.optionPrice4.length() <= 0 || binding.optionName5.length() <= 0 || binding.optionPrice5.length() <= 0) {
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


    public void loadData() {
        //데이터 불러오는 중이라고 알림창 띄우기
        loadDialog = new SpotsDialog(MenuAddActivity.this, "데이터를 불러오는 중입니다...", R.style.ProgressBar);
        loadDialog.setCancelable(false);
        loadDialog.show();

        myRef.child("market").child(uid).child("menu").child(menuKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    MenuInfo data = dataSnapshot.getValue(MenuInfo.class);
                    binding.menuName.setText(data.menuName);
                    binding.menuPrice.setText(data.menuPrice);
                    binding.menuExplain.setText(data.menuExplain);
                    if (data.option1Name != null) {
                        optionIndex = 1;
                        binding.optionName1.setText(data.option1Name);
                        binding.optionPrice1.setText(data.option1Price);
                        binding.optionSet1.setVisibility(View.VISIBLE);
                    }
                    if (data.option2Name != null) {
                        optionIndex = 2;
                        binding.optionName2.setText(data.option2Name);
                        binding.optionPrice2.setText(data.option2Price);
                        binding.optionSet2.setVisibility(View.VISIBLE);
                    }
                    if (data.option3Name != null) {
                        optionIndex = 3;
                        binding.optionName3.setText(data.option3Name);
                        binding.optionPrice3.setText(data.option3Price);
                        binding.optionSet3.setVisibility(View.VISIBLE);
                    }
                    if (data.option4Name != null) {
                        optionIndex = 4;
                        binding.optionName4.setText(data.option4Name);
                        binding.optionPrice4.setText(data.option4Price);
                        binding.optionSet4.setVisibility(View.VISIBLE);
                    }
                    if (data.option5Name != null) {
                        optionIndex = 5;
                        binding.optionName5.setText(data.option5Name);
                        binding.optionPrice5.setText(data.option5Price);
                        binding.optionSet5.setVisibility(View.VISIBLE);
                    }
                    StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(uid).child("menu").child(menuKey + ".jpg");
                    try {
                        Glide
                                .with(MenuAddActivity.this)
                                .using(new FirebaseImageLoader())
                                .load(ref)
                                .listener(new RequestListener<StorageReference, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        Toast.makeText(MenuAddActivity.this, "이미지 표시 에러", Toast.LENGTH_SHORT).show();
                                        Log.d("이미지 표시 에러 : ", e.getMessage());
                                        loadDialog.dismiss();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        binding.menuImageViewContainer.setVisibility(View.VISIBLE);
                                        binding.menuTextViewContainer.setVisibility(View.INVISIBLE);

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
                                        loadDialog.dismiss();
                                        return false;
                                    }
                                })
                                .signature(new StringSignature(data.aTime)) //이미지저장시간
                                .placeholder(R.drawable.jamsil)
                                .thumbnail(0.1f)
                                .crossFade()
                                .into(binding.menuImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(uid).child("menu").child(menuKey+".jpg");
//                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            sendUri = uri;
//                            menuImageUrl = String.valueOf(uri);
//                            Log.d("다운로드 URL", menuImageUrl);
//                            //피카소를 이용하여 저장소에 저장된 사진을 url로 이미지뷰에 연결하기
//                            Picasso.with(getApplicationContext())
//                                    .load(menuImageUrl)
//                                    .fit()
//                                    .centerInside()
//                                    .into(binding.menuImageView, new Callback.EmptyCallback() {
//                                        @Override
//                                        public void onSuccess() {
//                                            binding.menuImageViewContainer.setVisibility(View.VISIBLE);
//                                            binding.menuTextViewContainer.setVisibility(View.INVISIBLE);
//                                            BitmapDrawable d = (BitmapDrawable) binding.menuImageView.getDrawable();
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
//                                            loadDialog.dismiss();
//                                        }
//                                        @Override
//                                        public void onError() {
//                                            super.onError();
//                                            Toast.makeText(MenuAddActivity.this, "이미지 표시 에러", Toast.LENGTH_SHORT).show();
//                                            loadDialog.dismiss();
//                                        }
//                                    });
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            Toast.makeText(MenuAddActivity.this, "저장소 이미지 주소 가져오기 실패", Toast.LENGTH_SHORT).show();
//                            loadDialog.dismiss();
//                        }
//                    });
                } else {
                    Toast.makeText(MenuAddActivity.this, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    loadDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MenuAddActivity.this, "데이터 가져오기 실패 : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                loadDialog.dismiss();
            }
        });

    }

    public void submit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuAddActivity.this);
        if (isEdit) {
            builder.setTitle("메뉴 수정 확인");
            builder.setMessage("작성하신 내용대로 메뉴를 수정하시겠습니까?");
        } else {
            builder.setTitle("메뉴 추가 확인");
            builder.setMessage("작성하신 내용대로 메뉴를 추가하시겠습니까?");
        }
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isEdit) {
                    menuKey = myRef.child("market").child(uid).child("menu").push().getKey();
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("aseq").setValue(menuCount);
                }

                myRef.child("market").child(uid).child("menu").child(menuKey).child("option1Name").setValue(null);
                myRef.child("market").child(uid).child("menu").child(menuKey).child("option1Price").setValue(null);
                myRef.child("market").child(uid).child("menu").child(menuKey).child("option2Name").setValue(null);
                myRef.child("market").child(uid).child("menu").child(menuKey).child("option2Price").setValue(null);
                myRef.child("market").child(uid).child("menu").child(menuKey).child("option3Name").setValue(null);
                myRef.child("market").child(uid).child("menu").child(menuKey).child("option3Price").setValue(null);
                myRef.child("market").child(uid).child("menu").child(menuKey).child("option4Name").setValue(null);
                myRef.child("market").child(uid).child("menu").child(menuKey).child("option4Price").setValue(null);
                myRef.child("market").child(uid).child("menu").child(menuKey).child("option5Name").setValue(null);
                myRef.child("market").child(uid).child("menu").child(menuKey).child("option5Price").setValue(null);

                myRef.child("market").child(uid).child("menu").child(menuKey).child("aTime").setValue(System.currentTimeMillis() + "");
                myRef.child("market").child(uid).child("menu").child(menuKey).child("isMain").setValue(false);
                myRef.child("market").child(uid).child("menu").child(menuKey).child("menuName").setValue(binding.menuName.getText().toString());
                myRef.child("market").child(uid).child("menu").child(menuKey).child("menuPrice").setValue(binding.menuPrice.getText().toString());
                myRef.child("market").child(uid).child("menu").child(menuKey).child("menuExplain").setValue(binding.menuExplain.getText().toString());
                if (optionIndex == 1 || optionIndex == 2 || optionIndex == 3 || optionIndex == 4 || optionIndex == 5) {
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("option1Name").setValue(binding.optionName1.getText().toString());
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("option1Price").setValue(binding.optionPrice1.getText().toString());
                }
                if (optionIndex == 2 || optionIndex == 3 || optionIndex == 4 || optionIndex == 5) {
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("option2Name").setValue(binding.optionName2.getText().toString());
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("option2Price").setValue(binding.optionPrice2.getText().toString());
                }
                if (optionIndex == 3 || optionIndex == 4 || optionIndex == 5) {
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("option3Name").setValue(binding.optionName3.getText().toString());
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("option3Price").setValue(binding.optionPrice3.getText().toString());
                }
                if (optionIndex == 4 || optionIndex == 5) {
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("option4Name").setValue(binding.optionName4.getText().toString());
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("option4Price").setValue(binding.optionPrice4.getText().toString());
                }
                if (optionIndex == 5) {
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("option5Name").setValue(binding.optionName5.getText().toString());
                    myRef.child("market").child(uid).child("menu").child(menuKey).child("option5Price").setValue(binding.optionPrice5.getText().toString());
                }
                uploadImage();
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

    private File getTempFile() {
        File file = new File(getExternalCacheDir(), "menuTmpImage.jpg");
        try {
            file.createNewFile();
        } catch (Exception e) {
            Log.e("파일 생성", "실패");
        }
        return file;
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //매장 대표사진 설정에서 결과를 받아왔을 경우
        if (requestCode == GET_MARKET_IMAGE && resultCode == RESULT_OK) {
            imageCropUri = data.getData(); //인텐트에서 이미지에 대한 데이터 추출
            cropImage();
        } else if (requestCode == REQUEST_CROP && resultCode == RESULT_OK) {
//            File tempFile = getTempFile();
            if (tempFile.exists()) {
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
                } else {
                    bitmap = BitmapFactory.decodeFile(tempFile.toString());
                }

                binding.menuImageViewContainer.setVisibility(View.VISIBLE);
                binding.menuTextViewContainer.setVisibility(View.INVISIBLE);
                binding.menuImageView.setImageBitmap(bitmap);
                sendUri = Uri.fromFile(tempFile);
            }
        } else if (requestCode == REQUEST_CROP && resultCode != RESULT_OK) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, GET_MARKET_IMAGE);
        }
    }

    public void uploadImage() {
        //데이터 저장하는 중이라고 알림창 띄우기
        uploadDialog = new SpotsDialog(MenuAddActivity.this, "데이터를 저장하는 중입니다...", R.style.ProgressBar);
        uploadDialog.setCancelable(false);
        uploadDialog.show();

        //저장소에 대한 참조 만들기
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        //실제로 이미지가 저장될 곳의 참조
        StorageReference mountainsRef = mStorageRef.child("market").child(uid).child("menu").child(menuKey + ".jpg");

        //비트맵을 jpg로 변환시켜서 변수에 저장
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //jpg형식으로 저장된 변수를 저장소에 업로드하는 함수
        UploadTask uploadTask = mountainsRef.putBytes(data);
        //성공했을 시와 실패했을 시를 받아오는 리스너 부착
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                uploadDialog.dismiss();
                Toast.makeText(MenuAddActivity.this, "제출 실패.", Toast.LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                 taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                String photoUri =  String.valueOf(downloadUrl);
//                myRef.child("market").child(uid).child("menu").child(menuKey).child("menuImageURL").setValue(photoUri);
                uploadDialog.dismiss();
                setResult(RESULT_OK);
                finish();
            }
        });
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
