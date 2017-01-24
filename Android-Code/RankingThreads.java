package com.example.schedulelibrary;

/**
 * Created by warrens on 19.01.17.
 */

public class RankingThreads implements Runnable{
    public Thread t;
    private String threadName;
    private Schedule caller;
    private int passedScheduleIndex;
    private Action[] array;
    private ScheduleAndIndex item;
    private boolean end = false;

    public RankingThreads(String name, Schedule caller, int passedScheduleIndex, Action[] array){
        this.threadName = name;
        this.caller = caller;
        this.passedScheduleIndex = passedScheduleIndex;
        this.array = array;
    }

    public void run(){
        //System.out.print("\n"+"Running "+threadName+"...\n");
        while(!end){
            double rating = this.getScheduleRating(array);
            this.returnTotal(rating);
            this.getNextSchedule();
        }
        //System.out.print("\n"+threadName+" exiting \n");
    }

    public void start(){
        //System.out.print("\n"+"Starting "+this.threadName+"\n");
        if(t==null){
            t = new Thread(this, this.threadName);
        }
        t.start();
    }

    public double getScheduleRating(Action[] schedule){
        int total =0;
        for(int i  = 0; i < schedule.length;i++){
            int rating = schedule[i].windowStart-schedule[i].optimalTime;
            if(rating<0){
                rating = (-1)*(rating);
            }
            total+=rating;
        }
        return total;
    }

    private synchronized void returnTotal(double a){
        this.caller.returnRating(this.passedScheduleIndex,a);
    }

    private void getNextSchedule(){
        this.item = caller.getNewSchedule();
        this.array = item.schedule;
        this.passedScheduleIndex = item.index;
        this.end = item.finalPass;
    }
}
