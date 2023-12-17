package com.example.bai3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bai3.adapter.CustomAdapter;
import com.example.bai3.models.FolderInDevice;
import com.example.bai3.models.ListPath;
import com.example.bai3.utils.Helper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 1;
    public String path = null;
    private Button btnBack;
    private Button btnSelected;
    private Button btnExit;
    private RecyclerView recyclerView;

    private CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MappingViewById();
        handleEvents();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        ArrayList<File> list = Helper.listFoldersAndFilesFromSDCard(path);
        customAdapter = new CustomAdapter(this, list);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            // Nếu đã có quyền, hiển thị danh sách các file MP3
//            displayMp3Files();
        }

        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                File file = customAdapter.getListItem().get(position);

                Log.d("AAA", file.getAbsolutePath());
                if(file.isDirectory()){
                    File[] files = file.listFiles();
                    ArrayList<File> fileList = new ArrayList<>();
                    if ( files != null && files.length != 0 ) {
                        Collections.addAll(fileList, files);
                        customAdapter.setListItem(fileList);
                        recyclerView.setAdapter(customAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        customAdapter.setPath(file.getAbsolutePath());
                    }else{
                        Toast.makeText(MainActivity.this, "Folder is empty!", Toast.LENGTH_SHORT).show();
                    }
                }else if(file.getName().endsWith(".mp3") || file.getName().endsWith(".awb")){
                    customAdapter.setPath(file.getAbsolutePath());
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(file.getAbsolutePath());
                    byte [] data = mmr.getEmbeddedPicture();
                    Bitmap bitmap = null;
                    if(data != null) {
                        bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                    }
                    Type listType = new TypeToken<ArrayList<ListPath>>() {}.getType();
                    Intent intent = new Intent(MainActivity.this, PlayMp3Activity.class);
                    ArrayList<ListPath> listPaths = customAdapter.getListPaths();
                    Bundle bundle = new Bundle();
                    bundle.putString("paths", new Gson().toJson(listPaths, listType));
                    bundle.putString("uri", file.getAbsolutePath());
                    bundle.putString("name", file.getName());
                    if(bitmap != null){
                        Type listType1 = new TypeToken<Bitmap>() {}.getType();
                        bundle.putString("image", new Gson().toJson(bitmap, listType1));
                    }
                    intent.putExtra("bundle", bundle);
//                    sd = songList.get(selectedFile.getName());
//                    if(mp.getMediaPlayer() != null){
//                        mp.getMediaPlayer().reset();
//                    }
//                    mp.setSongDetail(sd);
//                    mp.setPlayer();
//                    //set ảnh và tên cho popup playing
//                    if(sd.getSrc()!=null){
//                        img_popup.setImageBitmap(sd.getSrc());
//                    }
//                    name_popup.setText(sd.getTitle());
//                    artist_popup.setText(sd.getArtist());
//                    mp.getMediaPlayer().start();
                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            displayMp3Files();
        }
    }

    private void handleEvents() {
        handleEventBtnBack();
        handleEventBtnSelected();
        handleEventBtnExit();
    }

    private void handleEventBtnExit() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Warning")
                        .setMessage("Do you want exit?")
                        .setIcon(R.drawable.warning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("Nooooo",null)
                        .show();
            }
        });
    }

    private void handleEventBtnSelected() {
        btnSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ListPath> list = customAdapter.getListPaths();
                if(list != null && list.size() > 0){
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(list.get(0).getPath());
                    byte [] data = mmr.getEmbeddedPicture();
                    Bitmap bitmap = null;
                    if(data != null) {
                        bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                    }
                    Type listType = new TypeToken<ArrayList<ListPath>>() {}.getType();
                    Intent intent = new Intent(MainActivity.this, PlayMp3Activity.class);
                    ArrayList<ListPath> listPaths = customAdapter.getListPaths();
                    Bundle bundle = new Bundle();
                    bundle.putString("paths", new Gson().toJson(listPaths, listType));
                    bundle.putString("uri", list.get(0).getPath());
                    bundle.putString("name", list.get(0).getName());
                    if(bitmap != null){
                        Type listType1 = new TypeToken<Bitmap>() {}.getType();
                        bundle.putString("image", new Gson().toJson(bitmap, listType1));
                    }
                    intent.putExtra("bundle", bundle);
                    MainActivity.this.startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Not exists file mp3!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleEventBtnBack() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path = customAdapter.getPath();
                Log.d("AAA", path);
                if(path != null){
                    path = Helper.removeLastSegment(path);
                    Log.d("AAA", path);
                    customAdapter.setPath(path);

                    ArrayList<File> list = Helper.listFoldersAndFilesFromSDCard(path);
                    customAdapter.setListItem(list);
                    recyclerView.setAdapter(customAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
            }
        });
    }

    private void MappingViewById() {
        btnBack = findViewById(R.id.btnBack);
        btnSelected = findViewById(R.id.btnSelected);
        btnExit = findViewById(R.id.btnExit);
        recyclerView = findViewById(R.id.recycleView);
    }


}