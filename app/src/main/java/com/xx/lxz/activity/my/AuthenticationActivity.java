package com.xx.lxz.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.activity.my.authentication.AlipayAuthenActivity;
import com.xx.lxz.activity.my.authentication.CommonContactActivity;
import com.xx.lxz.activity.my.authentication.ContactActivity;
import com.xx.lxz.activity.my.authentication.IdentifyActivity;
import com.xx.lxz.activity.my.authentication.OperatorAuthenActivity;
import com.xx.lxz.activity.my.authentication.PersonInfoActivity;
import com.xx.lxz.activity.my.authentication.TBAuthenActivity;
import com.xx.lxz.activity.my.authentication.XueXinAuthenActivity;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.Apply;
import com.xx.lxz.bean.Common;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthenticationActivity extends BaseActivity {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_identify_info)
    LinearLayout ll_identify_info;
    @BindView(R.id.ll_per_info)
    LinearLayout ll_per_info;
    @BindView(R.id.ll_contact_info)
    LinearLayout ll_contact_info;
    @BindView(R.id.ll_contact_info_usually)
    LinearLayout ll_contact_info_usually;
    @BindView(R.id.ll_run)
    LinearLayout ll_run;
    @BindView(R.id.ll_taobao)
    LinearLayout ll_taobao;
    @BindView(R.id.ll_zhifubao)
    LinearLayout ll_zhifubao;
    @BindView(R.id.ll_zhengxin)
    LinearLayout ll_zhengxin;

    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    private int step=1;
    private int localCurrentStep=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=AuthenticationActivity.this;
        tv_title.setText("认证信息");
        customProgressDialog=new CustomProgressDialog(mActivity);
    }

    @OnClick({R.id.iv_back,R.id.ll_identify_info,R.id.ll_per_info,R.id.ll_contact_info,R.id.ll_contact_info_usually,
            R.id.ll_run,R.id.ll_taobao,R.id.ll_zhifubao,R.id.ll_zhengxin})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.ll_identify_info://身份信息
                localCurrentStep=1;
                getIdenAuthInfo();
