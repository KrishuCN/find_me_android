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
package com.arckz.find_me.util;


import android.os.Environment;


public class Configs {

	/** 是否测试环境 */
	public static final boolean DEBUG = false;

	/** URL:文件存放路径 */
	public static final String URL_FILE_HOME = Environment.getExternalStorageDirectory() + "/findme/";
	/** URL:图片存放路径 */
	public static final String URL_FILE_IMG = URL_FILE_HOME + "image/";
	/** URL:log存放路径 */
	public static final String URL_FILE_LOG = URL_FILE_HOME + "crash/";

	/** 文件：用户信息 */
	public static String FILE_USER_INFO = "CP_USER.DAT";
}