package com.arckz.find_me.util

import org.json.JSONObject

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/05/12  下午 3:48
 *     desc  :
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

}