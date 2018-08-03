package com.xx.lxz;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.bqs.crawler.cloud.sdk.BqsCrawlerCloudSDK;

public class App extends Application {

    private static App instance;
    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;

    private static final String TAG = App.class.getName();
    public static final String UPDATE_STATUS_ACTION = "com.xx.lxz.action.UPDATE_STATUS";
    private Handler handler;


    public static synchronized App getInstance() {
        return instance;
    }
    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化屏幕宽高
        getScreenSize();

        //初始化白骑士
        BqsCrawlerCloudSDK.initialize(this);

    }

    /**
     * 初始化屏幕信息
     */
    public void getScreenSize() {
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }
}
