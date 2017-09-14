package com.example.scheduler.BackgroundTasks;

/**
 * Created by warrens on 08.08.17.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.schedulecreationlibrary.Action;
import com.example.schedulecreationlibrary.Schedule;
import com.example.scheduler.MainActivity;
import java.io.FileOutputStream;

/**
 * Created by warrens on 11.05.17.
 */

public class StoreDataTask extends AsyncTask<Schedule, Integer, String> {

    public Context context;
    public int progressChangedValue;
    private MainActivity activity;
    public String[] wattage;

    public StoreDataTask(Context c, int a, MainActivity main,String[] w){
        context = c;
        progressChangedValue = a;
        activity = main;
        wattage = w;
    }

    private String[] actionNames = {
            "Hob",
            "Oven",
            "TumbleDryer",
            "WashingMachine",
            "Computer",
            "Kettle",
            "DishWasher",
            "Shower"
    };
    private String[][][] parseableData;
    protected void onPreExecute(){

    }
    //create a file for each device and schedule, indicating what times the device is on or off at every minute during the day the plan is for.
    protected String doInBackground(Schedule... list){
        long startTime = System.currentTimeMillis();
        Schedule lists = list[0];
        String display = "";
        parseableData = new String[1][1][1];
        parseableData[0][0][0] = "0";
        Action[][] fullList;
        if(progressChangedValue>1){
            fullList = lists.getTopNRankedSchedules(progressChangedValue);
        }else{
            fullList = lists.getTopNRankedSchedules(1);
        }
        parseableData = new String[fullList.length][actionNames.length][1440];
        for(int i = 0; i<parseableData.length;i++){
            if(checkCancelled()){
                break;
            }
            for(int j = 0; j<parseableData[i].length;j++){
                if(checkCancelled()){
                    break;
                }
                for(int q = 0; q<parseableData[i][j].length;q++){
                    if(checkCancelled()){
                        break;
                    }
                    parseableData[i][j][q] = "0";
                }
            }
            for(int j = 0; j<fullList[i].length;j++){
                if(checkCancelled()){
                    break;
                }
                int index = 0;
                switch(fullList[i][j].name){
                    case "Hob":
                        index = 0;
                        break;
                    case "Oven":
                        index = 1;
                        break;
                    case "TumbleDryer":
                        index = 2;
                        break;
                    case "WashingMachine":
                        index = 3;
                        break;
                    case "Computer":
                        index = 4;
                        break;
                    case "Kettle":
                        index = 5;
                        break;
                    case "DishWasher":
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
                    if(checkCancelled()){
                        break;
                    }
                    parseableData[i][index][q] = "1";
                }
            }
        }
        for(int i = 0; i<fullList.length;i++){
            if(checkCancelled()){
                break;
            }
            for(int j = 0; j<fullList[i].length;j++){
                if(checkCancelled()){
                    break;
                }
                if(j<fullList[i].length-1){
                    display+= fullList[i][j].name+"\t"+fullList[i][j].getTimeString(fullList[i][j].windowStart)+"-"+fullList[i][j].getTimeString(fullList[i][j].windowEnd)+",";
                }
                else {
                    display+= fullList[i][j].name+"\t"+fullList[i][j].getTimeString(fullList[i][j].windowStart)+"-"+fullList[i][j].getTimeString(fullList[i][j].windowEnd);
                }
            }
            display+="\n";
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;

        String TimingsFile = "timings.txt";
        try{
            FileOutputStream fos = activity.openFileOutput(TimingsFile,context.MODE_APPEND);
            String submit = elapsedTime+",";
            fos.write(submit.getBytes());
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return display;
    }
    protected void onProgressUpdate(Integer... progress) {

    }
    protected void onPostExecute(String result) {
        if(!checkCancelled()){
            if(parseableData.length>0){
                activity.setDisplay(result);
                activity.choicesPopUp();
                String toastString = "Tomorrow's Schedule Set";
                int durationOfToast = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, toastString, durationOfToast);
                toast.show();
                String[] pass = new String[]{"1"};
                new createFilesTask(parseableData, context,activity, wattage).execute(pass);
            }else{
                String toastString = "No Schedules exist for this input";
                int durationOfToast = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, toastString, durationOfToast);
                toast.show();
            }
        }
    }

    public boolean checkCancelled(){
        return activity.checkTasksStop();
    }
}