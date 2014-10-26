package com.tw.wechat;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity基础类
 * 
 * @author Lee
 * 
 */
public abstract class BaseActivity extends Activity {

	/**
	 * 初始化页面相关数据
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void initData(Bundle savedInstanceState);

	/**
	 * 初始化页面相关View
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void initViews();

	/**
	 * 初始化页面相关View监听事件
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void initViewListeners(Bundle savedInstanceState);
}
