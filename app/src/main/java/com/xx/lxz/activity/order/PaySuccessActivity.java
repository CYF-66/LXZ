package com.xx.lxz.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xx.lxz.R;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.RefreshModel;
import com.xx.lxz.bean.RefreshtEvent;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.util.StatusBarUtil;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaySuccessActivity extends BaseActivity {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_money)
    TextView tv_money;//金额
    @BindView(R.id.btn_complete)
    Button btn_complete;//金额

    private Activity mActivity;
    private String comeFromPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=PaySuccessActivity.this;
        tv_title.setText("分期还款");
        comeFromPosition=getIntent().getStringExtra("comeFromPosition");
    }

    @OnClick({R.id.iv_back,R.id.btn_complete})

    public void onClick(View v) {
        Intent intent = null;
        RefreshModel refreshMode=null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                if(comeFromPosition.equals("1")){
                    refreshMode = new RefreshModel();
                    refreshMode.setActive(GlobalConfig.ACTIVE_REFRESH);
                    refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER_PAY);
                    EventBus.getDefault().post(
                            new RefreshtEvent(refreshMode));
                }else if(comeFromPosition.equals("2")){
                    refreshMode = new RefreshModel();
                    refreshMode.setActive(GlobalConfig.ACTIVE_REFRESH);
                    refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER_CHECK);
                    EventBus.getDefault().post(
                            new RefreshtEvent(refreshMode));
                }

                finish();
                break;
            case R.id.btn_complete://完成
                if(comeFromPosition.equals("1")){
                    refreshMode = new RefreshModel();
                    refreshMode.setActive(GlobalConfig.ACTIVE_REFRESH);
                    refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER_PAY);
                    EventBus.getDefault().post(
                            new RefreshtEvent(refreshMode));
                }else if(comeFromPosition.equals("2")){
                    refreshMode = new RefreshModel();
                    refreshMode.setActive(GlobalConfig.ACTIVE_REFRESH);
                    refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER_CHECK);
                    EventBus.getDefault().post(
                            new RefreshtEvent(refreshMode));
                }
                finish();
                break;
        }
    }
}
