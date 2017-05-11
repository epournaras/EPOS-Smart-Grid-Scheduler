package com.example.application.fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.util.Log;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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

                            String toastString = "Added "+selectedActiv+" with window "+tempMin+"-"+tempMax+" with optimal time "+selectedOptimalTime;
                            Context context = getActivity();
                            int durationOfToast = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, toastString, durationOfToast);
                            toast.show();
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
                String toastStringu = "Please wait...";
                Context contextu = getActivity();
                int durationOfToastu = Toast.LENGTH_SHORT;
                Toast toastu = Toast.makeText(contextu, toastStringu, durationOfToastu);
                toastu.show();
                getList(layoutView);
                String display = "";
                String[][][] parseableData = new String[1][1][1];
                parseableData[0][0][0] = "0";
                try {
                    String battery;
                    Context context = getActivity();
                    list.removeAll(Collections.singleton(null));
                    list.trimToSize();
                    Action[] array = new Action[list.size()];
                    list.toArray(array);
                    if(array.length>0){
                        lists = new Schedule(array);
                        //Log.d("LISTS",""+list.size()+"\n");
                        try{
                            battery = ((MainActivity)getActivity()).getBatteryLevels();
                            FileOutputStream fos= context.openFileOutput("batteryLevelFile.txt", context.MODE_APPEND);
                            fos.write(battery.getBytes());
                            ((MainActivity)getActivity()).setBatteryLevels("");
                            fos.write("Start Make Schedules".getBytes());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        lists.makeScheduleList();
                        try{
                            battery = ((MainActivity)getActivity()).getBatteryLevels();
                            FileOutputStream fos= context.openFileOutput("batteryLevelFile.txt", context.MODE_APPEND);
                            fos.write(battery.getBytes());
                            ((MainActivity)getActivity()).setBatteryLevels("");
                            fos.write("End Make Schedules".getBytes());
                            fos.write("Start Rate Schedules".getBytes());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        lists.sortSchedulesByRating();
                        try{
                            battery = ((MainActivity)getActivity()).getBatteryLevels();
                            FileOutputStream fos= context.openFileOutput("batteryLevelFile.txt", context.MODE_APPEND);
                            fos.write(battery.getBytes());
                            ((MainActivity)getActivity()).setBatteryLevels("");
                            fos.write("End Rate Schedules".getBytes());
                            fos.write("Start Rank Schedules".getBytes());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        timings = lists.getTimings();

                        try{
                            FileOutputStream fosTimings = context.openFileOutput("Timings.txt", context.MODE_APPEND);
                            fosTimings.write(timings.getBytes());
                            FileInputStream fisTimings = context.openFileInput("Timings.txt");
                            int ch;
                            StringBuilder builder = new StringBuilder();
                            while ((ch = fisTimings.read()) != -1) {
                                builder.append((char) ch);
                            }
                            String ttt = builder.toString();
                            Log.d("Timings", ttt);
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        Action[][] fullList;
                        if(progressChangedValue>5){
                            fullList = lists.getTopNRankedSchedules(progressChangedValue);
                        }else{
                            fullList = lists.getTopNRankedSchedules(5);
                        }

                        parseableData = new String[fullList.length][actionNames.length][1440];
                        for(int i = 0; i<parseableData.length;i++){
                            for(int j = 0; j<parseableData[i].length;j++){
                                for(int q = 0; q<parseableData[i][j].length;q++){
                                    parseableData[i][j][q] = "0";
                                }
                            }
                            for(int j = 0; j<fullList[i].length;j++){
                                int index = 0;
                                switch(fullList[i][j].name){
                                    case "cooking (Hob)":
                                        index = 0;
                                        break;
                                    case "cooking(Oven)":
                                        index = 1;
                                        break;
                                    case "Dry clothes (Tumble Dryer)":
                                        index = 2;
                                        break;
                                    case "Wash clothes (Washing Machine)":
                                        index = 3;
                                        break;
                                    case "Use Computer":
                                        index = 4;
                                        break;
                                    case "Boil Water (Kettle)":
                                        index = 5;
                                        break;
                                    case "Wash Dishes (dishwasher)":
                                        index = 6;
                                        break;
                                    case "Shower":
                                        index = 7;
                                        break;
                                    default:
                                        index = 7;
                                        break;
                                }
                                for(int q = fullList[i][j].windowStart;q<fullList[i][j].windowEnd+1;q++){
                                    parseableData[i][index][q] = "1";
                                }
                            }
                        }
                        for(int i = 0; i<fullList.length;i++){
                            int index = i+1;
                            display+="\n"+"Schedule "+index+"\n";
                            for(int j = 0; j<fullList[i].length;j++){
                                display+= fullList[i][j].name+"\t"+fullList[i][j].getTimeString(fullList[i][j].windowStart)+"-"+fullList[i][j].getTimeString(fullList[i][j].windowEnd)+"\n";
                            }
                        }
                        String toastString = "Tomorrow's Schedule Set";
                        int durationOfToast = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, toastString, durationOfToast);
                        toast.show();
                    }else{
                        String toastString = "No input";
                        int durationOfToast = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, toastString, durationOfToast);
                        toast.show();
                    }
                    storeData(parseableData);
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
    /* store data in the following way:
     *"Plan/Time,00:00,00:01:,00:02,...,23:59
     * Plan 1,[0 or 1],[0 or 1],[0 or 1],...,[0 or 1]
     * Plan 2,[0 or 1],[0 or 1],[0 or 1],...,[0 or 1]
     * Plan 3,[0 or 1],[0 or 1],[0 or 1],...,[0 or 1]
     *                      .
     *                      .
     *                      .
     * Plan X,[0 or 1],[0 or 1],[0 or 1],...,[0 or 1]"
     *
     * A file of this type should be made for every appliance
     * The title should be USER_ID-APPLIANCE_NAME-DATE-SETUP.txt
     * where SETUP is a count of how many other schedules were created that day prior to this one.
     *
     *
     */
    //a[Schedule][Device][Whether on or off at this index converted to a time+1]
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void storeData(String[][][] a) {
        if(a.length>1){
            if(a[0].length>1){
                Action helper = new Action();
                String[] device = new String[a[0].length];

                String title = "Plan/Time,";
                for(int i = 0; i<1440;i++){
                    if(i!=1439){
                        title += helper.getTimeString(i)+",";
                    }else{
                        title += helper.getTimeString(i)+"\n";
                    }
                }
                for(int i = 0; i<device.length;i++){
                    device[i] = title;
                }
                for(int i = 0; i<a.length;i++){
                    for(int j = 0; j<a[i].length;j++){
                        int planNumber = i+1;
                        device[j]+="Plan "+planNumber+",";
                        for(int q = 0; q<a[i][j].length;q++){
                            if(q<=a[i][j].length-1){
                                device[j]+=a[i][j][q]+",";
                            }else{
                                device[j]+=a[i][j][q]+"\n";
                            }
                        }
                    }
                }


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh_mm_ss-a");
                String date = simpleDateFormat.format(new Date());



                try {
                    FileInputStream fisPSched = getActivity().openFileInput("PastSchedules.txt");
                    int ch;
                    StringBuilder builder = new StringBuilder();
                    while ((ch = fisPSched.read()) != -1) {
                        char s = (char) ch;
                        String st = ""+s;
                        if(!st.equals(null)){
                            builder.append((char) ch);
                        }
                    }
                    String setup = builder.toString();
                    int setupInt = Integer.parseInt(setup);
                    setupInt++;
                    String android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String fileFirstHalfTitle = android_id+"-";
                    String fileSecondHalfTitle = "-"+date+"-"+setupInt+".txt" ;
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    FileOutputStream fOut=null;
                    for(int i = 0; i<device.length;i++){
                        String submitString = device[i];
                        String fileName = fileFirstHalfTitle+actionFileNames[i]+"-"+fileSecondHalfTitle;
                        File file1 = new File(root+ File.separator + fileName);
                        if(!file1.exists()) {
                            try {
                                file1.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            fOut = new FileOutputStream(file1);
                            fOut.write(submitString.getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    fOut = getActivity().openFileOutput("PastSchedules.txt", Context.MODE_PRIVATE);
                    setup = ""+setupInt;
                    fOut.write(setup.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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

