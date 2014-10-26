package com.tw.wechat.http.thread;

import android.os.AsyncTask;

import com.tw.wechat.http.callback.HttpRequestCallback;

/**
 * 简单封装的轻量级异步类AsyncTask
 * 
 * @author Lee
 * 
 */
public abstract class AbsAsyncTask extends AsyncTask<Object, Integer, Object> {

	public HttpRequestCallback mCallback;

	public AbsAsyncTask(HttpRequestCallback callback) {
		this.mCallback = callback;
	}

	@Override
	protected Object doInBackground(Object... params) {
		return process(params);
	}

	@Override
	protected void onPostExecute(Object result) {
		if (result == null) {
			if (null != mCallback) {
				mCallback.onErrorCallBack();
			}
		} else {
			if (null != mCallback) {
				mCallback.onSuccessCallBack(result);
			}
		}

	}

	/**
	 * 异步执行的主要方法
	 * 
	 * @param params
	 * @return
	 */
	protected abstract Object process(Object... params);

}
