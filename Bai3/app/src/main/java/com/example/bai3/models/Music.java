package com.example.bai3.models;

import java.util.ArrayList;

public class Music {
    private String image;
    private String pathUrl;
    private String name;
    private int duration;
    private String albumName;
    private ArrayList<String> listPathMusic;
    private int index = -1;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<String> getListPathMusic() {
        return listPathMusic;
    }

    public void setListPathMusic(ArrayList<String> listPathMusic) {
        this.listPathMusic = listPathMusic;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}


