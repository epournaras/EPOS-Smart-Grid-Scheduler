package com.example.application.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.TimingLogger;
import android.widget.Toast;
import com.example.application.MainActivity;
import com.example.schedulelibrary.Schedule;

import java.io.FileOutputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by warrens on 11.05.17.
 */
public class createSchedules extends AsyncTask<Schedule, Integer, Schedule> {
    public Context context;
    private MainActivity main;
    public int progressChangedValue;
    public createSchedules(Context c, int progress, MainActivity m){
        context = c;
        main = m;
        progressChangedValue = progress;
    }
    protected void onPreExecute(){
        String toastString = "Please wait...";
        int durationOfToast = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastString, durationOfToast);
        toast.show();
    }

    protected Schedule doInBackground(Schedule... lists){
        Schedule list = lists[0];
        long startTime = System.currentTimeMillis();
        list.makeScheduleList();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        String TimingsFile = "timings.txt";
        try{
            FileOutputStream fos = main.openFileOutput(TimingsFile,context.MODE_APPEND);
            String submit = elapsedTime+",";
            fos.write(submit.getBytes());
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        startTime = System.currentTimeMillis();
        list.sortSchedulesByRating();
        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        try{
            FileOutputStream fos = main.openFileOutput(TimingsFile,context.MODE_APPEND);
            String submit = elapsedTime+",";
            fos.write(submit.getBytes());
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onCancelled(Schedule result){

    }

    protected void onPostExecute(Schedule result) {
        if(!isCancelled()){
            Schedule[] pass = new Schedule[]{result};
            new StoreDateTask(context,progressChangedValue, main).execute(pass);
        }

    }
}
