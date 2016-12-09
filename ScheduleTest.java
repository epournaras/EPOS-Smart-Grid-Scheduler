public class ScheduleApp {
	public static void main(String[] args){
		char[] windowStart = {'1','2',':','0','0'};
		char[] windowEnd = {'1','2',':','3','0'};
		int windowStartHour = (Character.getNumericValue(windowStart[0])*10)+Character.getNumericValue(windowStart[1]);
        int windowStartMinute = (Character.getNumericValue(windowStart[3])*10)+Character.getNumericValue(windowStart[4]);
        int windowEndHour = (Character.getNumericValue(windowEnd[0])*10)+Character.getNumericValue(windowEnd[1]);
        int windowEndMinute = (Character.getNumericValue(windowEnd[3])*10)+Character.getNumericValue(windowEnd[4]);
		int windowEndTotal = windowEndHour*60+windowEndMinute;
		int windowStartTotal = windowStartHour*60+windowStartMinute;
		
		Action testOne = new Action("One", windowStartTotal, windowEndTotal, 30, 0.65);
		
		windowStart[1] = '1'; windowStart[3] = '3';
		windowEnd[1] = '2'; windowEnd[3] = '1';windowEnd[4] = '5'; 
		windowStartHour = (Character.getNumericValue(windowStart[0])*10)+Character.getNumericValue(windowStart[1]);
        windowStartMinute = (Character.getNumericValue(windowStart[3])*10)+Character.getNumericValue(windowStart[4]);
        windowEndHour = (Character.getNumericValue(windowEnd[0])*10)+Character.getNumericValue(windowEnd[1]);
        windowEndMinute = (Character.getNumericValue(windowEnd[3])*10)+Character.getNumericValue(windowEnd[4]);
		windowEndTotal = windowEndHour*60+windowEndMinute;
		windowStartTotal = windowStartHour*60+windowStartMinute;
		
		Action testTwo = new Action("Two", windowStartTotal,windowEndTotal, 45, 0.5);
		
		windowStart[1] = '4'; windowStart[3] = '0';
		windowEnd[1] = '4'; windowEnd[3] = '4';windowEnd[4] = '5'; 
		windowStartHour = (Character.getNumericValue(windowStart[0])*10)+Character.getNumericValue(windowStart[1]);
        windowStartMinute = (Character.getNumericValue(windowStart[3])*10)+Character.getNumericValue(windowStart[4]);
        windowEndHour = (Character.getNumericValue(windowEnd[0])*10)+Character.getNumericValue(windowEnd[1]);
        windowEndMinute = (Character.getNumericValue(windowEnd[3])*10)+Character.getNumericValue(windowEnd[4]);
		windowEndTotal = windowEndHour*60+windowEndMinute;
		windowStartTotal = windowStartHour*60+windowStartMinute;
		
		Action testThree = new Action("Three",windowStartTotal,windowEndTotal, 45, 0.5);
		
		windowStart[1] = '4'; windowStart[3] = '1';
		windowEnd[1] = '4'; windowEnd[3] = '2';windowEnd[4] = '5'; 
		windowStartHour = (Character.getNumericValue(windowStart[0])*10)+Character.getNumericValue(windowStart[1]);
        windowStartMinute = (Character.getNumericValue(windowStart[3])*10)+Character.getNumericValue(windowStart[4]);
        windowEndHour = (Character.getNumericValue(windowEnd[0])*10)+Character.getNumericValue(windowEnd[1]);
        windowEndMinute = (Character.getNumericValue(windowEnd[3])*10)+Character.getNumericValue(windowEnd[4]);
		windowEndTotal = windowEndHour*60+windowEndMinute;
		windowStartTotal = windowStartHour*60+windowStartMinute;
		
		Action testFour = new Action("Four", windowStartTotal,windowEndTotal, 15, 0.5);
		
		Action[] list = {testOne,testTwo,testThree, testFour};
		
		Schedule testSchedule = new Schedule();
		
		
		if(testSchedule.checkPosition(4, testFour, list)){
			System.out.print("Conflict \n");
		}else{
			System.out.print("No conflict \n");
		}
	}
}
