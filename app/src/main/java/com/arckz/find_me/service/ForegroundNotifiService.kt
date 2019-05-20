package com.arckz.find_me.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.arckz.find_me.util.Configs
import com.arckz.find_me.util.NotificationUtils

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/05/20  上午 10:53
 *     desc  : This foreground service made for let XGPushService alive
 *
 * </pre>
 */
class ForegroundNotifiService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationUtils = NotificationUtils(this)
            val builder = notificationUtils.getForgroundChannelNotification("FindMeService","XGPushService is running ..")
            startForeground(Configs.ANDROID_NOTIFICATION_ID,builder.build())
        }else{
            startForeground(Configs.ANDROID_NOTIFICATION_ID,NotificationUtils(this).createChannelsLowVersion())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}