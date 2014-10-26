package com.tw.wechat.http;

import http.IfaceHttp;
import http.IfaceHttpRequest;
import http.IfaceHttpRequest.HttpMethod;
import http.IfaceHttpResponse;
import http.apache.HttpApache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import utils.APIUtils;
import utils.StringUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.tw.wechat.http.callback.DefaultCallbackImpl;
import com.tw.wechat.http.callback.HttpRequestCallback;
import com.tw.wechat.http.callback.HttpRequestCallbackImpl;
import com.tw.wechat.http.thread.AbsAsyncTask;
import com.tw.wechat.http.thread.ExpandedThreadPool;

/**
 * 基础HTTP任务
 * 
 * @author Lee
 * 
 */
public abstract class HttpTaskExecute extends AbsHttpTask {

	/**
	 * 回调对象
	 */
	private HttpRequestCallback mCallback;

	/**
	 * 线程池对象
	 */
	private static ExpandedThreadPool mThreadPool;

	/**
	 * 设置回调对象
	 * 
	 * @param handler
	 * @param callback
	 */
	public void setRequestCallback(Handler handler, HttpRequestCallback callback) {
		mCallback = new HttpRequestCallbackImpl(handler, callback);
	}

	/**
	 * 设置回调对象
	 * 
	 * @param context
	 * @param callback
	 */
	public void setRequestCallback(Context context, HttpRequestCallback callback) {
		mCallback = new HttpRequestCallbackImpl(context, callback);
	}

	@Override
	public boolean todo(Context context) {
		return todo(context, new DefaultCallbackImpl());
	}

	@SuppressLint("NewApi")
	@Override
	public boolean todo(Context context, HttpRequestCallback callback) {

		setRequestCallback(context, callback);

		AbsAsyncTaskImpl mHttpTask;

		mHttpTask = new AbsAsyncTaskImpl(context, mCallback);

		if (APIUtils.hasHoneycomb()) {
			if (mThreadPool == null)
				mThreadPool = new ExpandedThreadPool();
			if (mThreadPool == null)
				mHttpTask.executeOnExecutor(mThreadPool);
			else
				mHttpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			mHttpTask.execute();
		}

		return false;
	}

	private class AbsAsyncTaskImpl extends AbsAsyncTask {

		private Context mContext;

		private InputStream mInput;

		private int mTimeOut;

		public AbsAsyncTaskImpl(Context context, HttpRequestCallback callback) {
			super(callback);
			this.mContext = context;
		}

		public AbsAsyncTaskImpl(Context context, HttpRequestCallback callback, int timeOut) {
			super(callback);
			this.mContext = context;
			this.mTimeOut = timeOut;
		}

		@Override
		protected Object process(Object... params) {

			if (TextUtils.isEmpty(getUrl()) || mContext == null)
				return null;

			return connectServer();
		}

		public Object connectServer() {

			IfaceHttp mHttp = null;
			try {
				mHttp = new HttpApache(mContext, IFACE_USERAGENT);
				if (mTimeOut > 0) {
					mHttp.setConnectionTimeOut(mTimeOut);
					mHttp.setResponseTimeOut(mTimeOut);
				}

				IfaceHttpRequest request = null;

				if (isGet()) {
					request = mHttp.newRequest(StringUtils.getWrappedUrl(getUrl(), getParams()));
					request.setMethod(HttpMethod.GET);

				} else if (!isGet() && null != getParams() && getParams().size() > 0) {
					request = mHttp.newRequest(getUrl());
					request.setMethod(HttpMethod.POST);
					request.addStringParams(getParams());
				}

				IfaceHttpResponse response = mHttp.execute(request);

				if (response == null)
					return null;

				int responseCode = response.getStatusCode();

				if (responseCode != 200)
					return null;

				mInput = response.getContent();
				if (mInput == null)
					return null;

				// 数据解压处理
				List<String> list = response.getHeader("Content-Encoding");
				String encodeing = null;
				if (list != null && list.size() > 0)
					encodeing = list.get(0);

				if (!StringUtils.isEmpty(encodeing) && encodeing.contains("gzip")) {
					byte[] bytes = gunzip(mInput);
					if (bytes != null)
						return StringUtils.getStrFromInput(new ByteArrayInputStream(bytes));
				}

				return StringUtils.getStrFromInput(mInput);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (mHttp != null)
					mHttp.dispose();
			}
			return null;
		}
	}

	/**
	 * GZIP解压
	 * 
	 * @param bytes
	 * @return
	 */
	private byte[] gunzip(InputStream input) {
		// 数据输入流
		InputStream in = null;
		// 二进制输出流
		ByteArrayOutputStream out = null;
		// 结果返回
		byte[] ret = null;
		try {
			out = new ByteArrayOutputStream();
			in = new GZIPInputStream(input);
			byte[] buffer = new byte[1024];
			int count;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
			ret = out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
}
