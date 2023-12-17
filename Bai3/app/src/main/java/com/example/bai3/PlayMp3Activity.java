package com.example.bai3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bai3.models.ListPath;
import com.example.bai3.utils.Helper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMp3Activity extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView txtDuration;
    private TextView txtTotalTime;
    private CircleImageView imageMusic;
    private ImageView btnPrevious;
    private ImageView btnStart;
    private ImageView btnNext;
    private MediaPlayer mp;
    private TextView txtMusicName;
    private long elapsedTimeMillis;
    RotateAnimation rotateAnimation;

    ArrayList<ListPath> listPaths;
    String musicName;
    String uri;
    private float currentRotation = 0f;
    private Handler handler;
    float timeSeekBar = 0f;
    private Bitmap musicImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mp3);
        
        MappingViewById();

        rotateAnimation = new RotateAnimation(
                currentRotation, currentRotation + 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(6000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setFillAfter(true);

        imageMusic.startAnimation(rotateAnimation);

        Type listType = new TypeToken<ArrayList<ListPath>>() {}.getType();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        listPaths = new Gson().fromJson(bundle.getString("paths"), listType);
        uri = bundle.getString("uri");
        musicName = bundle.getString("name");
        Type listType1 = new TypeToken<Bitmap>() {}.getType();
        musicImage = new Gson().fromJson(bundle.getString("image"), listType1);

        if(listPaths != null && listPaths.size() > 0 && uri != null){
            txtMusicName.setText(musicName);
            mp = new MediaPlayer();
            try {
                mp.setDataSource(uri);
                mp.prepare();
                mp.start();
            } catch (Exception e) {
                Log.e("AAA", "prepare() failed");
            }

            if(musicImage != null){
                imageMusic.setImageBitmap(musicImage);
            }

            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    elapsedTimeMillis += 1000; // Cập nhật thời gian mỗi giây
                    updateTimerText();
                    timeSeekBar += 100 / (mp.getDuration() / 1000f);
                    seekBar.setProgress((int) Math.ceil(timeSeekBar));
                    if (elapsedTimeMillis >= mp.getDuration() - 1000) {
                        // Dừng cập nhật thời gian khi đã đạt đến số mili giây mong muốn
                        btnStart.setImageResource(R.drawable.play);
                        rotateAnimation.cancel();
                        handler.removeMessages(0);
                    }else{
                        handler.sendEmptyMessageDelayed(0, 1000); // Gửi tin nhắn sau 1 giây
                    }
                }
            };

            handler.sendEmptyMessage(0);
            txtTotalTime.setText(Helper.convertMillisecondsToMinutes(mp.getDuration()));

            handleEvents();
        }

    }

    private void updateTimerText() {
        long seconds = elapsedTimeMillis / 1000;

        String timeFormatted = String.format("%02d:%02d", seconds / 60, seconds % 60);
        txtDuration.setText(timeFormatted);
    }

    private void handleEvents() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                mp.seekTo(mp.getDuration() * seekBar.getProgress() / 100);
//                Toast.makeText(getApplicationContext(), String.valueOf(seekBar.getProgress()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(mp.getDuration() * seekBar.getProgress() / 100);
//                Toast.makeText(getApplicationContext(), String.valueOf(seekBar.getProgress()), Toast.LENGTH_SHORT).show();
                seekBar.setProgress(seekBar.getProgress());
                elapsedTimeMillis = mp.getDuration() * seekBar.getProgress() / 100;
                txtDuration.setText(Helper.convertMillisecondsToMinutes(elapsedTimeMillis));
                timeSeekBar = seekBar.getProgress();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = findIndexByMusicPath();
                if(index != -1){
                    if(index - 1 >= 0 && index - 1 < listPaths.size()){
                        musicName = listPaths.get(index + 1).getName();
                        txtMusicName.setText(musicName);
                        btnStart.setImageResource(R.drawable.pause);
                        elapsedTimeMillis = 0;
                        timeSeekBar = 0f;
                        seekBar.setProgress((int) timeSeekBar);
                        mp.stop();
                        mp = new MediaPlayer();
                        try {
                            mp.setDataSource(listPaths.get(index - 1).getPath());
                            mp.prepare();
                            mp.start();
//                            seekBar
                        } catch (Exception e) {
                            Log.e("AAA", "prepare() failed");
                        }
                        uri = listPaths.get(index - 1).getPath();
                        txtTotalTime.setText(Helper.convertMillisecondsToMinutes(mp.getDuration()));
                        handler.sendEmptyMessage(0);
                        imageMusic.startAnimation(rotateAnimation);
                    }else{
                        Toast.makeText(getApplicationContext(), "There are no more songs!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = findIndexByMusicPath();
                if(index != -1){
                    if(index + 1 >= 0 && index + 1 < listPaths.size()){
                        musicName = listPaths.get(index + 1).getName();
                        txtMusicName.setText(musicName);
                        btnStart.setImageResource(R.drawable.pause);
                        elapsedTimeMillis = 0;
                        timeSeekBar = 0f;
                        seekBar.setProgress((int) timeSeekBar);
                        mp.stop();
                        mp = new MediaPlayer();
                        try {
                            mp.setDataSource(listPaths.get(index + 1).getPath());
                            mp.prepare();
                            mp.start();
                        } catch (Exception e) {
                            Log.e("AAA", "prepare() failed");
                        }
                        uri = listPaths.get(index + 1).getPath();
                        txtTotalTime.setText(Helper.convertMillisecondsToMinutes(mp.getDuration()));
                        handler.sendEmptyMessage(0);
                        imageMusic.startAnimation(rotateAnimation);
                    }else{
                        Toast.makeText(getApplicationContext(), "There are no more songs!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying()){
//                    if(elapsedTimeMillis >= mp.getDuration() - 1000){
//                        elapsedTimeMillis = 0;
//                        timeSeekBar = 0f;
//                        seekBar.setProgress(0);
//                    }
                    mp.pause();
                    currentRotation = currentRotation + (currentRotation % 360);
                    rotateAnimation.cancel();
                    btnStart.setImageResource(R.drawable.play);
                    handler.removeMessages(0);
                }else{
                    if(elapsedTimeMillis >= mp.getDuration() - 1000){
                        elapsedTimeMillis = 0;
                        timeSeekBar = 0f;
                        seekBar.setProgress(0);
                        txtDuration.setText("00:00");
                        handler.sendEmptyMessage(0);
                    }else{
                        handler.sendEmptyMessage(0);
                    }

                    mp.start();
                    imageMusic.startAnimation(rotateAnimation);
                    btnStart.setImageResource(R.drawable.pause);
                }
            }
        });
    }

    private int findIndexByMusicPath(){
        for(int i = 0 ; i < listPaths.size() ; i++){
            if(listPaths.get(i).getPath().equalsIgnoreCase(uri)){
                return i;
            }
        }
        return -1;
    }

    private void MappingViewById() {
        seekBar = findViewById(R.id.seekBar);
        txtDuration = findViewById(R.id.txtDuration);
        txtTotalTime = findViewById(R.id.txtTotalTime);
        imageMusic = findViewById(R.id.imageView);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnStart = findViewById(R.id.btnStart);
        txtMusicName = findViewById(R.id.txtMusicName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
        handler.removeMessages(0);
        rotateAnimation.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        rotateAnimation.cancel();
    }
}
