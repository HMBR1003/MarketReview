package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.baseballbaedal.baseballbaedal.BusinessMan.BusinessSignupActivity;
import org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuAddActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.Test.DataTestView;
import org.baseballbaedal.baseballbaedal.Test.TestData;
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
    int i;

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
                startActivityForResult(intent, MENU_ADD_REQUEST);
            }
        });

        adapter = new MenuListAdapter();
        binding.menuListView.setAdapter(adapter);
        ref = FirebaseDatabase.getInstance().getReference("market");
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    int count = 0;
                    binding.menuListView.setVisibility(View.VISIBLE);
                    binding.menuListText.setVisibility(View.GONE);
                    adapter.clear();
                    key = new String[(int) dataSnapshot.getChildrenCount()];
                    i=0;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        key[i] = data.getKey();
                        Iterator<DataSnapshot> it1 = data.getChildren().iterator();
                        boolean isMain = it1.next().getValue(Boolean.class);
                        if (isMain)
                            break;
                        else {
                            count++;
                        }
                        i++;
                    }
                    if(count==dataSnapshot.getChildrenCount()){
                        ref.child(uid).child("menu").child(key[0]).child("isMain").setValue(true);
                    }
                    i = 0;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        key[i] = data.getKey();
                        it = data.getChildren().iterator();
                        boolean isMain = it.next().getValue(Boolean.class);
                        String menuExplain = it.next().getValue(String.class);
                        String menuImageURL = it.next().getValue(String.class);
                        String menuName = it.next().getValue(String.class);
                        String menuPrice = it.next().getValue(String.class) + "원";

                        if (it.hasNext()) {
                            menuExplain += '\n' + "옵션 : " + it.next().getValue(String.class);
                            it.next().getValue(String.class);
                        }
                        if (it.hasNext()) {
                            menuExplain += ", " + it.next().getValue(String.class);
                            it.next().getValue(String.class);
                        }
                        if (it.hasNext()) {
                            menuExplain += ", " + it.next().getValue(String.class);
                            it.next().getValue(String.class);
                        }
                        if (it.hasNext()) {
                            menuExplain += ", " + it.next().getValue(String.class);
                            it.next().getValue(String.class);
                        }
                        if (it.hasNext()) {
                            menuExplain += ", " + it.next().getValue(String.class);
                            it.next().getValue(String.class);
                        }
                        adapter.addItem(new MenuData(menuName, menuPrice, menuExplain, menuImageURL, isMain));
                        adapter.notifyDataSetChanged();
//                        Log.d("다운로드 URL", menuImageURL);
                        i++;
                    }
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
                    for (int i = 0; i < adapter.getCount(); i++) {
                        ref.child(uid).child("menu").child(key[i]).child("isMain").setValue(false);
                    }
                    ref.child(uid).child("menu").child(key[checkedItem]).child("isMain").setValue(true);
                    checkedItem = -1;
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
            }
        });

        binding.deleteStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            for (int i = 0; i < selectedPosition.length; i++) {
                                if (selectedPosition[i]) {
                                    ref.child(uid).child("menu").child(key[i]).setValue(null);
                                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                                    StorageReference deleteRef = mStorageRef.child("market").child(uid).child("menu").child(key[i]).child("menu.jpg");
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
                                    binding.commonContainer.setVisibility(View.VISIBLE);
                                    binding.deleteModeContainer.setVisibility(View.GONE);
                                    isDeleteMode = false;
                                    binding.infoText.setText("메뉴 정보");
                                    Toast.makeText(MenuManageActivity.this, "선택한 메뉴가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
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
//            if (items.size() == 1)
//                ref.child(uid).child("menu").child(key[0]).child("isMain").setValue(true);
            final MenuDataView view = new MenuDataView(getApplicationContext());
            MenuData item = items.get(position);
            view.setMenuDataName(item.getMenuDataName());
            view.setMenuDataPrice(item.getMenuDataPrice());
            view.setMenuDataExplain(item.getMenuDataExplain());
            if (item.getIsMain()) {
                view.VisibleIsMainText();
            }
            //이미지 url
            imageUrl = item.getMenuDataImage();

            Picasso.with(getApplicationContext())
                    .load(imageUrl)
                    .fit()
                    .placeholder(R.drawable.jamsil)
                    .centerInside()
                    .into(view.menuDataImage, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
//                            try {
//                                FileOutputStream out = new FileOutputStream(getTempFile().getPath());
//                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                                out.close();
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            sendUri = Uri.fromFile(getTempFile());
//                            dialog.dismiss();
                        }

                        @Override
                        public void onError() {
                            super.onError();
                            Toast.makeText(MenuManageActivity.this, "이미지 표시 에러", Toast.LENGTH_SHORT).show();
                        }
                    });
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


            //url로부터 이미지 비트맵을 가져와 이미지뷰에 세팅하는핸들러 설정
//            handler = new Handler() {
//                public void handleMessage(Message msg) {
//                    view.setMenuDataImage((Bitmap)msg.obj);
//                }
//            };
//
//            //url에서 비트맵 추출하는 작업을 스레드로 실행
//            new Thread() {
//                public void run() {
//                    Bitmap b = getBitmap(imageUrl);
//                    Message msg = Message.obtain(handler, 1111, b);
//                    handler.sendMessage(msg);
//                }
//            }.start();

        }
//    public static Bitmap getBitmap(String imageURL) {
//        Bitmap imgBitmap = null;
//        HttpURLConnection conn = null;
//        BufferedInputStream bis = null;
//
//        try {
//            URL url = new URL(imageURL);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.connect();
//
//            int nSize = conn.getContentLength();
//            bis = new BufferedInputStream(conn.getInputStream(), nSize);
//            imgBitmap = BitmapFactory.decodeStream(bis);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (bis != null) {
//                try {
//                    bis.close();
//                } catch (IOException e) {
//                }
//            }
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//
//        return imgBitmap;
//    }
    }
}
