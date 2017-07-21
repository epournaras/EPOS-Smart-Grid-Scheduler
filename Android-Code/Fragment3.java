package com.example.application.fragment;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.application.R;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
            fis.close();
            display = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        String[] planParts = display.split("\n");
        char[][] planPartsCharArray = new char[planParts.length][];
        for(int i = 0; i<planParts.length;i++){
            planPartsCharArray[i]=planParts[i].toCharArray();
        }
        char[][] times = new char[planParts.length][11];
        for(int i=0; i<planPartsCharArray.length;i++){
            boolean time = false;
            int index = 0;
            for(int j=0; j<planPartsCharArray[i].length;j++){
                if((planPartsCharArray[i][j] == '0')||(planPartsCharArray[i][j] == '1')||(planPartsCharArray[i][j] == '2')){
                    time = true;
                }
                if(time ==true){
                    if(index<5){
                        times[i][index] = planPartsCharArray[i][j];
                        index++;
                    }
                }
            }
        }
        String[] timeStrings = new String[times.length];
        for(int i = 0; i<timeStrings.length;i++){
            timeStrings[i] = new String(times[i]);
            if(i==0){
                try{
                    FileOutputStream fos = getActivity().openFileOutput("timesToNotify.txt", Context.MODE_PRIVATE);
                    fos.write(timeStrings[i].getBytes());
                    fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                try{
                    FileOutputStream fos = getActivity().openFileOutput("timesToNotify.txt", Context.MODE_APPEND);
                    fos.write((timeStrings[i]+",").getBytes());
                    fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }
        TextView textView = (TextView)v.findViewById(R.id.text);
        textView.setText(display);
        return v;
    }
}
