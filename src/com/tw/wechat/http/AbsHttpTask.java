package com.tw.wechat.http;

import java.util.HashMap;

import android.content.Context;

import com.tw.wechat.http.callback.HttpRequestCallback;

/**
 * 基础HTTP任务类
 * 
 * @author Lee
 * 
 */
public abstract class AbsHttpTask implements IfaceHttpTask, IfaceHttpParams {

	/**
	 * 是否是Get请求,这边只是简单区分Get请求或者是POST请求
	 * 
	 * @return
	 */
	public abstract boolean isGet();

	/**
	 * 解析数据
	 * 
	 * @param context
	 * @param obj
	 * @return
	 */
	public abstract Object paras(Context context, Object obj);

	/**
	 * 获取请求地址
	 * 
	 * @return
	 */
	public abstract String getUrl();

	/**
	 * 获取请求参数
	 * 
	 * @return
	 */
	public abstract HashMap<String, String> getParams();

	/**
	 * 执行网络请求
	 * 
	 * @param context
	 * @param callback
	 * @return
	 */
	public abstract boolean todo(Context context, HttpRequestCallback callback);

	/**
	 * 执行网络请求
	 * 
	 * @param context
	 * @return
	 */
	public abstract boolean todo(Context context);

}
