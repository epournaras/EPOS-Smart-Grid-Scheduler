package com.example.scheduler.BackgroundTasks;

/**
 * Created by warrens on 23.08.17.
 */

//given a set of states, a wattage, a parent task from which it was called, and which schedule and device it is to work on, create an Energy plan for one possible plan and device.

public class deviceFileFiller implements Runnable {
    public Thread t;
    private String threadName;
    public String[] timesData;
    public String appWattage;
    public createFilesTask caller;
    public int schIndex;
    public int devIndex;

    public deviceFileFiller(String name,String[] timeOfDayOnOrOff, String wattage, createFilesTask task, int scheduleIndex, int deviceIndex){
        this.threadName = name;
        this.timesData = timeOfDayOnOrOff;
        this.appWattage = wattage;
        this.caller = task;
        this.schIndex = scheduleIndex;
        this.devIndex = deviceIndex;
    }

    public void run(){
        String dataToReturn = "Plan "+schIndex;
        for(int i =0;i<timesData.length;i++){

            if(timesData[i].equals("1")){
                dataToReturn+=","+appWattage;
            }else{
                dataToReturn+=","+"0";
            }
        }

        returnData(dataToReturn);
    }

    public void start(){
        if(t==null){
            t = new Thread(this, this.threadName);
        }
        t.start();
    }

    public void returnData(String d){
        caller.returnDataTask(d, schIndex, devIndex);
    }
}
