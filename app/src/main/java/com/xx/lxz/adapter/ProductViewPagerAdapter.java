package com.xx.lxz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.xx.lxz.fragment.RentFragment;
import com.xx.lxz.fragment.SecondHandRentFragment;

/**
 * ViewPager通用
 *
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-04-07  16:29
 */

public class ProductViewPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;

    public ProductViewPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = RentFragment.newInstance(position);
                break;
            case 1:
                fragment = SecondHandRentFragment.newInstance(position);
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
//        Fragment fragment = titles[position];
//        getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }
}
