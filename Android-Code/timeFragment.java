package com.example.scheduleapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.text.format.DateFormat;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.*;
import java.util.Calendar;


public class timeFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    Context context = getActivity();

    public int[] time = new int[2];
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        try{
            String time = hourOfDay+":"+minute;
            if(minute == 0 ){
                time+=0;
            }
            if(hourOfDay == 0){
                time = 0+time;
            }
            Context context = getActivity();
            FileInputStream fisThree = context.openFileInput("minOrMax.txt");
            StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = fisThree.read()) != -1){
                builder.append((char)ch);
            };
            String test = builder.toString();

            if(test.equals("min")){
                FileOutputStream tempMin = context.openFileOutput("tempMin.txt", Context.MODE_PRIVATE);
                tempMin.write(time.getBytes());
            }
            else{
                FileOutputStream tempMax = context.openFileOutput("tempMax.txt", Context.MODE_PRIVATE);
                tempMax.write(time.getBytes());
            }
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
}