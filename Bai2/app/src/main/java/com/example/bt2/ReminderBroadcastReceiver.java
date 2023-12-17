package com.example.bt2;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bt2.Activities.MainActivity;
import com.example.bt2.Activities.NoteActivity;
import com.example.bt2.Activities.NoteDetailActivity;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "alarm_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Intent notificationIntent = new Intent(context, NoteDetailActivity.class);
        notificationIntent.putExtra("ID_NOTE", bundle.getInt("NOTE_ID"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_label_important)
                .setContentTitle(bundle.getString("NOTE_TITLE"))
                .setContentText(bundle.getString("NOTE_DESC"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Gửi thông báo
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            // Kiểm tra phiên bản Android để tạo kênh thông báo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Hẹn giờ", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}