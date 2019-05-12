package com.arckz.find_me.activity

import com.arckz.find_me.R
import com.arckz.find_me.adapter.PushAdapter
import com.arckz.find_me.base.BaseActivity
import com.arckz.find_me.util.NotificationDb
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun initView() {
        setContentView(R.layout.activity_main)
        rv_push.adapter = PushAdapter(this,NotificationDb.getInstance(this).getAllNotifacationData(1,10,null))
    }

    override fun initData() {
        requestPermission()



    }

    override fun initListener() {
    }

    private fun requestPermission() {
        XXPermissions.with(this)
            .constantRequest()
            .request(object : OnPermission {
                override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                    ToastUtils.showShort("有权限未授权成功")
                    denied?.forEach {
                        LogUtils.d("授权失败：$it")
                    }
                }

                override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {
                    granted?.forEach {
                        LogUtils.d("授权成功：$it")
                    }
                }
            })
    }

}
