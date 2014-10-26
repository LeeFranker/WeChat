package com.tw.wechat.http.callback;

import android.content.Context;
import android.os.Handler;

/**
 * 实现监听网络请求任务回调接口
 * 
 * @author Lee
 * 
 */
public class HttpRequestCallbackImpl implements HttpRequestCallback {

	private HttpRequestCallback mRequestCallback;
	private Handler mHandler;

	public HttpRequestCallbackImpl(Handler handler, HttpRequestCallback requestCallback) {
		this.mRequestCallback = requestCallback;
		this.mHandler = handler;
	}

	public HttpRequestCallbackImpl(Context context, HttpRequestCallback requestCallback) {
		this.mRequestCallback = requestCallback;
		this.mHandler = new Handler(context.getMainLooper());
	}

	@Override
	public void onErrorCallBack() {
		if (mHandler == null)
			return;
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (mRequestCallback != null)
					mRequestCallback.onErrorCallBack();

			}
		});

	}

	@Override
	public void onSuccessCallBack(final Object object) {
		if (mHandler == null)
			return;
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (mRequestCallback != null)
					mRequestCallback.onSuccessCallBack(object);

			}
		});

	}

}
