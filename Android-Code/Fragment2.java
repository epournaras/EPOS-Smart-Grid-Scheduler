package com.example.application.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import com.example.application.R;

public class Fragment2 extends Fragment {

    public Fragment2() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment2, container, false);
        try{
            Bundle args = getArguments();
            String display = args.getString("display");
            if(display.equals("Nothing to show yet!")){
                try {
                    FileInputStream fis = getActivity().openFileInput("TomorrowSchedule.txt");
                    StringBuilder builder = new StringBuilder();
                    int ch;
                    while((ch = fis.read()) != -1){
                        builder.append((char)ch);
                    }
                    display = builder.toString();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:mm:ss");
                String format = simpleDateFormat.format(new Date());
                String toStore = "\n"+format+"\n"+display;
                String fileName = "PastSchedules.txt";
                String tomorrowsSchedule = "TomorrowSchedule.txt";
                FileOutputStream fos;
                try {
                    fos = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                    fos.write(toStore.getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    fos = new FileOutputStream(tomorrowsSchedule, false);
                    fos.write(display.getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            TextView textView = (TextView)v.findViewById(R.id.text);
            textView.setText(display);
        }catch(NullPointerException ex){
            ex.printStackTrace();
        }
        return v;  // this replaces 'setContentView'
    }
}
