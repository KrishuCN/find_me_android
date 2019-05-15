package com.arckz.find_me.activity

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.arckz.find_me.R
import com.arckz.find_me.`interface`.INotifacation
import com.arckz.find_me.adapter.PushAdapter
import com.arckz.find_me.base.BaseActivity
import com.arckz.find_me.bean.PushNotifiBean
import com.arckz.find_me.reciver.MessageReceiver
import com.arckz.find_me.util.NotificationDb
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),INotifacation,Toolbar.OnMenuItemClickListener {

    private var pushAdapter: PushAdapter? = null
    private var dataList: MutableList<PushNotifiBean>? = null
    private var currentPage = 1 //默认第一页
    private val lineSize = 10 //每次显示数
    private var allRecorders = 0 //默认总条数
    private var pageSize = 0 //总页数
    private lateinit var toolBar:Toolbar
    private var linearLayoutManager:LinearLayoutManager?= null

    override fun initView() {
        setContentView(R.layout.activity_main)
        toolBar = findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.mipmap.menu_left_white)
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener(this)
        setSupportActionBar(toolbar)
        collapsing_toolbar.setCollapsedTitleTextColor(ColorUtils.getColor(R.color.colorPurple))
        collapsing_toolbar.setExpandedTitleColor(ColorUtils.getColor(R.color.colorAccent))
        collapsing_toolbar.collapsedTitleGravity = Gravity.CENTER
        linearLayoutManager = LinearLayoutManager(this)
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
        rv_push.layoutManager = linearLayoutManager
        rv_push.adapter = pushAdapter
    }

    override fun initListener() {
        MessageReceiver.registUpdateReceiver(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
       when(item?.itemId){
           R.id.action_setting -> {
               ToastUtils.showShort(item.title)
           }
       }
        return true
    }


    override fun receivedMsg(data: PushNotifiBean) {
        dataList?.add(0,data)
        pushAdapter?.notifyDataSetChanged()
        allRecorders = NotificationDb.getInstance(this).count
        collapsing_toolbar.title = "推送总数：$allRecorders 条"
        //RecyclerView滚动到顶部
        val mTopSmoothScroller = TopSmoothScroller(this)
        mTopSmoothScroller.targetPosition = 0
        linearLayoutManager?.startSmoothScroll(mTopSmoothScroller)
    }


//    override fun onRefresh() {
//        currentPage = 1
//        dataList?.clear()
//        dataList?.addAll(NotificationDb.getInstance(this).getNotifacationData(currentPage,lineSize,null))
//        pushAdapter?.notifyDataSetChanged()
//        rv_push.refreshComplete(10)
//    }

//    override fun onLoadMore() {
//        if (currentPage < pageSize) {
//            currentPage++
//            dataList?.addAll(NotificationDb.getInstance(this).getNotifacationData(currentPage, lineSize, null))
//            pushAdapter?.notifyDataSetChanged()
//            rv_push.refreshComplete(dataList!!.size,pageSize)
//        }else{
//            rv_push.setNoMore(true)
//        }
//    }


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


    class TopSmoothScroller(context: Context?) : LinearSmoothScroller(context) {
    override fun getHorizontalSnapPreference(): Int {
        return SNAP_TO_START
    }

    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }
}

    private fun getTestListData(): List<PushNotifiBean> {
        val xgNotification = PushNotifiBean()
        xgNotification.title = "这是跟拍标题"
        xgNotification.update_time = "2019-5-13"
        xgNotification.id = 12345566
        xgNotification.content = "这是内容这是内容这是内容这是内容这是内容这是内容这是内容这是内容"
        return arrayListOf(xgNotification)
    }
}
