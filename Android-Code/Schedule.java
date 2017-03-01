package com.example.schedulelibrary;

/*
 * Created by warrens on 24.01.17.
 */
import android.util.TimingLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;

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
    private Action[] lastStartingList;

    private int startingIndex;
    public int count = 0;
    private int schedulesToCreate = 80000;
    private double[] ratings;
    private int passedSchedulesCount=0;
    private Action[][] schedulesList;
    private RatedSchedule[] rankedSchedules;
    public boolean rankingDone = false;
    public RatedSchedule[] ratedList;
    public Schedule(Action[] a){
        list.addAll(Arrays.asList(a));
    }
    public Schedule(){
        this.startingIndex = 0;
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

    public void makeScheduleList(){
        start = System.nanoTime();
        startTimeCreateSchedule = System.nanoTime();
        if(list.size()>0){
            initialiseActionList();
            Action[] referenceList = new Action[actionList.length];
            for(int i = 0 ; i < referenceList.length ; i++ ){
                referenceList[i] = new Action(actionList[i]);
                referenceList[i].windowEnd = referenceList[i].windowStart + referenceList[i].duration;
            }
            int cores = Runtime.getRuntime().availableProcessors();
            ArrayList<Action[]> listOfStartingLists = new ArrayList<Action[]>();
            Action[] actionTemp = new Action[referenceList.length];
            boolean positionsLeft = true;
            for(int i = 0; i<cores&&positionsLeft;i++){
                if(i==0){
                    actionTemp[0] = intialiseListForSplit(referenceList, 0);
                }else{
                    actionTemp[0].windowStart++;
                    actionTemp[0].windowEnd=actionTemp[0].windowStart+actionTemp[0].duration;
                    actionTemp[0] = intialiseListForSplit(actionTemp,0);
                }
                if(actionTemp[0]!=null){
                    listOfStartingLists.add(cloneActionArray(actionTemp));
                }else{
                    positionsLeft = false;
                }
            }
            startingIndex = 0;
            listOfStartingLists.trimToSize();
            Action[][] listArray = new Action[listOfStartingLists.size()][];
            listOfStartingLists.toArray(listArray);

            lastStartingList = listArray[listArray.length-1];
            ArrayList<ThreadManager> threads = new ArrayList<ThreadManager>();

            for(int i = 0 ; i<listArray.length; i++){
                threads.add(new ThreadManager("Thread "+i,listArray[i],this,0,this.schedulesToCreate/cores));
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
            endTimeCreateSchedule = System.nanoTime();
            createScheduleDuration = (endTimeCreateSchedule - startTimeCreateSchedule)/1000000;

        }
    }

    public void setSchedulesToCreate(int a){
        this.schedulesToCreate = a;
    }

    public void printTopNRankedSchedules(int n){
        if(this.rankedSchedules!=null){
            if(this.rankedSchedules.length == 0){
                System.out.print("No Schedules Exist");
            }else{
                if(n<this.rankedSchedules.length){
                    for(int i = 0; i<n;i++){
                        System.out.print("Schedule #"+i+"\n");
                        printSchedule(this.rankedSchedules[i].schedule);
                    }
                }else{
                    for(int i = 0; i<this.rankedSchedules.length;i++){
                        System.out.print("Schedule #"+i+"\n");
                        printSchedule(this.rankedSchedules[i].schedule);
                    }
                }
            }
        }
    }

    public Action[][] getTopNSchedules(int n){
        Action[][] nSchedules = new Action[n][];
        for(int i = 0;i<n;i++){
            nSchedules[i] = new Action[this.rankedSchedules[i].schedule.length];
            System.arraycopy(this.rankedSchedules[i].schedule,0,nSchedules[i],0,this.rankedSchedules[i].schedule.length);
        }
        return nSchedules;
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
                scheduleList.add(cloneActionArray(list));
            }
        }
    }

    /*
     * Go to the last list that was given to a thread, use it as an input to changeWindow to get the next list to use to get another schedule
     */
    public synchronized Action[] getNextList(){
        Action[] temp = null;
        temp = cloneActionArray(lastStartingList);
        temp[startingIndex].windowStart++;
        temp[startingIndex].windowEnd=temp[startingIndex].windowStart+temp[startingIndex].duration;
        temp[startingIndex] = changeWindow(startingIndex, temp);
        if(temp[startingIndex]!=null){
            lastStartingList = temp;
        }else{
            temp = null;
        }
        return temp;
    }
    /*
     * pass in the list of actions in their original form (original entered window)
     * or in an altered form in order to advance the window to the next acceptable state.
     * pass in the index at which alterations are to begin.
     * create a clone of each action not to be altered and place it in a temp array. These are the actions whose windows all proceeding actions will be tested against for conflicts.
     * when an acceptable window for an action is found, place it in the temp array.
     * when all actions are placed, return the temp array containing all the placed actions.
     */
    public Action intialiseListForSplit(Action[] referenceList, int indexAtWhichToSplit){
        Action temp = new Action(referenceList[indexAtWhichToSplit]);
        temp = changeWindow(indexAtWhichToSplit, referenceList);
        if(temp!=null) return temp;
        temp=null;
        return null;
    }

    public Action[] getSchedule(Action[] referenceList, int startingIndex){
        boolean noSchedule = false;
        Action[] temp = new Action[referenceList.length];
        for(int i = 0; i<startingIndex; i++){
            temp[i] = new Action(referenceList[i]);
        }
        for(int i = startingIndex; i<referenceList.length&&!noSchedule;i++){
            if(temp[i]==null){
                temp[i] = new Action(actionList[i]);
            }
            temp[i] = changeWindow(i,temp);
            if(temp[i]==null){
                if(i!=startingIndex){
                    i-=2;
                }else{
                    temp = null;
                    noSchedule = true;
                }
            }
        }
        return temp;
    }


    /*
     * Take in an action in an action array.
     * Move the window of the action, check if the new position of the window conflicts with actions preceding this action in the array.
     * If not: return the action with the new window.
     * If it conflicts, move the window of the action again.
     * If the window moves to the end of possible window positions and there was no position in which it did not conflict with preceding actions, return null.
     */
    public Action changeWindow(int indexOfItem, Action[] currentSchedule){
        try{
            Action clone = new Action(currentSchedule[indexOfItem]);
            clone.windowEnd = clone.windowStart+clone.duration;
            boolean positionOk = false;
            while(clone.windowStart+clone.duration<actionList[indexOfItem].windowEnd&&!positionOk){
                positionOk = checkPosition(indexOfItem, clone, currentSchedule);
                if(!positionOk){
                    clone.windowStart++;
                    clone.windowEnd = clone.windowStart+clone.duration;
                }
            }
            if(clone.windowStart+clone.duration>actionList[indexOfItem].windowEnd){
                clone = null;
                return null;
            }else{
                return clone;
            }
        }catch(NullPointerException e){
            System.out.print("ERROR: Action "+currentSchedule[indexOfItem].name+" is null \n");
            return null;
        }
    }

    /*
     * given an action placed in a given array of actions.
     * Check that none of the previous actions conflict with the given action.
     */
    public boolean checkPosition(int indexOfItem, Action a, Action[] currentSchedule){
        boolean noConflict = true;
        for(int i = 0; i<currentSchedule.length&&noConflict; i++){
            if(currentSchedule[i]!=null&&i!=indexOfItem){
                noConflict = checkConflict(a,currentSchedule[i], false);
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

    public Action[] cloneActionArray(Action[] array){
        Action[] clone = new Action[array.length];
        for(int i = 0; i<array.length;i++){
            if(array[i]!=null){
                clone[i] = new Action(array[i]);
            }
        }
        return clone;
    }

    /*
     * Threads attempt to pass back the ratings they found for the particular schedule they were working on.
     */
    public synchronized void returnRating(int i, double a){
        ratings[i] = a;
    }

    /*
     * Using the ArrayList of Schedules (Arrays of Actions), create their rating by totalling the rating of each of their actions
     * Then, using this rating rank them in order of lowest to highest rating.
     */
    public void rankSchedulesByRating(){
        startTimeRateSchedule = System.nanoTime();
        schedulesList = new Action[scheduleList.size()][];
        scheduleList.toArray(schedulesList);
        scheduleList.clear();
        scheduleList.trimToSize();

        RatedSchedule[] ratedScheduleList = new RatedSchedule[schedulesList.length];
        for(int i = 0; i<schedulesList.length;i++){
            ratedScheduleList[i] = new RatedSchedule(1);
            ratedScheduleList[i].schedule = schedulesList[i];
        }
        int cores = Runtime.getRuntime().availableProcessors();
        ArrayList<RankingThreads> threads = new ArrayList<RankingThreads>();
        ratings = new double[schedulesList.length];
        for(int i = 0; i<cores;i++){
            threads.add(new RankingThreads("Thread "+i,this, i, schedulesList[i]));
            passedSchedulesCount++;
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
        this.ratedList = new RatedSchedule[schedulesList.length];
        for(int i = 0; i<schedulesList.length;i++){
            this.ratedList[i] = new RatedSchedule(ratings[i], schedulesList[i]);
        }
        endTimeRateSchedule = System.nanoTime();
        rateScheduleDuration = (endTimeRateSchedule-startTimeRateSchedule)/1000000;
        startTimeRankSchedule = System.nanoTime();
        sortSchedulesByRating();
        endTimeRankScehdule = System.nanoTime();
        rankScheduleDuration = (endTimeRankScehdule - startTimeRankSchedule)/1000000;
        end = System.nanoTime();
        fullDuration = (end - start)/1000000;
        this.timings = createScheduleDuration+","+rateScheduleDuration+","+rankScheduleDuration+","+fullDuration+"\n";
    }

    public void sortSchedulesByRating(){
        List<RatedSchedule> ratedListToPass = Arrays.asList(ratedList);
        Collections.sort(ratedListToPass,new ratingComparator());
        Collections.reverse(ratedListToPass);
        this.rankedSchedules = ratedListToPass.toArray(new RatedSchedule[ratedListToPass.size()]);
    }
    public synchronized ScheduleAndIndex getNewSchedule(){

        ScheduleAndIndex passBack = new ScheduleAndIndex();
        if(passedSchedulesCount<schedulesList.length){
            int index = passedSchedulesCount;
            Action[] schedule = schedulesList[passedSchedulesCount];
            passBack = new ScheduleAndIndex(index, schedule, false);
            passedSchedulesCount++;
        }else{
            passBack = new ScheduleAndIndex();
        }
        return passBack;
    }

    public synchronized void receiveSortedRatedSchedules(RatedSchedule[] a){
        this.rankedSchedules = new RatedSchedule[a.length];
        System.arraycopy(a, 0, this.rankedSchedules, 0, a.length);
    }

    public String getTimings(){
        return this.timings;
    }
}

class ratingComparator implements Comparator<RatedSchedule> {

    public int compare(RatedSchedule a, RatedSchedule b){
        return Double.compare(b.getRating(), a.getRating());
    }
}

class actionComparator implements Comparator<Action>{
    public int compare(Action a, Action b){
        return Integer.compare(a.getWindowStart(),b.getWindowStart());
    }
}
