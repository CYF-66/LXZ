package com.xx.lxz.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

public class GuidePageAdapter extends PagerAdapter {


	private ArrayList<View> pageViews;
	
    public GuidePageAdapter() {
		super();
	}

    
	public GuidePageAdapter(ArrayList<View> pageViews) {
		super();
		this.pageViews = pageViews;
	}


	 // 销毁position位置的界面
    @Override
    public void destroyItem(View v, int position, Object arg2) {
        ((ViewPager) v).removeView(pageViews.get(position));

    }

    @Override
    public void finishUpdate(View arg0) {

    }

    // 获取当前窗体界面数
    @Override
    public int getCount() {
        return pageViews.size();
    }

   // 初始化position位置的界面
    @Override
    public Object instantiateItem(View v, int position) {
        ((ViewPager) v).addView(pageViews.get(position));
        return pageViews.get(position);
    }

    // 判断有对象生成的界面
    @Override
    public boolean isViewFromObject(View v, Object arg1) {
        return v == arg1;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
