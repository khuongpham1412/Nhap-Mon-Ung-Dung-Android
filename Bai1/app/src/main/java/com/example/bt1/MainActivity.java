package com.example.bt1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bt1.Fragments.LinearFragment;
import com.example.bt1.Fragments.RelativeFragment;
import com.example.bt1.Fragments.TableFragment;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Fragment fragment;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.tool_bar_home);
        setSupportActionBar(toolbar);
        toolbar.setTitle("MODERN ART UI");
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Nhóm 35");
        builder.setMessage("Duy,Đại,Khương");
        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());

        alertDialog = builder.create();
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.linearRadioButton:
                    fragment = new LinearFragment();
                    title = "MODERN ART UI - LinearLayout";
                    break;
                case R.id.relativeRadioButton:
                    fragment = new RelativeFragment();
                    title = "MODERN ART UI - RelativeLayout";
                    break;
                case R.id.tableRadioButton:
                    fragment = new TableFragment();
                    title = "MODERN ART UI - Tableayout";
                    break;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerLayout, fragment)
                    .commit();
            toolbar.setTitle(title);
        });
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
            case R.id.more_info:

                alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}