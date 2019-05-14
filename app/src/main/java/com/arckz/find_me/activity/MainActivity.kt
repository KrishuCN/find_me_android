package com.arckz.find_me.activity

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.arckz.find_me.R
import com.arckz.find_me.adapter.PushAdapter
import com.arckz.find_me.base.BaseActivity
import com.arckz.find_me.bean.XGNotification
import com.arckz.find_me.util.NotificationDb
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.LogUtils
import com.github.jdsjlzx.interfaces.OnLoadMoreListener
import com.github.jdsjlzx.interfaces.OnRefreshListener
import com.github.jdsjlzx.recyclerview.LRecyclerView
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter
import com.github.jdsjlzx.recyclerview.ProgressStyle
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), OnLoadMoreListener, OnRefreshListener,LRecyclerView.LScrollListener {


    private var pushAdapter: PushAdapter? = null
    private var dataList: MutableList<XGNotification>? = null
    private var currentPage = 1 //默认第一页
    private val lineSize = 10 //每次显示数
    private var allRecorders = 0 //默认总条数
    private var pageSize = 0 //总页数
    private lateinit var toolBar:Toolbar


    override fun initView() {
        setContentView(R.layout.activity_main)
        toolBar = findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.mipmap.menu_left_white)
        toolbar.inflateMenu(R.menu.toolbar_menu)
        setSupportActionBar(toolbar)
        collapsing_toolbar.setCollapsedTitleTextColor(ColorUtils.getColor(R.color.colorPurple))
        collapsing_toolbar.setExpandedTitleColor(ColorUtils.getColor(R.color.colorAccent))
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        requestPermission()
        allRecorders = NotificationDb.getInstance(this).count
        collapsing_toolbar.title = "推送总数：$allRecorders 条"
        //计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize
        dataList = NotificationDb.getInstance(this).getNotifacationData(currentPage, lineSize, null)
        pushAdapter = PushAdapter(this, dataList!!)
        rv_push.layoutManager = LinearLayoutManager(this)
        rv_push.adapter = LRecyclerViewAdapter(pushAdapter)
        rv_push.setRefreshProgressStyle(ProgressStyle.Pacman)
    }

    override fun initListener() {
        rv_push.setOnRefreshListener(this)
        rv_push.setOnLoadMoreListener(this)
    }

    override fun onRefresh() {
        currentPage = 1
        dataList?.clear()
        dataList?.addAll(NotificationDb.getInstance(this).getNotifacationData(currentPage,lineSize,null))
        pushAdapter?.notifyDataSetChanged()
        rv_push.refreshComplete(10)
    }

    override fun onLoadMore() {
        if (currentPage < pageSize) {
            currentPage++
            dataList?.addAll(NotificationDb.getInstance(this).getNotifacationData(currentPage, lineSize, null))
            pushAdapter?.notifyDataSetChanged()
            rv_push.refreshComplete(dataList!!.size,pageSize)
        }else{
            rv_push.setNoMore(true)
        }
    }


    override fun onScrolled(distanceX: Int, distanceY: Int) {

    }

    override fun onScrollUp() {
    }

    override fun onScrollDown() {
    }

    override fun onScrollStateChanged(state: Int) {
    }

    private fun requestPermission() {
        XXPermissions.with(this)
            .constantRequest()
            .request(object : OnPermission {
                override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
//                    ToastUtils.showShort("有权限未授权成功")
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
