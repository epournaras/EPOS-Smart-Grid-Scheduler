package com.example.application.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import com.example.application.R;

public class Fragment1 extends Fragment {
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
        getList(layoutView);
        setTimes(layoutView);
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

    public void setTimes(View layoutView){
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
                }catch(Exception e) {
                    e.printStackTrace();
                }
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
            }
        }, 100);
    }
    public void setMinuteChoices(View layoutView, ArrayAdapter<String> s,Spinner sT, ArrayAdapter<String> d, Spinner sD){
        final Handler handler = new Handler();
        final ArrayAdapter<String> use = s;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String fullTime= simpleDateFormat.format(new Date());
                String date = fullTime.substring(0,10);
                String title = date+"'s Schedule";
                getActivity().setTitle(title);
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
                }catch(NullPointerException e){
                    e.printStackTrace();
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
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
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
                }
                handler.postDelayed(this, 100);
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
