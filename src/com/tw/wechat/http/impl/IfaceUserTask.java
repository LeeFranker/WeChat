package com.tw.wechat.http.impl;

import java.util.HashMap;

import org.json.JSONObject;

import thread.utils.JsonUtils;
import android.content.Context;
import android.text.TextUtils;

import com.tw.wechat.bean.UserInfo;
import com.tw.wechat.http.HttpTaskExecute;

/**
 * 请求用户信息的任务
 * 
 * @author Lee
 * 
 */
public class IfaceUserTask extends HttpTaskExecute {

	@Override
	public String getUrl() {
		return "http://thoughtworks-ios.herokuapp.com/user/jsmith";
	}

	@Override
	public HashMap<String, String> getParams() {
		return null;
	}

	@Override
	public Object paras(Context context, Object obj) {
		if (obj == null)
			return null;

		String result = (String) obj;

		if (TextUtils.isEmpty(result))
			return null;

		JSONObject resObj = null;
		try {
			resObj = new JSONObject(result);
			if (null != resObj) {
				UserInfo userInfo = new UserInfo();
				userInfo.profile_image = JsonUtils.readString(resObj, "profile-image");
				userInfo.avatar = JsonUtils.readString(resObj, "avatar");
				userInfo.username = JsonUtils.readString(resObj, "username");
				userInfo.nick = JsonUtils.readString(resObj, "nick");
				return userInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			resObj = null;
		}
		return null;
	}

	@Override
	public boolean isGet() {
		return true;
	}

}
