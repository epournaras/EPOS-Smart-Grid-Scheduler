package com.example.scheduler.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedulecreationlibrary.Action;
import com.example.schedulecreationlibrary.Schedule;
import com.example.scheduler.MainActivity;
import com.example.scheduler.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by warrens on 07.08.17.
 */

public class createTomorrowsPlans extends android.support.v4.app.DialogFragment {
    public String selectedActiv;
    public String selectedDuration;
    public String selectedDurHr  ="00",selectedDurMin = "00";
    public String selectedOptItemOne="00",selectedOptItemTwo="00";
    public int startMinMin = 0, endMinMax=59;
    public int startHour=0,endHour=23;
    public boolean zeroZeroAdded = false;
    public ArrayList<String> hours = new ArrayList<String>();
    public ArrayList<String> minutes =new ArrayList<String>();
    public String[] durHours = new String[]{"00","01","02","03"};
    public ArrayList<String> durMins = new ArrayList<String>();
    public boolean outOfBounds = false;
    public String[] actionNames;
    private ArrayList<String> actionNamesAL= new ArrayList<String>();
    boolean optimalTimePicked = false;
    private ArrayList<Action> list = new ArrayList<Action>();
    private ArrayList<String> actions = new ArrayList<String>();
    private Schedule lists;
    private Context myContext;
    public int count = 0;
    private String currentText;
    private int progressChangedValue = 0;
    private String timings;
    public ProgressBar pg;
    @Override
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final Context context=getActivity();
        final View layoutView=inflater.inflate(R.layout.fragment_create_tomorrows_plans,container,false);
        pg = (ProgressBar)layoutView.findViewById(R.id.progressBar);
        getDialog().setCanceledOnTouchOutside(true);
        StringBuilder builder = new StringBuilder();
        String tempMin = "";
        String tempMax = "";
        String[] optimalTimes;
        getList(layoutView);
        setTimes(layoutView);
        String[] applianceNamesArray;
        String applianceNamesFile = "applianceNames.txt";
        String applianceNames = "";
        try{
            FileInputStream fis = getActivity().openFileInput(applianceNamesFile);
            int ch;
            while((ch = fis.read())!=-1){
                builder.append((char) ch);
            }
            fis.close();
            applianceNames = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }

        applianceNamesArray = applianceNames.split(",");

        String appliancesEnabledDataFile = "appliancesEnabledDataFile.txt";
        String appliancesEnabled="";
        try{
            FileInputStream fis = getActivity().openFileInput(appliancesEnabledDataFile);
            builder = new StringBuilder();
            int chr;
            while ((chr = fis.read()) != -1) {
                builder.append((char) chr);
            }
            fis.close();
            appliancesEnabled = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        String[] appliancesEnabledArray = appliancesEnabled.split("\n");
        String[][] enableTable = new String[appliancesEnabledArray.length][2];
        for(int i = 0 ; i<enableTable.length;i++){
            enableTable[i] = appliancesEnabledArray[i].split(",");
            if(enableTable[i][1].equals("true")){
                actionNamesAL.add(enableTable[i][0]);
            }
        }
        actionNames = new String[actionNamesAL.size()];
        actionNamesAL.toArray(actionNames);
        try{
            FileInputStream fisGetFiles = getActivity().openFileInput("tempMin.txt");
            builder = new StringBuilder();
            int chr;
            while ((chr = fisGetFiles.read()) != -1) {
                builder.append((char) chr);
            }
            fisGetFiles.close();
            tempMin = builder.toString();
            fisGetFiles = getActivity().openFileInput("tempMax.txt");
            builder = new StringBuilder();
            int ch;
            while ((ch = fisGetFiles.read()) != -1) {
                builder.append((char) ch);
            }
            fisGetFiles.close();
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
        for(int i = 1; i<60;i++){
            if(i<10){
                durMins.add("0"+i);
            }else{
                durMins.add(""+i);
            }
        }
        final String PREFS_NAME = "MyPrefsFile";
        myContext = getActivity();
        final SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        if(settings.getBoolean("first_time", true)||settings.getBoolean("unanswered",true)) {
//            DialogFragment newFragment = new surveyFragment();
//            FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
//            newFragment.show(fragManager, "surveyFragment");
//            settings.edit().putBoolean("first_time", false).commit();
//        }
        Button b = (Button) layoutView.findViewById(R.id.minTime);

        getList(layoutView);
        ArrayAdapter<String> adapterActiv;
        final Spinner activDrp = (Spinner)layoutView.findViewById(R.id.spActiv);
        adapterActiv = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, actionNamesAL);
        adapterActiv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activDrp.setAdapter(adapterActiv);

        ArrayAdapter<CharSequence> adapterActive;
        final Spinner activeDrp = (Spinner)layoutView.findViewById(R.id.spinnerDurHr);
        adapterActive = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, durHours);
        adapterActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrp.setAdapter(adapterActive);
        selectedDurHr = activeDrp.getSelectedItem().toString();

        ArrayAdapter<String> adapterActiveDurMin;
        final Spinner activeDrpDurMin = (Spinner)layoutView.findViewById(R.id.spinnerDurMin);
        adapterActiveDurMin = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, durMins);
        adapterActiveDurMin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpDurMin.setAdapter(adapterActiveDurMin);
        selectedDurHr = activeDrpDurMin.getSelectedItem().toString();

        activeDrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedDurHr = activeDrp.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activeDrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedDurMin = activeDrpDurMin.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner spinnerOptHr = (Spinner)layoutView.findViewById(R.id.spinnerOptHr);
        ArrayAdapter<String> spinnerOptHrAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,hours);
        spinnerOptHrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptHr.setAdapter(spinnerOptHrAdapter);
        for(int i = 0; i<24;i++){
            if(i<10){
                spinnerOptHrAdapter.add("0"+i);
            }else{
                spinnerOptHrAdapter.add(""+i);
            }
        }
        spinnerOptHrAdapter.notifyDataSetChanged();

        final Spinner spinnerOptMin = (Spinner)layoutView.findViewById(R.id.spinnerOptMin);
        ArrayAdapter<String> spinnerOptMinAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,minutes);
        spinnerOptMinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptMin.setAdapter(spinnerOptMinAdapter);
        for(int i = 0; i<60;i++){
            if(i<10){
                spinnerOptMinAdapter.add("0"+i);
            }else{
                spinnerOptMinAdapter.add(""+i);
            }
        }
        spinnerOptMinAdapter.notifyDataSetChanged();

        spinnerOptHr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedOptItemOne = spinnerOptHr.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerOptMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedOptItemTwo = spinnerOptMin.getSelectedItem().toString();
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

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    settings.edit().putBoolean("putMin",true).commit();
                    FileOutputStream fosThree = getActivity().openFileOutput("minOrMax.txt", Context.MODE_PRIVATE);
                    fosThree.write("min".getBytes());
                    fosThree.close();
                    DialogFragment newFragment = new timeFragment();
                    FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
                    newFragment.show(fragManager, "timeFragment");
                    optimalTimePicked = false;
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
                    fosThree.close();
                    DialogFragment newFragment = new timeFragment();
                    FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
                    newFragment.show(fragManager, "timeFragment");
                    optimalTimePicked = false;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        Button bThree = (Button) layoutView.findViewById(R.id.addItemButton);
        bThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activDrp.getSelectedItem()==null){
                    String toastString = "Add an Appliance.";
                    Context context = getActivity();
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                }else{
                    if(settings.getBoolean("putMin", false)&&settings.getBoolean("putMax",false)){
                        try {
                            //Log.d("LENGTH",""+list.size()+"\n")
                            String selectedOptimalTime = "00:00";
                            try{

                                selectedDurHr = activeDrp.getSelectedItem().toString();
                                selectedDurMin = activeDrpDurMin.getSelectedItem().toString();
                                selectedDuration = selectedDurHr+":"+selectedDurMin;
                            }catch(NullPointerException e){
                                e.printStackTrace();
                            }
                            //Get the last entered time window.
                            StringBuilder builder = new StringBuilder();
                            FileInputStream fisGetFiles = getActivity().openFileInput("tempMin.txt");
                            int chr;
                            while ((chr = fisGetFiles.read()) != -1) {
                                builder.append((char) chr);
                            }
                            fisGetFiles.close();
                            String tempMin = builder.toString();
                            fisGetFiles = getActivity().openFileInput("tempMax.txt");
                            builder = new StringBuilder();
                            int ch;
                            while ((ch = fisGetFiles.read()) != -1) {
                                builder.append((char) ch);
                            }
                            fisGetFiles.close();
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
                                selectedActiv = activDrp.getSelectedItem().toString();
                                selectedOptItemOne = spinnerOptHr.getSelectedItem().toString();
                                selectedOptItemTwo = spinnerOptMin.getSelectedItem().toString();
                                selectedOptimalTime = selectedOptItemOne+":"+selectedOptItemTwo;
                                boolean parallel;
                                switch(selectedActiv){
                                    case "Hob":
                                        parallel = false;
                                        break;
                                    case "Oven":
                                        parallel = true;
                                        break;
                                    case "TumbleDryer":
                                        parallel = true;
                                        break;
                                    case "WashingMachine":
                                        parallel = true;
                                        break;
                                    case "Computer":
                                        parallel = false;
                                        break;
                                    case "Kettle":
                                        parallel = true;
                                        break;
                                    case "DishWasher":
                                        parallel = true;
                                        break;
                                    case "Shower":
                                        parallel = false;
                                        break;
                                    default:
                                        parallel = false;
                                        break;
                                }
                                list.add(count, new Action(selectedActiv, tempMin, tempMax, selectedDuration, selectedOptimalTime, parallel));
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

            }
        });
        Button bFour = (Button) layoutView.findViewById(R.id.sendButton);
        bFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity passMain = ((MainActivity)getActivity());
                passMain.cancelBackgroundTasks();
                settings.edit().putBoolean("defaultBool", true).commit();
                getList(layoutView);
                String display = "";
                try {
                    Context context = getActivity();
                    list.removeAll(Collections.singleton(null));
                    list.trimToSize();

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
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String time = simpleDateFormat.format(new Date());
                    char[] timeChars = time.toCharArray();
                    String date = ""+timeChars[0]+timeChars[1]+timeChars[2]+timeChars[3]+timeChars[4]+timeChars[5]+timeChars[6]+timeChars[7]+timeChars[8]+timeChars[9];
                    try{
                        FileOutputStream fos = getActivity().openFileOutput("lastSendPressDayDate.txt", Context.MODE_PRIVATE);
                        fos.write(date.getBytes());
                        fos.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                } catch (NullPointerException ex) {
                    String toastString = "Error Send.";
                    Context context = getActivity();
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                    ex.printStackTrace();
                }
                getList(layoutView);
                dismiss();
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
                        fos.close();
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
        this.setAppliances(layoutView, adapterActiv);
        this.setHourChoices(layoutView,spinnerOptHrAdapter);
        this.setMinuteChoices(layoutView,spinnerOptMinAdapter,spinnerOptHr,adapterActiveDurMin,activeDrp);
        this.setListView(layoutView);
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
                    boolean parallel;
                    switch(temp[0]){
                        case "Hob":
                            parallel = false;
                            break;
                        case "Oven":
                            parallel = true;
                            break;
                        case "TumbleDryer":
                            parallel = true;
                            break;
                        case "WashingMachine":
                            parallel = true;
                            break;
                        case "Computer":
                            parallel = false;
                            break;
                        case "Kettle":
                            parallel = true;
                            break;
                        case "DishWasher":
                            parallel = true;
                            break;
                        case "Shower":
                            parallel = false;
                            break;
                        default:
                            parallel = false;
                            break;
                    }
                    list.add(new Action(temp[0], temp[1], temp[2], temp[3], temp[4],parallel));
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

    public void setListView(View lV){
        final View layoutView = lV;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    getList(layoutView);
                    handler.postDelayed(this, 100);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    public void setTimes(View lV){
        final View layoutView = lV;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StringBuilder builder = new StringBuilder();
                String tempMin = "00:00";
                String tempMax = "23:59";
                try{
                    FileInputStream fisGetFiles = getActivity().openFileInput("tempMin.txt");
                    int chr;
                    while ((chr = fisGetFiles.read()) != -1) {
                        builder.append((char) chr);
                    }
                    fisGetFiles.close();
                    tempMin = builder.toString();
                    fisGetFiles = getActivity().openFileInput("tempMax.txt");
                    builder = new StringBuilder();
                    int ch;
                    while ((ch = fisGetFiles.read()) != -1) {
                        builder.append((char) ch);
                    }
                    fisGetFiles.close();
                    tempMax = builder.toString();
                    TextView min = (TextView)layoutView.findViewById(R.id.startTimeTxt);
                    TextView max = (TextView)layoutView.findViewById(R.id.endTimeTxt);
                    String startHourS = tempMin.substring(0,2);
                    String startMinMinS = tempMin.substring(3,5);
                    String endHourS = tempMax.substring(0,2);
                    String endMinMaxS =tempMax.substring(3,5);
                    startHour = Integer.parseInt(startHourS);
                    startMinMin = Integer.parseInt(startMinMinS);
                    endHour = Integer.parseInt(endHourS);
                    endMinMax = Integer.parseInt(endMinMaxS);
                    min.setText(tempMin);
                    max.setText(tempMax);
                    handler.postDelayed(this, 100);
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    public void setAppliances(View layoutView, ArrayAdapter<String> s){
        final View lv = layoutView;
        final Handler handler = new Handler();
        final ArrayAdapter<String> applianceList = s;

        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                String appliancesEnabledDataFile = "appliancesEnabledDataFile.txt";
                String appliancesEnabled="";
                try{
                    FileInputStream fis = getActivity().openFileInput(appliancesEnabledDataFile);
                    StringBuilder builder = new StringBuilder();
                    int chr;
                    while ((chr = fis.read()) != -1) {
                        builder.append((char) chr);
                    }
                    fis.close();
                    appliancesEnabled = builder.toString();
                    applianceList.clear();
                    String[] appliancesEnabledArray = appliancesEnabled.split("\n");
                    String[][] enableTable = new String[appliancesEnabledArray.length][2];
                    for(int i = 0 ; i<enableTable.length;i++){
                        enableTable[i] = appliancesEnabledArray[i].split(",");
                        if(enableTable[i][1].equals("true")){
                            applianceList.add(enableTable[i][0]);
                        }
                    }
                    double countOfCurrentlyAddedAppliances = applianceList.getCount();
                    Boolean[] gatekeeper = {false,false,false,false,false,false,false,false};
                    getList(lv);
                    TextView actionsList = (TextView) lv.findViewById(R.id.addedActions);
                    String currentActions = actionsList.getText().toString();
                    String[] array = currentActions.split("\n");
                    String[][] detailsOfActions = new String[array.length][];
                    double appliancesAddedToTomorrowsSchedule = 0;
                    for(int i = 0; i<array.length;i++){
                        detailsOfActions[i] = array[i].split("\t");
                        switch(detailsOfActions[i][0]){
                            case "Hob":
                                if(!gatekeeper[0]){
                                    appliancesAddedToTomorrowsSchedule++;
                                    gatekeeper[0] = true;
                                }
                                break;
                            case "Oven":
                                if(!gatekeeper[1]){
                                    appliancesAddedToTomorrowsSchedule++;
                                    gatekeeper[1] = true;
                                }
                                break;
                            case "TumbleDryer":
                                if(!gatekeeper[2]){
                                    appliancesAddedToTomorrowsSchedule++;
                                    gatekeeper[2] = true;
                                }
                                break;
                            case "WashingMachine":
                                if(!gatekeeper[3]){
                                    appliancesAddedToTomorrowsSchedule++;
                                    gatekeeper[3] = true;
                                }
                                break;
                            case "Computer":
                                if(!gatekeeper[4]){
                                    appliancesAddedToTomorrowsSchedule++;
                                    gatekeeper[4] = true;
                                }
                                break;
                            case "Kettle":
                                if(!gatekeeper[5]){
                                    appliancesAddedToTomorrowsSchedule++;
                                    gatekeeper[5] = true;
                                }
                                break;
                            case "DishWasher":
                                if(!gatekeeper[6]){
                                    appliancesAddedToTomorrowsSchedule++;
                                    gatekeeper[6] = true;
                                }
                                break;
                            case "Shower":
                                if(!gatekeeper[7]){
                                    appliancesAddedToTomorrowsSchedule++;
                                    gatekeeper[7] = true;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    double progressDouble = (appliancesAddedToTomorrowsSchedule)/(countOfCurrentlyAddedAppliances);
                    int progress =  (int) (progressDouble*100);
                    if(progress<=100){
                        pg.setProgress(progress);
                    }
                    handler.postDelayed(this, 100);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, 100);
    }
    public void setMinuteChoices(View layoutView, ArrayAdapter<String> s,Spinner spinnerT, ArrayAdapter<String> da, Spinner spinnerD){
        final Handler handler = new Handler();
        final ArrayAdapter<String> use = s;
        final Spinner sT = spinnerT;
        final ArrayAdapter<String> d = da;
        final Spinner sD = spinnerD;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                selectedDurHr = sD.getSelectedItem().toString();
                if(selectedDurHr.equals("00")){
                    if(zeroZeroAdded){
                        d.remove("00");
                        zeroZeroAdded = false;
                    }
                }else{
                    if(!zeroZeroAdded){
                        d.add("00");
                        zeroZeroAdded = true;
                    }
                }
                try{
                    if(sT.getSelectedItem()!=null){
                        selectedOptItemOne = sT.getSelectedItem().toString();
                        outOfBounds = false;
                    }else{
                        outOfBounds = true;
                    }
                    if(!outOfBounds){
                        StringBuilder builder = new StringBuilder();
                        String tempMin = "00:00";
                        String tempMax = "23:59";
                        try{
                            FileInputStream fisGetFiles = getActivity().openFileInput("tempMin.txt");
                            int chr;
                            while ((chr = fisGetFiles.read()) != -1) {
                                builder.append((char) chr);
                            }
                            fisGetFiles.close();
                            tempMin = builder.toString();
                            fisGetFiles = getActivity().openFileInput("tempMax.txt");
                            builder = new StringBuilder();
                            int ch;
                            while ((ch = fisGetFiles.read()) != -1) {
                                builder.append((char) ch);
                            }
                            fisGetFiles.close();
                            tempMax = builder.toString();
                            String startHourS = tempMin.substring(0,2);
                            String startMinMinS = tempMin.substring(3,5);
                            String endHourS = tempMax.substring(0,2);
                            String endMinMaxS =tempMax.substring(3,5);
                            startHour = Integer.parseInt(startHourS);
                            startMinMin = Integer.parseInt(startMinMinS);
                            endHour = Integer.parseInt(endHourS);
                            endMinMax = Integer.parseInt(endMinMaxS);
                            int test = Integer.parseInt(selectedOptItemOne);
                            for(int i = 0; i<60;i++){
                                if(i<10){
                                    use.remove("0"+i);
                                }else{
                                    use.remove(""+i);
                                }
                            }
                            if((test == endHour)&&(test==startHour)){
                                for(int i = startMinMin; i<endMinMax;i++){
                                    if(i<10){
                                        use.add("0"+i);
                                    }else{
                                        use.add(""+i);
                                    }
                                }
                            }else{
                                if(test == startHour){
                                    for(int i = startMinMin; i<60;i++){
                                        if(i<10){
                                            use.add("0"+i);
                                        }else{
                                            use.add(""+i);
                                        }
                                    }
                                }else{
                                    if(test == endHour){
                                        for(int i = 0; i<endMinMax;i++){
                                            if(i<10){
                                                use.add("0"+i);
                                            }else{
                                                use.add(""+i);
                                            }
                                        }
                                    }else{

                                        for(int i = 0; i<60;i++) {
                                            if (i < 10) {
                                                use.add("0" + i);
                                            } else {
                                                use.add("" + i);
                                            }
                                        }
                                    }
                                }
                            }
                            handler.postDelayed(this, 100);
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch(NullPointerException e){
                    e.printStackTrace();
                }

            }
        }, 100);
    }

    public void setHourChoices(View layoutView, ArrayAdapter<String> s){
        final Handler handler = new Handler();
        final ArrayAdapter<String> use = s;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i<60;i++){
                    if(i<10){
                        use.remove("0"+i);
                    }else{
                        use.remove(""+i);
                    }
                }
                if(startHour<=endHour){

                }
                for(int i = startHour; i<endHour+1;i++){
                    if(i<10){
                        use.add("0"+i);
                    }else{
                        use.add(""+i);
                    }
                }
                handler.postDelayed(this, 100);
            }
        }, 100);
    }
}
