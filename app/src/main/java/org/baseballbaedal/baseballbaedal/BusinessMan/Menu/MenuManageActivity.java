//package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;
//
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.databinding.DataBindingUtil;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.PaintDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.ActionMode;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;
//
//
//import org.baseballbaedal.baseballbaedal.R;
//import org.baseballbaedal.baseballbaedal.databinding.ActivityMenuManageBinding;
//
//import java.io.BufferedInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Iterator;
//
//public class MenuManageActivity extends AppCompatActivity {
//    public static final int MENU_ADD_REQUEST = 555;
//    public static final int MENU_EDIT_REQUEST = 444;
//    ActivityMenuManageBinding binding;
//    DatabaseReference ref;
//    MenuListAdapter adapter;
//    Iterator<DataSnapshot> it;
//    ValueEventListener listener;
//    String key[];
//    String imageUrl[];
//
//    String menuExplain[];
//    String menuName[];
//    String menuPrice[];
//    boolean isMain[];
//
//    boolean selectedPosition[];
//    boolean isDeleteMode;
//    boolean isMainSelect;
//    int i;
//    int j;
//
//
//
//    int checkedItem = -1;
//
//    AlertDialog deleteDialog;
//
//    String uid;
//
//    @Override
//    public void onBackPressed() {
//        if (isDeleteMode) {
//            binding.commonContainer.setVisibility(View.VISIBLE);
//            binding.deleteModeContainer.setVisibility(View.GONE);
////                for(int i = 0 ;i < adapter.getCount() ; i++){
////                    getViewByPosition(i,binding.menuListView).setBackgroundColor(Color.rgb(255, 255, 255));
//////                    binding.menuListView.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
////                }
//            isDeleteMode = false;
//            adapter.notifyDataSetChanged();
//            binding.infoText.setText("메뉴 정보");
//        } else if (isMainSelect) {
//            binding.commonContainer.setVisibility(View.VISIBLE);
//            binding.mainModeContainer.setVisibility(View.GONE);
//            binding.infoText.setText("메뉴 정보");
//            isMainSelect = false;
//            adapter.notifyDataSetChanged();
//            checkedItem = -1;
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    void clear() {
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//
//        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_manage);
//
//        Intent intent = getIntent();
//        uid = intent.getStringExtra("uid");
//
//        //타이틀 설정
//        binding.toolBar.setTitle("메뉴 관리");
//        binding.toolBar.setTitleTextColor(Color.WHITE);
//        setSupportActionBar(binding.toolBar);
//        //뒤로가기 버튼 만들기
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        binding.addMenuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MenuAddActivity.class);
//                intent.putExtra("isEdit", false);
//                startActivityForResult(intent, MENU_ADD_REQUEST);
//            }
//        });
//
//        adapter = new MenuListAdapter();
//        binding.menuListView.setAdapter(adapter);
//        ref = FirebaseDatabase.getInstance().getReference("market");
//        listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(final DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getChildrenCount() > 0) {
//                    int count = 0;
//                    binding.menuListView.setVisibility(View.VISIBLE);
//                    binding.menuListText.setVisibility(View.GONE);
//                    adapter.clear();
//
//                    key = new String[(int) dataSnapshot.getChildrenCount()];
//                    imageUrl = new String[(int) dataSnapshot.getChildrenCount()];
//                    menuExplain = new String[(int) dataSnapshot.getChildrenCount()];
//                    menuName = new String[(int) dataSnapshot.getChildrenCount()];
//                    menuPrice = new String[(int) dataSnapshot.getChildrenCount()];
//                    isMain = new boolean[(int) dataSnapshot.getChildrenCount()];
//
//                    //메인메뉴가 있는 지 검사 후 없으면 맨 처음것으로 설정
//                    i = 0;
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        key[i] = data.getKey();
//                        Iterator<DataSnapshot> it1 = data.getChildren().iterator();
//                        boolean isMain = it1.next().getValue(Boolean.class);
//                        if (isMain)
//                            break;
//                        else {
//                            count++;
//                        }
//                        i++;
//                    }
//                    if (count == dataSnapshot.getChildrenCount()) {
//                        ref.child(uid).child("menu").child(key[0]).child("isMain").setValue(true);
//                    }
//
//                    //리스트 정보 가져오기기
//                    i = 0;
//                    j = -1;
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        key[i] = data.getKey();
//                        //이미지 url
//                        StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(uid).child("menu").child(key[i] + ".jpg");
//                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                j++;
//                                imageUrl[j] = String.valueOf(uri);
//                                Log.d("다운로드 주소"+j, String.valueOf(uri));
//
//                                if(j==dataSnapshot.getChildrenCount()-1){
//                                    for(int au=0; au< dataSnapshot.getChildrenCount(); au++){
//                                        adapter.addItem(new MenuData(menuName[au], menuPrice[au], menuExplain[au], isMain[au], uid, key[au]));
//                                    }
//                                    adapter.notifyDataSetChanged();
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                Toast.makeText(MenuManageActivity.this, "저장소 이미지 주소 가져오기 실패", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        it = data.getChildren().iterator();
//                        isMain[i] = it.next().getValue(Boolean.class);
//                        menuExplain[i] = it.next().getValue(String.class);
//                        menuName[i] = it.next().getValue(String.class);
//                        menuPrice[i] = it.next().getValue(String.class) + "원";
//
//                        if (it.hasNext()) {
//                            menuExplain[i] += '\n' + "옵션 : " + it.next().getValue(String.class);
//                            it.next().getValue(String.class);
//                        }
//                        if (it.hasNext()) {
//                            menuExplain[i] += ", " + it.next().getValue(String.class);
//                            it.next().getValue(String.class);
//                        }
//                        if (it.hasNext()) {
//                            menuExplain[i] += ", " + it.next().getValue(String.class);
//                            it.next().getValue(String.class);
//                        }
//                        if (it.hasNext()) {
//                            menuExplain[i] += ", " + it.next().getValue(String.class);
//                            it.next().getValue(String.class);
//                        }
//                        if (it.hasNext()) {
//                            menuExplain[i] += ", " + it.next().getValue(String.class);
//                            it.next().getValue(String.class);
//                        }
//
//                        i++;
//                    }
//
//                } else {
//                    binding.menuListView.setVisibility(View.GONE);
//                    binding.menuListText.setVisibility(View.VISIBLE);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//
//
//        binding.menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (isDeleteMode) {
//                    if (selectedPosition[position]) {
//                        selectedPosition[position] = false;
//                        view.setBackgroundColor(Color.rgb(255, 255, 255));
//                    } else {
//                        selectedPosition[position] = true;
//                        view.setBackgroundColor(Color.rgb(103, 153, 255));
//                    }
//                    int i = 0;
//                    for (boolean a : selectedPosition) {
//                        i++;
//                        Log.d("불리언", i + Boolean.toString(a));
//                    }
//                } else if (isMainSelect) {
//                    checkedItem = position;
//                    adapter.notifyDataSetChanged();
//                    view.setBackgroundColor(Color.rgb(103, 153, 255));
////                    if (oldPosition < parent.getChildCount())
////                        parent.getChildAt(oldPosition).setBackgroundColor(Color.rgb(255, 255, 255));
////                    oldPosition = position;
//
//                } else {
//                    Intent intent = new Intent(MenuManageActivity.this, MenuAddActivity.class);
//                    intent.putExtra("menuId", key[position]);
//                    intent.putExtra("isEdit", true);
//                    startActivityForResult(intent, MENU_EDIT_REQUEST);
//                }
//            }
//        });
//
//        binding.selectMainMenuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.commonContainer.setVisibility(View.GONE);
//                binding.mainModeContainer.setVisibility(View.VISIBLE);
//                binding.infoText.setText("대표로 지정할 메뉴를 선택해 주세요");
//                isMainSelect = true;
//            }
//        });
//
//        binding.mainFinishButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkedItem < 0) {
//                    Toast.makeText(MenuManageActivity.this, "아무것도 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
//                } else {
//                    binding.commonContainer.setVisibility(View.VISIBLE);
//                    binding.mainModeContainer.setVisibility(View.GONE);
//                    binding.infoText.setText("메뉴 정보");
//                    isMainSelect = false;
//                    adapter.notifyDataSetChanged();
//                    ref.child(uid).child("menu").child(key[checkedItem]).child("isMain").setValue(true);
//                    for (int i = 0; i < adapter.getCount(); i++) {
//                        if (i == checkedItem)
//                            continue;
//                        ref.child(uid).child("menu").child(key[i]).child("isMain").setValue(false);
//                    }
//                    checkedItem = -1;
//                }
//            }
//        });
//
//        binding.mainCancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.commonContainer.setVisibility(View.VISIBLE);
//                binding.mainModeContainer.setVisibility(View.GONE);
//                binding.infoText.setText("메뉴 정보");
//                isMainSelect = false;
//                adapter.notifyDataSetChanged();
//                checkedItem = -1;
//            }
//        });
//
//        binding.deleteStartButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.commonContainer.setVisibility(View.GONE);
//                binding.deleteModeContainer.setVisibility(View.VISIBLE);
//                binding.infoText.setText("삭제할 메뉴를 선택해 주세요");
//                isDeleteMode = true;
//                selectedPosition = new boolean[adapter.getCount()];
//            }
//        });
//
//        binding.deleteCancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.commonContainer.setVisibility(View.VISIBLE);
//                binding.deleteModeContainer.setVisibility(View.GONE);
////                for(int i = 0 ;i < adapter.getCount() ; i++){
////                    getViewByPosition(i,binding.menuListView).setBackgroundColor(Color.rgb(255, 255, 255));
//////                    binding.menuListView.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
////                }
//                isDeleteMode = false;
//                adapter.notifyDataSetChanged();
//                binding.infoText.setText("메뉴 정보");
//            }
//        });
//
//        binding.deleteFinishButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int selectedCount = 0;
//                for (boolean a : selectedPosition) {
//                    if (a)
//                        selectedCount++;
//                }
//                if (selectedCount == 0) {
//                    Toast.makeText(MenuManageActivity.this, "선택된 항목이 없습니다.", Toast.LENGTH_SHORT).show();
//                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuManageActivity.this);
//                    builder.setTitle("메뉴 삭제 확인");
//                    builder.setMessage(selectedCount + "개의 항목을 정말 삭제하시겠습니까?");
//                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            for (int i = 0; i < selectedPosition.length; i++) {
//                                if (selectedPosition[i]) {
//                                    ref.child(uid).child("menu").child(key[i]).setValue(null);
//                                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
//                                    StorageReference deleteRef = mStorageRef.child("market").child(uid).child("menu").child(key[i] + ".jpg");
//                                    deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Log.d("메뉴이미지 삭제", "성공");
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Log.d("메뉴이미지 삭제", "실패");
//                                        }
//                                    });
//                                    binding.commonContainer.setVisibility(View.VISIBLE);
//                                    binding.deleteModeContainer.setVisibility(View.GONE);
//                                    isDeleteMode = false;
//                                    binding.infoText.setText("메뉴 정보");
//                                    Toast.makeText(MenuManageActivity.this, "선택한 메뉴가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    });
//                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    deleteDialog = builder.create();
//                    deleteDialog.setCancelable(false);
//                    deleteDialog.setOnKeyListener(new Dialog.OnKeyListener() {
//                        @Override
//                        public boolean onKey(DialogInterface arg0, int keyCode,
//                                             KeyEvent event) {
//                            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                                deleteDialog.dismiss();
//                            }
//                            return true;
//                        }
//                    });
//                    deleteDialog.show();
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        ref.child(uid).child("menu").removeEventListener(listener);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        ref.child(uid).child("menu").addValueEventListener(listener);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == MENU_ADD_REQUEST && resultCode == RESULT_OK) {
//            Toast.makeText(this, "메뉴가 추가되었습니다.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    //뒤로가기 버튼 기능 설정
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    class MenuListAdapter extends BaseAdapter {
//
//
//        @Override
//        public void notifyDataSetChanged() {
//            super.notifyDataSetChanged();
//            Log.d("리스트뷰 데이터", "초기화됨" + isDeleteMode);
//        }
//
//        ArrayList<MenuData> items = new ArrayList<MenuData>();
//
//        //        Handler handler;
//        @Override
//        public int getCount() {
//            return items.size();
//        }
//
//        public void addItem(MenuData item) {
//            items.add(item);
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return items.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public void clear() {
//            items.clear();
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            final MenuDataView view = new MenuDataView(getApplicationContext());
//            MenuData item = items.get(position);
//            view.setMenuDataName(item.getMenuDataName());
//            view.setMenuDataPrice(item.getMenuDataPrice());
//            view.setMenuDataExplain(item.getMenuDataExplain());
//            if (item.getIsMain()) {
//                view.VisibleIsMainText();
//            }
//
//            try {
//                Glide
//                        .with(MenuManageActivity.this)
//                        .load(imageUrl[position])
//                        .thumbnail(0.1f)
//                        .into(view.menuDataImage);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            //이미지 url
////            StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(item.getUid()).child("menu").child(item.getMenuKey()+".jpg");
////            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                @Override
////                public void onSuccess(Uri uri) {
////                    imageUrl = String.valueOf(uri);
////
////                }
////            }).addOnFailureListener(new OnFailureListener() {
////                @Override
////                public void onFailure(@NonNull Exception exception) {
////                    Toast.makeText(MenuManageActivity.this, "저장소 이미지 주소 가져오기 실패", Toast.LENGTH_SHORT).show();
////                }
////            });
//
//            if (isDeleteMode) {
//                if (selectedPosition[position]) {
//                    view.setBackgroundColor(Color.rgb(103, 153, 255));
//                } else {
//                    view.setBackgroundColor(Color.rgb(255, 255, 255));
//                }
//            } else if (isMainSelect) {
//                if (position == checkedItem) {
//                    view.setBackgroundColor(Color.rgb(103, 153, 255));
//                }
//            }
//
//
//            return view;
//
//
//        }
//
//    }
//}
package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityMenuManageBinding;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class MenuManageActivity extends AppCompatActivity {
    public static final int MENU_ADD_REQUEST = 555;
    public static final int MENU_EDIT_REQUEST = 444;
    ActivityMenuManageBinding binding;
    DatabaseReference ref;
    MenuListAdapter adapter;
    Iterator<DataSnapshot> it;
    ValueEventListener listener;
    String key[];
    boolean selectedPosition[];
    boolean isDeleteMode;
    boolean isMainSelect;
    boolean refresh;
    int i;
    int menuCount;
    int checkedItem = -1;

    AlertDialog deleteDialog;

    String uid;

    @Override
    public void onBackPressed() {
        if (isDeleteMode) {
            binding.commonContainer.setVisibility(View.VISIBLE);
            binding.deleteModeContainer.setVisibility(View.GONE);
//                for(int i = 0 ;i < adapter.getCount() ; i++){
//                    getViewByPosition(i,binding.menuListView).setBackgroundColor(Color.rgb(255, 255, 255));
////                    binding.menuListView.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
//                }
            isDeleteMode = false;
            adapter.notifyDataSetChanged();
            binding.infoText.setText("메뉴 정보");
        } else if (isMainSelect) {
            binding.commonContainer.setVisibility(View.VISIBLE);
            binding.mainModeContainer.setVisibility(View.GONE);
            binding.infoText.setText("메뉴 정보");
            isMainSelect = false;
            adapter.notifyDataSetChanged();
            checkedItem = -1;
        } else {
            super.onBackPressed();
        }
    }

    void clear() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_manage);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        //타이틀 설정
        binding.toolBar.setTitle("메뉴 관리");
        binding.toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolBar);
        //뒤로가기 버튼 만들기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.addMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuAddActivity.class);
                intent.putExtra("isEdit", false);
                intent.putExtra("menuCount", menuCount + 1);
                startActivityForResult(intent, MENU_ADD_REQUEST);
            }
        });

        adapter = new MenuListAdapter();
        binding.menuListView.setAdapter(adapter);
        ref = FirebaseDatabase.getInstance().getReference("market");
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuCount = (int) dataSnapshot.getChildrenCount();
                if (menuCount > 0) {
                    int count = 0;
                    binding.menuListView.setVisibility(View.VISIBLE);
                    binding.menuListText.setVisibility(View.GONE);
                    adapter.clear();
                    key = new String[menuCount];
                    i = 0;

                    //메인메뉴가 있는 지 검사
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        key[i] = data.getKey();
                        Iterator<DataSnapshot> it1 = data.getChildren().iterator();
                        it1.next();
                        it1.next();
                        boolean isMain = it1.next().getValue(Boolean.class);
                        if (isMain)
                            break;
                        else {
                            count++;
                        }
                        i++;
                    }

                    //메인메뉴가 없는 경우 첫 번째 메뉴를 메인메뉴로 설정
                    if (count == menuCount) {
                        ref.child(uid).child("menu").child(key[0]).child("isMain").setValue(true);
                    }

                    MenuData menuDatas[] = new MenuData[menuCount];

                    i = 0;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        key[i] = data.getKey();
                        it = data.getChildren().iterator();
                        String aTime = it.next().getValue(String.class);
                        int aseq = it.next().getValue(Integer.class);
                        boolean isMain = it.next().getValue(Boolean.class);
                        String menuExplain = it.next().getValue(String.class);
                        String menuName = it.next().getValue(String.class);
                        String menuPrice = it.next().getValue(String.class) + "원";
                        String option = "없음";

                        if (it.hasNext()) {
                            option = "";
                            option += it.next().getValue(String.class);
                            it.next().getValue(String.class);
                        }
                        if (it.hasNext()) {
                            option += ", " + it.next().getValue(String.class);
                            it.next().getValue(String.class);
                        }
                        if (it.hasNext()) {
                            option += ", " + it.next().getValue(String.class);
                            it.next().getValue(String.class);
                        }
                        if (it.hasNext()) {
                            option += ", " + it.next().getValue(String.class);
                            it.next().getValue(String.class);
                        }
                        if (it.hasNext()) {
                            option += ", " + it.next().getValue(String.class);
                            it.next().getValue(String.class);
                        }

                        menuDatas[i] = new MenuData(menuName, menuPrice, menuExplain, option, isMain, uid, key[i], aTime, aseq);
