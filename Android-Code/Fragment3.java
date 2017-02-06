package com.example.application.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.application.R;

import java.io.FileInputStream;

public class Fragment3 extends Fragment {

    public Fragment3() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment3, container, false);
        Bundle args = getArguments();
        String display = args.getString("displayToday");
        try {
            FileInputStream fis = getActivity().openFileInput("TodaySchedule.txt");
            StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = fis.read()) != -1){
                builder.append((char)ch);
            }
            display = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        TextView textView = (TextView)v.findViewById(R.id.text);
        textView.setText(display);
        return v;
    }
}
