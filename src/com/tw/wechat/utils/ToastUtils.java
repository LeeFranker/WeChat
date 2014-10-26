package com.tw.wechat.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Toast辅助类
 * 
 * @author Lee
 * 
 */
public class ToastUtils {

	/**
	 * Toast框提示，信息居中显示
	 * 
	 * @param context
	 * @param msg
	 */
	public static void AlertMessageInCenter(Context context, int msg) {
		String message = context.getString(msg);
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * Toast框提示,信息居下显示
	 * 
	 * @param context
	 * @param msg
	 */
	public static void AlertMessageInBottom(Context context, int msg) {
		String message = context.getString(msg);
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
	}

	/**
	 * Toast框提示，信息居中显示
	 * 
	 * @param context
	 * @param msg
	 */
	public static void AlertMessageInCenter(Context context, String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * Toast框提示,信息居下显示
	 * 
	 * @param context
	 * @param msg
	 */
	public static void AlertMessageInBottom(Context context, String msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

}