//                        Log.d("다운로드 URL", menuImageURL);
                        i++;
                    }

                    //메뉴 순서대로 리스트에 추가
                    for (int iii = 1; iii < menuCount + 1; iii++) {
                        for (int jjj = 0; jjj < menuCount; jjj++) {
                            if (menuDatas[jjj].getAseq() == iii) {
                                adapter.addItem(menuDatas[jjj]);
                                break;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    binding.menuListView.setVisibility(View.GONE);
                    binding.menuListText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        binding.menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDeleteMode) {
                    if (selectedPosition[position]) {
                        selectedPosition[position] = false;
                        view.setBackgroundColor(Color.rgb(255, 255, 255));
                    } else {
                        selectedPosition[position] = true;
                        view.setBackgroundColor(Color.rgb(103, 153, 255));
                    }
                    int i = 0;
                    for (boolean a : selectedPosition) {
                        i++;
                        Log.d("불리언", i + Boolean.toString(a));
                    }
                } else if (isMainSelect) {
                    checkedItem = position;
                    adapter.notifyDataSetChanged();
                    view.setBackgroundColor(Color.rgb(103, 153, 255));
//                    if (oldPosition < parent.getChildCount())
//                        parent.getChildAt(oldPosition).setBackgroundColor(Color.rgb(255, 255, 255));
//                    oldPosition = position;

                } else {
                    Intent intent = new Intent(MenuManageActivity.this, MenuAddActivity.class);
                    intent.putExtra("menuId", key[position]);
                    intent.putExtra("isEdit", true);
                    startActivityForResult(intent, MENU_EDIT_REQUEST);
                }
            }
        });

        binding.selectMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref.child(uid).child("menu").removeEventListener(listener);

                binding.commonContainer.setVisibility(View.GONE);
                binding.mainModeContainer.setVisibility(View.VISIBLE);
                binding.infoText.setText("대표로 지정할 메뉴를 선택해 주세요");
                isMainSelect = true;
            }
        });

        binding.mainFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedItem < 0) {
                    Toast.makeText(MenuManageActivity.this, "아무것도 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    binding.commonContainer.setVisibility(View.VISIBLE);
                    binding.mainModeContainer.setVisibility(View.GONE);
                    binding.infoText.setText("메뉴 정보");
                    isMainSelect = false;
                    adapter.notifyDataSetChanged();

                    ref.child(uid).child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> mit;
                            String mainKey;
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                mainKey = data.getKey();
                                mit = data.getChildren().iterator();
                                mit.next();
                                int aseq = mit.next().getValue(Integer.class);
                                if (aseq == checkedItem+1) {
                                    ref.child(uid).child("menu").child(mainKey).child("isMain").setValue(true);
                                    break;
                                }
                            }
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                mainKey = data.getKey();
                                mit = data.getChildren().iterator();
                                mit.next();
                                int aseq = mit.next().getValue(Integer.class);
                                if (aseq == checkedItem+1) {
                                    continue;
                                }
                                ref.child(uid).child("menu").child(mainKey).child("isMain").setValue(false);
                            }

                            checkedItem = -1;

                            ref.child(uid).child("menu").addValueEventListener(listener);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        binding.mainCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.commonContainer.setVisibility(View.VISIBLE);
                binding.mainModeContainer.setVisibility(View.GONE);
                binding.infoText.setText("메뉴 정보");
                isMainSelect = false;
                adapter.notifyDataSetChanged();
                checkedItem = -1;

                ref.child(uid).child("menu").addValueEventListener(listener);
            }
        });

        binding.deleteStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref.child(uid).child("menu").removeEventListener(listener);

                binding.commonContainer.setVisibility(View.GONE);
                binding.deleteModeContainer.setVisibility(View.VISIBLE);
                binding.infoText.setText("삭제할 메뉴를 선택해 주세요");
                isDeleteMode = true;
                selectedPosition = new boolean[adapter.getCount()];
            }
        });

        binding.deleteCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.commonContainer.setVisibility(View.VISIBLE);
                binding.deleteModeContainer.setVisibility(View.GONE);
