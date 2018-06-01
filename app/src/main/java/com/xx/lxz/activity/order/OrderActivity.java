package com.xx.lxz.activity.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.xx.lxz.R;
import com.xx.lxz.adapter.OrderViewPagerAdapter;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.base.BaseFragment;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.fragment.CheckOrderFragment;
import com.xx.lxz.fragment.CompleteOrderFragment;
import com.xx.lxz.fragment.ToBePayOrderFragment;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.StringUtil;


import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener{

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;

    public static OrderActivity activity;
    private BaseFragment[] fragmentsOrder;
    private OrderViewPagerAdapter viewPagerOrderAdapter;
    private String[] ordertTitles={GlobalConfig.CATEGORY_NAME_COMPLETE,GlobalConfig.CATEGORY_NAME_NOTPAY,GlobalConfig.CATEGORY_NAME_CHECK} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        activity=OrderActivity.this;
        //订单
        setTab();
    }

    private void setTab() {
        View topView = StatusBarUtil.createTranslucentStatusBarView(this, getResources().getColor(R.color.white));
        ll_top.addView(topView);

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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
