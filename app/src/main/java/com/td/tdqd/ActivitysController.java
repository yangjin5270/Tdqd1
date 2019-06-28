package com.td.tdqd;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ActivitysController {

    private static final String TAG="ActivitysController";
    public static List<Activity>  activities = new ArrayList<>();


    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finshAll(){

        for (Activity activtiy:
                activities) {
            if(!activtiy.isFinishing()){
                activtiy.finish();
            }
        }
    }

}
