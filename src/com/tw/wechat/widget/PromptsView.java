package com.tw.wechat.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tw.wechat.R;

/**
 * 在页面加载网络数据的提示View，当加载网络数据显示“加载中...” 
 * 当加载网络数据发生异常，显示“加载失败”
 * 
 * @author Lee
 * 
 */
public class PromptsView extends LinearLayout {
	private View mTipsLayout;// 加载失败布局
	private View mLoadingLayout;// 加载中布局
	private TextView mTipsText;// 提示文本
	private TextView mLoadingText;// 加载文本
	private LayoutInflater mInflater;

	public PromptsView(Context context) {
		this(context, null);
	}

	public PromptsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.setGravity(Gravity.CENTER);
		this.setVisibility(View.GONE);
		this.setOrientation(LinearLayout.VERTICAL);
		this.mTipsLayout = mInflater.inflate(R.layout.common_view_tips, null);
		this.mLoadingLayout = mInflater.inflate(R.layout.common_view_loading, null);
		this.mTipsText = (TextView) mTipsLayout.findViewById(R.id.common_tips_text);
		this.mLoadingText = (TextView) mLoadingLayout.findViewById(R.id.common_loading_text);
		this.addView(mTipsLayout);
		this.addView(mLoadingLayout);
	}

	/*** 隐藏提示 ***/
	public void showContent() {
		mTipsLayout.setVisibility(View.GONE);
		mLoadingLayout.setVisibility(View.GONE);
		this.setVisibility(View.GONE);
	}

	/*** 显示加载中提示 ***/
	public void showLoading() {
		mTipsLayout.setVisibility(View.GONE);
		mLoadingLayout.setVisibility(View.VISIBLE);
		this.setVisibility(View.VISIBLE);
	}

	public void showLoading(String str) {
		showLoading();
		mLoadingText.setText(str);
	}

	public void showLoading(int resId) {
		showLoading();
	}

	private void showTips() {
		mTipsLayout.setVisibility(View.VISIBLE);
		mLoadingLayout.setVisibility(View.GONE);
		this.setVisibility(View.VISIBLE);
	}

	public void showTips(String str, Drawable topIcon) {
		showTips();
		mTipsText.setText(str);
		mTipsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		mTipsText.setCompoundDrawables(null, topIcon, null, null);
	}

	public void showTips(int resId, Drawable topIcon) {
		showTips();
		mTipsText.setText(resId);
		mTipsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		mTipsText.setCompoundDrawables(null, topIcon, null, null);
	}

	/*** 设置错误提示 ***/
	public void showError() {
		showTips();
		mTipsText.setText(R.string.error);
	}

	public void showError(String str) {
		showTips();
		mTipsText.setText(str);
	}

	public void showError(int resId) {
		showTips();
		mTipsText.setText(resId);
	}

}
