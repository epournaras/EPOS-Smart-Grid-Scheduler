package com.example.application.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.schedulelibrary.Action;
import com.example.schedulelibrary.Schedule;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.example.application.R;


public class Fragment1 extends Fragment {
    public String selectedActiv;
    private String[] actionNames = {"cooking", "vacuuming", "charging car", "showering"};
    private  String[] actionDurations = {"00:40", "00:25", "01:20", "00:15"};
    private ArrayList<Action> list = new ArrayList<Action>();
    private Schedule lists;
    private Context myContext;
    public Fragment1() {
        // Required empty public constructor
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment1, container, false);
        Button b = (Button) v.findViewById(R.id.minTime);
        myContext = getActivity();
        ArrayAdapter<CharSequence> adapterActiv;
        final Spinner activDrp = (Spinner)v.findViewById(R.id.spActiv);
        adapterActiv = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, actionNames);
        adapterActiv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activDrp.setAdapter(adapterActiv);
        selectedActiv = activDrp.getSelectedItem().toString();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fosThree = getActivity().openFileOutput("minOrMax.txt", Context.MODE_PRIVATE);
                    fosThree.write("min".getBytes());
                    DialogFragment newFragment = new timeFragment();
                    FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
                    newFragment.show(fragManager, "timeFragment");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Button bTwo = (Button) v.findViewById(R.id.maxTime);
        bTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fosThree = getActivity().openFileOutput("minOrMax.txt", Context.MODE_PRIVATE);
                    fosThree.write("max".getBytes());
                    DialogFragment newFragment = new timeFragment();
                    FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
                    newFragment.show(fragManager, "timeFragment");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Button bThree = (Button) v.findViewById(R.id.addItemButton);
        bThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Log.d("LENGTH",""+list.size()+"\n")
                    selectedActiv = activDrp.getSelectedItem().toString();

                    //Get the last entered time window.
                    StringBuilder builder = new StringBuilder();
                    FileInputStream fisGetFiles = getActivity().openFileInput("tempMin.txt");
                    int chr;
                    while ((chr = fisGetFiles.read()) != -1) {
                        builder.append((char) chr);
                    }
                    String tempMin = builder.toString();
                    fisGetFiles = getActivity().openFileInput("tempMax.txt");
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
                    String optimalTime = "00:00";
                    list.add(new Action(selectedActiv, tempMin, tempMax, duration, optimalTime, 0));
                    String toastString = "Added "+selectedActiv+" with window "+tempMin+"-"+tempMax+" with optimal time "+optimalTime;
                    Context context = getActivity();
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context,toastString,durationOfToast);
                    toast.show();
                } catch (IOException ex) {
                    //Log.d("EXCEPTION", "We never stood a chance \n");
                    ex.printStackTrace();
                }
            }
        });
        Button bFour = (Button) v.findViewById(R.id.sendButton);
        bFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String displayName = "";
                    String displayMaxTime = "";
                    String displayMinTime = "";
                    FileOutputStream fosInput = getActivity().openFileOutput("nameOfAction.txt", Context.MODE_PRIVATE);
                    list.trimToSize();
                    Action[] array = new Action[list.size()];
                    list.toArray(array);
                    lists = new Schedule(array);
                    //Log.d("LISTS",""+list.size()+"\n");
                    lists.makeScheduleList();
                    Action[][] fullList = lists.getTopNSchedules(5);
                    Log.d("LISTS",""+fullList.length+"\n");
                    int i = 0;
                    if (fullList.length !=0) {
                        for (int j = 0; j < fullList[i].length; j++) {
                            displayName += fullList[i][j].name + ",";
                            displayMinTime += fullList[i][j].getTimeString(fullList[i][j].windowStart) + ",";
                            displayMaxTime += fullList[i][j].getTimeString(fullList[i][j].windowEnd) + ",";

                        }
                        fosInput.write(displayName.getBytes());
                        fosInput = getActivity().openFileOutput("minTime.txt", Context.MODE_PRIVATE);
                        fosInput.write(displayMinTime.getBytes());
                        fosInput = getActivity().openFileOutput("maxTime.txt", Context.MODE_PRIVATE);
                        fosInput.write(displayMaxTime.getBytes());
                    }
                    Intent intent = new Intent(getActivity(), Fragment2.class);
                    startActivity(intent);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //ToDo list added actions in text field.
        //ToDo add remove action.
        //ToDo have the Flexibility slider change how many Schedules are displayed.
        //ToDo stop the change of screens after send and add a toast to declare the job done.
        return v;  // this replaces 'setContentView'
    }
}
