
public class ThreadManager implements Runnable{
	public Thread t;
	private String threadName;
	private Action[] list;
	private Schedule caller;
	private int startingIndex;
	private Action[] spareList;
	private int schedulesToGet;
	
	public ThreadManager(String name, Action[] list, Schedule caller, int startingIndex){
		System.out.print("Creating "+name+"\n");
		this.threadName = name;
		this.list = list;
		this.caller = caller;
		this.startingIndex = startingIndex;
		this.schedulesToGet = 10000;
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
			int count = 0;
			System.out.print("Running "+threadName+"...\n");
			this.list = this.caller.getSchedule(this.list, startingIndex+1);
			if(this.list!=null){
				this.spareList = this.caller.cloneActionArray(this.list);
				this.caller.returnActionList(this.caller.cloneActionArray(this.list));
				boolean done = false;
				int i = this.list.length -1;
				while(!done&&count<=schedulesToGet){
					if(i>startingIndex){
						this.list[i] = this.caller.changeWindow(i, this.list);
						
						if(this.list[i]==null){
							this.list[i] = new Action(this.spareList[i]);
							i--;
							if(!done){
								if(i!=startingIndex){
									this.list[i].windowStart++;
								}
							}
						}else{
							if(i==this.list.length-1){
								this.caller.returnActionList(this.caller.cloneActionArray(this.list));
								i--;
								count++;
								if(!done){
									if(i!=startingIndex){
										this.list[i].windowStart++;
									}
								}
							}else{
								i++;
							}
						}
					}else{
						done = true;
					}
					
				}
				
			}			
			this.list = this.caller.getNextList();
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
		t.start();
	}
}
