package com.arckz.find_me.activity

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import com.arckz.find_me.R
import com.arckz.find_me.adapter.PushAdapter
import com.arckz.find_me.base.BaseActivity
import com.arckz.find_me.bean.XGNotification
import com.arckz.find_me.util.NotificationDb
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.jdsjlzx.interfaces.OnLoadMoreListener
import com.github.jdsjlzx.interfaces.OnRefreshListener
import com.github.jdsjlzx.recyclerview.LRecyclerView
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import com.github.jdsjlzx.recyclerview.ProgressStyle
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : BaseActivity(), OnLoadMoreListener, OnRefreshListener,LRecyclerView.LScrollListener {


    private var pushAdapter: PushAdapter? = null
    private var dataList: List<XGNotification>? = null
    private var currentPage = 1 //默认第一页
    private val lineSize = 10 //每次显示数
    private var allRecorders = 0 //默认总条数
    private var pageSize = 0 //总页数
    override fun initView() {
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        requestPermission()
        allRecorders = NotificationDb.getInstance(this).count
        //计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize
        rv_number.text = "$allRecorders 条"
        dataList = NotificationDb.getInstance(this).getNotifacationData(currentPage, lineSize, null)
        pushAdapter = PushAdapter(this, dataList!!)
        rv_push.layoutManager = LinearLayoutManager(this)
        rv_push.adapter = LRecyclerViewAdapter(pushAdapter)
        rv_push.setRefreshProgressStyle(ProgressStyle.BallBeat)


    }

    override fun initListener() {
        rv_push.setOnRefreshListener(this)
        rv_push.setOnLoadMoreListener(this)
    }

    override fun onLoadMore() {
        ToastUtils.showShort("LoadMore")
        if (currentPage < pageSize) {
            currentPage++
            dataList = NotificationDb.getInstance(this).getNotifacationData(currentPage, lineSize, null)
            pushAdapter?.notifyDataSetChanged()
            rv_push.refreshComplete(dataList!!.size,pageSize)
        }else{
            rv_push.setNoMore(true)
        }
    }

    override fun onRefresh() {
        ToastUtils.showShort("Refresh")
        currentPage = 1
        dataList = NotificationDb.getInstance(this).getNotifacationData(currentPage,lineSize,null)
        pushAdapter?.notifyDataSetChanged()
        rv_push.refreshComplete(10)
    }

    override fun onScrolled(distanceX: Int, distanceY: Int) {

    }

    override fun onScrollUp() {
    }

    override fun onScrollDown() {
        rv_push.setLoadMoreEnabled(true)
    }

    override fun onScrollStateChanged(state: Int) {
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
//                    granted?.forEach {
//                        LogUtils.d("授权成功：$it")
//                    }
                }
            })
    }

    private fun getTestListData(): List<XGNotification> {
        val xgNotification = XGNotification()
        xgNotification.title = "这是跟拍标题"
        xgNotification.update_time = "2019-5-13"
        xgNotification.id = 12345566
        xgNotification.content = "这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容"
        return arrayListOf(xgNotification)
    }
}
