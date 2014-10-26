package com.tw.wechat;

import imagelogic.ImageLogic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.tw.wechat.adapter.ViewPagerAdapter;

/**
 * 点击图片显示展示图片的的详情页面
 * 
 * @author Lee
 * 
 */
public class DetailsActivity extends BaseActivity {

	private Context mContext;
	private ViewPager mViewPager;
	private List<String> images;
	private LayoutInflater inflater;
	private ArrayList<View> mDataList = new ArrayList<View>();
	private ImageLogic mImageLogic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_main);
		mContext = this;
		initViews();
		initViewListeners(savedInstanceState);
		initData(savedInstanceState);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		mImageLogic = ImageLogic.create(mContext);
		Bundle bundle = getIntent().getExtras();
		images = bundle.getStringArrayList("images");
		showImages();
	}

	@Override
	protected void initViews() {
		inflater = LayoutInflater.from(mContext);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);

	}

	@Override
	protected void initViewListeners(Bundle savedInstanceState) {

	}

	private void showImages() {
		if (images == null || images.size() == 0)
			return;
		for (int i = 0; i < images.size(); i++) {
			View view = inflater.inflate(R.layout.activity_image_item_main, null);
			ImageView iv = (ImageView) view.findViewById(R.id.imageview);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DetailsActivity.this.finish();
				}
			});
			mImageLogic.display(iv, images.get(i));
			mDataList.add(view);
		}
		mViewPager.setAdapter(new ViewPagerAdapter(mDataList));
	}

}
