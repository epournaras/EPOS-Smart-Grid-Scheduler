public class ParallelSortingRegularSampling {
	public class Root implements Runnable{
		public void run(){
			
		}
		public void start(){
			
		}
	}
	public class WorkerThread implements Runnable{
		public void run(){
			
		}
		public void start(){
			
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
