package com.example.schedulelibrary;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;;

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
//  public RatedSchedule[] ratedList;
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
            int cores = Runtime.getRuntime().availableProcessors();
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
                System.out.print("Here 4\n");
            }
        }
    }


    //    /*
//     * given an action placed in a given array of actions.
//     * Check that none of the previous actions conflict with the given action.
//     */
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
    /*
     * Go to the last list that was given to a thread, use it as an input to changeWindow to get the next list to use to get another schedule
     */
//    public synchronized Action[] getNextList(){
//    	if(countOfUsedPositions<actionList[0].versions.length){
//    		Action[] temp = new Action[actionList.length];
//    		temp[0] = actionList[0].getVersion(countOfUsedPositions);
//    		countOfUsedPositions++;
//    		return temp;
//    	}
//    	return null;
//    }


//    public Action[] getSchedule(Action[] referenceList, int startingIndex){
//        boolean noSchedule = false;
//        Action[] temp = new Action[referenceList.length];
//        for(int i = startingIndex; i<referenceList.length&&!noSchedule;i++){
//            if(temp[i]==null){
//                temp[i] = actionList[i].getVersion(0);
//            }
//            temp[i] = changeWindow(i,temp);
//            if(temp[i]==null){
//                if(i!=startingIndex){
//                    i-=2;
//                }else{
//                    temp = null;
//                    noSchedule = true;
//                }
//            }
//        }
//        return temp;
//    }


    /*
     * Take in an action in an action array.
     * Move the window of the action, check if the new position of the window conflicts with actions preceding this action in the array.
     * If not: return the action with the new window.
     * If it conflicts, move the window of the action again.
     * If the window moves to the end of possible window positions and there was no position in which it did not conflict with preceding actions, return null.
     */
//    public Action changeWindow(int indexOfItem, Action[] currentSchedule){
//        try{
//        	boolean positionOk = false;
//        	int position = 0;
//        	for(int i = 0; i<currentSchedule[indexOfItem].versions.length&&!positionOk;i++){
//        		positionOk = checkPosition(indexOfItem, currentSchedule[indexOfItem].getVersion(i),currentSchedule);
//        		position = i;
//        	}
//        	if(!positionOk){
//        		return null;
//        	}else{
//        		return currentSchedule[indexOfItem].getVersion(position);
//        	}
//        }catch(NullPointerException e){
//            System.out.print("ERROR: Action "+currentSchedule[indexOfItem].name+" is null \n");
//            return null;
//        }
//    }
//
    /*
     * Threads attempt to pass back the ratings they found for the particular schedule they were working on.
     */
//    public synchronized void returnRating(int i, double a){
//        ratings[i] = a;
//    }
//
//    /*
//     * Using the ArrayList of Schedules (Arrays of Actions), create their rating by totalling the rating of each of their actions
//     * Then, using this rating rank them in order of lowest to highest rating.
//     */
//    public void rankSchedulesByRating(){
//    	System.out.print(scheduleList.size()+"\n");
//    	System.out.print("Here 2 \n");
//        schedulesList = new Action[scheduleList.size()][];
//        scheduleList.toArray(schedulesList);
//
//        int cores = Runtime.getRuntime().availableProcessors();
//        ArrayList<RankingThreads> threads = new ArrayList<RankingThreads>();
//        ratings = new double[schedulesList.length];
//
//        for(int i = 0; i<cores;i++){
//            threads.add(new RankingThreads("Thread "+i,this, i, schedulesList[i]));
//            passedSchedulesCount++;
//        }
//        threads.trimToSize();
//        for(int i = 0; i<threads.size();i++){
//            threads.get(i).start();
//        }
//        for(int i = 0; i<threads.size();i++){
//            try{
//                threads.get(i).t.join();
//            }catch(InterruptedException e){
//                System.out.print("Thread "+i+" interrupted\n");
//            }
//        }
//    }


//    public synchronized ScheduleAndIndex getNewSchedule(){
//
//        ScheduleAndIndex passBack = new ScheduleAndIndex();
//        if(passedSchedulesCount<schedulesList.length){
//            int index = passedSchedulesCount;
//            Action[] schedule = schedulesList[passedSchedulesCount];
//            passBack = new ScheduleAndIndex(index, schedule, false);
//            passedSchedulesCount++;
//        }else{
//            passBack = new ScheduleAndIndex();
//        }
//        return passBack;
//    }
//
//    public synchronized void receiveSortedRatedSchedules(RatedSchedule[] a){
//        this.rankedSchedules = new RatedSchedule[a.length];
//        System.arraycopy(a, 0, this.rankedSchedules, 0, a.length);
//    }
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
