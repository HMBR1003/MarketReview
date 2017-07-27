package org.baseballbaedal.baseballbaedal.BusinessMan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuAddActivity;
import org.baseballbaedal.baseballbaedal.R;
import org.baseballbaedal.baseballbaedal.databinding.ActivityNoticeBinding;

public class NoticeActivity extends AppCompatActivity {
    ActivityNoticeBinding binding;
    AlertDialog submitDialog, exitDialog;
    ProgressDialog dialog;
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            binding.noticeLimit.setText(binding.noticeText.length() + " / 200자");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notice);

        dialog = new ProgressDialog(NoticeActivity.this);

        binding.toolBar.setTitle("공지사항 등록/수정");
        binding.toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolBar);
        //뒤로가기 버튼 만들기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.noticeText.setClearButtonSet(false);
        binding.noticeText.addTextChangedListener(watcher);
        binding.submitButton.setButtonColor(getResources().getColor(R.color.buttonColor));
        binding.submitButton.setCornerRadius(15);
        loadData();

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);
                builder.setTitle("제출 확인");
                builder.setMessage("이대로 공지를 등록  하시겠습니까?");
                //확인 버튼설정 및 버튼을 눌렀을 때 동작 설정
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("market").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notice")
                                .setValue(binding.noticeText.getText().toString());
                        Toast.makeText(NoticeActivity.this, "공지가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
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
        });
    }

    //상단 뒤로가기 버튼 기능 설정
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
        AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);
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

    public void loadData() {
        //데이터 불러오는 중이라고 알림창 띄우기
        dialog.setProgress(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터를 불러오는 중입니다...");
        dialog.setCancelable(false);
        dialog.show();

        //데이터베이스 초기화


        //데이터 불러와서 화면에 세팅하기
        FirebaseDatabase.getInstance().getReference().child("market").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String notice = dataSnapshot.getValue(String.class);
                    binding.noticeText.setText(notice);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(NoticeActivity.this, "데이터 가져오기 실패 에러 내용 : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
