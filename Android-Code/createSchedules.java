package com.example.application.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.application.MainActivity;
import com.example.schedulelibrary.Action;
import com.example.schedulelibrary.Schedule;

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
        list.makeScheduleList();
        list.sortSchedulesByRating();
        return list;
    }
    protected void onProgressUpdate(Integer... progress) {

    }
    protected void onPostExecute(Schedule result) {
        Schedule[] pass = new Schedule[]{result};
        new StoreDateTask(context,progressChangedValue, main).execute(pass);
    }
}
