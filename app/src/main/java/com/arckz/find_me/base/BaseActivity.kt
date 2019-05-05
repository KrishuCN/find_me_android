package com.arckz.find_me.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.arckz.find_me.util.ActivityManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushManager

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/05/05  下午 3:06
 *     desc  : BaseActivity
 *
 * </pre>
 */
abstract class BaseActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.getInstance().addActivity(this)
        immersionBar()
        initView()
        initListener()
        initData()
        requestPermission()
    }




    private fun requestPermission() {
        XXPermissions.with(this)
            .constantRequest()
            .request(object : OnPermission{
                override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                    ToastUtils.showShort("必须同意所有权限")
                    AppUtils.exitApp()
                }

                override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {

                }
            })
    }

    override fun onDestroy() {
        ActivityManager.getInstance().removeActivity(this)
        super.onDestroy()
    }

    override fun onClick(v: View?) {

    }

    abstract fun initData()

    abstract fun initListener()

    abstract fun initView()


}