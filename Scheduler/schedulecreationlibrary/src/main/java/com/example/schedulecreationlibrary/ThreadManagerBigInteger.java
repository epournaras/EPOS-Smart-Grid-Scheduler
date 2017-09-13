package com.example.schedulecreationlibrary;

import java.math.BigInteger;

/**
 * Created by warrens on 08.08.17.
 */


/**
 *                                  FARZAM - This class is basically the same as ThreadManager. It is not finished or working yet, but I am attempting to create a version of
 *                                  Thread Manager that uses BigInteger instead of long.
 *
 */

public class ThreadManagerBigInteger implements Runnable{
    public Thread t;
    private String threadName;
    private Action[] list;
    private Schedule caller;
    private BigInteger startingIndex;
    private BigInteger endingIndex;

    public ThreadManagerBigInteger(String name, Action[] list, Schedule caller, BigInteger startingIndex, BigInteger endingIndex){
        this.threadName = name;
        this.list = list;
        this.caller = caller;
        this.startingIndex = startingIndex;
        this.endingIndex = endingIndex;
        System.out.print(threadName+":"+startingIndex.toString()+","+endingIndex.toString()+"\n");
    }

    /*
     * Initialise the list by creating the first list. This is when all items are in their first acceptable non-conflicting positions.
     * Call change window on the last item, each time it returns a result add that item to the end of the list and call returnActionList to add it to the list of schedules.
     * Do this until that returns null. When it returns null, call changeWindow on the 2nd last item,
     * Then go back to calling changeWindow on the last item. When that returns null again, call changeWindow on the 2nd last item.
     * When calling changeWindow returns null on the 2nd last item, call changeWindow on the 3rd last item.
     * When this process returns you to the first item, get a new starting list.
     */
    public void run(){
        long count = 0;
        long maxCount = 500000L;
        BigInteger step = endingIndex.divide(new BigInteger(""+maxCount));
        if(step.intValue()<1){
            step = new BigInteger(""+1);
        }
        System.out.print("\n"+"Running "+threadName+"...\n");
        BigInteger optimalPoint = new BigInteger(""+0);
        long multiplier = 1L;
        for(int i = 0; i< list.length;i++){
            optimalPoint = optimalPoint.add(new BigInteger(""+list[i].getOptimalIndex()*multiplier));
            multiplier*=list[i].versions.length;
        }
        BigInteger[] indeces = new BigInteger[list.length];
        boolean checkOne = false;
        boolean checkTwo = false;
        int resultOne = optimalPoint.compareTo(startingIndex);
        int resultTwo = optimalPoint.compareTo(endingIndex);
        switch(resultOne){
            case 0://both BigIntegers are equal
                checkOne = false;
                break;
            case 1://The first value is greater.
                checkOne = true;
                break;
            case -1://the second value is greater.
                checkOne = false;
        }

        switch(resultTwo){
            case 0://both BigIntegers are equal
                checkTwo = false;
                break;
            case 1://The first value is greater.
                checkTwo = false;
                break;
            case -1://the second value is greater.
                checkTwo = true;
        }
        if(checkOne&&checkTwo){
            BigInteger passOn = optimalPoint.add(new BigInteger(""+1));
            indeces = getCombinationBigInteger(passOn,list,0,indeces);
            Action[] check = new Action[list.length];
            for(int j = 0; j< list.length; j++){
                boolean c;
                int r = indeces[j].compareTo(new BigInteger(""+0));
                switch(r){
                    case 0:
                        c=true;
                        break;
                    case 1:
                        c=true;
                        break;
                    case -1:
                        c = false;
                        break;
                    default:
                        c = false;
                }
                if(c){
                    check[j] = list[j].getVersion(indeces[j].intValue());
                }else{
                    indeces[j] = indeces[j].multiply(new BigInteger(""+-1));
                    check[j] = list[j].getVersion(indeces[j].intValue());
                }
            }
            if(checkList(check)){
                caller.returnActionList(check);
            }
        }
        boolean checkInitial = true;
        int result = startingIndex.compareTo(endingIndex);
        switch(result){
            case 0:
                checkInitial = false;
                break;
            case 1:
                checkInitial = false;
                break;
            case -1:
                checkInitial = true;
                break;
        }
        for(BigInteger i = startingIndex; checkInitial&&count<maxCount;i=i.add(new BigInteger(""+step))){
            String returnedList = "";
            count++;
            boolean optCheck = false;
            int optResult = i.compareTo(optimalPoint);
            switch(optResult){
                case 0:
                    optCheck = true;
            }
            if(!optCheck){
                BigInteger passOn = i.add(new BigInteger(""+1));
                indeces = getCombinationBigInteger(passOn,list,0,indeces);
                Action[] check = new Action[list.length];
                for(int j = 0; j< list.length; j++){
                    boolean c ;
                    int r = indeces[j].compareTo(new BigInteger(""+0));
                    switch(r){
                        case 0:
                            c=true;
                            break;
                        case 1:
                            c=true;
                            break;
                        case -1:
                            c = false;
                            break;
                        default:
                            c = false;
                    }
                    if(c){
                        check[j] = list[j].getVersion(indeces[j].intValue());
                        returnedList+=check[j].name+","+Action.getTimeString(check[j].getWindowStart())+","+Action.getTimeString(check[j].getWindowEnd())+"\n";
                    }else{
                        indeces[j] = indeces[j].multiply(new BigInteger(""+-1));
                        check[j] = list[j].getVersion(indeces[j].intValue());
                        returnedList+=check[j].name+","+Action.getTimeString(check[j].getWindowStart())+","+Action.getTimeString(check[j].getWindowEnd())+"\n";
                    }

                }
                if(checkList(check)){
                    caller.returnActionList(check);
                }
            }
            result = i.compareTo(endingIndex);
            switch(result){
                case 0:
                    checkInitial = false;
                    break;
                case 1:
                    checkInitial = false;
                    break;
                case -1:
                    checkInitial = true;
                    break;
            }
        }
        System.out.println("\n" + this.threadName + " exiting.");
    }
    public void start(){
        System.out.print("\n"+"Starting "+this.threadName+"\n");
        if(t==null){
            t = new Thread(this, this.threadName);
        }
        t.start();
    }

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

