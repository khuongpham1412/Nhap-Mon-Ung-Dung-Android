package com.example.bai3.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.bai3.models.FolderInDevice;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Helper {
    public static ArrayList<File> listFoldersAndFilesFromSDCard(String path) {
        ArrayList<File> arrayListFolders = new ArrayList<>();
        try {
            File[] files = new File(path).listFiles(File::isDirectory);
            if (files != null && files.length > 0) {
                Collections.addAll(arrayListFolders, files);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return arrayListFolders;
    }

    public static String removeLastSegment(String input) {
        if (input != null && input.length() > 0) {
            int lastSlashIndex = input.lastIndexOf('/');

            if (lastSlashIndex != -1) {
                return input.substring(0, lastSlashIndex);
            }
        }

        return input;
    }

    public static String convertMillisecondsToMinutes(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        if(minutes <= 9){
            if(seconds <= 9){
                return String.format("0%d:0%d", minutes, seconds);
            }
            return String.format("0%d:%d", minutes, seconds);
        }else{
            if(seconds <= 9){
                return String.format("%d:0%d", minutes, seconds);
            }
            return String.format("%d:%d", minutes, seconds);
        }

    }
}
