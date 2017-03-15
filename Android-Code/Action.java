package com.example.schedulelibrary;
public class Action {
    public String name;
    public int windowStart;
    public int windowEnd;
    public int duration;
    public int optimalTime;

    public Action[] versions;
    private double rating;
    private int parentActionWindowStart;
    private int parentActionWindowEnd;
    private int optimalIndex;

    public Action(){
		/*
		 * allows the use of built in methods like getTimeString() or getIntTime()
		 */
    }
    //Instantiation method.
    public Action(String name, int a, int b, int c, int d){
        try{
            if(a>b||a+c>b){
                throw new RuntimeException();
            }else{
                this.name = name;
                this.windowStart = a;
                this.windowEnd = b;
                this.duration = c;
                this.optimalTime = d;
            }
        }catch(RuntimeException e){
            System.err.print("Bad input \n");
        }
    }
    public Action(String name, String start, String end, String duration, String optimalTime){
        int a = getIntTime(start);
        int b = getIntTime(end);
        int c = getIntTime(duration);
        int d = getIntTime(optimalTime);
        try{
            if(a>b||a+c>b){
                System.out.print("here");
                throw new RuntimeException();
            }else{
                this.name = name;
                this.windowStart = a;
                this.windowEnd = b;
                this.duration = c;
                this.optimalTime = d;
                this.createVersions();
            }
        }catch(RuntimeException e){
            System.err.print("Bad input\n");
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

    private void createVersions(){
        int windowSpace = (this.windowEnd-this.duration)-this.windowStart;

        this.versions = new Action[windowSpace];
        if(windowSpace == 0){
            this.versions = new Action[1];
            this.versions[0] = this;
        }else{
            for(int i = 0; i<windowSpace; i++){
                this.versions[i] = new Action(this.name,this.windowStart+i,this.duration+this.windowStart+i, this.duration,this.optimalTime);
                this.versions[i].setParentStats(this.windowStart, this.windowEnd);
                this.versions[i].setRating();
                if(this.windowStart+i == this.optimalTime){
                    this.optimalIndex = i;
                }
            }
        }
        System.out.print(this.name+" "+this.versions.length+"\n");
        //printSchedule(this.versions);
    }

    public void setParentStats(int a, int b){
        this.parentActionWindowStart = a;
        this.parentActionWindowEnd = b;
    }

    public void setRating(){
//    	int upperLimit = (this.parentActionWindowEnd-duration)-optimalTime;
//    	int lowerLimit = optimalTime-this.parentActionWindowStart;
//    	int furthestPoint;
//    	if(upperLimit <0){
//    		upperLimit*=(-1);
//    	}
//    	if(lowerLimit<0){
//    		lowerLimit*=(-1);
//    	}
//    	if(upperLimit>lowerLimit){
//    		furthestPoint = upperLimit;
//    	}else{
//    		furthestPoint = lowerLimit;
//    	}
//    	int difference = this.optimalTime-furthestPoint;
//    	if(difference<0){
//    		difference*=(-1);
//    	}
//    	int thisItemDifference = this.optimalTime-this.windowStart;
//    	if(thisItemDifference<0){
//    		thisItemDifference *=(-1);
//    	}
//    	if(difference>0){
//    		this.rating= thisItemDifference/difference;
//    	}else{
//    		this.rating=0;
//    	}
        this.rating = this.optimalTime - this.windowStart;
        if(this.rating<0){
            this.rating*=(-1);
        }
    }

    public double getRating(){
        return this.rating;
    }

    public Action getVersion(int i){
        return this.versions[i];
    }

    public int getWindowStart(){
        return this.windowStart;
    }

    public int getWindowEnd(){
        return this.windowEnd;
    }

    public void printSchedule(Action[] a){
        for (Action anA : a) {
            System.out.print(anA.name + ":\t" + anA.getTimeString(anA.windowStart) + "\t" + anA.getTimeString(anA.windowEnd) + "\t" + anA.getTimeString(anA.optimalTime) + "\n");
        }
    }

    public int getOptimalIndex(){
        return this.optimalIndex;
    }
}
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
