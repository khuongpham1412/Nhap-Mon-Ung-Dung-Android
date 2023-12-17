package com.example.bt2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLite extends SQLiteOpenHelper {
    private static final String DB_NAME = "note.db";
    private static final int DB_VERSION = 1;

    public SQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableNote = "CREATE TABLE Note(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title TEXT NOT NULL," +
                "Description TEXT NOT NULL," +
                "Image BLOB," +
                "Date TEXT NOT NULL," +
                "Time TEXT NOT NULL" +
                ")";

        db.execSQL(createTableNote);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
