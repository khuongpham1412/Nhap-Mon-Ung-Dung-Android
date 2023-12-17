package com.example.bt2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bt2.DAO.NoteDAO;
import com.example.bt2.DTO.Note;
import com.example.bt2.R;
import com.example.bt2.Utilities.ConvertImage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class NoteDetailActivity extends AppCompatActivity {
    private Toolbar toolBarNoteDetail;
    private ImageView imgNote;
    private TextView titleNote;
    private TextView descNote;
    private TextView dateTimeNote;
    private NoteDAO dao;
    private int ID;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        toolBarNoteDetail = (Toolbar) findViewById(R.id.tool_bar_note_detail);
        imgNote = (ImageView) findViewById(R.id.img_note);
        titleNote = (TextView) findViewById(R.id.title_note);
        descNote = (TextView) findViewById(R.id.desc_note);
        dateTimeNote = (TextView) findViewById(R.id.date_time_note);
        dao = new NoteDAO(this);

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID_NOTE", -1);
        note = dao.getDataById(ID);

        setSupportActionBar(toolBarNoteDetail);
        toolBarNoteDetail.setNavigationOnClickListener(v -> finishAndRemoveTask());

//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        if (note.getImage() != null) {
            imgNote.setImageBitmap(ConvertImage.getImage(note.getImage()));
        }
        titleNote.setText(note.getTitle());
        descNote.setText(note.getDesc());
        dateTimeNote.setText(note.getDate() + " - " + note.getTime());
    }
}