package com.xx.lxz;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.xx.lxz.activity.home.HomeActivity;
import com.xx.lxz.activity.my.MyActivity;
import com.xx.lxz.activity.order.OrderActivity;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends TabActivity{


    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_order) LinearLayout ll_order;
    @BindView(R.id.ll_my) LinearLayout ll_my;

    private TabHost tabHost;
    public static MainActivity mainActivity;
    private SharedPreferencesUtil shareUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparentForWindow(this);
        mainActivity = this;
        shareUtil = SharedPreferencesUtil.getinstance(mainActivity);

        initTab();
        init();
    }

    // 初始化tab
    @SuppressWarnings("deprecation")
    public void initTab() {
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("home").setIndicator("home")
                .setContent(new Intent(this, HomeActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("order").setIndicator("order")
                .setContent(new Intent(this, OrderActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("my").setIndicator("my")
                .setContent(new Intent(this, MyActivity.class)));
    }
    private void init() {
        ll_order.setSelected(false);
        ll_my.setSelected(false);
        ll_home.setSelected(false);
        tabHost.setCurrentTabByTag("home");
        ll_home.setSelected(true);
    }

    /**
     * 监听事件
     * @param v
     */
    @OnClick({R.id.ll_home,R.id.ll_order,R.id.ll_my})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_home:
                ll_order.setSelected(false);
                ll_my.setSelected(false);
                ll_home.setSelected(false);
                tabHost.setCurrentTabByTag("home");
                ll_home.setSelected(true);
                break;
            case R.id.ll_order:
                ll_order.setSelected(false);
                ll_my.setSelected(false);
                ll_home.setSelected(false);
                tabHost.setCurrentTabByTag("order");
                ll_order.setSelected(true);
                break;
            case R.id.ll_my:
                ll_order.setSelected(false);
                ll_my.setSelected(false);
                ll_home.setSelected(false);
                tabHost.setCurrentTabByTag("my");
                ll_my.setSelected(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
//        unregisterReceiver(mBroadcastReceiver);

        super.onDestroy();
    }
}
