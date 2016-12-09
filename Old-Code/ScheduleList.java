package com.example.activitieslibrary;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.sort;
import android.util.Log;



/**
 * Created by warrens on 19.10.16.
 */

public class ScheduleList {
    //A list of schedule items.
    //This item is checked against the existing list of scheduled items. If a conflicting item is found, the new item is rejected (item A and/or item B have the cannot overlap window flag set, or have the cannot overlap operation flag set and there is no possible time for them not to overlap).
    //If the total time that the scheduled items take is more than the limit set by the user also reject.
    //create a list of possible schedules
    public ArrayList<Schedule> list;

    public ScheduleList createList(){
        list = new ArrayList<Schedule>();
        return this;
    }

    public boolean addScheduleItem(Schedule a){
        if(!this.list.contains(a)){
            int t = list.size();
            boolean itemDoesntConflict = true;
            for(int i = 0; i<t && itemDoesntConflict; i++){
                if (((a.minTime < list.get(i).maxTime) && (a.minTime > list.get(i).minTime))||((a.maxTime < list.get(i).maxTime) && (a.maxTime > list.get(i).minTime))) {
                    itemDoesntConflict = doItemsConflict(a, list.get(i));
                }
            }
            if(itemDoesntConflict){
                list.add(a);
                return true;
            }
        }
        return false;
    }

    public boolean doItemsConflict(Schedule a, Schedule b){
        boolean itemDoesntConflict = true;
        if(a.noOverlapWindow||b.noOverlapWindow) {
            if (((a.minTime < b.maxTime) && (a.minTime > b.minTime))||((a.maxTime < b.maxTime) && (a.maxTime > b.minTime)) ) {
                itemDoesntConflict = false;
            }
        }
        if(a.activity.noOverlapSchedule||b.activity.noOverlapSchedule){
            //if the already added item has a window that encompasses the window of the item attempting to be added.
            if((( a.maxTime < b.maxTime ) && ( a.maxTime>b.minTime ) && ( a.minTime > b.minTime ))){
                        /*
                        The time window for the activity in the list is so large that is contains the time window for the activity attempting to be added. If the activity already added
                        cannot fit in the space before the new window starts, or after the new window finishes
                        WinA min                           WinB Min       WinB Max                          WinA Max
                            {--Section before window starts--[~~New Window~~]--Section after window finishes--}
                        Or if the Activities cannot fit in the space between WinA min and WinB Max, or in the space between WinB Min and WinA Max, then reject adding the new activity.
                         */
                if(( a.minTime - b.minTime < b.activity.minWindowMinutes ) && ( b.maxTime - a.maxTime < b.activity.minWindowMinutes ) &&
                        ( a.maxTime - b.minTime < a.activity.minWindowMinutes + b.activity.minWindowMinutes ) &&
                        ( b.maxTime-a.minTime < a.activity.minWindowMinutes + b.activity.minWindowMinutes)) {
                    itemDoesntConflict = false;
                }
            }
            //if the schedule item attempting to be added has a window that encompasses the window of an already added item
            if((( b.maxTime < a.maxTime ) && ( b.maxTime > a.minTime ) && ( b.minTime > a.minTime ))){
                        /*
                        The time window for the activity in the list is so large that is contains the time window for the activity attempting to be added. If the activity already added
                        cannot fit in the space before the new window starts, or after the new window finishes
                        WinA min                           WinB Min       WinB Max                          WinA Max
                            [--Section before window starts--{~~New Window~~}--Section after window finishes--]
                        Or if the Activities cannot fit in the space between WinA min and WinB Max, or in the space between WinB Min and WinA Max, then reject adding the new activity.
                         */
                if(( b.minTime - a.minTime < a.activity.minWindowMinutes ) && ( a.maxTime - b.maxTime < a.activity.minWindowMinutes ) &&
                        ( b.maxTime - a.minTime < a.activity.minWindowMinutes + b.activity.minWindowMinutes ) &&
                        ( a.maxTime - b.minTime < a.activity.minWindowMinutes + b.activity.minWindowMinutes )) {
                    itemDoesntConflict = false;
                }
            }
                    /*
                    if the item attempting to be added has a window that overlaps the beginning of another window, is it still possible for both of them to fit without overlapping schedules
                    WinA start   WinB start        WinA end           WinB end
                    [------------{~-~-~-~-~-~-~-~-~]~~~~~~~~~~~~~~~~~~~}
                     */
            if(( b.minTime - a.minTime < a.activity.minWindowMinutes ) && ( a.maxTime - b.maxTime < b.activity.minWindowMinutes ) &&
                    ( b.maxTime - a.minTime < b.activity.minWindowMinutes + a.activity.minWindowMinutes )){
                itemDoesntConflict = false;
            }
                    /*
                    if the item to be added has the start of its window overlap the end of the other items window, is it still possible for them to fit without overlapping schedules
                    WinA start   WinB start        WinA end           WinB end
                    {------------[~-~-~-~-~-~-~-~-~}~~~~~~~~~~~~~~~~~~~]
                     */
            if(( a.minTime - b.minTime < b.activity.minWindowMinutes ) && ( b.maxTime - a.maxTime < a.activity.minWindowMinutes ) &&
                    ( a.maxTime - b.minTime < b.activity.minWindowMinutes + a.activity.minWindowMinutes )){
                itemDoesntConflict = false;
            }
        }
        return itemDoesntConflict;
    }

