package com.example.activitieslibrary;

/**
 * Created by warrens on 19.10.16.
 */

public class Schedule {
    //Takes in an activity and time frame, creates a schedule item to be stored in the schedule list that can easily be compared with other items to see if it conflicts or not.
    public Activities activity;
    public int timeWindowSize;
    public int minTime; //recorded in minutes for calculations
    public int maxTime; //recorded in minutes for calculations
    public String minTimeString; //recorded as String for display
    public String maxTimeString; //recorded as String for display
    public boolean noOverlapWindow = false;
    public double priority;

    public Schedule createSchedule(Activities a, String minTime, String maxTime, double priority){
        char[] minTimeArr = minTime.toCharArray();
        char[] maxTimeArr = maxTime.toCharArray();
        //see if there is a joda-time method for this
        int minTimeHour = (Character.getNumericValue(minTimeArr[0])*10)+Character.getNumericValue(minTimeArr[1]);
        int minTimeMinute = (Character.getNumericValue(minTimeArr[3])*10)+Character.getNumericValue(minTimeArr[4]);
        int maxTimeHour = (Character.getNumericValue(maxTimeArr[0])*10)+Character.getNumericValue(maxTimeArr[1]);
        int maxTimeMinute = (Character.getNumericValue(maxTimeArr[3])*10)+Character.getNumericValue(maxTimeArr[4]);

        int b = minTimeHour*60+minTimeMinute;
        int c = maxTimeHour*60+maxTimeMinute;

        if(a.minWindowMinutes<=b){
            this.activity = a;
            this.timeWindowSize = (c-b);
            this.minTime = b;
            this.maxTime = c;
            this.minTimeString = minTime;
            this.maxTimeString = maxTime;
            this.priority = priority;
            return this;
        }
        else return null;
    }

    public Schedule changeActivity(Activities n){
        if(n.minWindowMinutes<=this.timeWindowSize){
            this.activity = n;
            return this;
        }
        else return null;
    }
    public void setWindowFlag(boolean a){
        this.noOverlapWindow = a;
    }
    public Schedule changeWindow(String minTime, String maxTime){
        char[] minTimeArr = minTime.toCharArray();
        char[] maxTimeArr = maxTime.toCharArray();
        //Convert to integers representing minutes
        int minTimeHour = (Character.getNumericValue(minTimeArr[0])*10)+Character.getNumericValue(minTimeArr[1]);
        int minTimeMinute = (Character.getNumericValue(minTimeArr[3])*10)+Character.getNumericValue(minTimeArr[4]);
        int maxTimeHour = (Character.getNumericValue(maxTimeArr[0])*10)+Character.getNumericValue(maxTimeArr[1]);
        int maxTimeMinute = (Character.getNumericValue(maxTimeArr[3])*10)+Character.getNumericValue(maxTimeArr[4]);

        int a = minTimeHour*60+minTimeMinute;
        int b = maxTimeHour*60+maxTimeMinute;
        if((a-b)>=activity.minWindowMinutes){
            this.minTimeString = minTime;
            this.maxTimeString = maxTime;
            this.minTime = a;
            this.maxTime = b;
            this.timeWindowSize = a-b;
            return this;
        }
        else return null;
    }
}
