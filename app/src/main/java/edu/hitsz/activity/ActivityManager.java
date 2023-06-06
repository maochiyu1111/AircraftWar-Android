package edu.hitsz.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {
    private List<Activity> aList;

    private ActivityManager(){
        aList = new ArrayList<>();
    }

    private static final class InstanceHolder {
        static final ActivityManager instance = new ActivityManager();
    }

    public static ActivityManager getIns(){
        return InstanceHolder.instance;
    }

    public Activity get(int index){
        return aList.get(index);
    }

    public void add(Activity activity){
        aList.add(activity);
    }

    public void del(int index){
        aList.get(index).finish();
        aList.remove(index);
    }
}