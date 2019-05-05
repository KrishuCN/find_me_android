package com.arckz.find_me.util

import android.os.Process.myPid
import android.app.Activity
import android.os.Process


/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/05/05  下午 3:15
 *     desc  : Activity返回栈管理类
 *
 * </pre>
 */
class ActivityManager {
    private var activityList: MutableList<Activity>? = ArrayList()

    companion object{
        private var instance: ActivityManager? = null
        // 单例模式中获取唯一的ExitApplication实例
        @Synchronized
        fun getInstance(): ActivityManager {
            if (null == instance) {
                instance = ActivityManager()
            }
            return instance!!
        }

    }

    // 添加Activity到容器中
    fun addActivity(activity: Activity) {
        if (activityList == null)
            activityList = ArrayList()
        activityList!!.add(activity)
    }

    // 移除Activity
    fun removeActivity(activity: Activity) {
        if (activityList != null)
            activityList!!.remove(activity)
    }

    // 遍历所有Activity并finish
    fun exitSystem() {
        for (activity in activityList!!) {
            activity.finish()
        }
        // 退出进程
        android.os.Process.killProcess(myPid())
        System.exit(0)
    }
}