    public ScheduleList removeScheduleItem(Schedule a){
        if(this.list.contains(a)){
            list.remove(list.indexOf(a));
        }
        return this;
    }

    public Schedule[] getList(){
        this.list.trimToSize();
        return this.list.toArray(new Schedule[list.size()]);
    }

    public Schedule[][] createPossibleLists(){
        /*
          use this method to create a list of possible schedules including all the items currently in the list.
          reduce items window size to the minimum, and test it against all other items. If this new window size and placement conflicts, adjust the placement in the window and attempt again.
          when all items are placed without confliction, save this arrangement and then adjust the highest priority item to a new position and attempt to create another arrangement.
        */
        this.list.trimToSize();
        int size = list.size();
        Schedule[] finalList = this.getList();
        Schedule[] orderedFinalList = new Schedule[size];

        double [] priorityList = new double[size];
        for(int i = 0;i<size;i++) //create a list of all the priorities in finalList with corresponding indeces. ie the priority for finalList[5] is in priorityList[5].
        {
           priorityList[i] = finalList[i].priority;
        }
        sort(priorityList);
        for(int y = 0; y < priorityList.length / 2; y++)
        {
            double temp = priorityList[y];
            priorityList[y] = priorityList[priorityList.length - y - 1];
            priorityList[priorityList.length - y - 1] = temp;
        }

        for(int o = 0; o<priorityList.length;o++) {
            boolean found = false;
            for (int k = 0; k < finalList.length && !found; k++) {
                if(priorityList[o]==finalList[k].priority){
                    orderedFinalList[o] = finalList[k];
                    found = true;
                }
            }
        }
        ArrayList<ArrayList<Schedule>> arrangementsList = new ArrayList<ArrayList<Schedule>>();
        ArrayList<Schedule> arrangement = new ArrayList<Schedule>();
        arrangement.ensureCapacity(size);
        int[] maxTimes = new int[size];
        int[] minTimes = new int[size];

        for(int i =0; i<size;i++){      //Save all max and min times
            maxTimes[i] = orderedFinalList[i].maxTime;
            Log.d("MaxTime", ""+maxTimes[i]);
            minTimes[i] = orderedFinalList[i].minTime;
            Log.d("MinTime", ""+minTimes[i]);
        }


        //arrangementsList = arrangementFind(arrangementsList,orderedFinalList,0, minTimes, maxTimes);
        //arrangementsList.trimToSize();
        //int arrangementListSize = arrangementsList.size();
        Schedule[][]returnable = new Schedule[3][];

        for(int i = 0; i<returnable.length;i++) {
            //returnable[i]= arrangementsList.get(i).toArray(new Schedule[arrangementsList.get(i).size()]);
            returnable[i] = orderedFinalList;
        }
        return returnable;
    }
    public ArrayList<ArrayList<Schedule>> arrangementFind(ArrayList<ArrayList<Schedule>> arrangements, Schedule[] orderedList, int orderedListIndex, int[] minTimes, int[] maxTimes){
        boolean conflicts = false;
        /*
          Create a method where the 1st priorty item is placed, then checked if that doesnt conflict with anything.

          If it doesn't conflict, add it to arrangement and move on to the next item. Place that item in the 1st available position that doesnt conflict before moving onto the next to do the same.

          When all items have been placed in non conflicting arrangements, add the arrangement to the arrangementsList.

          Copy the last arrangement. Get the last item in the arrangement, if the last item can be placed in another position where it is not conflicting, save this new position for the item. Save this as the
          2nd arrangement in the arrangement list. Move the item into the next position again and save this as another arrangement.

          When all of the arrangements of where the last item can fit when all other items are locked into their place, move to the 2nd last item, move it to the next position where it does not conflict.

          Move on to the last item again and find all the arrangements when the 2nd last item is in the 2nd position. Save all these arrangements to the arrangementsList.

          When this is done, do the same again with the 2nd last item in the 3rd position etc.

          When all arrangements for all 2nd last item and last item position combinations have been saved, move to the 3rd last item.

          Move the 3rd last item to the next position where it does not conflict with the other items. Move onto the 2nd last item and find all the positions where the 2nd last item can be with the
          new position of the 3rd last item. For all the positions where the 2nd last item can be with the new position of the 3rd last item, find all the positions of the last item. Save all these arrangements

          Find the next position for the 3rd last item and do all the same again. Repeat for all possible positions of the 3rd last item before moving to the 4th last item etc till the 1st item.

          when there are no new positions for the 1st item, the arrangement list is complete.

          RECURSION
        */
        // Given an arrangement, an orderedList, an index and a size of orderedList, find the next possible position of the item at the given index of the orderedList for the current arrangement.

        // If the index is <size: call arrangementFind() for the same orderedList, size, arrangement and index+1.

        // If the index is !<size: find all positions of the item at the end of the orderedList, save arrangements and return to the previous item

        // If the index is <size and there is no next possible position, return to the previous item.
        if(orderedListIndex<orderedList.length){
            do{
                conflicts = false;
                for(int i = 0; i<orderedList.length&&!conflicts; i++){
                    conflicts = doItemsConflict(orderedList[orderedListIndex],orderedList[i]);
                }
                if(conflicts){
                    orderedList[orderedListIndex].minTime +=1;
                }
                orderedList[orderedListIndex].maxTime = orderedList[orderedListIndex].minTime + orderedList[orderedListIndex].activity.minWindowMinutes;
            }while(conflicts&&(orderedList[orderedListIndex].minTime + orderedList[orderedListIndex].activity.minWindowMinutes<=maxTimes[orderedListIndex]));
            if(conflicts){
                return arrangements;
            }
            else{
               return arrangementFind(arrangements,orderedList, orderedListIndex+1,minTimes,maxTimes);
            }
            /*
            if(conflicts){ //if conflicts is still true after all possible window times checked, restore original values and return to the previous item
                orderedList[orderedListIndex].minTime = minTimes[orderedListIndex];
                orderedList[orderedListIndex].maxTime = maxTimes[orderedListIndex];
                return arrangements;
            }else{ //continue finding item placement.
                arrangements = arrangementFind(arrangements, orderedList, orderedListIndex+1, minTimes, maxTimes);

                //when the next item returns back to this item, adjust the window of this item and test if there are any new undiscovered acceptable windows to find.
                while(orderedList[orderedListIndex].minTime+1 + orderedList[orderedListIndex].activity.minWindowMinutes<=maxTimes[orderedListIndex]){
                    orderedList[orderedListIndex].minTime+=1;//adjust the window
                    arrangements = arrangementFind(arrangements, orderedList, orderedListIndex, minTimes, maxTimes);//test all points after and including this new window for acceptable windows.
                }

                //when all points for this window have been checked: restore original window values and return to previous item
                orderedList[orderedListIndex].minTime = minTimes[orderedListIndex];
                orderedList[orderedListIndex].maxTime = maxTimes[orderedListIndex];
                return arrangements;
            }*/

        }else{
            //all items have been placed successfully. Add the arrangement and return to the last item.
            ArrayList<Schedule> arrangement = new ArrayList<Schedule>(Arrays.asList(orderedList));
            arrangements.add(arrangement);
            return arrangements;
        }
    }
}
