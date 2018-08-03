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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.activity.my.address.OnAddressSelectedListener;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.base.BaseResult;
import com.xx.lxz.bean.Addr;
import com.xx.lxz.bean.Address;
import com.xx.lxz.bean.Area;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.BottomDialog;
import com.xx.lxz.widget.CustomProgressDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAddressActivity extends BaseActivity implements OnAddressSelectedListener {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_address_details)
    EditText et_address_details;
    @BindView(R.id.ll_choose_address)
    LinearLayout ll_choose_address;
    @BindView(R.id.tv_show_addr)
    TextView tv_show_addr;
    @BindView(R.id.btn_sure)
    Button btn_sure;
    private Activity mActivity;
    private BottomDialog dialog;
    private Addr.Data address;
    private CustomProgressDialog customProgressDialog;
    private SharedPreferencesUtil shareUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=AddAddressActivity.this;
        tv_title.setText("添加地址");
        customProgressDialog=new CustomProgressDialog(mActivity);
        shareUtil = SharedPreferencesUtil.getinstance(this);
        et_name.setText(shareUtil.getString("name"));
        et_name.setFocusable(false);
        et_name.setFocusableInTouchMode(false);

        et_phone.setText(shareUtil.getString("phone"));
        et_phone.setFocusable(false);
        et_phone.setFocusableInTouchMode(false);

    }

    @OnClick({R.id.iv_back,R.id.btn_sure,R.id.ll_choose_address})

    public void OnClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.ll_choose_address://
                if(dialog == null){
                    dialog = new BottomDialog(mActivity);
                    dialog.setOnAddressSelectedListener(this);
                }
                dialog.show();

                break;
            case R.id.btn_sure://选取好地址返回
                String addressArea=tv_show_addr.getText().toString();
                String phone=et_phone.getText().toString();
                String addressDetails=et_address_details.getText().toString();
                String name=et_name.getText().toString();
                if(name.equals("")){
                    ToastUtil.ToastShort(mActivity,"收货人姓名不能为空");
                    return;
                }
                if(phone.equals("")){
                    ToastUtil.ToastShort(mActivity,"手机号不能为空");
                    return;
                }
                if(addressArea.equals("")){
                    ToastUtil.ToastShort(mActivity,"地址不能为空");
                    return;
                }
                if(addressDetails.equals("")){
                    ToastUtil.ToastShort(mActivity,"请填写详细地址");
                    return;
                }


//                Address addr=new Address();
//                addr.setName(name);
//                addr.setPhone(phone);
//                addr.setAddressArea(addressArea);
//                addr.setAddressDeatails(addressDetails);
//                addr.setDefault(true);
//                result(addr);
                saveAddress(addressArea+addressDetails);
                break;
        }
    }

    //关闭页面返回输入结果
    private void result(Address address) {
        Intent intent = new Intent();
        intent.putExtra("address",address);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onAddressSelected(Area province, Area city, Area county) {
        if(dialog != null){
            dialog.dismiss();
        }
        String p= province.getName().substring(0,province.getName().length()-1);
        String cty= city.getName().substring(0,city.getName().length()-1);
        String cou= county.getName().substring(0,county.getName().length()-1);
        tv_show_addr.setText(p + cty + cou);
    }

    /**
     * 查询地址
     */
    private void saveAddress(final String address) {
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
                        jsonobject.put("shipaddress", address);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.SAVEADDRESS,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.SAVEADDR;
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
                case MessageCode.SAVEADDR:
                    BaseResult<String> baseResult= new Gson().fromJson(result,
                        BaseResult.class);
                    if(baseResult.getCode()==1){
                        String addressArea=tv_show_addr.getText().toString();
                        String phone=et_phone.getText().toString();
                        String addressDetails=et_address_details.getText().toString();
                        String name=et_name.getText().toString();
                        Address addr=new Address();
                        addr.setName(name);
                        addr.setPhone(phone);
                        addr.setAddressArea(addressArea);
                        addr.setAddressDeatails(addressDetails);
                        addr.setDefault(true);
                        result(addr);
                    }else{
                        ToastUtil.ToastShort(mActivity,baseResult.getMessage());
                    }
                    break;
            }

        }
    };

}
