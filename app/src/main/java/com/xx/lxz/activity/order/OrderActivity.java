package com.xx.lxz.activity.order;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xx.lxz.App;
import com.xx.lxz.R;
import com.xx.lxz.activity.my.CusSerActivity;
import com.xx.lxz.activity.my.LoginActivity;
import com.xx.lxz.adapter.OrderViewPagerAdapter;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.base.BaseFragment;
import com.xx.lxz.bean.RefreshtEvent;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.fragment.CheckOrderFragment;
import com.xx.lxz.fragment.CompleteOrderFragment;
import com.xx.lxz.fragment.ToBePayOrderFragment;
import com.xx.lxz.util.LogUtil;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.StringUtil;
import com.xx.lxz.widget.CustomDialog;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.iv_kefu)
    ImageView iv_kefu;
    @BindView(R.id.rl_unlogin)
    RelativeLayout rl_unlogin;
    @BindView(R.id.ll_login)
    LinearLayout ll_login;
    @BindView(R.id.tv_login)
    TextView tv_login;

    public static OrderActivity activity;
    private BaseFragment[] fragmentsOrder;
    private OrderViewPagerAdapter viewPagerOrderAdapter;
    private SharedPreferencesUtil shareUtil;
    private Dialog mDialog;
    private String[] ordertTitles={GlobalConfig.CATEGORY_NAME_COMPLETE,GlobalConfig.CATEGORY_NAME_NOTPAY,GlobalConfig.CATEGORY_NAME_CHECK} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        activity=OrderActivity.this;
        shareUtil = SharedPreferencesUtil.getinstance(activity);
        if (!shareUtil.getBoolean("IsLogin")) {
        }else{
            //订单
            setTab();
            iv_kefu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(activity, CusSerActivity.class));
                }
            });
        }

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setTab() {
//        View topView = StatusBarUtil.createTranslucentStatusBarView(this, getResources().getColor(R.color.white));
//        ll_top.addView(topView);

        //设置TabLayout标签的显示方式
        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        //循环注入标签
        for (String tab : ordertTitles) {
            tab_layout.addTab(tab_layout.newTab().setText(tab));
        }
        //设置TabLayout点击事件
        tab_layout.setOnTabSelectedListener(this);
        fragmentsOrder = new BaseFragment[]{new CompleteOrderFragment(), new ToBePayOrderFragment(), new CheckOrderFragment()};
        viewPagerOrderAdapter = new OrderViewPagerAdapter(getSupportFragmentManager(), ordertTitles);
        view_pager.setAdapter(viewPagerOrderAdapter);
        tab_layout.setupWithViewPager(view_pager);
        view_pager.setCurrentItem(0);
        view_pager.setOffscreenPageLimit(1);

        StringUtil.reflex(tab_layout);
    }

    /**
     * 提醒登录
     */
    private void showDialog() {

        View rootView = (View) LayoutInflater.from(activity).inflate(R.layout.dialog_unlogin_remian, null);
        TextView tv_login=(TextView)rootView.findViewById(R.id.tv_login);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent=new Intent(activity, LoginActivity.class);
                startActivity(intent);
            }
        });
        if (mDialog == null) {
            mDialog = new CustomDialog.Builder(activity)
                    .setNotitle(true)
                    .setCancelable(true)
                    .setContentView(rootView)
                    .setBottomDialog(false)
                    .setWidth(App.SCREEN_WIDTH-App.SCREEN_WIDTH/6)
                    .setHeight(App.SCREEN_HEIGHT-App.SCREEN_HEIGHT/2)
                    .create();
        }
        mDialog.show();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        LogUtil.d("TEST", "tab position:" + tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                if(event.getMrefreshPosition().getPosition().equals(GlobalConfig.REFRESHPOSITIO_ORDER_PAY)){
                    view_pager.setCurrentItem(1);
//                    dataAddr.getData().remove(event.getMrefreshPosition().getDataPosition());
//                    adapter.notifyDataSetChanged();
                }else if(event.getMrefreshPosition().getPosition().equals(GlobalConfig.REFRESHPOSITIO_ORDER_CHECK)){
                    view_pager.setCurrentItem(2);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!shareUtil.getBoolean("IsLogin")) {
            rl_unlogin.setVisibility(View.VISIBLE);
            ll_login.setVisibility(View.GONE);
        }else{
            rl_unlogin.setVisibility(View.GONE);
            ll_login.setVisibility(View.VISIBLE);
        }
    }
}
