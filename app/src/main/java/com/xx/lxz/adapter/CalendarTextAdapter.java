package com.xx.lxz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.xx.lxz.R;
import com.xx.lxz.widget.wheelview.adapter.AbstractWheelTextAdapter1;

import java.util.ArrayList;

/**
 * Created by chenyf45 on 2018/6/11.
 */

public class CalendarTextAdapter extends AbstractWheelTextAdapter1 {
    ArrayList<String> list;

    public CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
        super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
        this.list = list;
        setItemTextResource(R.id.tempValue);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    @Override
    public CharSequence getItemText(int index) {
        return list.get(index) + "";
    }
}
