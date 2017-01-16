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
		public void run(){
			this.ratedSchedules = sort(this.ratedSchedules);
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
			
			this.sortSamplePoints();
			while(phaseThree!=true){
				try {
					wait(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
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
			this.assembleSortedParts();
		}
		
		//Phase II: Requires Lock.
		@SuppressWarnings("finally")
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
					}else{
						return false;
					}
				}
			}catch(IllegalMonitorStateException e){
				return false;
			}
			catch(ArrayIndexOutOfBoundsException e){
				return false;
			}
		}
		
		//Phase III:
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
		
		public RatedSchedule[] getPivotPoints(){
			return this.pivotPoints;
		}
		
		//Phase IV: requires lock
		@SuppressWarnings("finally")
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
					}else{
						return false;
					}
				}
			}catch(IllegalMonitorStateException e){
				return false;
			}
			catch(ArrayIndexOutOfBoundsException e){
				return false;
			}
			
		}
		 //Phase IV: Requires Lock
		@SuppressWarnings("finally")
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
					}else{
						return null;
					}
				}
			}catch(IllegalMonitorStateException e){
				return null;
			}
			catch(ArrayIndexOutOfBoundsException e){
				return null;
			}
		}
		
		//Phase V: Requires Lock
		@SuppressWarnings("finally")
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
					}else{
						return false;
					}
				}
			}catch(IllegalMonitorStateException e){
				return false;
			}
			catch(ArrayIndexOutOfBoundsException e){
				return false;
			}
		}
		
		//Phase VI:
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
