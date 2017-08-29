package org.baseballbaedal.baseballbaedal;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.baseballbaedal.baseballbaedal.Order.OrderListActivity;

import java.util.Map;

import static org.baseballbaedal.baseballbaedal.MainActivity.loginCount;
import static org.baseballbaedal.baseballbaedal.MainActivity.orderCount;
import static org.baseballbaedal.baseballbaedal.MainActivity.orderPushNumber;
import static org.baseballbaedal.baseballbaedal.MainActivity.pushCount;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyMS";
    NotificationManager notificationManager;
    Bitmap largeIcon;

    @Override
    public void onCreate() {
        super.onCreate();
        registerRestartAlarm(true);
        Log.d("서비스 실행","푸시서비스 실행됨");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerRestartAlarm(true);
        Log.d("서비스 실행","푸시서비스 중지됨");
    }

    public void registerRestartAlarm(boolean isOn) {
        Intent intent = new Intent(MyFirebaseMessagingService.this, RestartReceiver.class);
        intent.setAction(RestartReceiver.MESSAGE_RESTART_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (isOn) {
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 60000, sender);
        } else {
            am.cancel(sender);
        }

        Log.d("서비스 실행","푸시 리시버 알람 실행");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived() 호출됨.");

        //메세지 보낸 곳 발신자 코드 저장
        String from = remoteMessage.getFrom();

        //받아온 메세지내용 추출해서 저장
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String content1 = data.get("content1");
        String content2 = data.get("content2");
        String type = data.get("type");

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        sendNotification(title, content1,content2, type);
        if (type.equals("1")) {
            loginCount++;
        }
        else{
            pushCount++;
        }
        //앱에 알림 뱃지 달기 작업
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", MainActivity.pushCount+loginCount);
        //앱의  패키지 명
        intent.putExtra("badge_count_package_name", "org.baseballbaedal.baseballbaedal");
        // AndroidManifest.xml에 정의된 메인 activity 명
        intent.putExtra("badge_count_class_name", "org.baseballbaedal.baseballbaedal.MainActivity");
        sendBroadcast(intent);

        //알림이 왔을 때 화면 깨우기
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE );
        PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG" );
        wakeLock.acquire(3000);

    }

    private void sendNotification(String title, String content1,String content2, String type) {
        largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notifications_black_24dp);
        if (type.equals("1")) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                MainActivity.singOut();
                notificationManager.cancelAll();
                pushCount = 0;
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification.Builder builder = new Notification.Builder(this)
                    .setContentTitle("로그인 알림")
                    .setContentText("다른 기기에서 로그인 하였습니다.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setNumber(++MainActivity.loginCount)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    .setLargeIcon(largeIcon)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setTicker("로그인 알림");
            if (android.os.Build.VERSION.SDK_INT >= 24) {
                builder.setSubText(MainActivity.loginCount + "");
            }


            Notification notification = new Notification.BigTextStyle(builder)
                    .setSummaryText("로그인 알림")
                    .bigText("다른 기기에서 " + content1 + " 계정으로 로그인 하였습니다.")
                    .build();


            notificationManager.notify(9999 /* ID of notification */, notification);
        } else {
            Intent intent = new Intent(this, OrderListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    0);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification.Builder builder = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(content1)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setNumber(pushCount)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{0, 1000})
                    .setLargeIcon(largeIcon)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX);

            if (android.os.Build.VERSION.SDK_INT >= 24) {
                Notification.Builder groupBuilder = new Notification.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(true)
                        .setGroup("주문")
                        .setGroupSummary(true)
                        .setContentIntent(pendingIntent);
                builder.setGroup("주문");

                notificationManager.notify(1111, groupBuilder.build());

                builder.setSubText(MainActivity.pushCount + "");
            }

            Notification notification = new Notification.BigTextStyle(builder)
                    .bigText(content2)
                    .build();

            //알림이 사라지지 않게 하기 위한 플래그
//            notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;


            //알림에 LED 띄우기
            notification.ledARGB = getResources().getColor(R.color.buttonColor);
            notification.ledOnMS = 2000;
            notification.ledOffMS = 1000;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;

            notificationManager.notify(pushCount/* ID of notification */, notification);
        }
    }

}
