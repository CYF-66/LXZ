package com.xx.lxz.activity;

import android.Manifest;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;
import com.xx.lxz.BuildConfig;
import com.xx.lxz.R;
import com.xx.lxz.activity.home.HomeActivity;
import com.xx.lxz.activity.my.MyActivity;
import com.xx.lxz.activity.order.OrderActivity;
import com.xx.lxz.bean.RefreshModel;
import com.xx.lxz.bean.RefreshtEvent;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.update.UpdateManager;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.util.ToolUtils;
import com.xx.lxz.widget.MyDialog;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

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
    private UpdateManager mUpdateManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtil.setTransparentForWindow(this);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);

        mainActivity = this;
        shareUtil = SharedPreferencesUtil.getinstance(mainActivity);

        initTab();
        init();

        //开启百度推送
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
                ToolUtils.getMetaValue(mainActivity, "api_key"));

        //开启百度统计
        // 由于多进程等可能造成Application多次执行，建议此代码不要埋点在Application中，否则可能造成启动次数偏高
        // 建议此代码埋点在统计路径触发的第一个页面中，若可能存在多个则建议都埋点
        StatService.start(this);
        //测试模式
        StatService.setDebugOn(true);
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);//申请权限
//            ToastUtil.ToastShort(mainActivity,"请允许权限进行下载安装");
        } else {//已拥有权限
            // 这里来检测版本是否需要更新
            String versionName= BuildConfig.VERSION_NAME;
            mUpdateManager = new UpdateManager(
                    MainActivity.this, versionName);
            mUpdateManager.checkUpdateInfo();
        }
    }

    /**
     * 监听事件
     * @param v
     */
    @OnClick({R.id.ll_home,R.id.ll_order,R.id.ll_my})
    public void onClick(View v) {

        Intent intent=null;
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

    /**
     * 捕捉返回键
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            MyDialog dialog = null;
            MyDialog.Builder builder = new MyDialog.Builder(mainActivity);
            builder.setTitle(getResources().getString(R.string.dialog_tiShi));
            builder.setMessage(getResources().getString(R.string.sure_login_out));
            builder.setPositiveButton(getResources().getString(R.string.dialog_sure),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                            MainActivity.mainActivity.finish();
                            System.exit(0);
                        }
                    });

            builder.setNegativeButton(getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            if (dialog == null) {
                dialog = builder.create();
            }
            if (!dialog.isShowing()) {
                dialog.show();
                Window window = dialog.getWindow();
                window.setWindowAnimations(R.style.DialogAnimation);//设置dialog的显示动画
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    /**
     * 接收消息
     * @param event
     */
    @Subscriber
    public void onEventMainThread(RefreshtEvent event) {

        if(event.getMrefreshPosition()!=null){
            if(event.getMrefreshPosition().getActive().equals(GlobalConfig.ACTIVE_SKIPTO)){
                if(event.getMrefreshPosition().getPosition().equals(GlobalConfig.REFRESHPOSITIO_ORDER)){
                    ll_order.setSelected(false);
                    ll_my.setSelected(false);
                    ll_home.setSelected(false);
                    tabHost.setCurrentTabByTag("order");
                    ll_order.setSelected(true);

                    if(event.getMrefreshPosition().getDataPosition()==1){
                        RefreshModel refreshMode = new RefreshModel();
                        refreshMode.setActive(GlobalConfig.ACTIVE_SKIPTO);
                        refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER_PAY);
                        refreshMode.setDataPosition(1);
                        EventBus.getDefault().post(
                                new RefreshtEvent(refreshMode));
                    }else if(event.getMrefreshPosition().getDataPosition()==2){
                        RefreshModel refreshMode = new RefreshModel();
                        refreshMode.setActive(GlobalConfig.ACTIVE_SKIPTO);
                        refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER_CHECK);
                        refreshMode.setDataPosition(2);
                        EventBus.getDefault().post(
                                new RefreshtEvent(refreshMode));
                    }
//                    dataAddr.getData().remove(event.getMrefreshPosition().getDataPosition());
//                    adapter.notifyDataSetChanged();
                }
            }else if(event.getMrefreshPosition().getActive().equals(GlobalConfig.ACTIVE_SHOW_EXCEPTION)){
                if(event.getMrefreshPosition().getPosition().equals(GlobalConfig.REFRESHPOSITIO_SHOW_ALL)){
                    String msg=event.getMrefreshPosition().getMsg();
                    ToastUtil.ToastShort(mainActivity,msg);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 这里来检测版本是否需要更新
                    String versionName=NetUtil.getVersionName(this);
                    mUpdateManager = new UpdateManager(
                            MainActivity.this, versionName);
                    mUpdateManager.checkUpdateInfo();
                } else {
                    ToastUtil.ToastShort(mainActivity, "提示没有权限，安装不了咯");
                }
            }
        }
    }

}
