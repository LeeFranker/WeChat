package com.tw.wechat.widget;

import imagelogic.ImageLogic;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tw.wechat.R;

/**
 * listview顶部布局
 * 
 * @author Lee
 * 
 */
public class ListViewHeadView extends RelativeLayout {
	private TextView mNameText;
	private ImageView mIconImage;
	private ImageView mCoverImage;
	private LayoutInflater mInflater;
	private View mHeadView;
	private ImageLogic mImageLogic;

	public ListViewHeadView(Context context) {
		this(context, null);
	}

	public ListViewHeadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mImageLogic = ImageLogic.create(context);
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mHeadView = mInflater.inflate(R.layout.activity_listview_head_main, this);
		this.mNameText = (TextView) mHeadView.findViewById(R.id.head_name);
		this.mIconImage = (ImageView) mHeadView.findViewById(R.id.head_icon_image);
		this.mCoverImage = (ImageView) mHeadView.findViewById(R.id.head_cover_image);
		initViews();
	}

	public void initViews() {
		mCoverImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 更换相册封面
			}
		});
		mIconImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入个人朋友圈

			}
		});
	}

	/*** 设置图像 ***/
	public void setHeadIconImage(String imageUrl) {
		if (TextUtils.isEmpty(imageUrl))
			return;
		mImageLogic.display(mIconImage, imageUrl);
	}

	/*** 设置封面 ***/
	public void setHeadCoverImage(String imageUrl) {
		if (TextUtils.isEmpty(imageUrl))
			return;
		mImageLogic.display(mCoverImage, imageUrl);
	}

	/*** 设置名字 ***/
	public void setHeadName(String name) {
		if (TextUtils.isEmpty(name))
			return;
		mNameText.setText(name);
	}
}
