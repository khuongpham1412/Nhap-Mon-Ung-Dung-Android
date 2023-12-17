package com.example.bai4.background_task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.bai4.notification.MyNotification;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LLL", "HAIZ");
        MyNotification myNotification = MyNotification.getInstance(context);
        myNotification.createAndSendNotificationChannel();
    }
}