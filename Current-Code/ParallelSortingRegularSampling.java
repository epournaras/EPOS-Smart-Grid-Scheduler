import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class ParallelSortingRegularSampling {
	public ArrayList<RatedSchedule[]> samplePointsArrayList = new ArrayList<RatedSchedule[]>();
	public int workersReported = 0;
	public boolean phaseOne = false;
	public boolean phaseTwo = false;
	public boolean phaseThree= false;
	public boolean phaseFour = false;
	public boolean phaseFive = false;
	public boolean phaseSix = false;
	
	private ReentrantLock phaseTwoLock = new ReentrantLock();
	private ReentrantLock phaseFourLockPartOne = new ReentrantLock();
	private ReentrantLock phaseFourLockPartTwo = new ReentrantLock();
	private ReentrantLock phaseFiveLock = new ReentrantLock();
	/* 
	 * PHASE I:
	 * Split data into equal parts and distribute to workerThreads and root. All threads then sort their local data.
	 * PHASE II:
	 * get sample points at 0, n/p^2,2n/P^2,3n/P^2...(P-1)(n/p^2)
	 * Where n is the size of the set, and P is the number of threads. (including root)
	 * PHASE III:
	 * Gather the sample points, sort them. Choose from these p^2 points p-1 pivot points and broadcast them to the other threads.
	 * PHASE IV:
	 * Other threads take their local data and separate it into p parts using the pivot values.
	 * PHASE V:
	 * Thread i gathers the i-th part of every other threads local data and sorts it, including the i-th part of its own data only.
	 * PHASE VI:
	 * Root collects all sorted local data from all other threads and assembles it in order
	 */
	public ParallelSortingRegularSampling(RatedSchedule[] data, int numberOfCores, Schedule caller){
		//PHASE I:
		int dataSize = data.length;
		ArrayList<ArrayList<RatedSchedule>> splitData = new ArrayList<ArrayList<RatedSchedule>>();
		splitData.ensureCapacity(numberOfCores);
		for(int i = 0; i<numberOfCores;i++){
			splitData.get(i).ensureCapacity(dataSize/numberOfCores);
		}
		int index = 0;
		for(int j = 0; j<numberOfCores;j++){
			for(int i = 0; i<dataSize/numberOfCores; i++){
				splitData.get(j).add(data[index]);
				index++;
			}
			splitData.get(j).trimToSize();
		}
		splitData.trimToSize();
		RatedSchedule[][] splitDataArray = new RatedSchedule[splitData.size()][];
		for(int i = 0; i<splitDataArray.length; i++){
			splitDataArray[i] = new RatedSchedule[splitData.get(i).size()];
			splitData.get(i).toArray(splitDataArray[i]);
		}
		
		Root root = new Root(splitDataArray[0], numberOfCores-1, "Root", caller);
		ArrayList<WorkerThread> workers = new ArrayList<WorkerThread>();
		for(int i = 1; i<numberOfCores;i++){
			workers.add(new WorkerThread(splitDataArray[i], root, numberOfCores-1, "Worker "+i, i));
		}
		workers.trimToSize();
		
		root.start();
		for(int i = 0;i<workers.size();i++){
			workers.get(i).start();
		}
		for(int i = 0; i<workers.size();i++){
			try{
				workers.get(i).t.join();
			}catch(InterruptedException e){
				System.out.print("Worker"+i+" interrupted\n");
			}
		}
		try{
			root.t.join();
		}catch(InterruptedException e){
			System.out.print("Root interrupted\n");
		}
	}
	
	//The Root is the master thread, used for gathering and redistributing data at different phases. It also sorts its own data as if it were a worker thread as well.
	public class Root implements Runnable{
		public RatedSchedule[] ratedSchedules;
		public int numberOfWorkers;
		public String threadName;
		public Thread t;
		private RatedSchedule[] allSamplePoints;
		public int dataSize;
		public RatedSchedule[] pivotPoints;
		public ArrayList<ArrayList<RatedSchedule>> assembledParts;
		public ArrayList<RatedSchedule[]> sortedParts;
		public Schedule caller;
		
		
		public Root(RatedSchedule[] a, int i, String n, Schedule caller){
			this.ratedSchedules = a;
			this.numberOfWorkers = i;
			this.threadName = n;
			this.dataSize = a.length;
			this.assembledParts.ensureCapacity(this.numberOfWorkers);
			this.sortedParts.ensureCapacity(this.numberOfWorkers);
			this.caller = caller;
		}
		
		public void run(){
			//PHASE I:
			this.ratedSchedules = sort(this.ratedSchedules);
			
			//PHASE II:
			RatedSchedule[] samplePoints = new RatedSchedule[this.numberOfWorkers];
			for(int i = 0; i<this.numberOfWorkers;i++){
				int index = (i*this.ratedSchedules.length)/(this.numberOfWorkers*this.numberOfWorkers);
				samplePoints[i] = this.ratedSchedules[index];
			}
			this.gatherSamplePoints(samplePoints);
			while(phaseOne!=true){
				try {
					wait(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//PHASE III:
			this.sortSamplePoints();
			while(phaseThree!=true){
				try {
					wait(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//PHASE IV:
			ArrayList<ArrayList<RatedSchedule>> parts = new ArrayList<ArrayList<RatedSchedule>>();
			parts.ensureCapacity(this.numberOfWorkers);
			int index = 0;
			for(int i = 0; i<this.ratedSchedules.length&&index<this.numberOfWorkers;i++){
				parts.get(index).add(this.ratedSchedules[i]);
				if(this.ratedSchedules[i].rating > this.pivotPoints[index].rating){
					index++;
				}
			}
			parts.trimToSize();
			RatedSchedule[][] seperatedParts = new RatedSchedule[parts.size()][];
			parts.toArray(seperatedParts);
			boolean partsPassed = false;
			while(!partsPassed){
				partsPassed = this.sendParts(seperatedParts);
			}
			
			while(phaseFive!=true){
				try {
					wait(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//PHASE V:
			do{
				this.ratedSchedules = this.getPart(0);
			}while(this.ratedSchedules==null);
			
			this.ratedSchedules = sort(this.ratedSchedules);
			boolean sortedPartAdded = false;
			while(!sortedPartAdded){
				sortedPartAdded = this.receiveSortedPart(this.ratedSchedules, 0);
			}
			
			while(phaseSix!=true){
				try {
					wait(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//PHASE VI:
			this.assembleSortedParts();
		}
		
		/*
		 * Phase II: Requires Lock.
		 * The workers use this method to pass back the sample points they have taken from their sorted local data. 
		 */
		public boolean gatherSamplePoints(RatedSchedule[] samplePoints){
			boolean lockAcquired = false;
			try{
				phaseTwoLock.tryLock();
				try{
					lockAcquired = true;
					samplePointsArrayList.add(samplePoints);
					workersReported++;
					if(workersReported==this.numberOfWorkers+1){
						phaseOne = true;
						workersReported = 0;
					}
				}finally{
					if(lockAcquired){
						phaseTwoLock.unlock();
						return true;
					}
				}
			}catch(IllegalMonitorStateException e){
				return false;
			}
			catch(ArrayIndexOutOfBoundsException e){
				return false;
			}
			return false;
		}
		
		/*
		 * Phase III:
		 * Now that all sample points have been gathered, the root sorts them by size as well. 
		 * This is an attempt to find a truer median as well as a method to find pivot points for further sorting.
		 */
		public void sortSamplePoints(){
			RatedSchedule[][] temp = new RatedSchedule[samplePointsArrayList.size()][];
			samplePointsArrayList.toArray(temp);
			this.allSamplePoints = new RatedSchedule[this.numberOfWorkers*this.numberOfWorkers];
			int startingIndex = 0;
			for(int i = 0; i<temp.length;i++){
				System.arraycopy(temp[i], 0, this.allSamplePoints, startingIndex, temp[i].length);
				startingIndex+=temp[i].length;
			}
			this.allSamplePoints = sort(this.allSamplePoints);
			phaseTwo = true;
			makePivotValues();
		}
		/*
		 * The Root uses this method to get pivot points at regular intervals from the sorted sample points
		 */
		private void makePivotValues(){
			int size = allSamplePoints.length;
			this.pivotPoints = new RatedSchedule[numberOfWorkers];
			int counter = 0;
			for(int i = size/numberOfWorkers; i < size; i+=size/numberOfWorkers){
				this.pivotPoints[counter] = allSamplePoints[i];
				counter++;
			}
			phaseThree = true;
		}
		
		/*
		 * Used by worker threads to contact the root to get their pivot points.
		 */
		public RatedSchedule[] getPivotPoints(){
			return this.pivotPoints;
		}
		
		/*
		 * Phase IV: requires lock
		 * The Worker threads use this method to send the i parts of their local data to the Root, so that it may gather them and redistribute them to the i-th worker
		 */
		public boolean sendParts(RatedSchedule[][] parts){
			boolean lockAcquired = false;
			try{
				phaseFourLockPartOne.tryLock();
				try{
					lockAcquired = true;
					workersReported++;
					for(int i = 0; i < parts.length; i++){
						for(int j = 0; j< parts[i].length;j++){
							this.assembledParts.get(i).add(parts[i][j]);
						}
					}
					if(workersReported == numberOfWorkers+1){
						workersReported = 0;
						phaseFive = true;
					}
				}finally{
					if(lockAcquired){
						phaseFourLockPartOne.unlock();
						return true;
					}
				}
			}catch(IllegalMonitorStateException e){
				return false;
			}
			catch(ArrayIndexOutOfBoundsException e){
				return false;
			}
			return false;
		}
		 /*
		  * Phase IV: Requires Lock
		  * Used by all threads to get their i-th part from the assembled parts.
		  */
		public RatedSchedule[] getPart(int i){
			boolean lockAcquired = false;
			RatedSchedule[] part = new RatedSchedule[1];
			try{
				phaseFourLockPartTwo.tryLock();
				try{
					lockAcquired = true;
					this.assembledParts.get(i).trimToSize();
					part = new RatedSchedule[this.assembledParts.get(i).size()];
					this.assembledParts.get(i).toArray(part);
				}finally{
					if(lockAcquired){
						phaseFourLockPartTwo.unlock();
						return part;
					}
				}
			}catch(IllegalMonitorStateException e){
				return null;
			}
			catch(ArrayIndexOutOfBoundsException e){
				return null;
			}
			return null;
		}
		
		/*
		 * Phase V: Requires Lock
		 * Once the i-th worker has sorted the i-th part, it returns it to the root via this method.
		 */
		public boolean receiveSortedPart(RatedSchedule[] a, int i){
			boolean lockAcquired = false;
			try{
				phaseFiveLock.tryLock();
				try{
					lockAcquired = true;
					this.sortedParts.add(i, a);
					workersReported++;
					if(this.numberOfWorkers+1 == workersReported){
						phaseSix = true;
					}
				}finally{
					if(lockAcquired){
						phaseFiveLock.unlock();
						return true;
					}
				}
			}catch(IllegalMonitorStateException e){
				return false;
			}
			catch(ArrayIndexOutOfBoundsException e){
				return false;
			}
			return false;
		}
		
		/*
		 * Phase VI:
		 * Once the Root has all parts, it will assemble them in order as one RatedSchedule array and send it back to its schedule caller.
		 */
		public void assembleSortedParts(){
			this.sortedParts.trimToSize();
			RatedSchedule[][] sortedPartsArray = new RatedSchedule[this.sortedParts.size()][];
			this.sortedParts.toArray(sortedPartsArray);
			RatedSchedule[] assembledSortedParts = new RatedSchedule[this.dataSize];
			int index = 0;
			for(int i = 0; i<sortedPartsArray.length;i++){
				for(int j = 0 ; j<sortedPartsArray[i].length;j++){
					assembledSortedParts[index] = sortedPartsArray[i][j];
					index++;
				}
			}
			this.caller.receiveSortedRatedSchedules(assembledSortedParts);
		}
		
		public void start(){
			System.out.print("Starting "+this.threadName+"\n");
			if(this.t==null){
				this.t = new Thread(this, this.threadName);
			}
			this.t.start();
		}
	}
	
	/* The Worker thread receives data and sorts it using quicksort. It then passes data to its root, gets back new data and sorts that too. 
	 * This process repeats until the Root stops giving back data (Phase 6)
	 */
	public class WorkerThread implements Runnable{
		public RatedSchedule[] ratedSchedules;
		public Root root;
		public String threadName;
		private Thread t;
		private int numberOfWorkers;
		private RatedSchedule[] pivotPoints;
		private int workerID;
		
		
		public WorkerThread(RatedSchedule[] a, Root father, int g, String n, int w){
			this.ratedSchedules = a;
			this.root = father;
			this.threadName = n;
			this.numberOfWorkers = g;
			this.workerID = w;
		}
		
		public void run(){
			//Phase I:
			this.ratedSchedules = sort(this.ratedSchedules);
			
			//Phase II
			RatedSchedule[] samplePoints = new RatedSchedule[this.numberOfWorkers];
			for(int i = 0; i<this.numberOfWorkers;i++){
				int index = (i*this.ratedSchedules.length)/(this.numberOfWorkers*this.numberOfWorkers);
				samplePoints[i] = this.ratedSchedules[index];
			}
			boolean pointsPassed = false;
			while(!pointsPassed){
				pointsPassed=this.root.gatherSamplePoints(samplePoints);
			}
			
			while(phaseThree==false){
				try{
					wait(10);
				}catch(InterruptedException e){
					
				}
			}
			
			//Phase III:
			this.pivotPoints = this.root.getPivotPoints();
			
			//Phase IV:
			ArrayList<ArrayList<RatedSchedule>> parts = new ArrayList<ArrayList<RatedSchedule>>();
			parts.ensureCapacity(this.numberOfWorkers);
			int index = 0;
			for(int i = 0; i<this.ratedSchedules.length&&index<this.numberOfWorkers;i++){
				parts.get(index).add(this.ratedSchedules[i]);
				if(this.ratedSchedules[i].rating > this.pivotPoints[index].rating){
					index++;
				}
			}
			parts.trimToSize();
			RatedSchedule[][] seperatedParts = new RatedSchedule[parts.size()][];
			parts.toArray(seperatedParts);
			boolean partsPassed = false;
			while(!partsPassed){
				partsPassed = this.root.sendParts(seperatedParts);
			}
			while(phaseFive == false){
				try{
					wait(10);
				}catch(InterruptedException e){
					
				}
			}
			
			//Phase V:
			do{
				this.ratedSchedules = this.root.getPart(this.workerID);
			}while(this.ratedSchedules==null);
			this.ratedSchedules = sort(this.ratedSchedules);
			
			//Phase VI:
			sendSortedPart(this.ratedSchedules, this.workerID);
		}
		
		/*
		 * Send back the i-th part that was given to this i-th thread, now sorted, to the root for assembly into the final sorted array.
		 */
		public void sendSortedPart(RatedSchedule[] s, int w){
			boolean sent = false;
			while(!sent){
				sent=this.root.receiveSortedPart(s,w);
			}
		}
		
		public void start(){
			System.out.print("Starting "+this.threadName+"\n");
			if(this.t==null){
				this.t = new Thread(this, this.threadName);
			}
			this.t.start();
		}
	}
	
	/*
	 * Quicksort
	 */
	public RatedSchedule[] sort(RatedSchedule[] a){
		RatedSchedule[] concatonatedSortedSchedules = new RatedSchedule[a.length];
		if(a.length>1){
			RatedSchedule pivot = a[0];
			RatedSchedule[] tempHigher = new RatedSchedule[a.length];
			RatedSchedule[] tempLower = new RatedSchedule[a.length];
			
			int higherIndex = 0;
			int lowerIndex = 0;
			for(int i = 1;i<a.length;i++){
				if(a[i].rating>pivot.rating){
					tempHigher[higherIndex] = a[i];
					higherIndex++;
				}else{
					tempLower[lowerIndex] = a[i];
					lowerIndex++;
				}
			}
			
			RatedSchedule[] higher = new RatedSchedule[higherIndex];
			RatedSchedule[] lower = new RatedSchedule[lowerIndex];
			for(int i = 0; i<higherIndex;i++){
				higher[i] = tempHigher[i];
			}
			for(int i = 0; i<lowerIndex; i++){
				lower[i] = tempLower[i];
			}
			
			RatedSchedule[] sortedHigher = sort(higher);
			RatedSchedule[] sortedLower = sort(lower);
			
			System.arraycopy(sortedLower, 0, concatonatedSortedSchedules, 0, sortedLower.length);
			concatonatedSortedSchedules[sortedLower.length] = pivot;
			System.arraycopy(sortedHigher, 0, concatonatedSortedSchedules, sortedLower.length+1, sortedHigher.length);
			
		}else{
			concatonatedSortedSchedules = a;
		}
		return concatonatedSortedSchedules;
	}
}
