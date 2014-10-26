package com.tw.wechat.widget;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tw.wechat.R;
import com.tw.wechat.bean.TweetsInfo;
import com.tw.wechat.bean.UserInfo;
import com.tw.wechat.utils.DeviceUtils;

/**
 * 评论的视图
 * 
 * @author Lee
 * 
 */
public class CommentView extends LinearLayout {

	private Context mContext;
	private int mBlack, mCommentBg;
	private int mTextSize;
	private int mSpace;

	public CommentView(Context context) {
		this(context, null);
	}

	public CommentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		this.mBlack = mContext.getResources().getColor(R.color.black);
		this.mTextSize = DeviceUtils.sp2px(mContext, 10f);
		this.mSpace = DeviceUtils.dip2px(mContext, 5f);
		this.setPadding(mSpace, mSpace, mSpace, mSpace);
		this.mCommentBg = mContext.getResources().getColor(R.color.comment_bg);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		this.setBackgroundColor(mCommentBg);
	}

	public void updateCommentView(List<TweetsInfo> list) {
		if (list == null || list.size() == 0) {
			this.setVisibility(View.GONE);
			return;
		}
		removeAllViews();
		String content = null;
		String name = null;
		for (int i = 0; i < list.size(); i++) {
			TweetsInfo info = list.get(i);
			if (info != null) {
				content = info.content;
				UserInfo user = info.userInfo;
				if (user != null) {
					name = user.username;
				}
			}
			if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(name)) {
				TextView tv = new TextView(mContext);
				LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				tv.setLayoutParams(layout);
				tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				tv.setTextColor(mBlack);
				tv.setTextSize(mTextSize);
				tv.setText(name + ":" + content);
				addView(tv);
			}
		}
		this.setVisibility(View.VISIBLE);
	}
}
