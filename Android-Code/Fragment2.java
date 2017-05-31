package com.example.application.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
                    String toastString = "Couldnt get Tomorrow's schedule.";
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Context context = getActivity();
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                    e.printStackTrace();
                }
            }else{
                String tomorrowsSchedule = "TomorrowSchedule.txt";
                FileOutputStream fos;
                try {
                    fos = getActivity().openFileOutput(tomorrowsSchedule, Context.MODE_PRIVATE);
                    fos.write(display.getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            TextView textView = (TextView)v.findViewById(R.id.text);
            textView.setText(display);
        }catch(NullPointerException ex){
            TextView textView = (TextView)v.findViewById(R.id.text);
            textView.setText("Nothing to show yet!");
            ex.printStackTrace();
        }
        return v;  // this replaces 'setContentView'
    }
}
