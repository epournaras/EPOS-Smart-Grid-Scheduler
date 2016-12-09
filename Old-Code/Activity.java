package com.example.activitieslibrary;

/**
 * Created by warrens on 19.10.16.
 */

public class Activities {
    //The Activity that needs to executed.
    //Activity name, minimum time window, whether or not an activity can or cannot overlap schedule or window.
    public String name;
    public int minWindowMinutes; //taskTime
    public boolean noOverlapSchedule;

    public Activities createActivity(String n, int minWinMin, boolean oS){
        this.name = n;
        this.minWindowMinutes = minWinMin;
        this.noOverlapSchedule= oS;
        return this;
    }
}
