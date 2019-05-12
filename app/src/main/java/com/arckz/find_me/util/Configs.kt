/**
 * Copyright (c) 2012 (RJT). All Rights Reserved.
 *
 * FileName : AppConfig.java
 *
 * Description : Set the application's configuration.
 *
 * History :
 * 1.0 Richard.Cui 2012-09-20 Create
 */
package com.arckz.find_me.util


import android.os.Environment


object Configs {

    /** 是否测试环境  */
    val DEBUG = false

    /** URL:文件存放路径  */
    val URL_FILE_HOME = Environment.getExternalStorageDirectory().toString() + "/findme/"
    /** URL:图片存放路径  */
    val URL_FILE_IMG = URL_FILE_HOME + "image/"
    /** URL:log存放路径  */
    val URL_FILE_LOG = URL_FILE_HOME + "crash/"

    /** 文件：用户信息  */
    var FILE_USER_INFO = "CP_USER.DAT"
    //服务器做了标识判断
    const val CLIEN_TOKEN = "mH3RBK5Yfu5GIrdHCsJCYOy9HF99opL4XgQwPGv0yHs4XOiHDVRQ5Ahl60EJjG9h"
}