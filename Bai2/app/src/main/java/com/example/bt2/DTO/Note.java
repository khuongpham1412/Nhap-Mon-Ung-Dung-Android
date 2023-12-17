package com.example.bt2.DTO;

public class Note {
    private int id;
    private String title;
    private String desc;
    private byte[] image;
    private String date;
    private String time;

    public static final String CL_NAME = "Note";
    public static final String CL_ID = "ID";
    public static final String CL_TITLE = "Title";
    public static final String CL_DESC = "Description";
    public static final String CL_IMAGE = "Image";
    public static final String CL_DATE = "Date";
    public static final String CL_TIME = "Time";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
