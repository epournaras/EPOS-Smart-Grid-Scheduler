public class RatedSchedule {
	public double rating;
	public Action[] schedule;
	
	public RatedSchedule(int i){
		this.schedule = new Action[i];
	}
	
	public RatedSchedule(double r, Action[]s){
		this.rating = r;
		this.schedule =s;
	}
}
