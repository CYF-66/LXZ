package com.xx.lxz.activity.my.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bqs.crawler.cloud.sdk.BqsCrawlerCloudSDK;
import com.bqs.crawler.cloud.sdk.BqsParams;
import com.bqs.crawler.cloud.sdk.OnLoginResultListener;
import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.base.BaseResult;
import com.xx.lxz.bean.CusInfoBean;
import com.xx.lxz.bean.StepMode;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.StepView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xx.lxz.api.MessageCode.GETCUSINFO;
import static com.xx.lxz.api.MessageCode.TOMOBILE;

public class OperatorAuthenActivity extends BaseActivity implements OnLoginResultListener {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.btn_auth)
    Button btn_auth;

    @BindView(R.id.step_view)
    StepView mStepView;

    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_authen);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=OperatorAuthenActivity.this;
        tv_title.setText("授权认证");

        customProgressDialog=new CustomProgressDialog(mActivity);
//        List<String> steps = Arrays.asList(new String[]{"身份认证", "信息认证", "联系人信息", "常用联系人","运营商认证",""});
        List<StepMode> stepModes=new ArrayList<>();

        StepMode stepMode1=new StepMode();
        stepMode1.setStep(1);
        stepMode1.setTextInfo("身份认证");
        StepMode stepMode2=new StepMode();
        stepMode2.setStep(2);
        stepMode2.setTextInfo("信息认证");
        StepMode stepMode3=new StepMode();
        stepMode3.setStep(3);
        stepMode3.setTextInfo("联系人信息");
        StepMode stepMode4=new StepMode();
        stepMode4.setStep(4);
        stepMode4.setTextInfo("常用联系人");
        StepMode stepMode5=new StepMode();
        stepMode5.setStep(5);
        stepMode5.setTextInfo("运营商认证");
        StepMode stepMode6=new StepMode();
        stepMode6.setStep(6);
        stepMode6.setTextInfo("");

        stepModes.add(stepMode1);
        stepModes.add(stepMode2);
        stepModes.add(stepMode3);
        stepModes.add(stepMode4);
        stepModes.add(stepMode5);
        stepModes.add(stepMode6);
        mStepView.setSteps(stepModes);
        mStepView.selectedStep(5);

    }

    //授权白骑士
    private void authenBQS(String name,String phone,String identity) {
        BqsParams params = new BqsParams();
        params.setName(name);//
        params.setCertNo(identity);
        params.setMobile(phone);
        params.setPartnerId(GlobalConfig.PARTNERID);

        params.setThemeColor(getResources().getColor(R.color.white));
        params.setForeColor(getResources().getColor(R.color.black));
        params.setFontColor(getResources().getColor(R.color.black));
        params.setProgressBarColor(getResources().getColor(R.color.blue));

//        params.setDisableOneKeyLogin(true);
        BqsCrawlerCloudSDK.setParams(params);

        BqsCrawlerCloudSDK.setFromActivity(this);

        BqsCrawlerCloudSDK.setOnLoginResultListener(this);
    }

    @OnClick({R.id.iv_back,R.id.btn_auth})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.btn_auth://认证
                BqsCrawlerCloudSDK.loginMno();
//                finish();
                break;
        }
    }

    @Override
    public void onLoginSuccess(int serviceId) {
       //serviceId服务类型可以参考ServiceId类，eg:ServiceId.TB_SERVICE_ID为淘宝授权成功
        String msg = "授权成功 serviceId=" + serviceId;
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        System.out.println(msg);
        successRequest(serviceId);
    }

    @Override
    public void onLoginFailure(String resultCode, String resultDesc, int serviceId) {
//        Toast.makeText(this, resultCode +" ,resultDesc=" + resultDesc +",  serviceId=" + serviceId, Toast.LENGTH_SHORT).show();
        System.out.println("onLoginFailure resultCode=" + resultCode +" ,resultDesc=" + resultDesc +",  serviceId=" + serviceId);
        Toast.makeText(this, "取消认证", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCustomInfo();
//        toMobileAuth();
    }

    /**
     * 检测
     */
    private void toMobileAuth() {
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
//                        jsonobject.put("type", 1);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.GETMOBILEAUTH,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = TOMOBILE;
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
     * 获取客户信息
     */
    private void getCustomInfo() {
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
//                        jsonobject.put("type", 1);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.GETCUSINFO,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = GETCUSINFO;
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
     * 第三登陆成功回调
     */
    private void successRequest(final int serviceId) {
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
                        jsonobject.put("sessionid", serviceId);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.AUTHREQUEST,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.SUCCESSREQUEST;
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
                case MessageCode.TOMOBILE:
                    BaseResult<String> baseResult1 = new Gson().fromJson(result,
                            BaseResult.class);
                    if(baseResult1.getCode()==1){

                    }else{

                    }
                    break;
                case MessageCode.SUCCESSREQUEST:
                    BaseResult<String> baseResult2 = new Gson().fromJson(result,
                            BaseResult.class);
                    if(baseResult2.getCode()==1){
                        startActivity(new Intent(mActivity,TBAuthenActivity.class));
                        finish();
                    }else{
                        ToastUtil.ToastShort(mActivity,baseResult2.getMessage());
                    }
                    break;
                case MessageCode.GETCUSINFO:
                    CusInfoBean cusInfoBean = new Gson().fromJson(result,
                            CusInfoBean.class);
                    if(cusInfoBean.getCode()==1){
                        String name=cusInfoBean.getData().getName();
                        String phone=cusInfoBean.getData().getPhone();
                        String identity=cusInfoBean.getData().getIdentity();

                        authenBQS(name,phone,identity);
                    }else{
                        ToastUtil.ToastShort(mActivity,cusInfoBean.getMessage());
                    }
                    break;
            }

        }
    };
}
