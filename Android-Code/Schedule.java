package com.example.schedulelibrary;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import static java.util.Arrays.sort;
import java.util.concurrent.locks.*;
import java.util.regex.Pattern;

public class Schedule {
    public ArrayList<Action> list = new ArrayList<Action>();
    public Action[] actionList = new Action[1];
    public ArrayList<Action[]> scheduleList = new ArrayList<Action[]>();
    private Action[] lastStartingList;
    private ReentrantLock lock = new ReentrantLock();
    private ReentrantLock nextListLock = new ReentrantLock();
    int startingIndex;
    int count = 0;

    public Schedule(Action[] a){
        for(int i = 0;i<a.length;i++){
            list.add(a[i]);
        }
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

    public void initialiseActionList(){
        list.trimToSize();
        int numberOfActions = list.size();
        actionList = new Action[numberOfActions];
        int[] temp = new int[numberOfActions];
        for(int i = 0; i<numberOfActions; i++){
            actionList[i] = list.get(i);
            temp[i] = actionList[i].windowStart;
        }
        Action[] tempList = new Action[numberOfActions];
        sort(temp);
        boolean found = false;
        for(int i = 0; i < numberOfActions; i++){
            for(int j = 0; j<numberOfActions&&!found; j++){
                if(temp[i] == actionList[j].windowStart){
                    tempList[i] = actionList[j];
                    found = true;
                }
            }
            found = false;
        }
        actionList = tempList;
        tempList = null;
        temp = null;
    }

    public void makeScheduleList(){
        initialiseActionList();
        Action[] referenceList = new Action[actionList.length];
        for(int i = 0 ; i < referenceList.length ; i++ ){
            referenceList[i] = new Action(actionList[i]);
            referenceList[i].windowEnd = referenceList[i].windowStart + referenceList[i].duration;
        }
        int cores = getNumCores();
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
            threads.add(new ThreadManager("Thread"+i,listArray[i],this,0));
        }
        threads.trimToSize();
        for(int i = 0; i<threads.size();i++){
            threads.get(i).start();
        }
        for(int i = 0; i<threads.size();i++) {
            try {
                threads.get(i).t.join();
            } catch (InterruptedException e) {
                System.out.print("Thread" + i + " interrupted\n");
            }
        }
    }

    public Action[][] getFullList(){
        scheduleList.trimToSize();
        Action[][] listArray = new Action[scheduleList.size()][];
        if(!scheduleList.isEmpty()) {
            for (int q = 0; q < scheduleList.size(); q++) {
                if (scheduleList.get(q) != null) {
                    listArray[q] = scheduleList.get(q);
                }
            }
            return listArray;
        }else{
            System.out.print("No schedules exist");
            return null;
        }
    }

    public void printScheduleList(){
        int j = 0;
        //System.out.print("HERE");
        scheduleList.trimToSize();
        Action[][] listArray = getFullList();
        if(listArray != null) {
            for (int q = 0; q < listArray.length; q++) {
                for (j = 0; j < listArray[q].length; j++) {
                    if (listArray[q][j] != null) {
                        System.out.print(listArray[q][j].name + " "
                                + listArray[q][j].getTimeString(listArray[q][j].windowStart)
                                + "-" + listArray[q][j].getTimeString(listArray[q][j].windowEnd) + "\n");
                    }
                }
            }
        }
    }

    /*
     * Used by threads to add the schedule they found to the list of schedules
     */
    public void returnActionList(Action[] list){
        boolean lockAcquired = false;
        try{
            lock.tryLock();
            try{
                lockAcquired = true;
                boolean noConflict = true;
                if(list!=null) {
                    for(int i= 0; i<list.length&&noConflict;i++){
                        noConflict = checkPosition(i, list[i],list);
                    }
                    if(noConflict){
                        scheduleList.add(cloneActionArray(list));
                    }
                }
            }finally{
                if(lockAcquired){
                    lock.unlock();
                }
            }
        }catch(IllegalMonitorStateException e){

        }
        catch(ArrayIndexOutOfBoundsException e){

        }
    }

    /*
     * Go to the last list that was given to a thread, use it as an input to changeWindow to get the next list to use to get another schedule
     */
    public Action[] getNextList(){
        boolean lockAcquired = false;
        Action[] temp = null;
        try{
            nextListLock.tryLock();
            try{
                lockAcquired = true;
                temp = cloneActionArray(lastStartingList);
                temp[startingIndex].windowStart++;
                temp[startingIndex].windowEnd=temp[startingIndex].windowStart+temp[startingIndex].duration;
                temp[startingIndex] = changeWindow(startingIndex, temp);
                if(temp[startingIndex]!=null){
                    lastStartingList = temp;
                }else{
                    temp = null;
                }
            }finally{
                if(lockAcquired){
                    nextListLock.unlock();
                    return temp;
                }
            }
        }catch(IllegalMonitorStateException e){

        }
        catch(ArrayIndexOutOfBoundsException e){

        }
        Action failure = new Action("Failure","00:00","00:00","00:00",0);
        Action[] tryFailed = {failure};
        return tryFailed;
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
    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     * @return The number of cores, or 1 if failed to get result
     */
    private int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Default to return 1 core
            return 1;
        }
    }
}
