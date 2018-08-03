package com.xx.lxz.activity.my.set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xx.lxz.R;
import com.xx.lxz.activity.my.AddressActivity;
import com.xx.lxz.activity.my.LoginActivity;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends BaseActivity {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_lxz_deal)
    LinearLayout ll_lxz_deal;
    @BindView(R.id.ll_register_deal)
    LinearLayout ll_register_deal;
    @BindView(R.id.ll_query_zx)
    LinearLayout ll_query_zx;
    @BindView(R.id.ll_rent_deal)
    LinearLayout ll_rent_deal;
    @BindView(R.id.ll_address_manager)
    LinearLayout ll_address_manager;
    @BindView(R.id.btn_out_login)
    Button btn_out_login;
    @BindView(R.id.tv_version)
    TextView tv_version;

    private Activity mActivity;
    private SharedPreferencesUtil shareUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=SetActivity.this;
        shareUtil = SharedPreferencesUtil.getinstance(mActivity);
        tv_title.setText("设置");

        if (!shareUtil.getBoolean("IsLogin")) {
            btn_out_login.setText("登录");
        }else{
            btn_out_login.setText("退出登录");
        }
        tv_version.setText(NetUtil.getVersionName(mActivity));
    }

    @OnClick({R.id.iv_back,R.id.ll_lxz_deal,R.id.ll_register_deal,R.id.ll_query_zx,R.id.ll_rent_deal,R.id.btn_out_login,R.id.ll_address_manager})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.ll_lxz_deal://还款管理协议
                intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("title","还款管理协议");
                intent.putExtra("url","https://91xiaoxiangzu.com/xxzapp/paymanage.html");
                startActivity(intent);
                break;
            case R.id.ll_register_deal://用户注册协议
                intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("title","用户协议");
                intent.putExtra("url","https://91xiaoxiangzu.com/xxzapp/login.html");
                startActivity(intent);
                break;
            case R.id.ll_query_zx://征信查询授权书
                String cid=shareUtil.getString("cid");
                intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("title","征信查询授权书");
                intent.putExtra("url","https://91xiaoxiangzu.com/xxzapp/proxy.html?cid"+cid);
                startActivity(intent);
                break;
            case R.id.ll_rent_deal://笑享租租赁协议
                intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("title","笑享租租赁协议");
                intent.putExtra("url","https://91xiaoxiangzu.com/xxzapp/contract.html");
                startActivity(intent);
                break;
            case R.id.ll_address_manager://地址管理
                intent=new Intent(this,AddressActivity.class);
                intent.putExtra("comeFrom","set");
                startActivity(intent);
                break;
            case R.id.btn_out_login://退出登录
                if(btn_out_login.getText().toString().equals("登录")){
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }else{
                    shareUtil.setString("phone", "");
                    shareUtil.setString("cid","");
                    shareUtil.setString("token", "");
                    shareUtil.setBoolean("IsLogin",false);
                    shareUtil.setString("phone","");
                    finish();
                }
                break;
        }
    }
}
