package com.example.application.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import com.example.application.MainActivity;
import com.example.schedulelibrary.Action;
import com.example.schedulelibrary.Schedule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.security.AccessController.getContext;

/**
 * Created by warrens on 11.05.17.
 */

public class StoreDateTask extends AsyncTask<Schedule, Integer, String> {

    public Context context;
    public int progressChangedValue;
    private MainActivity activity;

    public StoreDateTask(Context c, int a, MainActivity main){
        context = c;
        progressChangedValue = a;
        activity = main;
    }

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
    protected void onPreExecute(){

    }

    protected String doInBackground(Schedule... list){
        Schedule lists = list[0];
        String display = "";
        String[][][] parseableData = new String[1][1][1];
        parseableData[0][0][0] = "0";
        Action[][] fullList;
        if(progressChangedValue>1){
            fullList = lists.getTopNRankedSchedules(progressChangedValue);
        }else{
            fullList = lists.getTopNRankedSchedules(1);
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

        storeData(parseableData);
        return display;
    }
    protected void onProgressUpdate(Integer... progress) {

    }
    protected void onPostExecute(String result) {
        activity.setDisplay(result);
        String toastString = "Tomorrow's Schedule Set";
        int durationOfToast = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastString, durationOfToast);
        toast.show();
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
                    FileInputStream fisPSched = context.openFileInput("PastSchedules.txt");
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
                    String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
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
                    fOut = context.openFileOutput("PastSchedules.txt", Context.MODE_PRIVATE);
                    setup = ""+setupInt;
                    fOut.write(setup.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
