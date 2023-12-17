package com.example.bai3.models;

public class FolderInDevice {
    private String name;
    private String filePath;
    public FolderInDevice() {
    }
    public FolderInDevice(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
