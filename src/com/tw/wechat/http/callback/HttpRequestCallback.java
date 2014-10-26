package com.tw.wechat.http.callback;

/**
 * 监听网络请求任务回调接口
 * 
 * @author Lee
 * 
 */
public interface HttpRequestCallback {

	public void onErrorCallBack();// 网络异常

	public void onSuccessCallBack(Object object);// 成功请求

}
