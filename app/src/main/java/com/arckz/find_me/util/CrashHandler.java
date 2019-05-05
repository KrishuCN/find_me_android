package com.arckz.find_me.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();
    // 程序的Context对象
    private Context mContext;
    // 用来存储设备信息和异常信息
	private Map<String, Object> infos = new HashMap<String, Object>();

    // 用于格式化日期,作为日志文件名的一部分
    @SuppressLint("SimpleDateFormat")
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    
    String path =  Configs.URL_FILE_LOG;

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
    * 当UncaughtException发生时会转入该函数来处理
    */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

//      ScreenManager.getScreenManager().popActivity(Pay.activity);
//      arg0.stop();
//      arg0.destroy();

        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            System.exit(10);
        }
    }

    /**
    * 自定义错误处理,收集错误信息
    * 发送错误报告等操作均在此完成.
    * 开发者可以根据自己的情况来自定义异常处理逻辑
    * @param ex
    * @return true:如果处理了该异常信息; 否则返回false
    */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

//        final String msg = ex.getLocalizedMessage();
//        if (msg == null) {
//            return false;
//        }

        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序累了,休息一下马上回来...",
                        Toast.LENGTH_SHORT).show();
                // MsgPrompt.showMsg(mContext, "程序出错啦", msg+"\n点确认退出");
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);

        // 保存日志文件
        saveCrashInfo2File(ex);

        // 发送错误报告到服务器
        sendCrashReportsToServer(mContext);

        return true;
    }

    /**
     * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
     */
    public void sendPreviousReportsToServer() {
        sendCrashReportsToServer(mContext);
    }

    /**
     * 把错误报告发送给服务器,包含新产生的和以前没发送的.
     *
     * @param context
     */
    private void sendCrashReportsToServer(Context context) {
        String[] crFiles = getCrashReportFiles(context);
        if (crFiles != null && crFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<String>();
            sortedFiles.addAll(Arrays.asList(crFiles));
            for (String fileName : sortedFiles) {
                File carshReport = new File(context.getFilesDir(), fileName);
                postReport(carshReport);
                carshReport.delete();// 删除已发送的报告
            }
        }
    }

    private void postReport(File file) {
        // TODO 发送错误报告到服务器
    }

    /**
     * 获取错误报告文件名
     *
     * @param context
     * @return
     */
    private String[] getCrashReportFiles(Context context) {
        File filesDir = context.getFilesDir();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        };
        return filesDir.list(filter);
    }

    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
    * 保存错误信息到文件中
    * @param ex
    * @return
    */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();

        // for (Map.Entry entry : infos.entrySet()) {
        // String key = entry.getKey();
        // String value = entry.getValue();
        // sb.append(key + "=" + value + "\n");
        // }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

         infos.put("EXEPTION", ex.getLocalizedMessage());
         infos.put("STACK_TRACE", result);

        try {

            // Time t = new Time("GMT+8");
            // t.setToNow(); // 取得系统时间
            // int date = t.year * 10000 + t.month * 100 + t.monthDay;
            // int time = t.hour * 10000 + t.minute * 100 + t.second;

            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".txt";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
                System.err.println(sb.toString());
                // FileOutputStream trace = mContext.openFileOutput(fileName,
                // Context.MODE_PRIVATE);
                // mDeviceCrashInfo.store(trace, "");
                // trace.flush();
                // trace.close();

            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }
}
