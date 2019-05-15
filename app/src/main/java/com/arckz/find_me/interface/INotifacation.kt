package com.arckz.find_me.`interface`

import com.arckz.find_me.bean.PushNotifiBean

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/05/15  下午 4:19
 *     desc  : 收到消息通知更新
 *
 * </pre>
 */
interface INotifacation {
     fun receivedMsg(data:PushNotifiBean)
}