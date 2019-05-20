package com.arckz.find_me.base

import android.app.Application
import android.content.Intent
import android.os.Build
import com.arckz.find_me.R
import com.arckz.find_me.okhttp.OkHttpUtil
import com.arckz.find_me.service.ForegroundNotifiService
import com.arckz.find_me.util.Configs
import com.arckz.find_me.util.CrashHandler
import com.arckz.find_me.util.LocationUtils
import com.baidu.mapapi.SDKInitializer
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.LogUtils
import com.scwang.smartrefresh.header.FunGameHitBlockHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
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
class BaseApplication : Application() {
    var locationUtils: LocationUtils? = null

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        CrashHandler.getInstance().init(this)
        OkHttpUtil.initOKGO(this)
        XGPushConfig.enableDebug(this, false)
        XGPushConfig.getToken(this)
        registerTXPush()
        setCommonRefreshHeaderAndFooter()
        locationUtils = LocationUtils(this)
        SDKInitializer.initialize(applicationContext)
        startForegroundService()
    }

    private fun registerTXPush() {
//        XGPushManager.registerPush(this)
        XGPushManager.registerPush(this, Configs.PUSH_ALIAS, object : XGIOperateCallback {
            override fun onSuccess(data: Any?, flag: Int) {
                //token在设备卸载重装的时候有可能会变
                LogUtils.d("TPush", "注册成功，设备token为：$data")
            }

            override fun onFail(data: Any?, errCode: Int, msg: String?) {
                LogUtils.d("TPush", "注册失败，错误码：$errCode,错误信息：$msg")
            }
        })
    }

    private fun startForegroundService() {
        val mIntent = Intent(this, ForegroundNotifiService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mIntent)
        } else {
            startService(mIntent)
        }
    }

    companion object {
        var INSTANCE: BaseApplication? = null
        @JvmStatic
        fun setCommonRefreshHeaderAndFooter() {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColors(
                    ColorUtils.getColor(R.color.colorBlack)
                    , ColorUtils.getColor(R.color.colorWhite)
                )
                FunGameHitBlockHeader(context)
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                layout.setPrimaryColors(
                    ColorUtils.getColor(R.color.colorBlack)
                    , ColorUtils.getColor(R.color.colorWhite)
                )
                ClassicsFooter(context)
            }
        }
    }
}