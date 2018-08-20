package com.xx.lxz.activity.my;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.activity.MainActivity;
import com.xx.lxz.activity.my.set.WebViewActivity;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.base.BaseResult;
import com.xx.lxz.bean.Register;
import com.xx.lxz.util.LogUtil;
import com.xx.lxz.util.MyCountTimer;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.PublicParams;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

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
    @BindView(R.id.btn_login)
    Button btn_login;//登录
    @BindView(R.id.tv_register)
    TextView tv_register;//注册
    @BindView(R.id.tv_forget)
    TextView tv_forget;//忘记密码
    @BindView(R.id.tv_agree)
    TextView tv_agree;//同意协议

    private Activity mActivity;
    private String yzmToken;
    private SharedPreferencesUtil shareUtil;
    private CustomProgressDialog customProgressDialog;
    private MyCountTimer countTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=LoginActivity.this;
        tv_title.setText("登录");
        shareUtil = SharedPreferencesUtil.getinstance(this);
        // 绑定倒计时
        countTimer = new MyCountTimer(btn_send);
        customProgressDialog=new CustomProgressDialog(mActivity);

        btn_login.setText("快速登录");
    }

    @OnClick({R.id.iv_back,R.id.iv_phone_clear,R.id.btn_send,R.id.btn_login,R.id.tv_register,R.id.tv_forget,R.id.tv_agree})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.iv_phone_clear://返回
                et_phone.setText("");
                break;
            case R.id.tv_agree://返回
                intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("title","用户协议");
                intent.putExtra("url","https://91xiaoxiangzu.com/xxzapp/login.html");
                startActivity(intent);
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
            case R.id.btn_login://登录
//                et_phone.setText("");

                final String phoneNum=et_phone.getText().toString();
                final String yzmCode=et_check_num.getText().toString();
                if (phoneNum.equals("")) {
                    ToastUtil.ToastShort(mActivity, "手机号不能为空");
                    return;
                }
                if(yzmCode.equals("")){
                    ToastUtil.ToastShort(mActivity, "验证码不能为空");
                    return;
                }

                if(PublicParams.channelId.equals("")){
                    ToastUtil.ToastShort(mActivity, "百度推送绑定失败");
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//检查是否需要权限
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        //增加获取设备信息权限
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);//申请访问设备信息权限
                    } else {//已获取权限
                        //TODO

                        login(yzmToken,yzmCode,phoneNum,PublicParams.channelId,NetUtil.getDeviceId(mActivity));
                    }
                }else{
                    login(yzmToken,yzmCode,phoneNum,PublicParams.channelId,NetUtil.getDeviceId(mActivity));
                }

//                checkPermission(new CheckPermListener() {
//                    @Override
//                    public void agreeAllPermission() {
//                        login(yzmToken,yzmCode,phoneNum,PublicParams.channelId,NetUtil.getDeviceId(mActivity));
//                    }
//                }, "需要读取设备信息权限", Manifest.permission.READ_PHONE_STATE);
                break;
            case R.id.tv_register://注册
//                et_phone.setText("");
                intent=new Intent(mActivity, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_forget://忘记密码
//                et_phone.setText("");

                break;
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
     * 发送注册验证码
     */
    private void sendLoginCode (final String phone, final String token) {
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
                        jsonobject.put("phone", phone);
                        jsonobject.put("token", token);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.SENDLOGINCODE,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.LOGINCODE;
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
    private void login(final String yzmToken, final String yzmCode,final String phone,final String channelId,final String deviceid) {
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
                        jsonobject.put("yzmToken", yzmToken);
                        jsonobject.put("yzmCode", yzmCode);
                        jsonobject.put("phone", phone);
                        jsonobject.put("channelid", channelId);
                        jsonobject.put("deviceid", deviceid);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.LOGIN,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.LOGIN;
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
                case MessageCode.YZM:
                    countTimer.onFinish();
                    BaseResult<String> loginBean = new Gson().fromJson(result,
                            BaseResult.class);
                    if(loginBean.getCode()==1){
//                        ToastUtil.ToastShort(mActivity,loginBean.getMessage());
                        String token=loginBean.getData();
                        LogUtil.d("token="+token);
                        String phone=et_phone.getText().toString();
                        if (phone.equals("")) {
                            ToastUtil.ToastShort(mActivity, "手机号不能为空");
                            return;
                        }
                        token="123456"+token+"654321";
                        sendLoginCode(phone,token);
                    }else{
                        ToastUtil.ToastShort(mActivity,loginBean.getMessage());
                    }
                    break;

                case MessageCode.LOGINCODE:
                    BaseResult<String> baseResult = new Gson().fromJson(result,
                            BaseResult.class);

                    int code=baseResult.getCode();
                    if(code==1){
                        yzmToken=baseResult.getData();
                    }
                    ToastUtil.ToastShort(mActivity,baseResult.getMessage());
                    break;

                case MessageCode.LOGIN:
                    Register registerResult = new Gson().fromJson(result,
                            Register.class);

                    if(registerResult.getCode()==1){
                        String phoneN=registerResult.getData().getCustom().getPhone();
                        shareUtil.setString("phone", phoneN);
                        shareUtil.setString("cid", registerResult.getData().getCustom().getCid());
                        shareUtil.setString("token", registerResult.getData().getToken());
                        shareUtil.setBoolean("IsLogin",true);
                        shareUtil.setString("phone",registerResult.getData().getCustom().getPhone());

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

    /**
     * 权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    login(yzmToken,et_check_num.getText().toString().trim(),et_phone.getText().toString().trim(),PublicParams.channelId,NetUtil.getDeviceId(mActivity));
                }else{
                    ToastUtil.ToastShort(mActivity,"没有访问设备信息权限，请前去设置中开启权限");
                }
                break;
            default:
                break;
        }
    }
}
