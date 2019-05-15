package com.arckz.find_me.reciver

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.arckz.find_me.`interface`.INotifacation
import com.arckz.find_me.bean.PushNotifiBean
import com.arckz.find_me.service.LocationReportService
import com.arckz.find_me.util.NotificationDb
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.android.tpush.*
import org.json.JSONException
import org.json.JSONObject

import java.text.SimpleDateFormat
import java.util.Calendar

class MessageReceiver : XGPushBaseReceiver() {


    private fun show(context: Context?, text: String) {
        ToastUtils.showShort(text)
    }

    // 通知展示
    override fun onNotifactionShowedResult(
        context: Context?,
        notifiShowedRlt: XGPushShowedResult?
    ) {
        if (context == null || notifiShowedRlt == null) {
            return
        }
        val notific = PushNotifiBean()
        notific.msg_id = notifiShowedRlt.msgId
        notific.title = notifiShowedRlt.title
        notific.content = notifiShowedRlt.content
        // notificationActionType==1为Activity，2为url，3为intent
        notific.notificationActionType = notifiShowedRlt
            .notificationActionType
        //Activity,url,intent都可以通过getActivity()获得
        notific.activity = notifiShowedRlt.activity
        notific.update_time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(Calendar.getInstance().time)
        NotificationDb.getInstance(context).save(notific)
        show(context, "通知被展示: $notifiShowedRlt")
        LogUtils.d("+++++++++++++++++++++++++++++展示通知的回调")
        iNotification?.let { update(notific) }
        //开始定位上传
        LocationReportService().startLoc()
    }

    //反注册的回调
    override fun onUnregisterResult(context: Context?, errorCode: Int) {
        if (context == null) {
            return
        }
        var text = ""
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功"
        } else {
            text = "反注册失败$errorCode"
        }
        Log.d(LogTag, text)
        show(context, text)

    }

    //设置tag的回调
    override fun onSetTagResult(context: Context?, errorCode: Int, tagName: String) {
        if (context == null) {
            return
        }
        var text = ""
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"$tagName\"设置成功"
        } else {
            text = "\"$tagName\"设置失败,错误码：$errorCode"
        }
        Log.d(LogTag, text)
        show(context, text)

    }

    //删除tag的回调
    override fun onDeleteTagResult(context: Context?, errorCode: Int, tagName: String) {
        if (context == null) {
            return
        }
        var text = ""
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"$tagName\"删除成功"
        } else {
            text = "\"$tagName\"删除失败,错误码：$errorCode"
        }
        Log.d(LogTag, text)
        show(context, text)

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击。此处不能做点击消息跳转，详细方法请参照官网的Android常见问题文档
    override fun onNotifactionClickedResult(
        context: Context?,
        message: XGPushClickedResult?
    ) {
        val notificationManager = context!!
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        if (context == null || message == null) {
            return
        }
        var text = ""
        if (message.actionType == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE.toLong()) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            text = "通知被打开 :$message"
        } else if (message.actionType == XGPushClickedResult.NOTIFACTION_DELETED_TYPE.toLong()) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :$message"
        }
        Toast.makeText(
            context, "广播接收到通知被点击:$message",
            Toast.LENGTH_SHORT
        ).show()
        // 获取自定义key-value
        val customContent = message.customContent
        if (customContent != null && customContent.length != 0) {
            try {
                val obj = JSONObject(customContent)
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    val value = obj.getString("key")
                    Log.d(LogTag, "get custom value:$value")
                }
                // ...
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        // APP自主处理的过程。。。
        LogUtils.d(LogTag, text)
        show(context, text)
    }

    //注册的回调
    override fun onRegisterResult(
        context: Context?, errorCode: Int,
        message: XGPushRegisterResult?
    ) {
        if (context == null || message == null) {
            return
        }
        var text = ""
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message.toString() + "注册成功"
            // 在这里拿token
            val token = message.token
        } else {
            text = message.toString() + "注册失败错误码：" + errorCode
        }
        Log.d(LogTag, text)
        show(context, text)
    }

    // 消息透传的回调
    override fun onTextMessage(context: Context, message: XGPushTextMessage) {
        val text = "收到消息:$message"
        // 获取自定义key-value
        val customContent = message.customContent
        if (customContent != null && customContent.length != 0) {
            try {
                val obj = JSONObject(customContent)
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    val value = obj.getString("key")
                    Log.d(LogTag, "get custom value:$value")
                }
                // ...
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        LogUtils.d("++++++++++++++++透传消息")
        // APP自主处理消息的过程...
        LogUtils.d(LogTag, text)
        show(context, text)
    }

    companion object {
        val LogTag = "TPushReceiver"
        private var iNotification:INotifacation? = null
        fun registUpdateReceiver(receiver:INotifacation){
            iNotification = receiver
        }
        fun update(data:PushNotifiBean){
            iNotification?.receivedMsg(data)
        }
    }
}
