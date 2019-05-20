package com.arckz.find_me.util


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.arckz.find_me.R
import com.arckz.find_me.util.Configs.ANDROID_CHANNEL_ID
import com.arckz.find_me.util.Configs.ANDROID_CHANNEL_NAME

class NotificationUtils(base: Context) : ContextWrapper(base) {

    private var mManager: NotificationManager? = null

    private val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannels()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {

        // create android channel
        val androidChannel = NotificationChannel(
            ANDROID_CHANNEL_ID,
            ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
        )
        androidChannel.enableLights(true)
        androidChannel.enableVibration(true)
        androidChannel.lightColor = Color.GREEN
        androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        manager!!.createNotificationChannel(androidChannel)
    }

    fun createChannelsLowVersion():Notification{
        return NotificationCompat.Builder(baseContext,ANDROID_CHANNEL_ID)
            .setContentTitle("FindMe")
            .setContentText("XGPushService is running..")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(false)
            .setOngoing(true)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAndroidChannelNotification(title: String, body: String): Notification.Builder {
        return Notification.Builder(applicationContext, ANDROID_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(android.R.drawable.stat_notify_more)
            .setAutoCancel(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForgroundChannelNotification(title: String, body: String):Notification.Builder{
        return Notification.Builder(applicationContext, ANDROID_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(false)
            .setOngoing(true)
    }

    fun cancle(channelID:Int){
        manager?.cancel(channelID)
    }
}