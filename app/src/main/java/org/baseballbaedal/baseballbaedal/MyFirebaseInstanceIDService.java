package org.baseballbaedal.baseballbaedal;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyIID";

    @Override
    public void onCreate() {
        super.onCreate();
        registerRestartAlarm(true);
        Log.d("서비스 실행","아이디서비스 실행됨");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerRestartAlarm(true);
        Log.d("서비스 실행","아이디디서비스 중지됨");
    }

    public void registerRestartAlarm(boolean isOn) {
        Intent intent = new Intent(MyFirebaseInstanceIDService.this, RestartReceiver.class);
        intent.setAction(RestartReceiver.ID_RESTART_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (isOn) {
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 60000, sender);
        } else {
            am.cancel(sender);
        }

        Log.d("서비스 실행","아이디 리시버 알람 실행");
    }
    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "onTokenRefresh() 호출됨.");
        //리프레쉬된 토큰 변수에 저장
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed Token : " + refreshedToken);

        //로그인된 유저가 있다면 유저 데이터베이스에 토큰 저장
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
            myRef.child("users").child(user.getUid()).child("pushToken").setValue(FirebaseInstanceId.getInstance().getToken());
        }

    }

}
