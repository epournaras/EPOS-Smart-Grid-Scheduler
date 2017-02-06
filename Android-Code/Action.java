package com.example.schedulelibrary;
public class Action {
    public String name;
    public int windowStart;
    public int windowEnd;
    public int duration;
    public int optimalTime;
    public double shiftingFactor;

    public Action(){
		/*
		 * allows the use of built in methods like getTimeString() or getIntTime()
		 */
    }

    //cloning method
    public Action(Action a){
        this.name = a.name;
        this.windowStart = a.windowStart;
        this.windowEnd = a.windowEnd;
        this.duration = a.duration;
        this.optimalTime = a.optimalTime;
        this.shiftingFactor = a.shiftingFactor;
    }

    //Instantiation method.
    public Action(String name, int a, int b, int c, int d, double f){
        try{
            if(a>b||a+c>b){
                throw new RuntimeException();
            }else{
                this.name = name;
                this.windowStart = a;
                this.windowEnd = b;
                this.duration = c;
                this.optimalTime = d;
                this.shiftingFactor = f;
            }
        }catch(RuntimeException e){
            System.err.print("Bad input \n");
        }
    }
    public Action(String name, String start, String end, String duration, String optimalTime, double shiftingFactor){
        int a = getIntTime(start);
        int b = getIntTime(end);
        int c = getIntTime(duration);
        int d = getIntTime(optimalTime);
        try{
            if(a>b||a+c>b){
                throw new RuntimeException();
            }else{
                this.name = name;
                this.windowStart = a;
                this.windowEnd = b;
                this.duration = c;
                this.optimalTime = d;
                this.shiftingFactor = shiftingFactor;
            }
        }catch(RuntimeException e){
            System.err.print("Bad input \n");
        }
    }

    public String getTimeString(int a){
        int hour = a/60;
        int minute = a%60;
        String result;
        if(hour<10){
            result = "0"+hour+":";
        }else{
            result = hour+":";
        }
        if(minute<10){
            result+="0"+minute;
        }else{
            result+=minute;
        }
        return result;
    }

    public String getOptimalTime(){
        return getTimeString(this.optimalTime);
    }

    public static int getIntTime(String a){
        int time;
        char[] timeCharArray = a.toCharArray();
        int hour = (Character.getNumericValue(timeCharArray[0])*10)+Character.getNumericValue(timeCharArray[1]);
        int minute = (Character.getNumericValue(timeCharArray[3])*10)+Character.getNumericValue(timeCharArray[4]);
        time=hour*60+minute;
        return time;
    }

    public String[] getTimes(){
        String[] times = {getTimeString(this.windowStart), getTimeString(this.windowEnd), getTimeString(this.duration)};
        return times;
    }

    public int getWindowStart(){
        return this.windowStart;
    }

    public int getWindowEnd(){
        return this.windowEnd;
    }
}
