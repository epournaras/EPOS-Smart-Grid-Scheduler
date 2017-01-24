package com.example.schedulelibrary;

/**
 * Created by warrens on 19.01.17.
 */

public class RatedSchedule {
    public double rating;
    public Action[] schedule;

    public RatedSchedule(int i){
        this.schedule = new Action[i];
    }
    public RatedSchedule(int i, double p){
        this.schedule = new Action[i];
        this.rating = p;
    }

    public RatedSchedule(double r, Action[]s){
        this.rating = r;
        this.schedule =s;
    }
}
