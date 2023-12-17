package com.example.bai4;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bai4.background_task.AlarmReceiver;
import com.example.bai4.background_task.MyJobScheduler;
import com.example.bai4.utils.MySharedPreferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int pic_id = 123;
    private static final int REQUEST_CODE = 456;
    private Button camera_open_id;
    private ImageView click_image_id;
    private MySharedPreferences myPreferences;
    private static final String TAG = MainActivity.class.getSimpleName();
    MyJobScheduler myJobScheduler;
    private static int kJobId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);
        myPreferences = MySharedPreferences.getPreferences(this);

//        scheduleJob();

        Intent startServiceIntent = new Intent(this, MyJobScheduler.class);
//        startServiceIntent.putExtra("messenger", new Messenger(mHandler));
        startService(startServiceIntent);
//
        camera_open_id.setOnClickListener(v -> {
            // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with camera_intent, and request pic id
            startActivityForResult(camera_intent, pic_id);
        });
        scheduleDailyAlarm();
    }

    private void scheduleDailyAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Đặt giờ hẹn mỗi ngày lúc 8:00 AM
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 11);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);

            // Nếu giờ hiện tại đã qua 8:00 AM, đặt giờ hẹn cho ngày hôm sau
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }

//    private void scheduleJob() {
//        Log.d("BBB", "hi 1");
//        ComponentName componentName = new ComponentName(this, MyJobScheduler.class);
//        JobInfo jobInfo = new JobInfo.Builder(1, componentName)
////                .setRequiresCharging(false)  // Có yêu cầu thiết bị cần sạc pin
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)  // Có yêu cầu mạng không đo lường
//                .setPersisted(true)  // Công việc vẫn tồn tại sau khi khởi động lại
////                .setPeriodic(24 * 60 * 60 * 1000)  // Chu kỳ thực hiện công việc (24 giờ)
//                .setPeriodic(20 * 1000)
//                .build();
//
//        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        if (jobScheduler != null) {
//            jobScheduler.schedule(jobInfo);
//        }
//    }
//    public void startJob(View view) {
//        ComponentName serviceComponent = new ComponentName(this, MyJobScheduler.class);
//        JobInfo.Builder builder = new JobInfo.Builder(kJobId++, serviceComponent);
//        builder.setMinimumLatency(1 * 1000); // wait at least
//        builder.setOverrideDeadline(2 * 1000); // maximum delay
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
//        builder.setRequiresDeviceIdle(true); // device should be idle
//        builder.setRequiresCharging(false); // we don't care if the device is charging or not
//        PersistableBundle bundle = new PersistableBundle();
//        bundle.putString("abc","123");
//        builder.setExtras(bundle);
//        JobScheduler jobScheduler =
//                (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        jobScheduler.schedule(builder.build());
//    }
//
//    public void cancelJob(View view) {
//        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        tm.cancelAll();
//    }

    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            click_image_id.setImageBitmap(photo);
            saveImageToGallary();
        }
    }

//    private void SaveImage(Bitmap finalBitmap) {
//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/saved_images");
//        myDir.mkdirs();
//        Random generator = new Random();
//        int n = 10000;
//        n = generator.nextInt(n);
//        String fname = "Image-"+ n +".jpg";
//        File file = new File (myDir, fname);
//        if (file.exists ()) file.delete ();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void test(){
////        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
////        PackageManager.PERMISSION_GRANTED){
//            saveImageToGallary();
////        }else{
////            ActivityCompat.requestPermissions(this, new String[]{
////                    Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION
////            }, REQUEST_CODE);
////        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImageToGallary();
            }else{
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToGallary() {
        Uri images;
        ContentResolver contentResolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else{
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/*");
        Uri uri = contentResolver.insert(images, contentValues);

        try{
            BitmapDrawable bitmapDrawable = (BitmapDrawable) click_image_id.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Objects.requireNonNull(outputStream);

            Toast.makeText(this, "Save successfully", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Toast.makeText(this, "Save failure", Toast.LENGTH_SHORT).show();
        }
    }
//    public  boolean isStoragePermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
//                return true;
//            } else {
//
//                Log.v(TAG,"Permission is revoked");
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
//                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
//            return true;
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CAMERA: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//            Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
//            SaveImage(bitmap);
//        } else {
//            Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
//        }
//        return;
//    }

    // other 'case' lines to check for other
    // permissions this app might request
//}
// }
}
