package com.tw.wechat.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tw.wechat.R;
import com.tw.wechat.utils.ToastUtils;

/**
 * 页面顶部导航栏View
 * 
 * @author Lee
 * 
 */
public class ActionBarView extends LinearLayout {

	private TextView mTitleText;
	private TextView mBackText;
	private TextView mCameraText;
	private View mActionBarView;
	private LayoutInflater mInflater;
	private Context mContext;

	public ActionBarView(Context context) {
		this(context, null);
	}

	public ActionBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mActionBarView = mInflater.inflate(R.layout.activity_actionbar_main, this);
		this.mTitleText = (TextView) mActionBarView.findViewById(R.id.actionBar_title);
		this.mBackText = (TextView) mActionBarView.findViewById(R.id.actionBar_back);
		this.mCameraText = (TextView) mActionBarView.findViewById(R.id.actionBar_camera);
		initViews();
	}

	private void initViews() {
		setTitle("");
		mBackText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mContext != null && mContext instanceof Activity) {
					((Activity) mContext).finish();
					System.exit(0);
				}
			}
		});
		mCameraText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtils.AlertMessageInCenter(mContext, "点击照相机");
			}
		});
	}

	public void setTitle(String title) {
		mTitleText.setText(title);
	}

	public void setTitle(int resid) {
		mTitleText.setText(resid);
	}

}
