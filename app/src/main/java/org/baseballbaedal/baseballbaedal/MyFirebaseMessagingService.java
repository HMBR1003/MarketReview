package org.baseballbaedal.baseballbaedal;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.baseballbaedal.baseballbaedal.Order.OrderListActivity;

import java.util.Map;

import static org.baseballbaedal.baseballbaedal.MainActivity.orderPushNumber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyMS";
    NotificationManager notificationManager;
    Bitmap largeIcon;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived() 호출됨.");

        //메세지 보낸 곳 발신자 코드 저장
        String from = remoteMessage.getFrom();

        //받아온 메세지내용 추출해서 저장
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String content = data.get("content");
        String type = data.get("type");

        Log.v(TAG, "from : " + from + ", content : " + content);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        sendNotification(title, content, type);

        //앱에 알림 뱃지 달기 작업
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", MainActivity.pushCount + MainActivity.loginCount);
        //앱의  패키지 명
        intent.putExtra("badge_count_package_name", "org.baseballbaedal.baseballbaedal");
        // AndroidManifest.xml에 정의된 메인 activity 명
        intent.putExtra("badge_count_class_name", "org.baseballbaedal.baseballbaedal.MainActivity");
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancelAll();
    }

    private void sendNotification(String title, String content, String type) {
        largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notifications_black_24dp);
        if (type.equals("1")) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                MainActivity.singOut();
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
                    .bigText("다른 기기에서 " + content + " 계정으로 로그인 하였습니다.")
                    .build();


            notificationManager.notify(9999 /* ID of notification */, notification);
        } else {
            Intent intent = new Intent(this, OrderListActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    0);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(title)
//                    .setContentText(content)
//                    .setNumber(++MainActivity.pushCount)
////                    .setSubText(MainActivity.pushCount+"")
//                    .setAutoCancel(false)
//                    .setOngoing(true)
//                    .setSound(defaultSoundUri)
//                    .setVibrate(new long[]{1000})
//                    .setContentIntent(pendingIntent)
//                    .setPriority(Notification.PRIORITY_MAX)
//                    .setGroupSummary(true)
//                    .setGroup("abc");
//            if (android.os.Build.VERSION.SDK_INT >= 24) {
//                notificationBuilder.setSubText(MainActivity.pushCount + "");
//            }

            Notification.Builder builder = new Notification.Builder(this)
                    .setContentTitle("주문 알림")
                    .setContentText("뿌우우")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setNumber(++MainActivity.orderCount)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{0, 1000})
                    .setLargeIcon(largeIcon)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_MAX);
//                    .setTicker("주문 알림");
            if (android.os.Build.VERSION.SDK_INT >= 24) {
                builder.setSubText(MainActivity.orderCount + "");
            }

            Notification notification = new Notification.BigTextStyle(builder)
                    .setSummaryText("주문 알림")
                    .bigText("주문이왔어요~")
                    .build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notificationManager.notify(orderPushNumber /* ID of notification */, notification);
            orderPushNumber++;
            if (orderPushNumber > 100) {
                orderPushNumber = 0;
            }
        }
    }

}
