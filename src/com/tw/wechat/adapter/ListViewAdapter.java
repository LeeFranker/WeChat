package com.tw.wechat.adapter;

import imagelogic.ImageLogic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tw.wechat.DetailsActivity;
import com.tw.wechat.R;
import com.tw.wechat.bean.FriendModel;
import com.tw.wechat.bean.TweetsInfo;
import com.tw.wechat.bean.UserInfo;
import com.tw.wechat.widget.CommentView;
import com.tw.wechat.widget.ImageGridView;
import com.tw.wechat.widget.ZanPopupWindow;

/**
 * 朋友圈适配器
 * 
 * @author Lee
 * 
 */
public class ListViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<FriendModel> mDataList;// 数据
	private ImageLogic mImageLogic;// 图片模块
	private final int VIEW_TYPE = 2;// 总类型
	private final int TYPE_SINGLE = 0;// 单个图片
	private final int TYPE_MORE = 1;// 更多图片

	public ListViewAdapter(Context context) {
		this.mContext = context;
		this.mImageLogic = ImageLogic.create(context);
	}

	public void setmDataList(List<FriendModel> mDataList) {
		this.mDataList = mDataList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mDataList != null) {
			return mDataList.size();
		}
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		FriendModel friendModel = getItem(position);
		if (friendModel != null) {
			TweetsInfo sender = friendModel.mSender;
			if (sender != null) {
				List<String> images = sender.images;
				if (images != null && images.size() > 0) {
					if (images.size() == 1)
						return TYPE_SINGLE;
					else
						return TYPE_MORE;
				}
			}
		}
		return TYPE_SINGLE;
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE;
	}

	@Override
	public FriendModel getItem(int position) {
		if (mDataList != null) {
			return mDataList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MoreHolder moreholder = null;
		SingleHolder singleHolder = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case TYPE_SINGLE:
				convertView = View.inflate(mContext, R.layout.activity_listview_item_singal_main, null);
				singleHolder = new SingleHolder();
				singleHolder.name = (TextView) convertView.findViewById(R.id.item_name);
				singleHolder.info = (TextView) convertView.findViewById(R.id.item_info);
				singleHolder.icon = (ImageView) convertView.findViewById(R.id.item_image);
				singleHolder.location = (TextView) convertView.findViewById(R.id.item_location);
				singleHolder.time = (TextView) convertView.findViewById(R.id.item_time);
				singleHolder.content = (ImageView) convertView.findViewById(R.id.item_gridview);
				singleHolder.zan = (TextView) convertView.findViewById(R.id.item_zan);
				singleHolder.comment = (CommentView) convertView.findViewById(R.id.item_comment);
				convertView.setTag(singleHolder);
				break;
			case TYPE_MORE:
				convertView = View.inflate(mContext, R.layout.activity_listview_item_more_main, null);
				moreholder = new MoreHolder();
				moreholder.name = (TextView) convertView.findViewById(R.id.item_name);
				moreholder.info = (TextView) convertView.findViewById(R.id.item_info);
				moreholder.icon = (ImageView) convertView.findViewById(R.id.item_image);
				moreholder.location = (TextView) convertView.findViewById(R.id.item_location);
				moreholder.time = (TextView) convertView.findViewById(R.id.item_time);
				moreholder.gridView = (ImageGridView) convertView.findViewById(R.id.item_gridview);
				moreholder.zan = (TextView) convertView.findViewById(R.id.item_zan);
				moreholder.comment = (CommentView) convertView.findViewById(R.id.item_comment);
				convertView.setTag(moreholder);
				break;

			}

		} else {
			switch (type) {
			case TYPE_SINGLE:
				singleHolder = (SingleHolder) convertView.getTag();
				break;
			case TYPE_MORE:
				moreholder = (MoreHolder) convertView.getTag();
				break;
			}
		}

		FriendModel friendModel = null;
		switch (type) {
		case TYPE_SINGLE:
			friendModel = getItem(position);
			if (friendModel != null) {
				TweetsInfo sender = friendModel.mSender;
				if (sender != null) {
					UserInfo info = sender.userInfo;
					// 名字
					String name = info.username;
					if (TextUtils.isEmpty(name))
						singleHolder.name.setText("");
					else
						singleHolder.name.setText(name);
					// 图像
					mImageLogic.display(singleHolder.icon, info.avatar);
					// 简介
					String content = sender.content;
					if (TextUtils.isEmpty(content)) {
						singleHolder.info.setVisibility(View.GONE);
					} else {
						singleHolder.info.setText(content);
						singleHolder.info.setVisibility(View.VISIBLE);
					}
					// 图片
					final List<String> images = sender.images;
					if (images == null || images.size() == 0) {
						singleHolder.content.setVisibility(View.GONE);
					} else {
						singleHolder.content.setVisibility(View.VISIBLE);
						String imageUrl = images.get(0);
						mImageLogic.display(singleHolder.content, imageUrl);
					}
					singleHolder.content.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, DetailsActivity.class);
							Bundle bundle = new Bundle();
							bundle.putStringArrayList("images", (ArrayList<String>) images);
							intent.putExtras(bundle);
							mContext.startActivity(intent);
						}
					});
					// 位置
					singleHolder.location.setVisibility(View.GONE);
					// 发布时间
					singleHolder.time.setVisibility(View.GONE);
					// 点赞
					singleHolder.zan.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 显示点赞浮框
							ZanPopupWindow pop = new ZanPopupWindow(mContext);
							pop.setContentView(R.layout.activity_zan_main);
							int[] location = new int[2];
							v.getLocationOnScreen(location);
							pop.mWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - pop.mWindow.getWidth() - 20, location[1] - v.getHeight() / 2);
						}
					});
					// 显示评论
					singleHolder.comment.updateCommentView(friendModel.mCommentList);
				}
			}
			break;
		case TYPE_MORE:
			friendModel = getItem(position);
			if (friendModel != null) {
				TweetsInfo sender = friendModel.mSender;
				if (sender != null) {
					UserInfo info = sender.userInfo;
					// 名字
					String name = info.username;
					if (TextUtils.isEmpty(name))
						moreholder.name.setText("");
					else
						moreholder.name.setText(name);
					// 图像
					mImageLogic.display(moreholder.icon, info.avatar);
					// 简介
					String content = sender.content;
					if (TextUtils.isEmpty(content)) {
						moreholder.info.setVisibility(View.GONE);
					} else {
						moreholder.info.setText(content);
						moreholder.info.setVisibility(View.VISIBLE);
					}
					// 图片
					List<String> images = sender.images;
					moreholder.gridView.setVisibility(View.VISIBLE);
					moreholder.gridView.updateViewItems(images);
					// 位置
					moreholder.location.setVisibility(View.GONE);
					// 发布时间
					moreholder.time.setVisibility(View.GONE);
					// 点赞
					moreholder.zan.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// 显示点赞浮框
							ZanPopupWindow pop = new ZanPopupWindow(mContext);
							pop.setContentView(R.layout.activity_zan_main);
							int[] location = new int[2];
							v.getLocationOnScreen(location);
							pop.mWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - pop.mWindow.getWidth() - 20, location[1] - v.getHeight() / 2);
						}
					});
					// 显示评论
					moreholder.comment.updateCommentView(friendModel.mCommentList);
				}
			}
			break;
		}
		return convertView;
	}

	static class MoreHolder {
		TextView name;
		TextView info;
		TextView location;
		TextView time;
		ImageView icon;
		ImageGridView gridView;
		TextView zan;
		CommentView comment;
	}

	static class SingleHolder {
		TextView name;
		TextView info;
		TextView location;
		TextView time;
		ImageView icon;
		ImageView content;
		TextView zan;
		CommentView comment;
	}
}
