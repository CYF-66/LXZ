package com.xx.lxz.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.xx.lxz.R;
import com.xx.lxz.base.BaseFragment;

/**
 * Created by chenyf45 on 2018/5/29.
 */

public class ToBePayOrderFragment extends BaseFragment {


    private static final String ARG_SECTION_NUMBER = "section_number";
    private View mConvertView;
    /**
     * 实例化
     * @param sectionNumber
     * @return
     */
    public static ToBePayOrderFragment newInstance(int sectionNumber) {
        ToBePayOrderFragment fragment = new ToBePayOrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mConvertView = inflater.inflate(R.layout.fragment_tobepay_order, null);
        return mConvertView;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void initData() {

    }
}
