package com.xx.lxz.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * TOAST工具类
 * @author pc
 *
 */
public class ToastUtil {

	public static void ToastShort(Context context, String msg)
	{
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public static void ToastLong(Context context, String msg)
	{
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
