package com.xx.lxz.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.activity.MainActivity;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.base.BaseResult;
import com.xx.lxz.bean.Register;
import com.xx.lxz.util.LogUtil;
import com.xx.lxz.util.MyCountTimer;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_phone)
    EditText et_phone;//输入手机号
    @BindView(R.id.et_check_num)
    EditText et_check_num;//输入验证码
    @BindView(R.id.iv_phone_clear)
    ImageView iv_phone_clear;//清除手机号
    @BindView(R.id.btn_send)
    Button btn_send;//发送验证码
    @BindView(R.id.btn_register)
    Button btn_register;//注册

    private Activity mActivity;
    private String yzmToken;
    private SharedPreferencesUtil shareUtil;
    private MyCountTimer countTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=RegisterActivity.this;
        tv_title.setText("注册");
        shareUtil = SharedPreferencesUtil.getinstance(this);
        // 绑定倒计时
        countTimer = new MyCountTimer(btn_send);
        btn_register.setText("快速注册");
    }

    @OnClick({R.id.iv_back,R.id.iv_phone_clear,R.id.btn_send,R.id.btn_register})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.iv_phone_clear://返回
                et_phone.setText("");
                break;
            case R.id.btn_send://发送验证码
                String phone=et_phone.getText().toString();
                if (phone.equals("")) {
                    ToastUtil.ToastShort(mActivity, "手机号不能为空");
                    return;
                }
                countTimer.start();
                getYzmToken();

                break;
            case R.id.btn_register://注册
                String phoneNum=et_phone.getText().toString();
                String yzmCode=et_check_num.getText().toString();
                if (phoneNum.equals("")) {
                    ToastUtil.ToastShort(mActivity, "手机号不能为空");
                    return;
                }
                if(yzmCode.equals("")){
                    ToastUtil.ToastShort(mActivity, "验证码不能为空");
                    return;
                }
                register(yzmToken,yzmCode,phoneNum);
//                et_phone.setText("");
                break;
        }
    }

    /**
     * 发送注册验证码
     */
    private void sendRegistCode(final String phone, final String token) {
        if (!NetUtil.checkNet(mActivity)) {
            ToastUtil.ToastShort(mActivity, "网络异常");
        } else {
            class StartThread extends Thread {
                @Override
                public void run() {
                    super.run();
                    try {
                        JSONObject jsonobject = new JSONObject();
                        jsonobject.put("phone", phone);
                        jsonobject.put("token", token);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.SENDREGISTERCODE,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.REGISTERCODE;
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
     * 注册
     */
    private void register(final String yzmToken, final String yzmCode,final String phone) {
        if (!NetUtil.checkNet(mActivity)) {
            ToastUtil.ToastShort(mActivity, "网络异常");
        } else {
            class StartThread extends Thread {
                @Override
                public void run() {
                    super.run();
                    try {
                        JSONObject jsonobject = new JSONObject();
                        jsonobject.put("yzmToken", yzmToken);
                        jsonobject.put("yzmCode", yzmCode);
                        jsonobject.put("phone", phone);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.REGISTER,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.REGISTER;
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
     * 获取手机验证码token
     */
    private void getYzmToken() {
        if (!NetUtil.checkNet(mActivity)) {
            ToastUtil.ToastShort(mActivity, "网络异常");
        } else {
            class StartThread extends Thread {
                @Override
                public void run() {
                    super.run();
                    try {
                        JSONObject jsonobject = new JSONObject();
//                        jsonobject.put("type", 1);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.REFREASHTOKEN,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.YZM;
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
            if (result == null || result == "") {
//                ToastUtil.ToastShort(mActivity, "token验证失败,请重新登录");
                return;
            }
            switch (msg.what) {
                case MessageCode.YZM:
                    countTimer.onFinish();
                    BaseResult<String> stringBaseResult = new Gson().fromJson(result,
                            BaseResult.class);
                    if(stringBaseResult.getCode()==1){
                        String token=stringBaseResult.getData();
                        LogUtil.d("token="+token);

                        String phone=et_phone.getText().toString();
                        if (phone.equals("")) {
                            ToastUtil.ToastShort(mActivity, "手机号不能为空");
                            return;
                        }
                        token="123456"+token+"654321";
                        sendRegistCode(phone,token);
                    }else{
                        ToastUtil.ToastShort(mActivity,stringBaseResult.getMessage());
                    }
                    break;

                case MessageCode.REGISTERCODE:
                    BaseResult<String> baseResult = new Gson().fromJson(result,
                            BaseResult.class);

                    int code=baseResult.getCode();
                    if(code==1){
                        yzmToken=baseResult.getData();
                    }
                    ToastUtil.ToastShort(mActivity,baseResult.getMessage());
                    break;

                case MessageCode.REGISTER:
                    Register registerResult = new Gson().fromJson(result,
                            Register.class);
                    if(registerResult.getCode()==1){
                        shareUtil.setString("phone", registerResult.getData().getCustom().getPhone());
                        shareUtil.setString("cid", registerResult.getData().getCustom().getCid());
                        shareUtil.setString("token", registerResult.getData().getToken());
                        shareUtil.setBoolean("IsLogin",true);
                        MainActivity.mainActivity.finish();
                        startActivity(new Intent(mActivity,MainActivity.class));
                        finish();
                    }else{
                        ToastUtil.ToastShort(mActivity,registerResult.getMessage());
                    }
                    break;
            }

        }
    };


}
