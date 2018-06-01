package com.xx.lxz.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.xx.lxz.config.GlobalConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共类
 * 
 * @author pc
 * 
 */
public class PublicParams {

	private static final long serialVersionUID = 1L;
	static SharedPreferences sharedPreferences;
	static Editor editor;
//	public static boolean isloging=false;//是否登录
	public static List<String> statuslist=new ArrayList<>();
	public static Context context;



	public static void InitSharedPreferences(Context context2) {
		sharedPreferences = context2.getSharedPreferences(GlobalConfig.CATEGORY_NAME_SHAREDPREFERENCE_KEY,
				Context.MODE_PRIVATE);
		PublicParams.context = context2;

	}

	public static boolean getisonestart() {
		if ("-1".equals(sharedPreferences.getString("isonestart", "-1"))) {
			return true;
		} else {

			return false;
		}

	}

	// 判读是否是第一次启动应用
	public static void overonestart() {
		editor = sharedPreferences.edit();
		editor.putString("isonestart", "1");
		editor.commit();
	}

	// 退出登录
	public static void BowOut() {
		// 创建数据编辑器
		editor = sharedPreferences.edit();
		// 传递需要保存的数据

		editor.putString("userid", "-1");
		editor.putString("userName", "-1");
		editor.putString("password", "-1");
		editor.putString("nick_name", "-1");
		editor.putString("icon_url", "-1");
		editor.putString("sex", "-1");
		editor.putString("province", "-1");
		editor.putString("city", "-1");
		editor.putString("birthday", "-1");
		editor.putString("token", "-1");
		// ..
		editor.putString("isoneloging", "-1");

		editor.putString("jpush", "-1");

		editor.putString("jpushstart", "-1");
		editor.putString("jpushend", "-1");
		editor.commit();
//		isloging = false;
	}

}
