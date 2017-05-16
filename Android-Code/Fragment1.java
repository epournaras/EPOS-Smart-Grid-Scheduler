package com.example.application.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.application.MainActivity;
import com.example.schedulelibrary.Action;
import com.example.schedulelibrary.Schedule;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import com.example.application.R;

public class Fragment1 extends Fragment {
    public String selectedActiv;
    public String selectedDuration;
    public String selectedOptimalTime;
    private String[] actionNames = {
            "cooking (Hob)",
            "cooking(Oven)",
            "Dry clothes (Tumble Dryer)",
            "Wash clothes (Washing Machine)",
            "Use Computer",
            "Boil Water (Kettle)",
            "Wash Dishes (dishwasher)",
            "Shower"};
    private String[] actionFileNames  ={
            "Hob",
            "Oven",
            "Tumble_Dryer",
            "Washing_Machine",
            "Computer",
            "Kettle",
            "Dishwasher",
            "Shower"};
//    private  String[] actionDurations = {"00:40", "00:25", "01:20", "00:15"};
    boolean optimalTimePicked = false;
    private ArrayList<Action> list = new ArrayList<Action>();
    private ArrayList<String> actions = new ArrayList<String>();
    private Schedule lists;
    private Context myContext;
    public int count = 0;
    private String currentText;
    private int progressChangedValue = 0;
    private String timings;
    public Fragment1() {
        // Required empty public constructor
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View layoutView = inflater.inflate(R.layout.fragment1, container, false);
        StringBuilder builder = new StringBuilder();
        String tempMin = "";
        String tempMax = "";
        String[] optimalTimes = new String[1];
        setList();
        ((MainActivity)getActivity()).setDisplay("Nothing to show yet!");
        try{
            FileInputStream fisGetFiles = getActivity().openFileInput("tempMin.txt");
            int chr;
            while ((chr = fisGetFiles.read()) != -1) {
                builder.append((char) chr);
            }
            tempMin = builder.toString();
            fisGetFiles = getActivity().openFileInput("tempMax.txt");
            builder = new StringBuilder();
            int ch;
            while ((ch = fisGetFiles.read()) != -1) {
                builder.append((char) ch);
            }
            tempMax = builder.toString();
        }catch(Exception e) {
            e.printStackTrace();
        }
        Action help = new Action();
        int tempMinStart = help.getIntTime(tempMin);
        int tempMaxStart = help.getIntTime(tempMax);
        int theWindowSize = tempMaxStart-tempMinStart;
        if(tempMinStart<tempMaxStart){
            if(theWindowSize>0){
                optimalTimes = new String[theWindowSize];
                for(int i = tempMinStart;i<tempMaxStart;i++){
                    optimalTimes[i-tempMinStart] = help.getTimeString(i);
                }
            }
        }
        String[] durations = new String[180];
        for(int a = 1; a<181;a++){
            int hour = a/60;
            int minute = a%60;
            String result;
            if(hour<10){
                result = "0"+hour+":";
            }else{
                result = hour+":";
            }
            if(minute<10){
                result+="0"+minute;
            }else{
                result+=minute;
            }
            durations[a-1] = result;
        }
        final String PREFS_NAME = "MyPrefsFile";
        myContext = getActivity();
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(settings.getBoolean("first_time", true)||settings.getBoolean("unanswered",true)) {
            DialogFragment newFragment = new surveyFragment();
            FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
            newFragment.show(fragManager, "surveyFragment");
            settings.edit().putBoolean("first_time", false).commit();
        }
        Button b = (Button) layoutView.findViewById(R.id.minTime);

        getList(layoutView);
        ArrayAdapter<CharSequence> adapterActiv;
        final Spinner activDrp = (Spinner)layoutView.findViewById(R.id.spActiv);
        adapterActiv = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, actionNames);
        adapterActiv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activDrp.setAdapter(adapterActiv);
        selectedActiv = activDrp.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwo;
        final Spinner activeDrpTwo = (Spinner)layoutView.findViewById(R.id.spOptimalTimes);
        adapterActiveTwo = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, optimalTimes);
        adapterActiveTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwo.setAdapter(adapterActiveTwo);
        selectedOptimalTime = activeDrpTwo.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActive;
        final Spinner activeDrp = (Spinner)layoutView.findViewById(R.id.spDuration);
        adapterActive = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, durations);
        adapterActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrp.setAdapter(adapterActive);
        selectedDuration = activeDrp.getSelectedItem().toString();

        activeDrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedDuration = activeDrp.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        activDrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedActiv = activDrp.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        layoutView.findViewById(R.id.spOptimalTimes).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                optimalTimePicked = true;
                return false;
            }
        });

        activeDrpTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedOptimalTime = activeDrpTwo.getSelectedItem().toString();
            }

            public boolean onTouch(View v, MotionEvent event){
                optimalTimePicked = true;
                return false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    settings.edit().putBoolean("putMin",true).commit();
                    FileOutputStream fosThree = getActivity().openFileOutput("minOrMax.txt", Context.MODE_PRIVATE);
                    fosThree.write("min".getBytes());
                    DialogFragment newFragment = new timeFragment();
                    FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
                    newFragment.show(fragManager, "timeFragment");
                    optimalTimePicked = false;
                    setLimits(layoutView);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Button bTwo = (Button) layoutView.findViewById(R.id.maxTime);
        bTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    settings.edit().putBoolean("putMax",true).commit();
                    FileOutputStream fosThree = getActivity().openFileOutput("minOrMax.txt", Context.MODE_PRIVATE);
                    fosThree.write("max".getBytes());
                    DialogFragment newFragment = new timeFragment();
                    FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
                    newFragment.show(fragManager, "timeFragment");
                    optimalTimePicked = false;
                    setLimits(layoutView);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        Button bThree = (Button) layoutView.findViewById(R.id.addItemButton);
        bThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settings.getBoolean("putMin", false)&&settings.getBoolean("putMax",false)){
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
                        boolean notFound = true;
                        for (int i = 0; i < actionNames.length && notFound; i++) {
                            if (selectedActiv.equals(actionNames[i])) {
                                notFound = false;
                            }
                        }

                        Action opt = new Action();
                        int min = opt.getIntTime(tempMin);
                        int max = opt.getIntTime(tempMax);
                        int dur = opt.getIntTime(selectedDuration);
                        if(min+dur<=max){
                            list.add(count, new Action(selectedActiv, tempMin, tempMax, selectedDuration, selectedOptimalTime, false));
                            TextView addedActions = (TextView)layoutView.findViewById(R.id.addedActions);

                            currentText = addedActions.getText().toString();
                            currentText+=selectedActiv+"\t"+tempMin+"-"+tempMax+"\t"+selectedOptimalTime+"\n";

                            String text = selectedActiv+"\t"+tempMin+"-"+tempMax+"\t"+selectedOptimalTime;
                            actions.add(count, text);
                            count++;

                            addedActions.setText(currentText);
                            setList();
                        }else{
                            String toastString = "Window not large enough.";
                            if(max<min){
                                toastString = "Bad Input: Window End before Window Start.";
                            }
                            Context context = getActivity();
                            int durationOfToast = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, toastString, durationOfToast);
                            toast.show();
                            setList();
                        }
                        selectedOptimalTime = activeDrpTwo.getSelectedItem().toString();
                    } catch (IOException ex) {
                        //Log.d("EXCEPTION", "We never stood a chance \n");
                        ex.printStackTrace();
                    }
                }else{
                    if(!settings.getBoolean("putMin", false)){
                        String toastString = "Enter both a window end and start.";
                        Context context = getActivity();
                        int durationOfToast = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, toastString, durationOfToast);
                        toast.show();
                    }else{
                        if(!settings.getBoolean("putMax", false)){
                            String toastString = "Enter both a window start and end.";
                            Context context = getActivity();
                            int durationOfToast = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, toastString, durationOfToast);
                            toast.show();
                        }
                    }
                }
            }
        });
        Button bFour = (Button) layoutView.findViewById(R.id.sendButton);
        bFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getList(layoutView);
                String display = "";
                try {
                    Context context = getActivity();
                    list.removeAll(Collections.singleton(null));
                    list.trimToSize();
                    MainActivity passMain = ((MainActivity)getActivity());
                    Action[] array = new Action[list.size()];
                    list.toArray(array);
                    if(array.length>0){
                        passMain.callBackgroundTasks(array,progressChangedValue);
                    }else{
                        String toastString = "No input";
                        int durationOfToast = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, toastString, durationOfToast);
                        toast.show();
                    }
                    ((MainActivity)getActivity()).setDisplay(display);
                } catch (NullPointerException ex) {
                    String toastString = "Error Send.";
                    Context context = getActivity();
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                    ex.printStackTrace();
                }
                getList(layoutView);
            }
        });

        Button bRemove = (Button) layoutView.findViewById(R.id.removeButton);
        bRemove.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                getList(layoutView);
                if(!actions.isEmpty()){
                    actions.trimToSize();
                    String[] currentActions = new String[actions.size()];
                    actions.toArray(currentActions);
                    String ca = "";
                    FileOutputStream fos = null;
                    try{
                        fos = myContext.openFileOutput("currentActions.txt", Context.MODE_PRIVATE);
                        for(int i = 0; i<currentActions.length;i++){
                            ca+=currentActions[i]+",";
                        }
                        fos.write(ca.getBytes());
                        DialogFragment newFragment = new removeFragment();
                        FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
                        newFragment.show(fragManager, "removeFragment");
                    }catch(Exception e){
                        String toastString = "Remove error";
                        Context context = getActivity();
                        int durationOfToast = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, toastString, durationOfToast);
                        toast.show();
                    }
                }else{
                    String toastString = "Nothing to Remove";
                    Context context = getActivity();
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                }
            }
        });
        SeekBar simpleSeekBar;

        simpleSeekBar=(SeekBar)layoutView.findViewById(R.id.flexibilitySeekBar);
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if(progressChangedValue <= 1 ) {
                    progressChangedValue = 1;
                }
                String toastString = progressChangedValue+"";
                Context context = getActivity();
                int durationOfToast = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, toastString, durationOfToast);
                toast.show();
            }
        });
        this.setLimits(layoutView);
        this.setListView(layoutView);
        //ToDo restrict window Max input
        return layoutView;  // this replaces 'setContentView'
    }

    public void getList(View v){
        String a = ((MainActivity)getActivity()).sendActionList();
        if(a!=null){
            String[] testArray = a.split("[\\r\\n]+");
            list = new ArrayList<Action>();
            actions = new ArrayList<String>();
            count  = 0;
            currentText = "";
            String[] testEmpty = testArray[0].split(",");
            if(testEmpty.length>4) {
                for (int i = 0; i < testArray.length; i++) {
                    String[] temp = testArray[i].split(",");
                    list.add(new Action(temp[0], temp[1], temp[2], temp[3], temp[4],false));
                    actions.add(count, temp[0] + "\t" + temp[1] + "-" + temp[2] + "\t" + temp[4]);
                    currentText += temp[0] + "\t" + temp[1] + "-" + temp[2] + "\t" + temp[4] + "\n";
                    count++;
                }

            }
            TextView addedActions = (TextView) v.findViewById(R.id.addedActions);
            addedActions.setText(currentText);
        }
    }

    public void setList(){
        String listCSV = "";
        for(Action a: list){
            listCSV+=a.name+","+a.getTimeString(a.windowStart)+","+a.getTimeString(a.windowEnd)+","+a.getTimeString(a.duration)+","+a.getTimeString(a.optimalTime)+"\n";
        }
        ((MainActivity)getActivity()).setList(listCSV);
    }

    public void setLimits(View layoutView){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String[] optimalTimes = new String[1];
                String tempMini = "";
                String tempMaxi = "";
                try{
                    FileInputStream fisGetFiles = getActivity().openFileInput("tempMin.txt");
                    int chr;
                    StringBuilder builder = new StringBuilder();
                    while ((chr = fisGetFiles.read()) != -1) {
                        builder.append((char) chr);
                    }
                    tempMini = builder.toString();
                    fisGetFiles = getActivity().openFileInput("tempMax.txt");
                    builder = new StringBuilder();
                    int ch;
                    while ((ch = fisGetFiles.read()) != -1) {
                        builder.append((char) ch);
                    }
                    tempMaxi = builder.toString();
                }catch(Exception e) {
                    e.printStackTrace();
                }
                Action help = new Action();
                int tempMinStart = help.getIntTime(tempMini);
                int tempMaxStart = help.getIntTime(tempMaxi);
                int theWindowSize = tempMaxStart-tempMinStart;
                if(tempMinStart<tempMaxStart){
                    if(theWindowSize>0){
                        optimalTimes = new String[theWindowSize];
                        for(int i = tempMinStart;i<tempMaxStart;i++){
                            optimalTimes[i-tempMinStart] = help.getTimeString(i);
                        }
                    }
                }else{
                    optimalTimes = new String[1440];
                    for(int i = 0;i<1440;i++){
                        optimalTimes[i] = help.getTimeString(i);
                    }
                }
                final Spinner activeDrpTwo = (Spinner)layoutView.findViewById(R.id.spOptimalTimes);
                ArrayAdapter<CharSequence> adapterActiveTwo = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, optimalTimes);
                adapterActiveTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                activeDrpTwo.setAdapter(adapterActiveTwo);
                selectedOptimalTime = activeDrpTwo.getSelectedItem().toString();
                if(!optimalTimePicked){
                    handler.postDelayed(this, 100);
                }
            }
        }, 100);
    }
    public void setListView(View layoutView){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getList(layoutView);
                handler.postDelayed(this, 100);
            }
        }, 100);
    }
}