    public boolean checkList(Action[] list){
        boolean check = true;
        for(int i = 0; i<list.length&&check;i++){
            check = checkPosition(i, list[i], list);
        }
        return check;
    }
    //TODO convert get combination to BigInteger i instead of long i, and returns BigInteger[] instead of long[]
    public static long[] getCombination(long i, Action[] lists, int index, long[] indeces){
        indeces[index] = (i-1)%lists[index].versions.length;
        long passOn = (((i-1)-indeces[index])/lists[index].versions.length)+1;
        if(index+1<lists.length){
            indeces = getCombination(passOn, lists, index+1, indeces);
        }
        return indeces;
    }

    public static BigInteger[] getCombinationBigInteger(BigInteger i, Action[] lists, int index, BigInteger[] indeces){
        BigInteger iMinus = i.add(new BigInteger(""+-1));
        BigInteger[] result = iMinus.divideAndRemainder(new BigInteger(""+lists[index].versions.length));
        indeces[index] = result[1];
        BigInteger negativeResult = result[1].multiply(new BigInteger(""+-1));
        BigInteger firstHalf = iMinus.add(negativeResult);
        BigInteger secondHalf = firstHalf.divide(new BigInteger(""+lists[index].versions.length));
        BigInteger passOn = secondHalf.add(new BigInteger(""+1));
        if(index+1<lists.length){
            indeces = getCombinationBigInteger(passOn, lists, index+1, indeces);
        }
        return indeces;
    }
}
