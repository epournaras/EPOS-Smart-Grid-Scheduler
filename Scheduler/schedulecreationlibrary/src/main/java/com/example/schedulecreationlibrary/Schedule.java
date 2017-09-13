package com.example.schedulecreationlibrary;

/**
 * Created by warrens on 08.08.17.
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Schedule {
    private long startTimeCreateSchedule;
    private long endTimeCreateSchedule;
    private long startTimeRateSchedule;
    private long endTimeRateSchedule;
    private long startTimeRankSchedule;
    private long endTimeRankScehdule;
    private long start;
    private long end;
    private long createScheduleDuration;
    private long rateScheduleDuration;
    private long rankScheduleDuration;
    private long fullDuration;
    private String timings;

    public ArrayList<Action> list = new ArrayList<Action>();
    private Action[] actionList = new Action[1];
    private ArrayList<Action[]> scheduleList = new ArrayList<Action[]>();
    public int count = 0;
    private Action[][] schedulesList;
    public boolean rankingDone = false;
    public Action[][] finalList;

    public Schedule(Action[] a){
        list.addAll(Arrays.asList(a));
    }

    public Schedule(){

    }

    public void add(Action a){
        list.add(a);
    }

    public void remove(Action a){
        list.remove(a);
    }
	/*
	 * The list of Actions is complete. Now create Schedules from them.
	 */

    private void initialiseActionList(){
        Collections.sort(list,new actionComparator());
        this.actionList = list.toArray(new Action[list.size()]);
        list.clear();
        list.trimToSize();
    }


    /**
     *                                  FARZAM - This class is basically the same as ThreadManager. It is not finished or working yet, but I am attempting to create a version of
     *                                  Thread Manager that uses BigInteger instead of long.
     *
     */
    public void makeScheduleListBigInteger(){
        if(list.size()>0){
            initialiseActionList();
            BigInteger totalCombinations = BigInteger.ONE;
            for(int i = 0 ; i<this.actionList.length;i++) {
                if (this.actionList[i].versions.length > 0) {
                    totalCombinations = totalCombinations.multiply(new BigInteger(""+this.actionList[i].versions.length));
                    System.out.print(totalCombinations.toString()+"\n");
                }
            }
            int cores = (Runtime.getRuntime().availableProcessors())*2;
            BigInteger coresBigInt = new BigInteger(""+(Runtime.getRuntime().availableProcessors())*2);
            BigInteger[] startingPoints = new BigInteger[cores];
            for(int i =0;i<startingPoints.length;i++){
                startingPoints[i] = (totalCombinations.divide(coresBigInt)).multiply(new BigInteger(""+i));
            }

            ArrayList<ThreadManagerBigInteger> threads = new ArrayList<ThreadManagerBigInteger>();
            for(int i = 0 ; i<cores; i++){
                if(i+1<startingPoints.length){
                    threads.add(new ThreadManagerBigInteger("Thread "+i,actionList,this, startingPoints[i],startingPoints[i+1]));
                }else{
                    threads.add(new ThreadManagerBigInteger("Thread "+i,actionList,this, startingPoints[i],totalCombinations));
                }
            }
            threads.trimToSize();
            for(int i = 0; i<threads.size();i++){
                threads.get(i).start();
            }
            for(int i = 0; i<threads.size();i++){
                try{
                    threads.get(i).t.join();
                }catch(InterruptedException e){
                    System.out.print("Thread "+i+" interrupted\n");
                }
            }
        }
    }

    public void makeScheduleList(){
        start = System.nanoTime();
        startTimeCreateSchedule = System.nanoTime();
        if(list.size()>0){
            initialiseActionList();
            long totalCombinations = 1;
            for(int i = 0 ; i<this.actionList.length;i++){
                if(this.actionList[i].versions.length>0){
                    totalCombinations*=this.actionList[i].versions.length;
                }
            }
            if(totalCombinations<0){
                totalCombinations = 9223372036854775807L;
            }
            int cores = (Runtime.getRuntime().availableProcessors())*2;
            long[] startingPoints = new long[cores];
            for(int i = 0; i<startingPoints.length;i++){
                startingPoints[i] = (totalCombinations/cores)*i;
            }
            ArrayList<ThreadManager> threads = new ArrayList<ThreadManager>();
            for(int i = 0 ; i<cores; i++){
                if(i+1<startingPoints.length){
                    threads.add(new ThreadManager("Thread "+i,actionList,this, startingPoints[i],startingPoints[i+1]));
                }else{
                    threads.add(new ThreadManager("Thread "+i,actionList,this, startingPoints[i],totalCombinations));
                }
            }
            threads.trimToSize();
            for(int i = 0; i<threads.size();i++){
                threads.get(i).start();
            }
            for(int i = 0; i<threads.size();i++){
                try{
                    threads.get(i).t.join();
                }catch(InterruptedException e){
                    System.out.print("Thread "+i+" interrupted\n");
                }
            }
        }
        endTimeCreateSchedule = System.nanoTime();
        createScheduleDuration = (endTimeCreateSchedule - startTimeCreateSchedule)/1000000;
    }

    public void printTopNRankedSchedules(int n){
        if(this.finalList!=null){
            if(this.finalList.length == 0){
                System.out.print("No Schedules Exist");
            }else{
                if(n<this.finalList.length){
                    for(int i = 0; i<n;i++){
                        System.out.print("Schedule #"+i+"\n");
                        printSchedule(this.finalList[i]);
                    }
                }else{
                    for(int i = 0; i<this.finalList.length;i++){
                        System.out.print("Schedule #"+i+"\n");
                        printSchedule(this.finalList[i]);
                    }
                }
            }
        }
    }

    public Action[][] getTopNRankedSchedules(int n){
        if(this.finalList!=null){
            Action[][] topNRankedSchedules;
            if(finalList.length>=n){
                topNRankedSchedules = new Action[n][];
                for(int i = 0; i<n;i++){
                    topNRankedSchedules[i] = finalList[i];
                }
            }else{
                topNRankedSchedules = new Action[finalList.length][];
                for(int i = 0; i<finalList.length;i++){
                    topNRankedSchedules[i] = finalList[i];
                }
            }
            return topNRankedSchedules;
        }else{
            return null;
        }
    }

    private void printSchedule(Action[] a){
        for (Action anA : a) {
            System.out.print(anA.name + ":\t" + anA.getTimeString(anA.windowStart) + "\t" + anA.getTimeString(anA.windowEnd) + "\t" + anA.getTimeString(anA.optimalTime) + "\n");
        }
    }

    /*
     * Used by threads to add the schedule they found to the list of schedules
     */
    public synchronized void returnActionList(Action[] list){
        boolean noConflict = true;
        if(list!=null) {
            for(int i= 0; i<list.length&&noConflict;i++){
                noConflict = checkPosition(i, list[i],list);
            }
            if(noConflict){
                scheduleList.add(list);
            }
            else{
                System.out.print("Unusual conflict when returning list\n");
            }
        }
    }


    /*
     * given an action placed in a given array of actions.
     * Check that none of the previous actions conflict with the given action.
     */
    public boolean checkPosition(int indexOfItem, Action a, Action[] currentSchedule){
        boolean noConflict = true;
        if(a.isParallel()){
            return true;
        }else{
            for(int i = 0; i<currentSchedule.length&&noConflict; i++){
                if(currentSchedule[i]!=null&&i!=indexOfItem){
                    noConflict = checkConflict(a,currentSchedule[i], false);
                }
            }
        }
        return noConflict;
    }
    /*
     *Method to check if two Actions that require complete attention have windows that conflict. If A and B are two actions:
     *If A.WindowStart<B.WindowStart and A.WindowEnd < B.WindowEnd and A.WindowEnd>B.WindowEnd, then they overlap and conflict.
     *If A.WindowStart>B.WindowStart and A.WindowEnd < B.WindowEnd, then A is in the middle of B and they conflict.
     *This should be tested A vs B then B vs A. This can be performed in Parallel.
     *The Actions A and B must have windows the size of their duration.
     */
    public boolean checkConflict(Action a, Action b, boolean reverse){
        if(a.isParallel()||b.isParallel()){
            return true;
        }else{
            if(a.windowStart<=b.windowStart&&a.windowEnd<=b.windowEnd&&a.windowEnd>=b.windowStart){
                //System.out.print("Conflict between "+a.name+" and "+b.name+"\n");
                return false;
            }
            if(a.windowStart>=b.windowStart&&a.windowEnd<=b.windowEnd){
                //System.out.print("Conflict between "+a.name+" and "+b.name+"\n");
                return false;
            }
            if(!reverse){
                if(a.windowStart == b.windowStart){
                    return false;
                }
                if(a.windowEnd == b.windowEnd){
                    return false;
                }
                return checkConflict(b,a, true);
            }
            else{
                return true;
            }
        }
    }

    public void sortSchedulesByRating(){
        startTimeRankSchedule = System.nanoTime();
        schedulesList = new Action[scheduleList.size()][];
        scheduleList.toArray(schedulesList);
        List<Action[]> ratedListToPass = Arrays.asList(schedulesList);
        Collections.sort(ratedListToPass,new ratingComparator());
        Collections.reverse(ratedListToPass);
        this.finalList = ratedListToPass.toArray(new Action[ratedListToPass.size()][]);
        endTimeRankScehdule = System.nanoTime();
        rankScheduleDuration = (endTimeRankScehdule - startTimeRankSchedule)/1000000;
        end = System.nanoTime();
        fullDuration = (end - start)/1000000;
        this.timings = createScheduleDuration+","+rankScheduleDuration+","+fullDuration+"\n";
    }

    public String getTimings(){
        return this.timings;
    }
}

class ratingComparator implements Comparator<Action[]> {

    public int compare(Action[] a, Action[] b){
        double aRating = 0;
        double bRating = 0;
        for(int i = 0; i<a.length;i++){
            aRating+=a[i].getRating();
        }
        for(int i = 0; i<b.length;i++){
            bRating+=b[i].getRating();
        }
        bRating/=b.length;
        aRating/=a.length;
        return Double.compare(bRating, aRating);
    }
}

class actionComparator implements Comparator<Action>{
    public int compare(Action a, Action b){
        return Integer.compare(a.getWindowStart(),b.getWindowStart());
    }
}
