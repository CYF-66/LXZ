package com.xx.lxz.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xx.lxz.R;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.RefreshModel;
import com.xx.lxz.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TakeOrderSuccessActivity extends BaseActivity {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=TakeOrderSuccessActivity.this;
        tv_title.setText("申请成功");
    }

    @OnClick({R.id.iv_back,R.id.btn_complete})

    public void onClick(View v) {
        Intent intent = null;
        RefreshModel refreshMode=null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
        }
    }
}
