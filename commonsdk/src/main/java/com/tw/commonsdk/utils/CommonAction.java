package com.tw.commonsdk.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class CommonAction {
    private List<Activity> allActivitites = new ArrayList<Activity>();
    private List<Activity> allActivititesPages = new ArrayList<Activity>();
    private List<Activity> allActivititesInter = new ArrayList<Activity>();
    private volatile static CommonAction instance;

    private CommonAction() {

    }

    public synchronized static CommonAction getInstance() {
        if (instance == null) {
            synchronized (CommonAction.class) {
                if (instance == null) {
                    instance = new CommonAction();
                }
            }
        }
        return instance;
    }

    //在Activity基类的onCreate()方法中执行
    public void addActivity(Activity activity) {
        if (allActivitites != null) {
            allActivitites.add(activity);
        } else {
            allActivitites = new ArrayList<>();
            allActivitites.add(activity);
        }
    }

    //在Activity基类的onCreate()方法中执行
    public void addActivityActivy(Activity activity) {
        if (allActivititesPages != null) {
            allActivititesPages.add(activity);
        } else {
            allActivititesPages = new ArrayList<>();
            allActivititesPages.add(activity);
        }
    }

    //在Activity基类的onCreate()方法中执行销毁 预约通知消息的activity
    public void addActivityActivyInter(Activity activity) {
        if (allActivititesInter != null) {
            allActivititesInter.add(activity);
        } else {
            allActivititesInter = new ArrayList<>();
            allActivititesInter.add(activity);
        }
    }

    //注销是销毁所有的Activity
    public void outSign(Activity act) {
        for (Activity activity : allActivitites) {
            if (activity != null && activity != act) {
                activity.finish();
            }
        }
        if (act!=null){
            act.finish();
        }
        allActivitites = null;
    }

    //注销是销毁所有的Activity
    public void outSignPage() {
        if (null != allActivititesPages) {
            for (Activity activity : allActivititesPages) {
                if (activity != null) {
                    activity.finish();
                }
            }
            allActivititesPages = null;
        }
    }

    //注销是消息通知的Activity
    public void outSignPageInter() {
        if (null != allActivititesInter) {
            for (Activity activity : allActivititesInter) {
                if (activity != null) {
                    activity.finish();
                }
            }
            allActivititesInter = null;
        }
    }

    public void finishActivity(Activity activity) {

        if (activity != null) {
            this.allActivitites.remove(activity);
            activity.finish();
        }
    }

    /**
     * 在Activity的onDestory中调用，保障CommonAction中储存的是活跃的Activity，防止因为CommonAction中有销毁的Activity的引用，导致内存泄漏
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {

        if (activity != null) {
            if (this.allActivitites != null) {
                this.allActivitites.remove(activity);
            }
            if (this.allActivititesInter != null) {
                this.allActivititesInter.remove(activity);
            }

            if (this.allActivititesPages != null) {
                this.allActivititesPages.remove(activity);
            }
        }
    }

}
