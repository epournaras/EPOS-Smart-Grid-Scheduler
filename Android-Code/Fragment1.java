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
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.MainActivity;
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
    private ArrayList<String> actions = new ArrayList<String>();
    private Schedule lists;
    private Context myContext;
    public int count = 0;
    private String currentText;

    public Fragment1() {
        // Required empty public constructor
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View layoutView = inflater.inflate(R.layout.fragment1, container, false);
        Button b = (Button) layoutView.findViewById(R.id.minTime);
        myContext = getActivity();
        getBoxText(layoutView);
        getList();
        ArrayAdapter<CharSequence> adapterActiv;
        final Spinner activDrp = (Spinner)layoutView.findViewById(R.id.spActiv);
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

        Button bTwo = (Button) layoutView.findViewById(R.id.maxTime);
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

        Button bThree = (Button) layoutView.findViewById(R.id.addItemButton);
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

                    Action opt = new Action();
                    int min = opt.getIntTime(tempMin);
                    int max = opt.getIntTime(tempMax);
                    int optInt = min + (int)(Math.random() * ((max - min) + 1));
                    String optimalTime = opt.getTimeString(optInt);

                    list.add(count, new Action(selectedActiv, tempMin, tempMax, duration, optimalTime, 0));
                    TextView addedActions = (TextView)layoutView.findViewById(R.id.addedActions);

                    currentText = addedActions.getText().toString();
                    currentText+="\n"+count+".\t"+selectedActiv+"\t"+tempMin+"-"+tempMax+"\t"+optimalTime;

                    String text = count+".\t"+selectedActiv+"\t"+tempMin+"-"+tempMax+"\t"+optimalTime;
                    actions.add(count, text);

                    count++;

                    addedActions.setText(currentText);

                    String toastString = "Added "+selectedActiv+" with window "+tempMin+"-"+tempMax+" with optimal time "+optimalTime;
                    Context context = getActivity();
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                } catch (IOException ex) {
                    //Log.d("EXCEPTION", "We never stood a chance \n");
                    ex.printStackTrace();
                }
            }
        });
        Button bFour = (Button) layoutView.findViewById(R.id.sendButton);
        bFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setList();
                    setText();
                    list.trimToSize();
                    Action[] array = new Action[list.size()];
                    list.toArray(array);
                    lists = new Schedule(array);
                    //Log.d("LISTS",""+list.size()+"\n");
                    lists.makeScheduleList();
                    Action[][] fullList = lists.getTopNSchedules(5);
                    String display = "";
                    for(int i = 0; i<fullList.length;i++){
                        display+="Schedule "+i+"\n";
                        for(int j = 0; j<fullList[i].length;j++){
                            display+= fullList[i][j].name+"\t"+fullList[i][j].getTimeString(fullList[i][j].windowStart)+"-"+fullList[i][j].getTimeString(fullList[i][j].windowEnd)+"\n";
                        }
                    }
                    ((MainActivity)getActivity()).setDisplay(display);
                    String toastString = "Done creating schedules";
                    Context context = getActivity();
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        });
        Button bRemove = (Button) layoutView.findViewById(R.id.removeButton);
        bRemove.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(!actions.isEmpty()){
                    actions.trimToSize();
                    String[] currentActions = new String[actions.size()];
                    actions.toArray(currentActions);
                    try{
                        FileOutputStream fos = myContext.openFileOutput("currentActions.txt", Context.MODE_APPEND);
                        for(int i = 0; i<currentActions.length;i++){
                            fos.write(currentActions[i].getBytes());
                            fos.write(",".getBytes());
                        }
                    }catch(IOException e){

                    }
                    DialogFragment newFragment = new removeFragment();
                    FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
                    newFragment.show(fragManager, "removeFragment");
                    try{
                        FileInputStream toRemove = myContext.openFileInput("RemoveAction.txt");
                        StringBuilder builder = new StringBuilder();
                        int ch;
                        String index = "";
                        while((ch = toRemove.read()) != -1){
                            if((char)ch=='.'){
                                index = builder.toString();
                            }else{
                                builder.append((char)ch);
                            }
                        };
                        int ind = Integer.parseInt(index);
                        String removeAction = builder.toString();

                        actions.remove(ind);
                        actions.trimToSize();
                        for(int i = 0; i<actions.size();i++){
                            String change = "";
                            String oldString = actions.get(i);
                            int j = oldString.indexOf('.');
                            char[] oldStringCharArray = oldString.toCharArray();
                            char[] changeCharArray = new char[oldStringCharArray.length];
                            if(j>=0){
                                for(int a = j; a<oldStringCharArray.length;a++){
                                    changeCharArray[a] = oldStringCharArray[a];
                                }
                                change = new String(changeCharArray);
                                change = i+change;
                                actions.add(i, change);
                            }
                        }

                        actions.trimToSize();
                        String[] display = new String[actions.size()];
                        actions.toArray(display);
                        String actualDisplay = "\tAction\tWindow\tOptimal Time";
                        for(int i =0; i<display.length;i++){
                            actualDisplay+="\n"+display[i];
                        }
                        TextView addedActions = (TextView)layoutView.findViewById(R.id.addedActions);
                        addedActions.setText(actualDisplay);

                        list.remove(ind);
                        list.trimToSize();

                    }catch(IOException e){
                        String toastString = "Internal error";
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



        //ToDo have the Flexibility slider change how many Schedules are displayed.
        //ToDo stop the change of screens after send and add a toast to declare the job done.
        //ToDo restrict window Max input
        return layoutView;  // this replaces 'setContentView'
    }

    public void getList(){
        ArrayList<Action> a = ((MainActivity)getActivity()).sendActionList();
        try{
            this.list = a;
            this.count = list.size();
        }catch(NullPointerException e){
            this.list = new ArrayList<Action>();
        }
    }

    public void getBoxText(View v){
        String s = ((MainActivity)getActivity()).sendFragOneText();
        if(s!=null){
            TextView addedActions = (TextView)v.findViewById(R.id.addedActions);
            currentText = s;
            addedActions.setText(s);
        }
    }

    public void setText(){
        ((MainActivity)getActivity()).setFragOneText(currentText);
    }
    public void setList(){
        ((MainActivity)getActivity()).setList(list);
    }
}
