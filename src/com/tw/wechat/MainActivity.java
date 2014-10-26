package com.tw.wechat;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tw.wechat.adapter.ListViewAdapter;
import com.tw.wechat.bean.ActivitySource;
import com.tw.wechat.bean.FriendModel;
import com.tw.wechat.bean.UserInfo;
import com.tw.wechat.http.callback.HttpRequestCallback;
import com.tw.wechat.http.impl.IfaceTweetsTask;
import com.tw.wechat.http.impl.IfaceUserTask;
import com.tw.wechat.utils.ToastUtils;
import com.tw.wechat.widget.ActionBarView;
import com.tw.wechat.widget.ListViewHeadView;
import com.tw.wechat.widget.PromptsView;
import com.tw.wechat.widget.pull.PullToRefreshBase.OnRefreshListener;
import com.tw.wechat.widget.pull.PullToRefreshListView;

/**
 * 主页
 * 
 * @author Lee
 * 
 */
public class MainActivity extends BaseActivity implements OnScrollListener {
	private ActionBarView mActionBarView;// 顶部导航栏View
	private PromptsView mPromptsView;// 加载View
	private ListViewHeadView mHeadView;// listview头部View
	private View mFootView;// listview尾部View
	private PullToRefreshListView mRefreshListView;// 列表对象
	private ListView mListView;
	private Context mContext;

	private IfaceUserTask mUserTask = new IfaceUserTask();// 请求图像任务对象
	private IfaceTweetsTask mFriendTask = new IfaceTweetsTask();// 请求朋友圈任务对象

	private UserInfo mUserInfo;// 用户信息
	private ActivitySource mSource;// listview适配器数据
	private ListViewAdapter mAdapter;// listview适配器
	private int mCurrentpage = 0;// 默认第一页数据
	private List<FriendModel> mFriends;// 朋友圈数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		initViews();
		initViewListeners(savedInstanceState);
		initData(savedInstanceState);
	}

	@Override
	protected void initData(Bundle savedInstanceState) {
		mCurrentpage = 0;
		mActionBarView.setTitle(R.string.actionbar_title);
		mPromptsView.showLoading();
		loadUserInfo();
		loadFriendInfo();
	}

	@Override
	protected void initViews() {
		mActionBarView = (ActionBarView) findViewById(R.id.actionBar);
		mHeadView = new ListViewHeadView(mContext);
		mRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
		mPromptsView = (PromptsView) findViewById(R.id.promptsview);
		mListView = mRefreshListView.getRefreshableView();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFootView = inflater.inflate(R.layout.activity_listview_foot_main, null);
		mListView.addHeaderView(mHeadView, null, false);
		mListView.addFooterView(mFootView, null, false);
	}

	@Override
	protected void initViewListeners(Bundle savedInstanceState) {
		mRefreshListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (mSource != null) {
					mCurrentpage = 0;
					mFriends = mSource.getList(mCurrentpage);
					if (mAdapter != null) {
						if (mListView.getFooterViewsCount() == 0)
							mListView.addFooterView(mFootView, null, false);
						mAdapter.setmDataList(mFriends);
					}
				}
				mRefreshListView.onRefreshComplete();
			}
		});
		mRefreshListView.setOnScrollListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ToastUtils.AlertMessageInCenter(mContext, "点击位置:" + arg2);
			}
		});
	}

	/**
	 * 获取用户信息
	 */
	private void loadUserInfo() {
		mUserTask.todo(mContext, new HttpRequestCallback() {

			@Override
			public void onSuccessCallBack(Object object) {
				Object obj = mUserTask.paras(mContext, object);
				if (obj == null) {
					if (mPromptsView != null)
						mPromptsView.showError();
				} else {
					mUserInfo = (UserInfo) obj;
					updateListViewHeadView();
				}

			}

			@Override
			public void onErrorCallBack() {
				if (mPromptsView != null)
					mPromptsView.showError();
			}

		});

	}

	/**
	 * 获取朋友圈信息
	 */
	private void loadFriendInfo() {
		mFriendTask.todo(mContext, new HttpRequestCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccessCallBack(Object object) {
				Object obj = mFriendTask.paras(mContext, object);
				if (obj != null) {
					List<FriendModel> list = (List<FriendModel>) obj;
					if (list != null && list.size() > 0)
						mSource = new ActivitySource(list);
					refreshAdapter();
				}

			}

			@Override
			public void onErrorCallBack() {
				if (mListView != null && mListView.getFooterViewsCount() == 1) {
					mListView.removeFooterView(mFootView);
					ToastUtils.AlertMessageInCenter(mContext, "下部数据加载失败");
				}
			}

		});
	}

	/**
	 * 更新listview头部界面
	 */
	private void updateListViewHeadView() {
		if (mUserInfo == null || mContext == null)
			return;
		mHeadView.setHeadCoverImage(mUserInfo.profile_image);
		mHeadView.setHeadIconImage(mUserInfo.avatar);
		mHeadView.setHeadName(mUserInfo.username);
		mAdapter = new ListViewAdapter(mContext);
		refreshAdapter();
		mListView.setAdapter(mAdapter);
		mPromptsView.showContent();
	}

	/**
	 * 刷新适配器
	 */
	private void refreshAdapter() {
		if (mSource != null) {
			mFriends = mSource.getList(mCurrentpage);
			if (mAdapter != null) {
				mAdapter.setmDataList(mFriends);
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && view.getLastVisiblePosition() == (view.getCount() - 1)) {
			if (mSource != null) {
				if (mSource.isLastPage()) {
					if (mListView != null)
						mListView.removeFooterView(mFootView);
				} else {
					++mCurrentpage;
					mFriends = mSource.getList(mCurrentpage);
					if (mAdapter != null) {
						mAdapter.setmDataList(mFriends);
					}
				}
			}
		}
	}
}
