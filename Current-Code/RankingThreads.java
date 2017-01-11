public class RankingThreads implements Runnable{
	public Thread t;
	private String threadName;
	private Schedule caller;
	private int passedScheduleIndex;
	private Action[] array;
	
	public RankingThreads(String name, Schedule caller, int passedScheduleIndex, Action[] array){
		this.threadName = name;
		this.caller = caller;
		this.passedScheduleIndex = passedScheduleIndex;
		this.array = array;
	}
	
	public void run(){
		double rating = getScheduleRating(array);
		boolean added = false;
		while(!added){
			added = returnTotal(rating);
		}
	}
	
	public void start(){
		System.out.print("Starting "+this.threadName+"\n");
		if(t==null){
			t = new Thread(this, this.threadName);
		}
		t.start();
	}
	
	public double getScheduleRating(Action[] schedule){
		int total =0;
		for(int i  = 0; i < schedule.length;i++){
			int rating = schedule[i].windowStart-schedule[i].optimalTime;
			if(rating<0){
				rating = (-1)*(rating);
			}
			total+=rating;
		}
		return total;
	}
	
	private boolean returnTotal(double a){
		return this.caller.returnRating(this.passedScheduleIndex,a);
	}
}
