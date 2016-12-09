public class ThreadManager implements Runnable{
	private Thread t;
	private String threadName;
	private Action[] list;
	private Schedule caller;
	private int startingIndex;
	
	public ThreadManager(String name, Action[] list, Schedule caller, int startingIndex){
		System.out.print("Creating "+name+"\n");
		this.threadName = name;
		this.list = list;
		this.caller = caller;
		this.startingIndex = startingIndex;
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
		try{
			list = caller.getSchedule(list, startingIndex);
			if(list!=null){
				caller.returnActionList(caller.cloneActionArray(list));
			}
			int i = 1;
			while(i<list.length+1){
				list[list.length-i].windowStart++;
				list[list.length-i] = caller.changeWindow(list.length-i,list);
				if(list[list.length-i]!=null){
					if(i==1){
						caller.returnActionList(caller.cloneActionArray(list));
					}else{
						if(i>1){
							i--;
						}
					}
				}else{
					i++;
				}	
			}			
			list = caller.getNextList();
			Thread.sleep(50);
		}catch (InterruptedException e) {
			System.out.println("Thread " +  this.threadName + " interrupted.");
		}
		System.out.println("Thread " + this.threadName + " exiting.");
	}
	public void start(){
		System.out.print("Starting "+this.threadName+"\n");
		if(t==null){
			t = new Thread(this, this.threadName);
		}
	}
}
