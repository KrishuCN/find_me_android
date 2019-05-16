package com.arckz.find_me.util

import android.content.Context
import android.support.annotation.MenuRes
import android.view.View
import android.widget.PopupMenu
import org.json.JSONObject

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/05/12  下午 3:48
 *     desc  :公用工具类
 *
 * </pre>
 */
object CommonUtil {

    fun getJSONObject(map: Map<String, Any>): JSONObject {
        val data = JSONObject()
        try {
            for (key in map.keys) {
                data.put(key, map[key])
            }
        } catch (e: Exception) {
        }

        return data
    }

    fun getPopupMenu(context:Context, view: View, @MenuRes menuRes:Int):PopupMenu?{
        val popupMenu = PopupMenu(context,view)
        popupMenu.menuInflater.inflate(menuRes,popupMenu.menu)
        return popupMenu
    }

}