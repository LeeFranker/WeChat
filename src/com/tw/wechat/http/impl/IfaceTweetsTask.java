package com.tw.wechat.http.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import thread.utils.JsonUtils;
import android.content.Context;
import android.text.TextUtils;

import com.tw.wechat.bean.FriendModel;
import com.tw.wechat.bean.TweetsInfo;
import com.tw.wechat.bean.UserInfo;
import com.tw.wechat.http.HttpTaskExecute;

/**
 * 请求朋友圈的任务
 * 
 * @author Lee
 * 
 */
public class IfaceTweetsTask extends HttpTaskExecute {

	@Override
	public boolean isGet() {
		return true;
	}

	@Override
	public Object paras(Context context, Object obj) {

		if (obj == null)
			return null;

		String result = (String) obj;

		if (TextUtils.isEmpty(result))
			return null;

		List<FriendModel> list = new ArrayList<FriendModel>();
		JSONArray resArray = null;
		try {
			resArray = new JSONArray(result);
			if (resArray != null && resArray.length() > 0) {
				for (int i = 0; i < resArray.length(); i++) {
					JSONObject itemObj = resArray.getJSONObject(i);
					if (itemObj == null)
						continue;
					TweetsInfo sendInfo = parseTweetsIfno(itemObj);
					if (sendInfo == null)
						continue;
					if (sendInfo.userInfo == null)
						continue;
					FriendModel model = new FriendModel();
					model.mSender = sendInfo;
					JSONArray commentArray = JsonUtils.readArr(itemObj, "comments");
					if (commentArray != null && commentArray.length() > 0) {
						List<TweetsInfo> cList = new ArrayList<TweetsInfo>();
						for (int j = 0; j < commentArray.length(); j++) {
							JSONObject commentObj = JsonUtils.readObj(commentArray, j);
							TweetsInfo comment = parseTweetsIfno(commentObj);
							if (comment != null)
								cList.add(comment);
						}
						model.mCommentList = cList;
					}
					list.add(model);

				}
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resArray = null;
		}
		return null;

	}

	private TweetsInfo parseTweetsIfno(JSONObject obj) {
		if (obj == null)
			return null;
		// 过滤异常数据
		String error = JsonUtils.readString(obj, "error");
		if (!TextUtils.isEmpty(error))
			return null;
		TweetsInfo model = new TweetsInfo();
		String content = JsonUtils.readString(obj, "content");
		JSONArray imageArray = JsonUtils.readArr(obj, "images");
		ArrayList<String> images = new ArrayList<String>();
		if (imageArray != null && imageArray.length() > 0) {
			for (int i = 0; i < imageArray.length(); i++) {
				JSONObject urlObj = JsonUtils.readObj(imageArray, i);
				images.add(JsonUtils.readString(urlObj, "url"));
			}
		}
		JSONObject userObj = JsonUtils.readObj(obj, "sender");
		UserInfo userInfo = new UserInfo();
		userInfo.avatar = JsonUtils.readString(userObj, "avatar");
		userInfo.nick = JsonUtils.readString(userObj, "nick");
		userInfo.username = JsonUtils.readString(userObj, "username");
		model.content = content;
		model.images = images;
		model.userInfo = userInfo;
		return model;
	}

	@Override
	public String getUrl() {
		return "http://thoughtworks-ios.herokuapp.com/user/jsmith/tweets";
	}

	@Override
	public HashMap<String, String> getParams() {
		return null;
	}

}
