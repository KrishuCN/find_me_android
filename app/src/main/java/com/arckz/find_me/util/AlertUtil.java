package com.arckz.find_me.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.arckz.find_me.R;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;


public class AlertUtil {

	public static void toast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void toastLong(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static AlertDialog AlertDialog(Context context, String title, String message,
                                          DialogInterface.OnClickListener clickListener) {
		return new AlertDialog.Builder(context).setTitle(title).setMessage(message).setCancelable(false)
				.setPositiveButton("确定", clickListener).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public static AlertDialog AlertDialog(Context context, String message,
                                          DialogInterface.OnClickListener clickListener, DialogInterface.OnClickListener clickListener2) {
		return new AlertDialog.Builder(context).setTitle("提示").setMessage(message).setCancelable(false)
				.setPositiveButton("确定", clickListener).setNegativeButton("取消", clickListener2).show();
	}

	public static AlertDialog AlertDialog(Context context, String message,
                                          DialogInterface.OnClickListener clickListener) {
		return new AlertDialog.Builder(context).setTitle("提示").setMessage(message).setCancelable(false)
				.setPositiveButton("确定", clickListener).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}
	
	public static AlertDialog AlertDialog2(Context context, String message,
                                           DialogInterface.OnClickListener clickListener) {
		return new AlertDialog.Builder(context).setTitle("提示").setMessage(message).setCancelable(false)
				.setPositiveButton("去开通", clickListener).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public static AlertDialog AlertDialogNoTiltle(Context context, String message,
                                                  DialogInterface.OnClickListener clickListener) {
		return new AlertDialog.Builder(context).setMessage(message).setCancelable(false)
				.setPositiveButton("确定", clickListener).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public static void AlertDialogPositive(Activity context, String title, String message,
                                           DialogInterface.OnClickListener clickListener) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message).setCancelable(false)
				.setPositiveButton("确定", clickListener).show();
	}

	public static void AlertDialogPositive(Context context, String title, String message,
                                           DialogInterface.OnClickListener clickListener) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message).setCancelable(false)
				.setPositiveButton("确定", clickListener).show();
	}

	public static AlertView HintAlertDialogIOS(Context context, String title, String message,
                                               OnItemClickListener clickListener, boolean cancelable) {
		AlertView alertView = new AlertView(title, message, null, new String[] { "确定" }, null, context,
				AlertView.Style.Alert, clickListener);
		alertView.setCancelable(cancelable).show();
		return alertView;

	}

	public static void ConfirmAlertDialogIOS(Context context, String title, String message,
                                             OnItemClickListener clickListener, boolean cancelable) {
		new AlertView(title, message, "取消", new String[] { "确定" }, null, context, AlertView.Style.Alert, clickListener)
				.setCancelable(cancelable).show();
	}

	public static void SheetAlertDialogIOS(Activity context, String title, String[] item,
                                           OnItemClickListener clickListener, boolean cancelable) {
		new AlertView(title, null, "取消", null, item, context, AlertView.Style.ActionSheet, clickListener)
				.setCancelable(cancelable).show();
	}
//
//	public static void showSystemAlert(Context context, String title, String message,
//											DialogInterface.OnClickListener clickListener) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DefaultDialogTheme1);
//		builder.setMessage(message);
//		builder.setTitle(title);
//		builder.setPositiveButton("确定", clickListener);
//		builder.setCancelable(false);
//		final AlertDialog dialog = builder.create();
//		//在dialog show前添加此代码，表示该dialog属于系统dialog。
//		dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
//		dialog.show();
//	}


	public static void AlertXQDialog(Activity context, DialogInterface.OnClickListener clickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setIcon(R.mipmap.ic_launcher);
//		builder.setTitle("需求类型");
		//    指定下拉列表的显示数据
		final String[] cities = {"我要招工", "我要找工"};
		//    设置一个下拉的列表选择项
		builder.setItems(cities, clickListener);
		builder.show();
	}

	public static void AlertGZFSDialog(Activity context, DialogInterface.OnClickListener clickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setIcon(R.mipmap.ic_launcher);
//		builder.setTitle("需求类型");
		//    指定下拉列表的显示数据
		final String[] cities = {"点工", "月工", "包工", "不限"};
		//    设置一个下拉的列表选择项
		builder.setItems(cities, clickListener);
		builder.show();
	}

	public static AlertDialog AlertDialogPermission(Context context, String message,
                                                    DialogInterface.OnClickListener clickListener) {
		return new AlertDialog.Builder(context).setTitle("提示").setMessage(message).setCancelable(false)
				.setPositiveButton("去打开", clickListener).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

}