//                intent=new Intent(mActivity,IdentifyActivity.class);
//                startActivity(intent);
                break;
            case R.id.ll_per_info://个人信息
                localCurrentStep=2;
                if(step>=localCurrentStep){
                    intent=new Intent(mActivity,PersonInfoActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.ToastShort(mActivity,"请先认证身份信息！");
                }
                break;
            case R.id.ll_contact_info://联系人信息
                localCurrentStep=3;
                if(step>=localCurrentStep){
                    intent=new Intent(mActivity,ContactActivity.class);
                    startActivity(intent);
                }else{
                    switch (step){
                        case 1:
                            ToastUtil.ToastShort(mActivity,"请先认证身份信息！");
                            break;
                        case 2:
                            ToastUtil.ToastShort(mActivity,"请先认证个人信息！");
                            break;
                    }
                }
                break;
            case R.id.ll_contact_info_usually://常用联系人
                localCurrentStep=4;
                if(step>=localCurrentStep){
                    intent=new Intent(mActivity,CommonContactActivity.class);
                    startActivity(intent);
                }else{
                    switch (step){
                        case 1:
                            ToastUtil.ToastShort(mActivity,"请先认证身份信息！");
                            break;
                        case 2:
                            ToastUtil.ToastShort(mActivity,"请先认证个人信息！");
                            break;
                        case 3:
                            ToastUtil.ToastShort(mActivity,"请先认证联系人信息！");
                            break;
                    }
                }
                break;
            case R.id.ll_run://运营商认证
                localCurrentStep=5;
                if(step>localCurrentStep){
                    ToastUtil.ToastShort(mActivity,"运营商已认证！");
                }else if(step==localCurrentStep){
                    intent=new Intent(mActivity,OperatorAuthenActivity.class);
                    startActivity(intent);
                }else{
                    switch (step){
                        case 1:
                            ToastUtil.ToastShort(mActivity,"请先认证身份信息！");
                            break;
                        case 2:
                            ToastUtil.ToastShort(mActivity,"请先认证个人信息！");
                            break;
                        case 3:
                            ToastUtil.ToastShort(mActivity,"请先认证联系人信息！");
                            break;
                        case 4:
                            ToastUtil.ToastShort(mActivity,"请先认证常用联系人信息！");
                            break;
                    }
                }
                break;
            case R.id.ll_taobao://淘宝认证
                localCurrentStep=6;
                if(step>localCurrentStep){
                    ToastUtil.ToastShort(mActivity,"淘宝已认证！");
                }else if(step==localCurrentStep){
                    intent=new Intent(mActivity,TBAuthenActivity.class);
                    startActivity(intent);
                }else{
                    switch (step){
                        case 1:
                            ToastUtil.ToastShort(mActivity,"请先认证身份信息！");
                            break;
                        case 2:
                            ToastUtil.ToastShort(mActivity,"请先认证个人信息！");
                            break;
                        case 3:
                            ToastUtil.ToastShort(mActivity,"请先认证联系人信息！");
                            break;
                        case 4:
                            ToastUtil.ToastShort(mActivity,"请先认证常用联系人信息！");
                            break;
                        case 5:
                            ToastUtil.ToastShort(mActivity,"请先认证运营商！");
                            break;
                    }
                }

                break;
            case R.id.ll_zhifubao://支付宝认证
                localCurrentStep=7;
                if(step>localCurrentStep){
                    ToastUtil.ToastShort(mActivity,"支付宝已认证！");
                }else if(step==localCurrentStep){
                    intent=new Intent(mActivity,AlipayAuthenActivity.class);
                    startActivity(intent);
                }else{
                    switch (step){
                        case 1:
                            ToastUtil.ToastShort(mActivity,"请先认证身份信息！");
                            break;
                        case 2:
                            ToastUtil.ToastShort(mActivity,"请先认证个人信息！");
                            break;
                        case 3:
                            ToastUtil.ToastShort(mActivity,"请先认证联系人信息！");
                            break;
                        case 4:
                            ToastUtil.ToastShort(mActivity,"请先认证常用联系人信息！");
                            break;
                        case 5:
                            ToastUtil.ToastShort(mActivity,"请先认证运营商！");
                            break;
                        case 6:
                            ToastUtil.ToastShort(mActivity,"请先认证淘宝！");
                            break;
                    }
                }
                break;
            case R.id.ll_zhengxin://学信网认证
                localCurrentStep=8;
                if(step>localCurrentStep){
                    ToastUtil.ToastShort(mActivity,"学信网已认证！");
                }else if(step==localCurrentStep){
                    intent=new Intent(mActivity,XueXinAuthenActivity.class);
                    startActivity(intent);
                }else{
                    switch (step){
                        case 1:
                            ToastUtil.ToastShort(mActivity,"请先认证身份信息！");
                            break;
                        case 2:
                            ToastUtil.ToastShort(mActivity,"请先认证个人信息！");
                            break;
                        case 3:
                            ToastUtil.ToastShort(mActivity,"请先认证联系人信息！");
                            break;
                        case 4:
                            ToastUtil.ToastShort(mActivity,"请先认证常用联系人信息！");
                            break;
                        case 5:
                            ToastUtil.ToastShort(mActivity,"请先认证运营商！");
                            break;
                        case 6:
                            ToastUtil.ToastShort(mActivity,"请先认证淘宝！");
                            break;
                        case 7:
                            ToastUtil.ToastShort(mActivity,"请先认证支付宝！");
                            break;
                    }
                }
                break;
        }
    }

    /**
         * 查询产品详情
         */
        private void getIdenAuthInfo() {
            if (!NetUtil.checkNet(mActivity)) {
                ToastUtil.ToastShort(mActivity, "网络异常");
            } else {
                customProgressDialog.show();
                class StartThread extends Thread {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            JSONObject jsonobject = new JSONObject();
//                        jsonobject.put("id", product_id);

                            String result= HttpService.httpClientPost(mActivity, HttpConstant.GETIDENAUTHINFO,jsonobject);

                            Message msg = handler.obtainMessage();
                            msg.what = MessageCode.GETIDENAUTHINFO;
                            msg.obj = result;
                            msg.sendToTarget();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
                StartThread st = new StartThread();
                st.start();
            }
        }
    /**
         * 查看认证到哪一步
         */
        private void getApplyStep() {
            if (!NetUtil.checkNet(mActivity)) {
                ToastUtil.ToastShort(mActivity, "网络异常");
            } else {
                customProgressDialog.show();
                class StartThread extends Thread {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            JSONObject jsonobject = new JSONObject();
//                        jsonobject.put("id", product_id);

                            String result= HttpService.httpClientPost(mActivity, HttpConstant.GETAPPLYSTEP,jsonobject);

                            Message msg = handler.obtainMessage();
                            msg.what = MessageCode.GETAPPLYSTEP;
                            msg.obj = result;
                            msg.sendToTarget();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
                StartThread st = new StartThread();
                st.start();
            }
        }

        /**
         * 网络请求返回结果
         */
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                customProgressDialog.dismiss();
                if (result == null || result == "") {
//                ToastUtil.ToastShort(mActivity, "token验证失败,请重新登录");
                    return;
                }
                switch (msg.what) {
                    case MessageCode.GETIDENAUTHINFO:

                        Common common=new Gson().fromJson(result,
                                Common.class);
                        if(common.getCode()==1){//进入到身份认证页面
                            Intent intent=new Intent(mActivity,IdentifyActivity.class);
                            startActivity(intent);
                        }else{//身份已认证，请不要重复认证
                            ToastUtil.ToastShort(mActivity, common.getMessage());
                        }

                        break;

                    case MessageCode.GETAPPLYSTEP:
                        Apply apply=new Gson().fromJson(result,
                                Apply.class);
                        if(apply.getCode()==1){
                            step=apply.getData().getStep();
                        }else{
                            ToastUtil.ToastShort(mActivity,apply.getMessage());
                        }
                        break;
                }

            }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getApplyStep();
    }
}
