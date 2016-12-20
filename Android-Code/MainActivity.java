package com.example.scheduleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.schedulelibrary.Action;
import com.example.schedulelibrary.Schedule;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public String selectedActiv;
    String[] actionNames = {"cooking", "vacuuming", "charging car", "showering"};
    String[] actionDurations = {"00:40", "00:25", "01:20", "00:15"};
    ArrayList<Action> list = new ArrayList<Action>();
    Schedule lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            ArrayAdapter<CharSequence> adapterActiv;
            Spinner activDrp = (Spinner) findViewById(R.id.spActiv);
            adapterActiv = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, actionNames);
            adapterActiv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            activDrp.setAdapter(adapterActiv);

            selectedActiv = activDrp.getSelectedItem().toString();

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    //to indicate whether the time being selected is a min or max, "min" or "max" is sent to the timeFragment to tell it where to store the input.
    public void showTimePickerDialogMin(View v) {
        try {
            FileOutputStream fosThree = openFileOutput("minOrMax.txt", Context.MODE_PRIVATE);
            fosThree.write("min".getBytes());
            DialogFragment newFragment = new timeFragment();
            newFragment.show(getSupportFragmentManager(), "timeFragment");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //to indicate whether the time being selected is a min or max, "min" or "max" is sent to the timeFragment to tell it where to store the input.
    public void showTimePickerDialogMax(View v) {
        try {
            FileOutputStream fosThree = openFileOutput("minOrMax.txt", Context.MODE_PRIVATE);
            fosThree.write("max".getBytes());
            DialogFragment newFragment = new timeFragment();
            newFragment.show(getSupportFragmentManager(), "timeFragment");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //adds an item to the schedule list.
    public void addItem(View v) {
        try {
            //Log.d("LENGTH",""+list.size()+"\n");
            Spinner activDrp = (Spinner) findViewById(R.id.spActiv);
            selectedActiv = activDrp.getSelectedItem().toString();

            //Get the last entered time window.
            StringBuilder builder = new StringBuilder();
            FileInputStream fisGetFiles = openFileInput("tempMin.txt");
            int chr;
            while ((chr = fisGetFiles.read()) != -1) {
                builder.append((char) chr);
            }
            String tempMin = builder.toString();
            fisGetFiles = openFileInput("tempMax.txt");
            builder = new StringBuilder();
            int ch;
            while ((ch = fisGetFiles.read()) != -1) {
                builder.append((char) ch);
            }
            String tempMax = builder.toString();
            String duration = "";
            boolean notFound = true;
            for (int i = 0; i < actionNames.length && notFound; i++) {
                if (selectedActiv.equals(actionNames[i])) {
                    duration = actionDurations[i];
                    notFound = false;
                }
            }
            list.add(new Action(selectedActiv, tempMin, tempMax, duration, 0));

        } catch (IOException ex) {
            //Log.d("EXCEPTION", "We never stood a chance \n");
            ex.printStackTrace();
        }
    }

    //Switches to the screen to display the items added and their time windows.
    //create schedule arrangements from list, pick one, send it on to the display.
    public void sendMessage(View view) {

        try {
            String displayName = "";
            String displayMaxTime = "";
            String displayMinTime = "";
            FileOutputStream fosInput = openFileOutput("nameOfAction.txt", Context.MODE_PRIVATE);
            list.trimToSize();
            Action[] array = new Action[list.size()];
            list.toArray(array);
            lists = new Schedule(array);
            //Log.d("LISTS",""+list.size()+"\n");
            lists.makeScheduleList();
            Action[][] fullList = lists.getFullList();
            Log.d("LISTS",""+fullList.length+"\n");
            int i = 0;
            if (fullList.length !=0) {
                for (int j = 0; j < fullList[i].length; j++) {
                    displayName += fullList[i][j].name + ",";
                    displayMinTime += fullList[i][j].getTimeString(fullList[i][j].windowStart) + ",";
                    displayMaxTime += fullList[i][j].getTimeString(fullList[i][j].windowEnd) + ",";

                }
                fosInput.write(displayName.getBytes());
                fosInput = openFileOutput("minTime.txt", Context.MODE_PRIVATE);
                fosInput.write(displayMinTime.getBytes());
                fosInput = openFileOutput("maxTime.txt", Context.MODE_PRIVATE);
                fosInput.write(displayMaxTime.getBytes());
            }
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            startActivity(intent);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
