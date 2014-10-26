package com.tw.wechat.widget;

import imagelogic.ImageLogic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tw.wechat.DetailsActivity;
import com.tw.wechat.R;
import com.tw.wechat.utils.DeviceUtils;

/**
 * 显示多个图片的GridView
 * 
 * @author Lee
 * 
 */
public class ImageGridView extends RelativeLayout {

	private int itemWidth;// 宽度
	private int itemHeight;// 高度
	private int itemSpace;// 间隔
	private int columnNum = 3;// 列数

	private boolean isFirst; // 初次加载
	private ImageLogic mImageLogic;
	private List<String> mDataList;// 数据
	private Context mContext;

	public ImageGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		itemSpace = DeviceUtils.dip2px(context, 5f);
		mImageLogic = ImageLogic.create(context);
	}

	/**
	 * itemWidth = (getMeasuredWidth() - (columnNum - 1) * itemSpace)
	 * /columnNum; 平均屏幕的长度
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setItemsPosition();
	}

	public void setImageLogic(ImageLogic imageLogic) {
		this.mImageLogic = imageLogic;
	}

	/**
	 * 显示GridView
	 * 
	 * @param imageList
	 *            图片地址
	 */
	public void updateViewItems(List<String> imageList) {
		if (imageList == null || imageList.size() == 0) {
			setVisibility(View.GONE);
			return;
		}
		this.mDataList = imageList;
		int size = imageList.size();
		if (size > 1 && size <= 4)
			columnNum = 2;
		else
			columnNum = 3;
		removeAllViews();
		LayoutParams params = null;
		for (int i = 0; i < size; i++) {
			View view = createView(imageList.get(i));
			// 设置布局参数
			params = new LayoutParams(itemWidth, itemHeight);
			params.leftMargin = (itemWidth + itemSpace) * (i % columnNum);
			params.topMargin = (itemSpace + itemHeight) * (i / columnNum);
			addView(view, params);
		}
	}

	private void setItemsPosition() {
		LayoutParams rp = null;
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			rp = (LayoutParams) view.getLayoutParams();
			if (rp == null) {
				rp = new LayoutParams(itemWidth, itemHeight);
			}
			rp.width = itemWidth;
			rp.leftMargin = (itemWidth + itemSpace) * (i % columnNum);
			rp.topMargin = (itemHeight + itemSpace) * (i / columnNum);
			view.setLayoutParams(rp);
		}
	}

	private View createView(final String imageUrl) {
		View convertView = inflate(getContext(), R.layout.activity_gridview_item_main, null);
		convertView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ImageView itemImage = (ImageView) convertView.findViewById(R.id.gridview_item_iamge);
		mImageLogic.display(itemImage, imageUrl);

		// 计算View高度
		if (!isFirst) {
			isFirst = true;
			// 相对布局先要设置部位位置
			convertView.measure(0, 0);
			itemHeight = convertView.getMeasuredHeight();
			itemWidth = convertView.getMeasuredWidth();
		}

		// 设置监听事件
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("images", (ArrayList<String>) mDataList);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});

		return convertView;
	}

}