//                for(int i = 0 ;i < adapter.getCount() ; i++){
//                    getViewByPosition(i,binding.menuListView).setBackgroundColor(Color.rgb(255, 255, 255));
////                    binding.menuListView.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
//                }
                isDeleteMode = false;
                adapter.notifyDataSetChanged();

                ref.child(uid).child("menu").addValueEventListener(listener);

                binding.infoText.setText("메뉴 정보");
            }
        });

        binding.deleteFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedCount = 0;
                for (boolean a : selectedPosition) {
                    if (a)
                        selectedCount++;
                }
                if (selectedCount == 0) {
                    Toast.makeText(MenuManageActivity.this, "선택된 항목이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuManageActivity.this);
                    builder.setTitle("메뉴 삭제 확인");
                    builder.setMessage(selectedCount + "개의 항목을 정말 삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ref.child(uid).child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Iterator<DataSnapshot> dit;
                                    int count = (int) dataSnapshot.getChildrenCount();
                                    String deleteKey[] = new String[count];
                                    MenuData dmenuDatas[] = new MenuData[count];
                                    int r = 0;
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        deleteKey[r] = data.getKey();
                                        dit = data.getChildren().iterator();
                                        String aTime = dit.next().getValue(String.class);
                                        int aseq = dit.next().getValue(Integer.class);
                                        dmenuDatas[r] = new MenuData("", "", "", "", false, uid, deleteKey[r], aTime, aseq);
                                        r++;
                                    }
                                    for (int i = 0; i < selectedPosition.length; i++) {
                                        if (selectedPosition[i]) {
                                            for (int cc = 0; cc < count; cc++) {
                                                if (dmenuDatas[cc].getAseq() == i + 1) {
                                                    ref.child(uid).child("menu").child(dmenuDatas[cc].getMenuKey()).setValue(null);
                                                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                                                    StorageReference deleteRef = mStorageRef.child("market").child(uid).child("menu").child(dmenuDatas[cc].getMenuKey() + ".jpg");
                                                    deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("메뉴이미지 삭제", "성공");
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("메뉴이미지 삭제", "실패");
                                                        }
                                                    });
                                                    break;
                                                }
                                            }
                                        }
                                        if (i == selectedPosition.length - 1) {
                                            ref.child(uid).child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    int count2 = (int) dataSnapshot.getChildrenCount();
                                                    if (count2 > 0) {
                                                        Iterator<DataSnapshot> dit2;
                                                        String deleteKey2[] = new String[count2];
                                                        MenuData dmenuDatas2[] = new MenuData[count2];
                                                        int r2 = 0;
                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                            deleteKey2[r2] = data.getKey();
                                                            dit2 = data.getChildren().iterator();
                                                            String aTime = dit2.next().getValue(String.class);
                                                            int aseq = dit2.next().getValue(Integer.class);
                                                            boolean isMain = dit2.next().getValue(Boolean.class);
                                                            dmenuDatas2[r2] = new MenuData("", "", "", "", isMain, uid, deleteKey2[r2], aTime, aseq);
                                                            r2++;
                                                        }

                                                        MenuData tmp;
                                                        for (int bb = 0; bb < count2 - 1; bb++) {
                                                            for (int cc = bb + 1; cc < count2; cc++) {
                                                                if (dmenuDatas2[bb].getAseq() > dmenuDatas2[cc].getAseq()) {
                                                                    tmp = dmenuDatas2[bb];
                                                                    dmenuDatas2[bb] = dmenuDatas2[cc];
                                                                    dmenuDatas2[cc] = tmp;
                                                                }
                                                            }
                                                        }
                                                        for (int aa = 0; aa < count2; aa++) {
                                                            ref.child(uid).child("menu").child(dmenuDatas2[aa].getMenuKey()).child("aseq").setValue(aa + 1);
                                                        }

                                                        boolean mainExist = false;
                                                        for (int i = 0; i < count2; i++) {
                                                            if (dmenuDatas2[i].getIsMain()) {
                                                                mainExist = true;
                                                                break;
                                                            }
                                                        }
                                                        if (!mainExist) {
                                                            ref.child(uid).child("menu").child(dmenuDatas2[0].getMenuKey()).child("isMain").setValue(true);
                                                        }
                                                    }
                                                    binding.commonContainer.setVisibility(View.VISIBLE);
                                                    binding.deleteModeContainer.setVisibility(View.GONE);
                                                    isDeleteMode = false;
                                                    binding.infoText.setText("메뉴 정보");
                                                    ref.child(uid).child("menu").addValueEventListener(listener);
                                                    Toast.makeText(MenuManageActivity.this, "선택한 메뉴가 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    deleteDialog = builder.create();
                    deleteDialog.setCancelable(false);
                    deleteDialog.setOnKeyListener(new Dialog.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface arg0, int keyCode,
                                             KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                deleteDialog.dismiss();
                            }
                            return true;
                        }
                    });
                    deleteDialog.show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        ref.child(uid).child("menu").removeEventListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh = true;
        ref.child(uid).child("menu").addValueEventListener(listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MENU_ADD_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, "메뉴가 추가되었습니다.", Toast.LENGTH_SHORT).show();
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

    class MenuListAdapter extends BaseAdapter {

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            refresh = false;
            Log.d("리스트뷰 데이터", "초기화됨" + isDeleteMode);
        }

        ArrayList<MenuData> items = new ArrayList<MenuData>();
        String imageUrl;

        //        Handler handler;
        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(MenuData item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clear() {
            items.clear();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final MenuDataView view = new MenuDataView(getApplicationContext());
            MenuData item = items.get(position);
            view.setMenuDataName(item.getMenuDataName());
            view.setMenuDataPrice(item.getMenuDataPrice());
            view.setMenuDataExplain(item.getMenuDataExplain());
            view.setOption(item.getOption());
            if (item.getIsMain()) {
                view.VisibleIsMainText();
            }

            //이미지 url
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("market").child(item.getUid()).child("menu").child(item.getMenuKey() + ".jpg");

            try {
                Glide
                        .with(MenuManageActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(ref)
//                        .listener(new RequestListener<StorageReference, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                view.menuDataImage.setScaleType(ImageView.ScaleType.F);
//                                return false;
//                            }
//                        })
                        .override(300, 300)
                        .signature(new StringSignature(item.getATime()))
                        .placeholder(R.drawable.jamsil)
                        .thumbnail(0.1f)
                        .into(view.menuDataImage);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isDeleteMode) {
                if (selectedPosition[position]) {
                    view.setBackgroundColor(Color.rgb(103, 153, 255));
                } else {
                    view.setBackgroundColor(Color.rgb(255, 255, 255));
                }
            } else if (isMainSelect) {
                if (position == checkedItem) {
                    view.setBackgroundColor(Color.rgb(103, 153, 255));
                }
            }
            return view;
        }

    }
}