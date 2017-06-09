package org.baseballbaedal.baseballbaedal.BusinessMan.Menu;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    ActivityMenuManageBinding binding;
    DatabaseReference ref;
    MenuListAdapter adapter;
    Iterator<DataSnapshot> it;
    ValueEventListener listener;
    StorageReference storageRef;
    int i;
    int j;

    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_manage);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        Log.d("디렉토리", Environment.getExternalStorageDirectory().getAbsolutePath());
        //타이틀 설정
        binding.toolBar.setTitle("메뉴 관리");
        binding.toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(binding.toolBar);
        //뒤로가기 버튼 만들기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.addMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MenuAddActivity.class);
                startActivityForResult(intent,MENU_ADD_REQUEST);
            }
        });

        adapter = new MenuListAdapter();
        binding.menuListView.setAdapter(adapter);
        binding.menuListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ref = FirebaseDatabase.getInstance().getReference("market");
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0) {
                    binding.menuListView.setVisibility(View.VISIBLE);
                    binding.menuListText.setVisibility(View.GONE);
                    adapter.clear();
//                    String key[] = new String[(int) dataSnapshot.getChildrenCount()];

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        key[i] = data.getKey();
                        it = data.getChildren().iterator();

                        String menuExplain = it.next().getValue(String.class);
                        String menuImageURL = it.next().getValue(String.class);
                        String menuName = it.next().getValue(String.class);
                        String menuPrice = it.next().getValue(String.class)+"원";

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
                        adapter.addItem(new MenuData(menuName, menuPrice, menuExplain, menuImageURL));
                        adapter.notifyDataSetChanged();
                        Log.d("다운로드 URL", menuImageURL);
                    }
                }
                else{
                    binding.menuListView.setVisibility(View.GONE);
                    binding.menuListText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
//        adapter.addItem(new MenuData("아아","이건",
//                "테스트에요",
//                "https://firebasestorage.googleapis.com/v0/b/baseballbaedal.appspot.com/o/market%2FQK9vWDogObdhckFdXhHVOc963PS2%2Fmenu%2F-Km28pRZDz_-horuv3nq%2Fmenu.jpg?alt=media&token=0f0bc2e8-016e-4c96-970b-ed1e57e73a46"));





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
        if(requestCode==MENU_ADD_REQUEST&&resultCode==RESULT_OK){
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
        ArrayList<MenuData> items = new ArrayList<MenuData>();
        String imageUrl;
//        Handler handler;
        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(MenuData item){
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

        public void clear(){
            items.clear();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MenuDataView view = new MenuDataView(getApplicationContext());
            MenuData item = items.get(position);
            view.setMenuDataName(item.getMenuDataName());
            view.setMenuDataPrice(item.getMenuDataPrice());
            view.setMenuDataExplain(item.getMenuDataExplain());
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
