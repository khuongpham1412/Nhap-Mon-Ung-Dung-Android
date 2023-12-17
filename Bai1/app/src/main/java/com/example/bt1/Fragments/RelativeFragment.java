package com.example.bt1.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.bt1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RelativeFragment extends Fragment {
    private SeekBar seekBar;
    View rect1;
    View rect2;
    View rect3;
    View rect4;
    View rect5;
    private Random random;
    private List<View> listView = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_relative, container, false);

        seekBar = root.findViewById(R.id.seekBar);
        rect1 = (View) root.findViewById(R.id.rect_1);
        rect2 = (View) root.findViewById(R.id.rect_2);
        rect3 = (View) root.findViewById(R.id.rect_3);
        rect4 = (View) root.findViewById(R.id.rect_4);
        rect5 = (View) root.findViewById(R.id.rect_5);
        random = new Random();
        listView.add(rect1);
        listView.add(rect2);
        listView.add(rect3);
        listView.add(rect4);
        listView.add(rect5);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (int i = 0; i < 5; i++) {
                    int red = random.nextInt(256);
                    int green = random.nextInt(256);
                    int blue = random.nextInt(256);
                    int color = Color.rgb(red, green, blue);

                    listView.get(i).setBackgroundColor(color);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        return root;
    }
}