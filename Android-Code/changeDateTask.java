package com.example.application.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

import com.example.application.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by warrens on 15.05.17.
 */

public class changeDateTask extends AsyncTask<String, Integer, String>{
    private String todayDate;
    private String tomorrowDate;
    private MainActivity mainActivity;

    public changeDateTask(String da, String tda, MainActivity main){
        this.todayDate = da;
        this.tomorrowDate =tda;
        this.mainActivity = main;
    }

    protected void onPreExecute(){

    }

    protected String doInBackground(String... s){
        setDate(mainActivity);
        return todayDate;
    }

    protected void onProgressUpdate(Integer... a){

    }

    protected void onPostExecute(){

    }

    public void setDate(MainActivity mainActivity){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateTest = simpleDateFormat.format(new Date());
        dateTest = dateTest.substring(0,10);
        if(todayDate.equals(dateTest)){

        }else{
            todayDate = dateTest;
            String fullTime = simpleDateFormat.format(new Date(((new Date()).getTime() + 86400000)));
            tomorrowDate = fullTime.substring(0,10);
            mainActivity.setDate(todayDate,tomorrowDate);
        }
    }
