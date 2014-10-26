package com.tw.wechat.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tw.wechat.R;
import com.tw.wechat.utils.DeviceUtils;
import com.tw.wechat.utils.ToastUtils;

/**
 * 点赞的浮出框
 * 
 * @author Lee
 * 
 */
public class ZanPopupWindow {
	protected Context mContext;
	private TextView zanTv, commentTv;
	public PopupWindow mWindow;
	protected View mRootView;
	private int mWidth;

	public ZanPopupWindow(Context context) {
		mContext = context;
		mWidth = DeviceUtils.getScreenWidth(context) / 2;
		mWindow = new PopupWindow(context);
	}

	// 初始化
	protected void preShow() {
		if (mRootView == null)
			throw new IllegalStateException("mRootView==null");
		mWindow.setWidth(mWidth);
		mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		mWindow.setTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setOutsideTouchable(true);
		mWindow.setContentView(mRootView);
	}

	// 设置view
	public void setContentView(View root) {
		mRootView = root;
		zanTv = (TextView) mRootView.findViewById(R.id.zan_tv);
		commentTv = (TextView) mRootView.findViewById(R.id.comment_tv);
		zanTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtils.AlertMessageInCenter(mContext, "点赞");
				dismiss();
			}
		});
		commentTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtils.AlertMessageInCenter(mContext, "点击评论");
				dismiss();
			}
		});
		mWindow.setContentView(root);
		preShow();
	}

	// 设置view
	public void setContentView(int layoutResID) {
		LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(inflator.inflate(layoutResID, null));
		preShow();
	}

	// 设置dismiss监听
	public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
		mWindow.setOnDismissListener(listener);
	}

	// dismiss
	public void dismiss() {
		mWindow.dismiss();
	}

}
