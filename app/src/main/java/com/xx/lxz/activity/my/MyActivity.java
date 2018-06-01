package com.xx.lxz.activity.my;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.xx.lxz.R;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyActivity extends BaseActivity {

    @BindView(R.id.ll_top)
    LinearLayout ll_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        View topView = StatusBarUtil.createTranslucentStatusBarView(this, getResources().getColor(R.color.white));
        ll_top.addView(topView);
    }
}
