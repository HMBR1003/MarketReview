package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
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

import org.baseballbaedal.baseballbaedal.BaseActivity;
import org.baseballbaedal.baseballbaedal.NewActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityMenuManageBinding;

import java.util.Iterator;

public class MenuManageActivity extends NewActivity implements MenuAdapter.OnStartDragListener {
    public static final int MENU_ADD_REQUEST = 555;
    public static final int MENU_EDIT_REQUEST = 444;
    ActivityMenuManageBinding binding;
    MenuAdapter adapter;
    LinearLayoutManager layoutManager;
    ItemTouchHelper itemTouchHelper;
    DatabaseReference ref;
    Iterator<DataSnapshot> it;
    ValueEventListener listener;
    public static String key[];
    public static boolean selectedPosition[];
    public static boolean isDeleteMode;
    public static boolean isMainSelect;
    public static boolean isMoveMode;
    boolean refresh;
    int i;
    int menuCount;
    public static int checkedItem = -1;
    String uid;
    AlertDialog deleteDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_manage);
        isMoveMode = false;
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        //상단 툴바 설정
        setToolbar(binding.toolBar,"메뉴 관리", Color.WHITE,true);

        binding.addMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuAddActivity.class);
                intent.putExtra("isEdit", false);
                intent.putExtra("menuCount", menuCount + 1);
                startActivityForResult(intent, MENU_ADD_REQUEST);
            }
        });

        adapter = new MenuAdapter(this, this);
        layoutManager = new LinearLayoutManager(this);
        binding.menuListView.setLayoutManager(layoutManager);
        binding.menuListView.setAdapter(adapter);
        MenuTouchCallback callback = new MenuTouchCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.menuListView);

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
                        String menuPrice = it.next().getValue(String.class);
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
        binding.moveMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child(uid).child("menu").removeEventListener(listener);
                binding.commonContainer.setVisibility(View.GONE);
                binding.moveModeContainer.setVisibility(View.VISIBLE);
                binding.infoText.setText("메뉴 사진을 누른 채로 이동시킬 수 있습니다.");
                isMoveMode = true;
            }
        });

        binding.moveCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.commonContainer.setVisibility(View.VISIBLE);
                binding.moveModeContainer.setVisibility(View.GONE);
                binding.infoText.setText("메뉴 정보");
                isMoveMode = false;
                adapter.notifyDataSetChanged();

                ref.child(uid).child("menu").addValueEventListener(listener);
            }
        });

        binding.moveFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<menuCount; i++) {
                    String s = adapter.items.get(i).getMenuKey();
                    ref.child(uid).child("menu").child(s).child("aseq").setValue(i+1);
                }
                binding.commonContainer.setVisibility(View.VISIBLE);
                binding.moveModeContainer.setVisibility(View.GONE);
                binding.infoText.setText("메뉴 정보");
                isMoveMode = false;
                adapter.notifyDataSetChanged();

                ref.child(uid).child("menu").addValueEventListener(listener);
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
                                if (aseq == checkedItem + 1) {
                                    ref.child(uid).child("menu").child(mainKey).child("isMain").setValue(true);
                                    break;
                                }
                            }
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                mainKey = data.getKey();
                                mit = data.getChildren().iterator();
                                mit.next();
                                int aseq = mit.next().getValue(Integer.class);
                                if (aseq == checkedItem + 1) {
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
                selectedPosition = new boolean[adapter.getItemCount()];
            }
        });

        binding.deleteCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.commonContainer.setVisibility(View.VISIBLE);
                binding.deleteModeContainer.setVisibility(View.GONE);
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
        else if (requestCode == MENU_EDIT_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, "메뉴가 수정되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStartDrag(MenuViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onBackPressed() {
        if (isDeleteMode) {
            binding.commonContainer.setVisibility(View.VISIBLE);
            binding.deleteModeContainer.setVisibility(View.GONE);
            isDeleteMode = false;
            adapter.notifyDataSetChanged();

            ref.child(uid).child("menu").addValueEventListener(listener);

            binding.infoText.setText("메뉴 정보");
        } else if (isMainSelect) {
            binding.commonContainer.setVisibility(View.VISIBLE);
            binding.mainModeContainer.setVisibility(View.GONE);
            binding.infoText.setText("메뉴 정보");
            isMainSelect = false;
            adapter.notifyDataSetChanged();
            checkedItem = -1;

            ref.child(uid).child("menu").addValueEventListener(listener);
        } else if (isMoveMode) {
            binding.commonContainer.setVisibility(View.VISIBLE);
            binding.moveModeContainer.setVisibility(View.GONE);
            binding.infoText.setText("메뉴 정보");
            isMoveMode = false;
            adapter.notifyDataSetChanged();

            ref.child(uid).child("menu").addValueEventListener(listener);
        } else {
            super.onBackPressed();
        }
    }
}