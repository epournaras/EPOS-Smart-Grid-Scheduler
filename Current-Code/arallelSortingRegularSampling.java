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
	public RatedSchedules sort(RatedSchedules a){
		double[] sortedList = new double[a.ratings.length];
		RatedSchedules concatonatedSortedSchedules;
		if(a.ratings.length>1){
			double[] tempHigher = new double[a.ratings.length];
			double[] tempLower = new double[a.ratings.length];
			double pivot = a.ratings[0];
			Action[] pivotAction = a.schedules[0];
			Action[][] tempHigherActions = new Action[a.ratings.length][];
			Action[][] tempLowerActions = new Action[a.ratings.length][];
			
			int higherIndex = 0;
			int lowerIndex = 0;
			for(int i = 1;i<a.ratings.length;i++){
				if(a.ratings[i]>pivot){
					tempHigher[higherIndex] = a.ratings[i];
					tempHigherActions[higherIndex] = a.schedules[i];
					higherIndex++;
				}else{
					tempLower[lowerIndex] = a.ratings[i];
					tempLowerActions[lowerIndex] = a.schedules[i];
					lowerIndex++;
				}
			}
			
			double[] higher = new double[higherIndex];
			double[] lower = new double[lowerIndex];
			Action[][] higherActions = new Action[higherIndex][];
			Action[][] lowerActions = new Action[lowerIndex][];
			for(int i = 0; i<higherIndex;i++){
				higher[i] = tempHigher[i];
				higherActions[i] = tempHigherActions[i];
			}
			for(int i = 0; i<lowerIndex; i++){
				lower[i] = tempLower[i];
				lowerActions[i] = tempLowerActions[i];
			}
			
			RatedSchedules lowerSchedules = new RatedSchedules(lower, lowerActions);
			RatedSchedules higherSchedules = new RatedSchedules(higher, higherActions);
			
			higherSchedules = sort(higherSchedules);
			lowerSchedules = sort(lowerSchedules);
			
			System.arraycopy(lowerSchedules.ratings, 0, sortedList, 0, lowerSchedules.ratings.length);
			sortedList[lower.length] = pivot;
			System.arraycopy(higherSchedules.ratings, 0, sortedList, lowerSchedules.ratings.length+1, higherSchedules.ratings.length);
			
			concatonatedSortedSchedules = new RatedSchedules(sortedList.length);
			concatonatedSortedSchedules.ratings = sortedList;
			
			System.arraycopy(lowerSchedules.schedules, 0, concatonatedSortedSchedules.schedules, 0, lowerSchedules.schedules.length);
			concatonatedSortedSchedules.schedules[lower.length] = pivotAction;
			System.arraycopy(higherSchedules.schedules, 0, concatonatedSortedSchedules.schedules, lowerSchedules.schedules.length+1, higherSchedules.schedules.length);
		
		}else{
			concatonatedSortedSchedules = a;
		}
		return concatonatedSortedSchedules;
	}
}
