package com.example.bai4.notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bai4.R;

public class MyNotification {
    private static final String CHANNEL_ID = "CHANNEL_ID_NOTIFICATION";
    private NotificationCompat.Builder builder;
    @SuppressLint("StaticFieldLeak")
    private static MyNotification myNotification;
    private Context context;

    public MyNotification(Context context){
        this.context = context;
        if(builder == null){
             builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Selfie notification!")
                    .setContentText("Hey guys, it's time to take pictures, you know ^^")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Hey guys, it's time to take pictures, you know ^^"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
    }

    public static MyNotification getInstance(Context context){
        if(myNotification == null){
            myNotification = new MyNotification(context);
        }
        return myNotification;
    }
    public void createAndSendNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "chanel";
            String description = "chanel des";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, builder.build());
    }

//    public void sendNotification (){
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);
//        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        notificationManager.notify(1, builder.build());
//    }
}
