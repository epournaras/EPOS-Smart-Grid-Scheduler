public class ScheduleApp {
	public static void main(String[] args){		
		
		Schedule testSchedule = new Schedule();

		int n = 7;
		for(int i = 0; i < n; i++){
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
		  String maxTimeString;
		  String minTimeString;
		  String durationString;
		  int hour = maxTime/60;
		  int minute = maxTime%60;
		  if(hour<10){
		  	if(minute<10){
		  			maxTimeString = "0"+hour+":"+"0"+minute;
		 	}else{
		  			maxTimeString = "0"+hour+":"+minute;
		  	}
		  }else{
		  	if(minute<10){
		  			maxTimeString = hour+":"+"0"+minute;
		  	}else{
		  			maxTimeString = hour+":"+minute;
		  	}
		  }
		  hour = minTime/60;
		  minute = minTime%60;
		  if(hour<10){
		  	if(minute<10){
		  			minTimeString = "0"+hour+":"+"0"+minute;
		 	}else{
		 			minTimeString = "0"+hour+":"+minute;
		 	}
		  }else{
		  	if(minute<10){
		  			minTimeString = hour+":"+"0"+minute;
		  	}else{
		  			minTimeString = hour+":"+minute;
		  	}
		  }
		  hour = timeThree/60;
		  minute = timeThree%60;
		  if(hour<10){
		  	if(minute<10){
		  			durationString = "0"+hour+":"+"0"+minute;
		 	}else{
		 			durationString = "0"+hour+":"+minute;
		 	}
		  }else{
		  	if(minute<10){
		  			durationString = hour+":"+"0"+minute;
		  	}else{
		  			durationString = hour+":"+minute;
		  	}
		  }
		  System.out.print(minTimeString+" "+maxTimeString+" "+durationString+"\n");
		  Action temp = new Action("Action "+i, minTimeString, maxTimeString, durationString, 0);
		  testSchedule.add(cloneAction(temp));
		}
		
		testSchedule.initialiseActionList();
		testSchedule.makeScheduleList();
	}
	public static Action cloneAction(Action a){
		Action temp = new Action(a);
		return temp;
	}
}
