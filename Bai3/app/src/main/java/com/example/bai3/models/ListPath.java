package com.example.bai3.models;

import android.graphics.Bitmap;

import com.example.bai3.R;

import java.util.List;

public class ListPath {
    public ListPath(){

    }

    public ListPath(String name, String path, Bitmap image) {
        this.name = name;
        this.path = path;
        this.image = image;
    }
    public ListPath(String name, String path) {
        this.name = name;
        this.path = path;
    }
    private String name;
    private String path;
    private Bitmap image;
    private int imageDefault = R.drawable.sesu;

    public int getImageDefault() {
        return imageDefault;
    }

    public void setImageDefault(int imageDefault) {
        this.imageDefault = imageDefault;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
