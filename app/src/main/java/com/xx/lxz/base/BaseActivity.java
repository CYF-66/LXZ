package com.xx.lxz.base;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;


/**
 * 
 * 用于管理所有的类文件
 * @author Administrator
 *
 */
public class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		StatusBarUtil.setTranslucent(this);//设置状态栏半透明
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);

	}

	/**
	 * 关闭软键盘
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCurrentFocus() != null) {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}
}
