package com.example.bt2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.bt2.Adapters.NoteAdapter;
import com.example.bt2.DAO.NoteDAO;
import com.example.bt2.DTO.Note;
import com.example.bt2.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcyNote;
    private NoteDAO dao;
    private NoteAdapter adapter;
    private List<Note> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcyNote = (RecyclerView) findViewById(R.id.rcy_note);
        dao = new NoteDAO(this);
        rcyNote.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        list = dao.getAllData();
        adapter = new NoteAdapter(list);
        rcyNote.setAdapter(adapter);

        setSupportActionBar(findViewById(R.id.tool_bar_home));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                startActivity(new Intent(this, NoteActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        list.clear();
        list.addAll(dao.getAllData());
        adapter.notifyDataSetChanged();
    }
}