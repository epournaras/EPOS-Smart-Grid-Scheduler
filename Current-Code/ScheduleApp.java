public class ScheduleApp {
	public static void main(String[] args){		
		
		Schedule testSchedule = new Schedule();

		int n = 7;
		for(int i = 0; i < n; i++){
			Action temp = new Action();
			int timeOne = 0 + (int)(Math.random() * ((1199 - 0) + 1));	
			int timeTwo = 0 + (int)(Math.random() * ((1340 - 0) + 1));
			int timeThree = 15 + (int)(Math.random() * ((120 - 15) + 1));
		  
			int minTime;
			int maxTime;
			if(timeOne>timeTwo){	
		  		minTime = timeTwo;
		  		maxTime = timeOne;
			}else{
		  		minTime = timeOne;
		  		maxTime = timeTwo;
			}
			if(maxTime-minTime<timeThree){
		  		maxTime = minTime+timeThree;
			}
			int timeFour = minTime + (int)(Math.random() * (((maxTime - timeThree)- minTime) + 1));
			String maxTimeString = temp.getTimeString(maxTime);
			String minTimeString = temp.getTimeString(minTime);
			String durationString = temp.getTimeString(timeThree);
			String optimalTimeString = temp.getTimeString(timeFour);
			System.out.print(minTimeString+" "+maxTimeString+" "+durationString+"\n");
			temp = new Action("Action "+i, minTimeString, maxTimeString, durationString, optimalTimeString, 0);
			testSchedule.add(cloneAction(temp));
		}
		testSchedule.setSchedulesToCreate(n*100000);
		testSchedule.initialiseActionList();
		testSchedule.makeScheduleList();
	}
	public static Action cloneAction(Action a){
		Action temp = new Action(a);
		return temp;
	}
}
