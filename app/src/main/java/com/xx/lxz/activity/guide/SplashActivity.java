package com.xx.lxz.activity.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.xx.lxz.MainActivity;
import com.xx.lxz.R;
import com.xx.lxz.base.AppManager;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.util.LogUtil;
import com.xx.lxz.util.PublicParams;
import com.xx.lxz.util.SharedPreferencesUtil;


/**
 * 开始界面
 *
 * @author pc
 */
public class SplashActivity extends BaseActivity {

    private Activity activity;

    private static SharedPreferencesUtil shareUtil;

    private Intent mainIntent = null;

    private static String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        // 标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置窗体状态
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        activity = SplashActivity.this;
        shareUtil = SharedPreferencesUtil.getinstance(getApplicationContext());
        // 初始化..应用信息
        PublicParams.InitSharedPreferences(this);
        if (PublicParams.getisonestart()) {
            LogUtil.d(TAG, "INTO initScreenSize");
            mainIntent = new Intent(SplashActivity.this, PointActivity.class);
        } else {
            mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, 1000);

    }
}
