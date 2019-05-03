package com.itcast.whw.tool;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理活动
 * Create by wangweijun 2019/3/18
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    /**
     * 加入活动
     * @param activity
     */
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    /**
     * 移除活动
     * @param activity
     */
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    /**
     * 移除集合中的所有活动
     */
    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
