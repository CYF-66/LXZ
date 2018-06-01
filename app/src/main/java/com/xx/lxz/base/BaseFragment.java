package com.xx.lxz.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xx.lxz.activity.order.OrderActivity;
import com.xx.lxz.util.LogUtil;
import com.xx.lxz.util.SharedPreferencesUtil;

import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Author: cyf
 * Email: liushilin520@foxmail.com
 * Date: 2017-10-20  9:47
 */

public abstract class BaseFragment extends Fragment {

    public Activity mContext;
    protected View    mRootView;
    /**
     * 是否为可见状态
     */
    protected boolean isVisible;
    /**
     * 是否初始视图完成
     */
    protected boolean isPrepared;
    private SharedPreferencesUtil shareUtil;
    private String LanguageType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mContext = TradeContainerActivity.tradeContainerActivity;
        mContext = OrderActivity.activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mRootView==null){
            mRootView = bindLayout(inflater);
            ButterKnife.bind(this,mRootView);
            initView();
        }

        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        //这里 初始化view的各控件 数据
        isPrepared = true;
        lazyLoad();

    }

    /**
     * setUserVisibleHint是在onCreateView之前调用的
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        /**
         * 判断是否可见
         */
        if(getUserVisibleHint()) {

            isVisible = true;
            //执行可见方法-初始化数据之类
            onVisible();

        } else {

            isVisible = false;
            //不可见
            onInvisible();
        }

    }

    @Override
    public void onDestroyView() {
        LogUtil.d("TEST" , "-->onDestroyView");
        super .onDestroyView();
        if (null != mRootView) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 可见做懒加载
     */
    private void onVisible() {
        lazyLoad();
    }

    /**
     * 懒加载
     */
    private void lazyLoad() {
        /**
         * 判断是否可见，或者 初始化view的各控件
         */
        if(!isVisible || !isPrepared) {
            return;
        }
        //可见 或者 控件初始化完成 就 加载数据
        initData();

    }

    /**
     * 不可见-做一些销毁工作
     */
    protected void onInvisible() {


        if(mRootView!=null){
//            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
            LogUtil.d("TEST", "onInvisible=加载数据了");
        }
    }

    /**
     * 绑定布局
     *
     * @param inflater
     * @return
     */
    public abstract View bindLayout(LayoutInflater inflater);

    /**
     * 初始化布局
     */
    public abstract void initView();


    /**
     * 初始化数据
     */
    protected abstract void initData();
}
