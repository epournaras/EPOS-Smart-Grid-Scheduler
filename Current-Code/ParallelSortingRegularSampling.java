import java.util.ArrayList;

public class ParallelSortingRegularSampling {
	
	public int workersReported = 0;
	public boolean phaseOne = false;
	public boolean phaseTwo = false;
	public boolean phaseThree= false;
	public boolean phaseFour = false;
	public boolean phaseFourPartTwo = false;
	public boolean phaseFive = false;
	public boolean phaseSix = false;
	public int totalDataSize;
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
		totalDataSize = data.length;
		ArrayList<ArrayList<RatedSchedule>> splitData = new ArrayList<ArrayList<RatedSchedule>>();
		splitData.ensureCapacity(numberOfCores);
		int size = (totalDataSize/numberOfCores);
		for(int i = 0; i<numberOfCores;i++){
			splitData.add(new ArrayList<RatedSchedule>());
			splitData.get(i).ensureCapacity(size);
		}
		int index = 0;
		for(int j = 0; j<numberOfCores;j++){
			for(int i = 0; i<totalDataSize/numberOfCores; i++){
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
		public RatedSchedule[][][] assembledParts;
		private RatedSchedule[][] seperatedParts;
		public RatedSchedule[][] sortedParts;
		public RatedSchedule[][] samplePoints;
		public Schedule caller;
		
		
		public Root(RatedSchedule[] a, int i, String n, Schedule caller){
			this.ratedSchedules = a;
			this.numberOfWorkers = i;
			this.threadName = n;
			this.dataSize = a.length;
			this.assembledParts = new RatedSchedule[this.numberOfWorkers+1][this.numberOfWorkers+1][];
			this.samplePoints = new RatedSchedule[this.numberOfWorkers+1][this.numberOfWorkers];
			this.sortedParts = new RatedSchedule[this.numberOfWorkers+1][];
			this.caller = caller;
		}
		
		public void run(){
			//System.out.print("\n"+"Running "+threadName+"...\n");
			//PHASE I:
			this.ratedSchedules = sort(this.ratedSchedules);
			//PHASE II:
			RatedSchedule[] samplePoints = createSamplePoints(this.numberOfWorkers, this.ratedSchedules);
			this.gatherSamplePoints(samplePoints, 0);
			while(phaseOne!=true){
				try {
					wait(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}catch(IllegalMonitorStateException e){
					
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
			RatedSchedule[][] phaseFourSplitting = new RatedSchedule[numberOfWorkers+1][];
			RatedSchedule[][] temp = new RatedSchedule[2][];
			temp[1] = this.ratedSchedules;
			for(int i = 0; i < this.pivotPoints.length ;i++){
				temp = split(temp[1], this.pivotPoints[i].rating);
				phaseFourSplitting[i] = new RatedSchedule[temp[0].length];
				System.arraycopy(temp[0], 0, phaseFourSplitting[i], 0, temp[0].length);
			}
			phaseFourSplitting[numberOfWorkers] = temp[1];
			boolean partsPassed = false;
			do{
				partsPassed = this.sendParts(phaseFourSplitting, 0);
			}while(partsPassed==false);
			
			while(phaseFourPartTwo!=true){
				if(workersReported == this.numberOfWorkers+1){
					phaseFourPartTwo =true;
				}
				try {
					wait(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}catch (IllegalMonitorStateException e){
					
				}
			}
			seperatedParts = this.createParts();
			phaseFive = true;
			
			//PHASE V:
			this.ratedSchedules = this.getPart(0);
			
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
				}catch (IllegalMonitorStateException e){
					
				}
			}
			
			
			//PHASE VI:
			this.assembleSortedParts();
			//System.out.println("\n" + this.threadName + " exiting.");
		}
		
		/*
		 * Phase II: Requires Lock.
		 * The workers use this method to pass back the sample points they have taken from their sorted local data. 
		 */
		public synchronized void gatherSamplePoints(RatedSchedule[] newSamplePoints, int id){
			this.samplePoints[id] = new RatedSchedule[newSamplePoints.length];
			System.arraycopy(newSamplePoints, 0, this.samplePoints[id], 0, newSamplePoints.length);
			workersReported++;
			if(workersReported==this.numberOfWorkers+1){
				workersReported = 0;
				phaseOne = true;
			}
		}
		
		/*
		 * Phase III:
		 * Now that all sample points have been gathered, the root sorts them by size as well. 
		 * This is an attempt to find a truer median as well as a method to find pivot points for further sorting.
		 */
		public void sortSamplePoints(){
			this.allSamplePoints = new RatedSchedule[(this.numberOfWorkers+1)*(this.numberOfWorkers+1)];
			int startingIndex = 0;
			for(int i = 0; i<this.samplePoints.length;i++){
				for(int j = 0; j<this.samplePoints[i].length;j++){
					if(this.samplePoints[i][j]!=null){
						this.allSamplePoints[startingIndex] = this.samplePoints[i][j];
						startingIndex++;
					}
				}
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
			this.pivotPoints = new RatedSchedule[this.numberOfWorkers];
			int counter = 0;
			int i = size/(this.numberOfWorkers+1);
			while( i < size ){
				this.pivotPoints[counter] = allSamplePoints[i];
				counter++;
				i+=size/(this.numberOfWorkers+1);
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
		public synchronized boolean sendParts(RatedSchedule[][] parts, int id){
			workersReported++;
			int i = 0; 
			while(i < parts.length){
				this.assembledParts[id][i] = new RatedSchedule[parts[i].length];
				System.arraycopy(parts[i], 0, this.assembledParts[id][i], 0, parts[i].length);
				i++;
				
			}
			if(workersReported >= (this.numberOfWorkers+1)){
				workersReported = 0;
				phaseFourPartTwo = true;
			}
			return true;
		}
		
		private RatedSchedule[][] createParts(){
			RatedSchedule[][][] temp = new RatedSchedule[numberOfWorkers+1][numberOfWorkers+1][];
			int[] numberOfPartElements = new int[numberOfWorkers+1];
			for(int i = 0; i<this.assembledParts.length;i++){
				numberOfPartElements[i] =0;
				for(int j = 0; j<this.assembledParts[i].length;j++){
					temp[i][j] = this.assembledParts[j][i];
					numberOfPartElements[i]+=temp[i][j].length;
				}
			}
			RatedSchedule[][] parts = new RatedSchedule[numberOfWorkers+1][];
			int currentIndex = 0;
			for(int i = 0; i<numberOfWorkers+1;i++){
				parts[i] = new RatedSchedule[numberOfPartElements[i]];
				for(int j = 0; j<numberOfWorkers+1;j++){
					System.arraycopy(temp[i][j], 0, parts[i], currentIndex, temp[i][j].length);
					currentIndex+=temp[i][j].length;
				}
				currentIndex = 0;
			}
			return parts;
		}
		 /*
		  * Phase IV: Requires Lock
		  * Used by all threads to get their i-th part from the assembled parts.
		  */
		public synchronized RatedSchedule[] getPart(int i){
			return seperatedParts[i];
		}
		
		/*
		 * Phase V: Requires Lock
		 * Once the i-th worker has sorted the i-th part, it returns it to the root via this method.
		 */
		public synchronized boolean receiveSortedPart(RatedSchedule[] a, int i){
			this.sortedParts[i] = new RatedSchedule[a.length];
			System.arraycopy(a, 0, this.sortedParts[i], 0, a.length);
			workersReported++;
			if((this.numberOfWorkers+1) == workersReported){
				workersReported = 0;
				phaseSix = true;
			}
			return true;
		}
		
		/*
		 * Phase VI:
		 * Once the Root has all parts, it will assemble them in order as one RatedSchedule array and send it back to its schedule caller.
		 */
		public void assembleSortedParts(){
			RatedSchedule[] assembledSortedParts = new RatedSchedule[totalDataSize];
			int index = 0;
			for(int i = 0; i<this.sortedParts.length;i++){
				for(int j = 0 ; j<sortedParts[i].length;j++){
						assembledSortedParts[index] = this.sortedParts[i][j];
						index++;
				}
			}
			this.caller.receiveSortedRatedSchedules(assembledSortedParts);
		}
		
		public void start(){
			//System.out.print("\n"+"Starting "+this.threadName+"\n");
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
			//System.out.print("\n"+"Running "+threadName+"...\n");
			//Phase I:
			this.ratedSchedules = sort(this.ratedSchedules);
			
			//Phase II
			RatedSchedule[] samplePoints = createSamplePoints(this.numberOfWorkers, this.ratedSchedules);
			this.root.gatherSamplePoints(samplePoints, workerID);			
			while(phaseThree==false){
				try{
					wait(10);
				}catch(InterruptedException e){
					
				}catch(IllegalMonitorStateException e){
				
				}
			}
			
			//Phase III:
			this.pivotPoints = this.root.getPivotPoints();

			//Phase IV:
			RatedSchedule[][] phaseFourSplitting = new RatedSchedule[numberOfWorkers+1][];
			RatedSchedule[][] temp = new RatedSchedule[2][];
			temp[1] = this.ratedSchedules;
			for(int i = 0; i < this.pivotPoints.length;i++){
				temp = split(temp[1], this.pivotPoints[i].rating);
				phaseFourSplitting[i] = new RatedSchedule[temp[0].length];
				System.arraycopy(temp[0], 0, phaseFourSplitting[i], 0, temp[0].length);
			}
			phaseFourSplitting[numberOfWorkers] = temp[1];
			boolean partsPassed = false;
			do{
				partsPassed = this.root.sendParts(phaseFourSplitting, workerID);
			}while(partsPassed==false);	
			while(phaseFive == false){
				try{
					wait(10);
				}catch(InterruptedException e){
					
				}
				catch (IllegalMonitorStateException e){
					
				}
			}
			
			
			//Phase V:
			this.ratedSchedules = this.root.getPart(this.workerID);
			this.ratedSchedules = sort(this.ratedSchedules);
			
			//Phase VI:
			sendSortedPart(this.ratedSchedules, this.workerID);
			//System.out.println("\n" + this.threadName + " exiting.");
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
			//System.out.print("\n"+"Starting "+this.threadName+"\n");
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
			int j = 1;
			while(j<a.length){
				try{
					if(a[j].rating>pivot.rating){
						tempHigher[higherIndex] = a[j];
						higherIndex++;
					}else{
						tempLower[lowerIndex] = a[j];
						lowerIndex++;
					}
				}catch(NullPointerException e){
					System.out.print("\n"+"Nullpointer Exception at "+j+"\n");
				}
				j++;
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
	
	public RatedSchedule[][] split(RatedSchedule[] a, double b){
		int size = a.length;
		RatedSchedule[][] returnable = new RatedSchedule[2][];
		int sizeOfLower= 0;
		boolean splitFound = false;
		for(int i = 0;i<size&&splitFound==false;i++){
			if(a[i].rating<=b){
				sizeOfLower++;
			}else{
				splitFound = true;
			}
		}
		returnable[0] = new RatedSchedule[sizeOfLower];
		returnable[1] = new RatedSchedule[a.length-sizeOfLower];
		System.arraycopy(a, 0, returnable[0], 0, sizeOfLower);
		System.arraycopy(a, sizeOfLower, returnable[1], 0, a.length-sizeOfLower);
		return returnable;
	}
	
	public RatedSchedule[] createSamplePoints(int numberOfWorkers, RatedSchedule[] ratedSchedules){
		RatedSchedule[] samplePoints = new RatedSchedule[numberOfWorkers+1];
		int index = 0;
		for(int i = 0; i<numberOfWorkers+1;i++){
			samplePoints[i] = ratedSchedules[index];
			index +=(ratedSchedules.length/(numberOfWorkers+1));
			
		}
		return samplePoints;
	}
}

