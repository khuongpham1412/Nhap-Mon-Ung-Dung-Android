package com.example.bt2.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bt2.DTO.Note;
import com.example.bt2.Database.SQLite;

import java.util.ArrayList;
import java.util.List;

public class NoteDAO {
    private SQLiteDatabase database;
    private SQLite sql;

    public NoteDAO(Context context) {
        sql = new SQLite(context);
        database = sql.getWritableDatabase();
    }

    public long insert(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Note.CL_IMAGE, note.getImage());
        contentValues.put(Note.CL_TITLE, note.getTitle());
        contentValues.put(Note.CL_DESC, note.getDesc());
        contentValues.put(Note.CL_DATE, note.getDate());
        contentValues.put(Note.CL_TIME, note.getTime());

        return database.insert(Note.CL_NAME, null, contentValues);
    }

    public List<Note> getAllData() {
        List<Note> list = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Note.CL_NAME, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Note note = new Note();

            note.setId(cursor.getInt(0));
            note.setTitle(cursor.getString(1));
            note.setDesc(cursor.getString(2));
            note.setImage(cursor.getBlob(3));
            note.setDate(cursor.getString(4));
            note.setTime(cursor.getString(5));

            list.add(note);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public Note getLastItem() {
        Note note = new Note();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Note.CL_NAME + " ORDER BY ID DESC LIMIT 1", null);

        if (cursor.moveToFirst()) {
            note.setId(cursor.getInt(0));
            note.setTitle(cursor.getString(1));
            note.setDesc(cursor.getString(2));
        }

        cursor.close();
        return note;
    }

    public Note getDataById(int id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + Note.CL_NAME + " WHERE ID = " + id, null);
        cursor.moveToFirst();

        Note note = new Note();
        note.setId(cursor.getInt(0));
        note.setTitle(cursor.getString(1));
        note.setDesc(cursor.getString(2));
        note.setImage(cursor.getBlob(3));
        note.setDate(cursor.getString(4));
        note.setTime(cursor.getString(5));

        cursor.close();
        return note;
    }

    public int delete(int id) {
        return database.delete(Note.CL_NAME, "ID = " + id, null);
    }

    public int update(Note note) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Note.CL_TITLE, note.getTitle());
        contentValues.put(Note.CL_DESC, note.getDesc());
        contentValues.put(Note.CL_IMAGE, note.getImage());
        contentValues.put(Note.CL_DATE, note.getDate());
        contentValues.put(Note.CL_TIME, note.getTime());

        return database.update(Note.CL_NAME, contentValues, "ID = " + note.getId(), null);
    }
}
