public class ScheduleApp {
	public static void main(String[] args){		
		Action testOne = new Action("One", "08:30","11:15","00:45", 0.65);
		
		Action testTwo = new Action("Two", "13:05" ,"16:05","01:00", 0.5);
		
		Action testThree = new Action("Three","09:45","13:30","00:15" , 0.5);
		
		Action testFour = new Action("Four", "15:05","15:30","00:25" , 0.5);
		
		Action[] list = {testOne,testTwo,testThree, testFour};
		
		Schedule testSchedule = new Schedule(list);
		
		testSchedule.initialiseActionList();
		testSchedule.makeScheduleList();
	}
}
