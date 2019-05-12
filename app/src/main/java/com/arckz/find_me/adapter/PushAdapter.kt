package com.arckz.find_me.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.arckz.find_me.R
import com.arckz.find_me.bean.XGNotification

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/05/12  下午 5:24
 *     desc  : Home page push adapter
 *
 * </pre>
 */
class PushAdapter(context: Context,list:List<XGNotification>) : RecyclerView.Adapter<PushAdapter.PushHolder>() {
    var context: Context? = null
    var inflater:LayoutInflater? = null
    var list:List<XGNotification>? =null


    init {
        this.context = context
        this.list = list
        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PushHolder {
        return PushHolder(inflater?.inflate(R.layout.item_push,p0,false))
    }

    override fun getItemCount(): Int {
        return list?.size?:0
    }

    override fun onBindViewHolder(p0: PushHolder, p1: Int) {
        p0.push_time.text = list?.get(p1)?.update_time
        p0.push_title.text = list?.get(p1)?.title
        p0.push_content.text = list?.get(p1)?.content
        p0.push_msg_id.text = list?.get(p1)?.update_time
    }

    class PushHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var push_msg_id: TextView = itemView?.findViewById(R.id.push_msg_id) as TextView
        var push_title: TextView = itemView?.findViewById(R.id.push_title) as TextView
        var push_time: TextView = itemView?.findViewById(R.id.push_time) as TextView
        var push_content: TextView = itemView?.findViewById(R.id.push_content) as TextView
    }
}