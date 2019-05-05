package com.arckz.find_me.okhttp

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/05/05  下午 3:38
 *     desc  :
 *
 * </pre>
 */
interface RequestHandle {
    fun cancle()
    fun isRunning():Boolean
}