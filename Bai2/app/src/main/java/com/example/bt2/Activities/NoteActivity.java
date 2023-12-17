package com.example.bt2.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bt2.DAO.NoteDAO;
import com.example.bt2.DTO.Note;
import com.example.bt2.R;
import com.example.bt2.ReminderBroadcastReceiver;
import com.example.bt2.Utilities.ConvertImage;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class NoteActivity extends AppCompatActivity {
    private Toolbar toolBarAddUpdateNote;
    private ImageView imgAddUpdateNote;
    private ImageView imgNote;
    private TextInputEditText edtTitle;
    private TextInputEditText edtDate;
    private TextInputEditText edtTime;
    private EditText edtDesc;
    private AppCompatButton btnAddNote;
    private LinearLayout zoneUpdateDelete;
    private AppCompatButton btnUpdateNote;
    private AppCompatButton btnDeleteNote;
    private DatePickerDialog dateNote;
    private TimePickerDialog timeNote;
    private NoteDAO dao;
    private Calendar calendar;
    private Bitmap bitmap = null;
    // Var PickMedia:
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private Intent intent;
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        toolBarAddUpdateNote = (Toolbar) findViewById(R.id.tool_bar_add_update_note);
        imgAddUpdateNote = (ImageView) findViewById(R.id.img_add_update_note);
        imgNote = (ImageView) findViewById(R.id.img_note);
        edtTitle = (TextInputEditText) findViewById(R.id.edt_title);
        edtDate = (TextInputEditText) findViewById(R.id.edt_date);
        edtTime = (TextInputEditText) findViewById(R.id.edt_time);
        edtDesc = (EditText) findViewById(R.id.edt_desc);
        btnAddNote = (AppCompatButton) findViewById(R.id.btn_add_note);
        zoneUpdateDelete = (LinearLayout) findViewById(R.id.zone_update_delete);
        btnUpdateNote = (AppCompatButton) findViewById(R.id.btn_update_note);
        btnDeleteNote = (AppCompatButton) findViewById(R.id.btn_delete_note);

        intent = getIntent();
        ID = intent.getIntExtra("ID_NOTE", -1);

        setSupportActionBar(toolBarAddUpdateNote);
        toolBarAddUpdateNote.setTitle(ID == -1 ? "Add Note" : "Update Note");
        toolBarAddUpdateNote.setNavigationOnClickListener(v -> finish());
        calendar = Calendar.getInstance();
        dao = new NoteDAO(this);
        // gọi callback và truyền dg dần hình vô uri
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // callback đc khởi tạo khi ng dùng chọn hình hoặc đóng chọn
            if (uri != null) {
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imgNote.setImageURI(uri);
            }
        });

        imgAddUpdateNote.setOnClickListener(v -> setPicture());

        dateNote = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String m = String.valueOf(month + 1).length() == 2 ? String.valueOf(month + 1) : ("0" + (month + 1));
            String d = String.valueOf(dayOfMonth).length() == 2 ? String.valueOf(dayOfMonth) : ("0" + (dayOfMonth));
            edtDate.setText(year + "-" + (m) + "-" + d);
            edtDate.clearFocus();

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

        }
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));

        timeNote = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String h = String.valueOf(hourOfDay).length() == 2 ? String.valueOf(hourOfDay) : ("0" + hourOfDay);
            String m = String.valueOf(minute).length() == 2 ? String.valueOf(minute) : ("0" + (minute));
            edtTime.setText(h + ":" + m);
            edtTime.clearFocus();

            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

        }
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , true);

        if (ID != -1) {
            Note note = dao.getDataById(ID);

            if (note.getImage() != null) {
                imgNote.setImageBitmap(ConvertImage.getImage(note.getImage()));
            }
            edtTitle.setText(note.getTitle());
            edtDesc.setText(note.getDesc());
            edtDate.setText(note.getDate());
            edtTime.setText(note.getTime());

            btnAddNote.setVisibility(View.INVISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Note?");
            builder.setMessage("Bạn có muốn xóa Note có ID = " + ID + " không?");
            builder.setNegativeButton("Không", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                int result = dao.delete(ID);
                if (result > 0) {
                    Intent alarmIntent = new Intent(NoteActivity.this, ReminderBroadcastReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(NoteActivity.this, ID, alarmIntent, PendingIntent.FLAG_NO_CREATE);
                    if (pendingIntent != null) {
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent);
                    }
                    finish();
                    Toast.makeText(NoteActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NoteActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = builder.create();

            btnUpdateNote.setOnClickListener(v -> {
                if (validate()) {
                    Note noteUpdate = new Note();
                    noteUpdate.setId(ID);
                    if (bitmap != null) {
                        noteUpdate.setImage(ConvertImage.getBytes(bitmap));
                    }
                    noteUpdate.setTitle(edtTitle.getText().toString());
                    noteUpdate.setDesc(edtDesc.getText().toString());
                    noteUpdate.setDate(edtDate.getText().toString());
                    noteUpdate.setTime(edtTime.getText().toString());

                    int result = dao.update(noteUpdate);
                    if (result > 0) {
                        Intent alarmIntent = new Intent(NoteActivity.this, ReminderBroadcastReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(NoteActivity.this, ID, alarmIntent, PendingIntent.FLAG_NO_CREATE);
                        if (pendingIntent != null) {
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent);
                        }
                        setAlarm(calendar.getTimeInMillis());
                        finish();
                        Toast.makeText(this, "Cập nhập thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Cập nhập thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

            });
            btnDeleteNote.setOnClickListener(v -> alertDialog.show());
        }

        edtDate.setOnTouchListener((v, event) -> {
            dateNote.show();
            edtDate.clearFocus();
            return false;
        });
        edtTime.setOnTouchListener((v, event) -> {
            timeNote.show();
            edtTime.clearFocus();
            return false;
        });
        dateNote.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                edtDate.clearFocus();
            }
        });
        timeNote.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                edtTime.clearFocus();
            }
        });
        btnAddNote.setOnClickListener(v -> {
            if (validate()) {
                Note note = new Note();
                if (bitmap != null) {
                    note.setImage(ConvertImage.getBytes(bitmap));
                }
                note.setTitle(edtTitle.getText().toString());
                note.setDesc(edtDesc.getText().toString());
                note.setDate(edtDate.getText().toString());
                note.setTime(edtTime.getText().toString());

                long result = dao.insert(note);
                if (result > 0) {
                    setAlarm(calendar.getTimeInMillis());
                    Toast.makeText(this, "Thêm mới thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Thêm mới thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setPicture() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());

    }

    private boolean validate() {
        if (edtTitle.getText().length() == 0) {
            Toast.makeText(this, "Vui lòng nhập tiêu đề!", Toast.LENGTH_SHORT).show();
            edtTitle.requestFocus();
            return false;
        }
        if (edtDate.getText().length() == 0) {
            Toast.makeText(this, "Vui lòng chọn ngày!", Toast.LENGTH_SHORT).show();
            dateNote.show();
            edtDate.requestFocus();
            return false;
        }
        if (edtTime.getText().length() == 0) {
            Toast.makeText(this, "Vui lòng chọn thời gian!", Toast.LENGTH_SHORT).show();
            timeNote.show();
            edtTime.requestFocus();
            return false;
        }
        if (edtDesc.getText().length() == 0) {
            Toast.makeText(this, "Vui lòng chọn thời gian!", Toast.LENGTH_SHORT).show();
            edtDesc.requestFocus();
            return false;
        }

        return true;
    }

    private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        Note note = ID == -1 ? dao.getLastItem() : dao.getDataById(ID);
        bundle.putInt("NOTE_ID", note.getId());
        bundle.putString("NOTE_TITLE", note.getTitle());
        bundle.putString("NOTE_DESC", note.getDesc());
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, note.getId(), intent, 0);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }

    }
}