package com.arckz.find_me.activity

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.arckz.find_me.R
import com.arckz.find_me.`interface`.INotifacation
import com.arckz.find_me.adapter.PushAdapter
import com.arckz.find_me.base.BaseActivity
import com.arckz.find_me.bean.PushNotifiBean
import com.arckz.find_me.reciver.MessageReceiver
import com.arckz.find_me.util.AlertUtil
import com.arckz.find_me.util.CommonUtil
import com.arckz.find_me.util.NotificationDb
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermission
import com.hjq.permissions.XXPermissions
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : BaseActivity(),INotifacation,Toolbar.OnMenuItemClickListener,View.OnClickListener,OnRefreshListener,OnLoadMoreListener {

    private var pushAdapter: PushAdapter? = null
    private var dataList: MutableList<PushNotifiBean>? = null
    private var currentPage = 1 //默认第一页
    private val lineSize = 10 //每次显示数
    private var allRecorders = 0 //默认总条数
    private var pageSize = 0 //总页数
    private var linearLayoutManager:LinearLayoutManager?= null
    private var yanArray:Array<String>?= null

    override fun initView() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.mipmap.menu_left_white)
        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener(this)
        toolbar.setNavigationOnClickListener { txt_yan.text = yanArray!![Random.nextInt(yanArray!!.size)] }
        collapsing_toolbar.setCollapsedTitleTextColor(ColorUtils.getColor(R.color.colorPurple))
        collapsing_toolbar.setExpandedTitleColor(ColorUtils.getColor(R.color.colorAccent))
        collapsing_toolbar.collapsedTitleGravity = Gravity.CENTER
        linearLayoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        requestPermission()
        yanArray = resources.getStringArray(R.array.yan_array)
        txt_yan.text = yanArray!![Random.nextInt(yanArray!!.size)]
        allRecorders = NotificationDb.getInstance(this).count
        collapsing_toolbar.title = "$allRecorders 条"
        //计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize
        dataList = NotificationDb.getInstance(this).getNotifacationData(currentPage, lineSize, null)
        pushAdapter = PushAdapter(this, dataList!!)
        rv_push.layoutManager = linearLayoutManager
        rv_push.adapter = pushAdapter

    }

    override fun initListener() {
        MessageReceiver.registUpdateReceiver(this)
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setOnLoadMoreListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
       when(item?.itemId){
           R.id.action_setting -> {
            val popupMenu = CommonUtil.getPopupMenu(this,toolbar_menu_baseline,R.menu.menu_item)
               popupMenu?.setOnMenuItemClickListener {
                   ToastUtils.showShort(it.title)
                   when(it.title){
                       "清除全部记录" -> {
                           AlertUtil.AlertDialog(this,"确定要清空全部数据？") { _, _ ->
                               NotificationDb.getInstance(this).deleteAll()
                               dataList?.clear()
                               collapsing_toolbar.title = "0 条"
                               pushAdapter?.notifyDataSetChanged()
                           }
                       }
                   }
                   false
               }
               popupMenu?.setOnDismissListener {
               }
               popupMenu?.show()
           }
       }
        return true
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id){
            R.id.toolbar ->{}
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refreshLayout.finishRefresh(3000/*,false*/)//传入false表示刷新失败
        currentPage = 1
        dataList?.clear()
        dataList?.addAll(NotificationDb.getInstance(this).getNotifacationData(currentPage,lineSize,null))
        pushAdapter?.notifyDataSetChanged()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        if (currentPage < pageSize) {
            refreshLayout.finishLoadMore(1000)
            currentPage++
            dataList?.addAll(NotificationDb.getInstance(this).getNotifacationData(currentPage, lineSize, null))
            pushAdapter?.notifyDataSetChanged()
        }else{
            refreshLayout.finishLoadMoreWithNoMoreData()
        }
    }

    override fun onResume() {
        super.onResume()
//        GlobalScope.launch {
//            refreshLayout.autoRefreshAnimationOnly()
//            delay(3000L)
//            refreshLayout.finishRefresh()
//        }
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
