public class RatedSchedules {
	public double[] ratings;
	public Action[][] schedules;
	
	public RatedSchedules(int i){
		this.ratings = new double[i];
		this.schedules = new Action[i][];
	}
	
	public RatedSchedules(double[] r, Action[][]s){
		this.ratings = r;
		this.schedules =s;
	}
}
