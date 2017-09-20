package com.example.scheduler.BackgroundTasks;

/**
 * Created by warrens on 23.08.17.
 */

//given a set of states, a wattage, a parent task from which it was called, and which schedule and device it is to work on, create an Energy plan for one possible plan and device.

public class deviceFileFiller implements Runnable {
    public Thread t;
    private String threadName;
    public String[][][] data;
    public String appWattage;
    public createFilesTask caller;
    public int schSiz;
    public int devIndex;

    public deviceFileFiller(String name,String[][][] fullData, String wattage, createFilesTask task, int scheduleSize, int deviceIndex){
        this.threadName = name;
        this.data = fullData;
        this.appWattage = wattage;
        this.caller = task;
        this.schSiz = scheduleSize;
        this.devIndex = deviceIndex;
    }

    public void run(){
        for(int c = 0; c<schSiz;c++){
            singlePlan(c);
        }
    }

    public void start(){
        if(t==null){
            t = new Thread(this, this.threadName);
        }
        t.start();
    }

    public void returnData(String d, int schIndex){
        caller.returnDataTask(d, schIndex, devIndex);
    }

    public void singlePlan(int c){
        String dataToReturn = "Plan "+c;
        for(int i =0;i<data[c][devIndex].length;i++){
            if(data[c][devIndex][i].equals("1")){
                dataToReturn+=","+appWattage;
            }else{
                dataToReturn+=","+"0";
            }
        }
        returnData(dataToReturn,c);
    }
}