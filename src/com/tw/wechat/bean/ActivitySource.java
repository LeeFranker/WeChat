package com.tw.wechat.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 对首页数据简单封装下，因为在下拉刷新，或则上拉刷新，要频繁操作
 * 
 * @author Lee
 * 
 */
public class ActivitySource {

	public List<FriendModel> mFriends;// 总数据
	private int mItemSize = 5;//   每页增加5个
	private int mEndPage = 0;// 最后一页

	private int mCurrentPage = 0;// 当前页

	public ActivitySource(List<FriendModel> list) {
		this.mFriends = list;
		this.mEndPage = list.size() / mItemSize;
	}

	// 是否第一页
	public boolean isFirstPage() {
		return mCurrentPage == 0;
	}

	// 是否是最后一页
	public boolean isLastPage() {
		return mCurrentPage == mEndPage;
	}

	// 是否要加载更多数据
	public boolean isLoadMoreData(int page) {
		return page <= mEndPage;
	}

	/**
	 * 根据当前页获取对象数据
	 * 
	 * @param page
	 * @return
	 */
	public List<FriendModel> getList(int page) {
		if (page < 0)
			return null;
		if (!isLoadMoreData(page))
			return null;
		this.mCurrentPage = page;
		List<FriendModel> list = new ArrayList<FriendModel>();
		if (isLastPage()) {
			for (int i = 0; i < mFriends.size(); i++) {
				list.add(mFriends.get(i));
			}
			return list;
		} else {
			int end = (page + 1) * mItemSize;
			for (int i = 0; i < end; i++) {
				list.add(mFriends.get(i));
			}
			return list;
		}
	}
}
