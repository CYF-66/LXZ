package com.xx.lxz.activity.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xx.lxz.R;
import com.xx.lxz.activity.my.MessageActivity;
import com.xx.lxz.adapter.ProductViewPagerAdapter;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.base.BaseFragment;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.fragment.RentFragment;
import com.xx.lxz.fragment.SecondHandRentFragment;
import com.xx.lxz.util.PublicParams;
import com.xx.lxz.util.StringUtil;
import com.xx.lxz.widget.BadgeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.iv_msg)
    ImageView iv_msg;

    public static HomeActivity mActivity;
    private BaseFragment[] fragmentsOrder;
    private ProductViewPagerAdapter viewPagerOrderAdapter;
    private String[] proTitles={GlobalConfig.CATEGORY_NAME_RENT,GlobalConfig.CATEGORY_NAME_SECOND_HAND_RENT} ;

    public static ImageLoader imageLoader = null;
    public static DisplayImageOptions options = null;
    private BadgeView badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);
        mActivity=HomeActivity.this;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
                .createDefault(mActivity));
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(0xff000000, 10))
                .showStubImage(R.mipmap.login_logo).cacheInMemory().cacheOnDisc()
                .build();
        //订单
        setTab();
        iv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicParams.isHomeRead=true;
                Intent intent=new Intent(mActivity, MessageActivity.class);
                startActivity(intent);
            }
        });

        badge = new BadgeView(this, iv_msg);
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        badge.setTextColor(Color.WHITE); // 文本颜色
        badge.setBadgeBackgroundColor(Color.RED); // 提醒信息的背景颜色，自己设置
        badge.setTextSize(8); // 文本大小
        badge.setBadgeMargin(0); //各边间隔
//        badge.setText("");

    }

    private void setTab() {
//        View topView = StatusBarUtil.createTranslucentStatusBarView(this, getResources().getColor(R.color.white));
//        ll_top.addView(topView);

        //设置TabLayout标签的显示方式
        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        //循环注入标签
        for (String tab : proTitles) {
            tab_layout.addTab(tab_layout.newTab().setText(tab));
        }
        //设置TabLayout点击事件
        tab_layout.setOnTabSelectedListener(this);
        fragmentsOrder = new BaseFragment[]{new RentFragment(), new SecondHandRentFragment()};
        viewPagerOrderAdapter = new ProductViewPagerAdapter(getSupportFragmentManager(), proTitles);
        view_pager.setAdapter(viewPagerOrderAdapter);
        tab_layout.setupWithViewPager(view_pager);
        view_pager.setCurrentItem(0);
        view_pager.setOffscreenPageLimit(1);

        StringUtil.reflex(tab_layout);
    }

//    @OnClick({R.id.iv_msg})
//
//    public void onSkipToMsg(View v) {
//        Intent intent = null;
//        switch (v.getId()) {
//            case R.id.iv_img:
//                intent=new Intent(mActivity, MessageActivity.class);
//                startActivity(intent);
//                break;
//        }
//    }

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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!PublicParams.isHomeRead){
            badge.show();
        }else{
            badge.hide();
        }
    }
}
