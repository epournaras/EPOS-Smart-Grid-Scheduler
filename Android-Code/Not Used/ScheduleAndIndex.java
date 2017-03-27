package com.example.schedulelibrary;

/**
 * Created by warrens on 19.01.17.
 */

public class ScheduleAndIndex {
    public int index;
    public Action[] schedule;
    public boolean finalPass;

    public ScheduleAndIndex(int i, Action[] s, boolean a){
        this.index = i;
        this.schedule = s;
        this.finalPass = a;
    }

    public ScheduleAndIndex(){
        this.index = 0;
        this.schedule = null;
        this.finalPass = true;
    }
}
