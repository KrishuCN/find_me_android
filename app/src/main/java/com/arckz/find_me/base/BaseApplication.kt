package com.arckz.find_me.base

import android.app.Application
import com.arckz.find_me.okhttp.OkHttpUtil
import com.arckz.find_me.util.Configs
import com.arckz.find_me.util.CrashHandler
import com.arckz.find_me.util.LocationUtils
import com.baidu.mapapi.SDKInitializer
import com.blankj.utilcode.util.LogUtils
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushConfig
import com.tencent.android.tpush.XGPushManager

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/05/05  下午 3:06
 *     desc  : BaseApplication
 *
 * </pre>
 */
class BaseApplication:Application() {
  var locationUtils:LocationUtils? = null

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        CrashHandler.getInstance().init(this)
        OkHttpUtil.initOKGO(this)
        XGPushConfig.enableDebug(this,true)
        XGPushConfig.getToken(this)
        registerTXPush()
        locationUtils = LocationUtils(this)
        SDKInitializer.initialize(applicationContext)
    }

    private fun registerTXPush() {
//        XGPushManager.registerPush(this)
        XGPushManager.registerPush(this,Configs.PUSH_ALIAS, object : XGIOperateCallback {
            override fun onSuccess(data: Any?, flag: Int) {
                //token在设备卸载重装的时候有可能会变
                LogUtils.d("TPush", "注册成功，设备token为：$data")
            }

            override fun onFail(data: Any?, errCode: Int, msg: String?) {
                LogUtils.d("TPush", "注册失败，错误码：$errCode,错误信息：$msg")
            }
        })
    }

    companion object{
        var INSTANCE:BaseApplication? = null
    }
}