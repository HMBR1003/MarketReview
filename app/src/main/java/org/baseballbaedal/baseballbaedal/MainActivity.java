package org.baseballbaedal.baseballbaedal;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.baseballbaedal.baseballbaedal.BusinessMan.BusinessSignupActivity;
import org.baseballbaedal.baseballbaedal.BusinessMan.Menu.MenuManageActivity;
import org.baseballbaedal.baseballbaedal.BusinessMan.NoticeActivity;
import org.baseballbaedal.baseballbaedal.MainFragment.Delivery.Market.BasketActivity;
import org.baseballbaedal.baseballbaedal.MainFragment.DeliveryFragment;
import org.baseballbaedal.baseballbaedal.MainFragment.HomeFragment;
import org.baseballbaedal.baseballbaedal.MainFragment.TakeoutFragment;
import org.baseballbaedal.baseballbaedal.MainFragment.WeatherFragment;
import org.baseballbaedal.baseballbaedal.databinding.ActivityMainBinding;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import devlight.io.library.ntb.NavigationTabBar;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class MainActivity extends NewActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    public static final int LOGIN_REQUEST = 100;
    public static final int BUSINESS_SIGNUP_REQUEST = 200;
    public static final int SELECT_COL = 300;
    public static final int CHANGE_COL = 400;
    static int pushCount = 0;
    static int loginCount = 0;
    HomeFragment homeFragment;
    DeliveryFragment deliveryFragment;
    TakeoutFragment takeoutFragment;
    WeatherFragment weatherFragment;
    ViewPager viewPager;
    public static int isBusiness;
    public static int fragmentHeight = 0;
    long backTime;
    int colCheck = -1;


    NavigationTabBar navigationTabBar;
    ActivityMainBinding mainBinding;
    TextView userEmail;
    TextView userName;
    TextView selectedCol;
    MenuItem navLogin;
    MenuItem navCart;
    MenuItem navOrderList;
    MenuItem navReviewManage;
    MenuItem navChangeCol;
    MenuItem navNewBusiness;
    MenuItem navNotice;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    DatabaseReference myRef;
    String uid;

    AlertDialog loginDialog;

    SharedPreferences pref; //최초실행 체크 변수



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (fragmentHeight == 0) {
            int fullHeight;
            int fullWidth;
            int bottomHeight;
            int topHeight = 72;
            int toolbarHeight;
            bottomHeight = navigationTabBar.getHeight();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            fullHeight = displayMetrics.heightPixels;// 세로
            fullWidth = displayMetrics.widthPixels;

            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                topHeight = getResources().getDimensionPixelSize(resourceId);
            }

            View view = findViewById(R.id.mainActionBar);
            toolbarHeight = view.getHeight();

            fragmentHeight = fullHeight - bottomHeight - topHeight - toolbarHeight;
            BusProvider.getInstance().post(new HeightEvent(fragmentHeight,fullWidth));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        homeFragment = new HomeFragment();
        deliveryFragment = new DeliveryFragment();
        takeoutFragment = new TakeoutFragment();
        weatherFragment = new WeatherFragment();


        //구글 로그인 API 관련 작업
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("923425871569-vqi9qeuhlldersvjuo84ief9iepmukf1.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

//        //구글 광고 초기화 및 세팅
//        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4432641899551083~2094218055");
//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("4BCE50B4272FF67DEA2CA758DE49C66B")
//                .build();
//        mAdView.loadAd(adRequest);

        initUI();  //하단 UI 세팅
        //페이스북 해시 키 가져오기
        try {
            PackageInfo info = getPackageManager().getPackageInfo("org.baseballbaedal.baseballbaedal", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", "name not found");
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", "no such");
        }


        ((ImageView) findViewById(R.id.btnMenu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainBinding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

//        activityLoginBinding.loginContainer.addView(getToolbar("",true),0);
        //상단 UI 세팅
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        toolbar.setNavigationIcon(toolbar.getNavigationIcon());
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, mainBinding.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mainBinding.drawerLayout.setDrawerListener(toggle);
//        toggle.syncState();

        //왼쪽 슬라이드 메뉴 할당 및 리스너 부착
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //왼쪽 슬라이드 버튼들 아이디 할당
        Menu menu = navigationView.getMenu();
        navLogin = menu.findItem(R.id.nav_login);
        navNewBusiness = menu.findItem(R.id.nav_newBusiness);
        navReviewManage = menu.findItem(R.id.nav_reviewManage);
        navCart = menu.findItem(R.id.nav_cart);
        navOrderList = menu.findItem(R.id.nav_orderList);
        navChangeCol = menu.findItem(R.id.nav_changeCol);
        navNotice = menu.findItem(R.id.nav_notice);

        //왼쪽 슬라이드 메뉴 유저이메일과 유저 이름 아이디 할당
        View headerView = navigationView.getHeaderView(0);
        userEmail = (TextView) headerView.findViewById(R.id.userEmail);
        userName = (TextView) headerView.findViewById(R.id.userName);
        selectedCol = (TextView) headerView.findViewById(R.id.selectedCol);

        //파이어베이스 인증 객체 할당
        mAuth = FirebaseAuth.getInstance();
        //좌측 UI 세팅
        setLeftMenu();
        //인증 객체에 붙일 인증 상태 감시 리스너 정의
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //인증 상태가 바뀔 때마다 좌측 UI세팅
                setLeftMenu();
            }
        };
    }

    public void setBadge() {
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", pushCount + loginCount);
        //앱의  패키지 명
        intent.putExtra("badge_count_package_name", "org.baseballbaedal.baseballbaedal");
        // AndroidManifest.xml에 정의된 메인 activity 명
        intent.putExtra("badge_count_class_name", "org.baseballbaedal.baseballbaedal.MainActivity");
        sendBroadcast(intent);
    }

    //좌측 UI 세팅하는 함수
    public void setLeftMenu() {
        //현재 로그인한 유저 객체를 가져옴
        user = mAuth.getCurrentUser();
        //로그인한 유저가 있으면
        if (user != null) {
            //데이터베이스 유저 영역 참조변수 선언 및 초기화
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

            //데이터베이스에서 유저가 고객인지 사업자 등록중인지 사업자인지 담는 정보를 불러옴
            userRef.child(user.getUid()).child("isBusiness(0(not),1(applying),2(finish))").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //데이터 읽기가 완료 된 후의 동작
                    try {
                        //공통적인 로그인과 이메일 출력창 세팅
                        navLogin.setVisible(true);
                        navLogin.setTitle("로그아웃");
                        userEmail.setText(user.getEmail());

                        //유저의 사업자여부 데이터를 가져옴
                        isBusiness = dataSnapshot.getValue(Integer.class);

                        //고객일 경우
                        if (isBusiness == 0) {
                            userName.setText(user.getDisplayName() + "고객님");
                            navOrderList.setTitle("주문 내역");
                            navReviewManage.setTitle("리뷰 관리");
                            navNewBusiness.setTitle("사업자 신규등록 신청");
                            navNotice.setVisible(false);

                            //숨겼던 메뉴 보이게 함
                            navCart.setVisible(true);
                            navOrderList.setVisible(true);
                            navReviewManage.setVisible(true);
                            navNewBusiness.setVisible(true);
                            navChangeCol.setVisible(true);
                        }
                        //사업자 등록 신청한 사람일 경우
                        else if (isBusiness == 1) {
                            userName.setText(user.getDisplayName() + "고객님\n사업자 등록 신청중입니다.");
                            navOrderList.setTitle("주문 내역");
                            navReviewManage.setTitle("리뷰 관리");
                            navNewBusiness.setTitle("사업자 신청정보 수정");
                            navNotice.setVisible(false);

                            //숨겼던 메뉴 보이게 함
                            navCart.setVisible(true);
                            navOrderList.setVisible(true);
                            navReviewManage.setVisible(true);
                            navNewBusiness.setVisible(true);
                            navChangeCol.setVisible(true);
                        }
                        //사업자 등록이 완료된 사람일 경우
                        else if (isBusiness == 2) {
                            userName.setText(user.getDisplayName() + "점주님\n");
                            navCart.setVisible(false);
                            navOrderList.setTitle("주문 받은 내역");
                            navReviewManage.setTitle("메뉴 관리");
                            navNewBusiness.setTitle("매장 정보 수정");
                            navNotice.setVisible(true);

                            //숨겼던 메뉴 보이게 함
                            navOrderList.setVisible(true);
                            navReviewManage.setVisible(true);
                            navNewBusiness.setVisible(true);
                            navChangeCol.setVisible(true);
                        } else
                            Toast.makeText(MainActivity.this, "사업자여부 데이터가 0,1,2중 하나가 아닙니다.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(MainActivity.this, "메인 사업자여부 데이터 가져오기 성공", Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
//                            Toast.makeText(MainActivity.this, "메인 사업자여부 데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                }
            });
            //로그인한 유저가 없을 경우 초기화면으로 설정
        } else {
            userName.setText("로그인이 필요합니다");
            userEmail.setText("");
            navLogin.setTitle("로그인");
            navOrderList.setTitle("주문 내역");
            navReviewManage.setTitle("리뷰 관리");
            navNewBusiness.setTitle("사업자 신규등록 신청");
            navNotice.setVisible(false);

            navLogin.setVisible(true);
            navCart.setVisible(true);
            navOrderList.setVisible(true);
            navReviewManage.setVisible(true);
            navChangeCol.setVisible(true);
            navNewBusiness.setVisible(true);

        }
    }

    //동작설정 왼쪽 메뉴가 열렸을 때 뒤로가기 버튼 누르면 왼쪽 메뉴가 닫히게한다.
    //뒤로가기 키가 눌렸을 때 한번 더 물어본다
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - backTime < 2000) {
                ActivityCompat.finishAffinity(this);
                System.runFinalizersOnExit(true);
                System.exit(0);
            }
            Toast.makeText(this, "뒤로가기 키를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            backTime = System.currentTimeMillis();
        }
    }

    //로그아웃 동작 하는 함수
    public static void singOut() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
    }

    //좌측 네비게이션 바 리스너 동작 설정
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();  //눌려진 버튼의 아이디를 저장할 변수
        //유저 객체를 가져옴
        user = mAuth.getCurrentUser();

        //로그인 버튼 동작
        if (id == R.id.nav_login) {
            //로그인한 유저가 없을 경우엔 로그인창을 띄우는 동작을 함함
            if (user == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, LOGIN_REQUEST);
            }
            //로그인한 유저가 있을 경우엔 로그아웃할 지 물어본 후 로그아웃함
            else {
                //로그아웃할 지 경고창을 띄워 물어본다
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("로그아웃 확인");
                builder.setMessage("로그아웃 하시겠습니까?");
                //예 버튼 추가하기
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    //예 버튼 눌렀을 경우 로그아웃 시키기
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //db의 로그인 상태값을 0으로 만들어준 후
                        myRef.child("users").child(uid).child("isLogin").setValue(0);
                        //로그아웃 실행
                        MainActivity.singOut();
                        Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                //아니오 버튼 추가
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //정의한 경고창을 만든다
                AlertDialog dialog = builder.create();
                //다른화면을 눌러도 경고창이 꺼지지 않도록 설정
                dialog.setCancelable(false);
                //경고창을 띄운다
                dialog.show();
            }
        } else if (id == R.id.nav_cart) {  //왼쪽 슬라이드메뉴 장바구니 부분
            if (user == null) {
                pleaseLogin();
            } else {
                if(isBusiness!=2) {
                    Intent intent = new Intent(this, BasketActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        } else if (id == R.id.nav_orderList) {  //왼쪽 슬라이드메뉴 주문내역 부분
            if (user == null) {
                pleaseLogin();
            } else {

            }
        } else if (id == R.id.nav_reviewManage) {  //왼쪽 슬라이드메뉴 리뷰관리 부분
            if (user == null) {
                pleaseLogin();
            } else {
                if (isBusiness == 2) {
                    Intent intent = new Intent(this, MenuManageActivity.class);
                    intent.putExtra("uid", uid);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, BUSINESS_SIGNUP_REQUEST);
                } else {
                    Toast.makeText(this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (id == R.id.nav_changeCol) {  //왼쪽 슬라이드메뉴 경기장 변경 부분
            Intent intent = new Intent(this, ColSelectActivity.class);
            intent.putExtra("isFirst", false);
            startActivityForResult(intent, CHANGE_COL);

        } else if (id == R.id.nav_newBusiness) {  //왼쪽 슬라이드메뉴 사업자 신규 등록 부분
            if (user == null) {
                pleaseLogin();
            }
            //사업자 등록신청하는 화면을 새로 띄운다
            else {
                Intent intent = new Intent(this, BusinessSignupActivity.class);
                //uid와 사업자여부와 사용자 정보를 인텐트에 넣어서 전달해준다.
                intent.putExtra("uid", uid);
                intent.putExtra("isBusiness", isBusiness);
                intent.putExtra("name", user.getDisplayName());
                intent.putExtra("email", user.getEmail());
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, BUSINESS_SIGNUP_REQUEST);
            }
        } else if (id == R.id.nav_notice) {  //왼쪽 슬라이드메뉴 안내 문구 등록 부분
            Intent intent = new Intent(this, NoticeActivity.class);
            //uid와 사업자여부와 사용자 정보를 인텐트에 넣어서 전달해준다.
            intent.putExtra("uid", uid);
            intent.putExtra("isBusiness", isBusiness);
            intent.putExtra("name", user.getDisplayName());
            intent.putExtra("email", user.getEmail());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //로그인 해달라는 창을 띄우는 메서드
    public void pleaseLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage("먼저 로그인을 해주세요");
        builder.setPositiveButton("로그인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, LOGIN_REQUEST);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "구글 관련 서비스를 설정해주세요", Toast.LENGTH_SHORT).show();
    }

    //프래그먼트 어댑터
    private class FragmentAdapter extends FragmentStatePagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return homeFragment;
                case 1:
                    return deliveryFragment;
                case 2:
                    return takeoutFragment;
                case 3:
                    return weatherFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    @Override
    public void onResume() { //온리슘 시 파이어베이스 계정 객체에 리스너 부착
        super.onResume();
        //최초실행 체크하기
        pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if (first == false) {
            Log.d("최초실행여부", "최초실행입니다");
            Intent intent = new Intent(this, ColSelectActivity.class);
            intent.putExtra("isFirst", true);
            startActivityForResult(intent, SELECT_COL);
            //앱 최초 실행시 하고 싶은 작업
        } else {
            Log.d("최초실행여부", "최초실행이 아닙니다");
        }
        SharedPreferences colCheckpref = getSharedPreferences("selectedCol", MODE_PRIVATE);
        colCheck = colCheckpref.getInt("selectedCol", -1);
        switch (colCheck) {
            case 0:
                selectedCol.setText("잠실 야구장(두산,LG)");
                break;
            case 1:
                selectedCol.setText("고척 스카이돔(넥센)");
                break;
            case 2:
                selectedCol.setText("SK 행복드림구장");
                break;
            case 3:
                selectedCol.setText("한화 이글스파크");
                break;
            case 4:
                selectedCol.setText("삼성 라이온즈파크");
                break;
            case 5:
                selectedCol.setText("기아 챔피언스필드");
                break;
            case 6:
                selectedCol.setText("사직 야구장(롯데)");
                break;
            case 7:
                selectedCol.setText("KT 위즈파크");
                break;
            case 8:
                selectedCol.setText("마산 야구장(NC)");
                break;
            default:
                selectedCol.setText("선택된 야구장이 없습니다.");
                break;
        }

        //푸쉬 관련
        pushCount = 0;
        loginCount = 0;
        setBadge();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();

        //유저 객체를 가져옴
        user = mAuth.getCurrentUser();

        //유저가 로그인한 상태인 지 검사
        if (user != null) {
            uid = user.getUid();
            myRef = FirebaseDatabase.getInstance().getReference();

            //푸쉬토큰을 검사하여 다른 기기에서 로그인을 했는 지 확인한다
            myRef.child("users").child(uid).child("pushToken").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //현재 기기가 로그인 된 것이 아니라면
                    if (dataSnapshot.getValue() != null && !dataSnapshot.getValue(String.class).equals(FirebaseInstanceId.getInstance().getToken())) {
                        //로그아웃 후에
                        MainActivity.singOut();
                        //인증 상태 리스너를 추가
                        mAuth.addAuthStateListener(mAuthListener);
                        setLeftMenu();
                        Toast.makeText(MainActivity.this, "다른 기기에서 로그인하여 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.addAuthStateListener(mAuthListener);
                        setLeftMenu();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    setLeftMenu();
                }
            });
        } else {
            SharedPreferences sp = getSharedPreferences("basket", MODE_PRIVATE);
            sp.edit().clear().apply();
            mAuth.addAuthStateListener(mAuthListener);
            setLeftMenu();
        }
    }

    @Override
    public void onStop() {  //온스탑 시 파이어베이스 계정 객체에 리스너 제거
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //로그인 액티비티에서 로그인 성공 응답을 보내왔을 경우
        if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {

        }
        //사업자 신청 액티비티에서 성공 응답을 보내왔을 경우
        else if (requestCode == BUSINESS_SIGNUP_REQUEST && resultCode == RESULT_OK) {
            //제출 확인 안내 메세지 띄우기
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("안내");
            builder.setMessage("신청 내용이 제출되었습니다.\n운영진이 확인 후 승인해드리겠습니다.\n신청내용은 언제든지 다시 수정하실 수 있습니다.");
            //확인 버튼설정 및 버튼을 눌렀을 때 동작 설정
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }
        //첫 실행 시 경기장 선택 하고 응답 받아왔을 때
        else if (requestCode == SELECT_COL && resultCode == RESULT_OK) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst", true);
            editor.commit();
        }
    }

    //푸쉬 메세지를 수신하여 그 데이터를 인텐트로 받아온 경우의 동작
    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            processIntent(intent);
        }
        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent) {
        String from = intent.getStringExtra("from");
        if (from == null) {
            return;
        }
        String title = intent.getStringExtra("title");
        String contents = intent.getStringExtra("contents");
    }


    //하단 네비게이션 바 활성화 함수
    private void initUI() {
        viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
//        viewPager.setAdapter(new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return 4;
//            }
//
//            @Override
//            public boolean isViewFromObject(final View view, final Object object) {
//                return view.equals(object);
//            }
//
//            @Override
//            public void destroyItem(final View container, final int position, final Object object) {
//            }
//
//            @Override   //이부분에 프래그먼트로 변경 필요
//            public Object instantiateItem(final ViewGroup container, final int position) {
//                final View view;
//                switch (position) {
//                    case 0:
//                        view = LayoutInflater.from(getBaseContext()).inflate(R.layout.fragment_home, null, false);
//                        container.addView(view);
//                        return view;
//                    case 1:
//                        view = LayoutInflater.from(getBaseContext()).inflate(R.layout.fragment_delivery, null, false);
//                        container.addView(view);
//                        return view;
//                    case 2:
//                        view = LayoutInflater.from(getBaseContext()).inflate(R.layout.fragment_takeout, null, false);
//                        container.addView(view);
//                        return view;
//                    case 3:
//                        view = LayoutInflater.from(getBaseContext()).inflate(R.layout.fragment_baseinfo, null, false);
//                        container.addView(view);
//                        return view;
//                    default:
//                        return null;
//                }
//                final View view = LayoutInflater.from(
//                        getBaseContext()).inflate(R.layout.item_vp, null, false);
//
//                container.addView(view);
//                return view;
//            }
//        });

        final String[] colors = getResources().getStringArray(R.array.bottom_navigation_color);

        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        navigationTabBar.setBgColor(Color.WHITE);
        navigationTabBar.setInactiveColor(Color.rgb(116,212,159));
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.home1),
                        Color.parseColor(colors[0]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.home2))
                        .title("홈")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.delivery1),
                        Color.parseColor(colors[0]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("배달음식")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.takeout1),
                        Color.parseColor(colors[0]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
                        .title("테이크아웃")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.weather1),
                        Color.parseColor(colors[0]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("야구장 날씨")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
//                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }
}

