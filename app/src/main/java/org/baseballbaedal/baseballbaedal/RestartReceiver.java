package org.baseballbaedal.baseballbaedal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017-08-28-028.
 */

public class RestartReceiver extends BroadcastReceiver {

    static public final String MESSAGE_RESTART_SERVICE = "Message.restart";    // 값은 맘대로
    static public final String ID_RESTART_SERVICE = "ID.restart";    // 값은 맘대로

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i;
        if(intent.getAction().equals(MESSAGE_RESTART_SERVICE)){
            i = new Intent(context, MyFirebaseMessagingService.class);
            context.startService(i);
        }
        else{
            i = new Intent(context, MyFirebaseInstanceIDService.class);
            context.startService(i);
        }
    }
}

