package com.xx.lxz.activity.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xx.lxz.App;
import com.xx.lxz.R;
import com.xx.lxz.activity.my.AddAddressActivity;
import com.xx.lxz.activity.my.TakeOrderSuccessActivity;
import com.xx.lxz.activity.my.set.WebViewActivity;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.Address;
import com.xx.lxz.bean.Common;
import com.xx.lxz.bean.InfoBean;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomDialog;
import com.xx.lxz.widget.CustomProgressDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyActivity extends BaseActivity {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_address_content)
    LinearLayout ll_address_content;//整个地址内容
    @BindView(R.id.ll_have_address)
    LinearLayout ll_have_address;//已选地址
    @BindView(R.id.ll_no_address)
    LinearLayout ll_no_address;//没有选取地址
    @BindView(R.id.tv_pro_name)
    TextView tv_pro_name;//商品名称
    @BindView(R.id.tv_circle)
    TextView tv_circle;//租期
    @BindView(R.id.tv_price)
    TextView tv_price;//商品价格
    @BindView(R.id.tv_imeu)
    TextView tv_imeu;//IMEU
    @BindView(R.id.tv_room)
    TextView tv_room;//内存
    @BindView(R.id.tv_other)
    TextView tv_other;//配件

    @BindView(R.id.tv_name)
    TextView tv_name;//姓名
    @BindView(R.id.tv_phone_num)
    TextView tv_phone_num;//手机号
    @BindView(R.id.tv_all_details)
    TextView tv_all_details;//地址
    @BindView(R.id.tv_color)
    TextView tv_color;//颜色
    @BindView(R.id.tv_network)
    TextView tv_network;//网络
    @BindView(R.id.btn_apply)
    Button btn_apply;//确认申请
    @BindView(R.id.tv_query)
    TextView tv_query;//征信查询协议
    @BindView(R.id.tv_rent)
    TextView tv_rent;//产品租赁服务协议
    @BindView(R.id.tv_repay)
    TextView tv_repay;//还款管理协议
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    private String addr="";//详细地址
    private String product_id;//商品id
    private String product_leaseterm;//周期
    private String sku_id;//sku_id
    private String type;//来源
    private String msgid;//消息id
    private String product_name;//商品名称
    private String product_zq;//租期
    private String product_room;//内存
    private String product_network;//网络
    private String product_color;//颜色
    private String product_sign_price;//签约价
    private String product_circle_price;//每期价格
    private SharedPreferencesUtil shareUtil;

    private Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=ApplyActivity.this;
        shareUtil = SharedPreferencesUtil.getinstance(this);
        customProgressDialog=new CustomProgressDialog(mActivity);
        tv_title.setText("确认申请");
        product_id =  getIntent().getStringExtra("product_id");
        sku_id =  getIntent().getStringExtra("sku_id");
        type =  getIntent().getStringExtra("type");
        msgid =  getIntent().getStringExtra("msgid");
        product_name =  getIntent().getStringExtra("product_name");
        product_zq =  getIntent().getStringExtra("product_zq");
        product_room =  getIntent().getStringExtra("product_room");
        product_network =  getIntent().getStringExtra("product_network");
        product_color =  getIntent().getStringExtra("product_color");
        product_sign_price =  getIntent().getStringExtra("product_sign_price");
        product_circle_price =  getIntent().getStringExtra("product_circle_price");
        tv_pro_name.setText(product_name);
        tv_room.setText(product_room);
        tv_price.setText(product_sign_price+"元");
        tv_imeu.setText(product_circle_price+"元");
        tv_color.setText(product_color);
        tv_network.setText(product_network);
        tv_circle.setText(product_zq);

        getInfo();
        btn_apply.setBackground(mActivity.getResources().getDrawable(R.drawable.bt_bg));
        btn_apply.setEnabled(true);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    btn_apply.setBackground(mActivity.getResources().getDrawable(R.drawable.bt_bg));
                    btn_apply.setEnabled(true);
                }else{
                    btn_apply.setBackground(mActivity.getResources().getDrawable(R.drawable.bt_bg_unselect));
                    btn_apply.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.iv_back,R.id.ll_address_content,R.id.btn_apply,R.id.tv_query,R.id.tv_rent,R.id.tv_repay})

    public void ck(View v) {
        Intent intent = null;
        String cid;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.ll_address_content://调转地址页面选取地址
                intent=new Intent(mActivity, AddAddressActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.tv_query://征信查询协议
                cid=shareUtil.getString("cid");
                intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("title","征信查询授权书");
                intent.putExtra("url","https://91xiaoxiangzu.com/xxzapp/proxy.html?cid"+cid);
                startActivity(intent);
                break;
            case R.id.tv_rent://产品租赁服务协议
                cid=shareUtil.getString("cid");
                intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("title","产品租赁服务协议");
                String url="https://91xiaoxiangzu.com/xxzapp/contract.html?cid="+cid+"&pid="+product_id+"&sku_id="+sku_id+"&type="+type+"&msg_id="+msgid;
                intent.putExtra("url",url);
                startActivity(intent);
                break;
            case R.id.tv_repay://还款管理协议
                intent=new Intent(this,WebViewActivity.class);
                intent.putExtra("title","还款管理协议");
                intent.putExtra("url","https://91xiaoxiangzu.com/xxzapp/paymanage.html");
                startActivity(intent);
                break;
            case R.id.btn_apply:
                showDialog();
                break;
        }
    }

    /**
     * 性别和关系选择框
     */
    private void showDialog() {

        View rootView = (View) LayoutInflater.from(mActivity).inflate(R.layout.dialog_web_view, null);
        TextView tv_agree=(TextView)rootView.findViewById(R.id.tv_agree);
        WebView webview=(WebView)rootView.findViewById(R.id.webview);
        final ProgressBar progressBar=(ProgressBar)rootView.findViewById(R.id.progressBar1);
        WebSettings webSettings = webview.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview.clearCache(true);

        webview.setVerticalScrollBarEnabled(false); // 垂直不显示
        String cid=shareUtil.getString("cid");
        String url="https://91xiaoxiangzu.com/xxzapp/contract.html?cid="+cid+"&pid="+product_id+"&sku_id="+sku_id+"&type="+type+"&msg_id="+msgid;
        webview.loadUrl(url);
//        webview.addJavascriptInterface(new JavaScriptinterface(this), "webkit");
//        String time=String.valueOf(System.currentTimeMillis());
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO 自动生成的方法存根
                if(newProgress==100){
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }

            }
        });
        tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                applyBook();
            }
        });
        if (mDialog == null) {
            mDialog = new CustomDialog.Builder(mActivity)
                    .setNotitle(true)
                    .setCancelable(true)
                    .setContentView(rootView)
                    .setBottomDialog(false)
                    .setWidth(App.SCREEN_WIDTH-App.SCREEN_WIDTH/6)
                    .setHeight(App.SCREEN_HEIGHT-App.SCREEN_HEIGHT/4)
                    .create();
        }
        mDialog.show();
    }

    /**
     * 查询地址
     */
    private void applyBook() {
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
                        jsonobject.put("product_id", product_id);
//                        jsonobject.put("product_leaseterm", product_zq);
                        jsonobject.put("address", addr);
                        jsonobject.put("msgid", msgid);//消息id
                        jsonobject.put("type", type);//1:表示从首页发起， 2：表示从消息发起
                        jsonobject.put("sku_id", sku_id);//产品详细页面所选择的颜色，内存等配置  对应的sku_id

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.GETAPPLYBOOKS,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.APPLYSURE;
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
     * 查询信息
     */
    private void getInfo() {
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
                        jsonobject.put("msgid", msgid);//消息id
                        jsonobject.put("type", type);//1:表示从首页发起， 2：表示从消息发起
                        jsonobject.put("sku_id", sku_id);//产品详细页面所选择的颜色，内存等配置  对应的sku_id

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.GETINFO,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.GETINFO;
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
                case MessageCode.APPLYSURE:
                    Common baseResult= new Gson().fromJson(result,
                            Common.class);
                    if(baseResult.getCode()==1){

//                        RefreshModel refreshMode = new RefreshModel();
//                        refreshMode.setActive(GlobalConfig.ACTIVE_SKIPTO);
//                        refreshMode.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER_CHECK);
//                        refreshMode.setDataPosition(2);
//                        EventBus.getDefault().post(
//                                new RefreshtEvent(refreshMode));
                        startActivity(new Intent(mActivity, TakeOrderSuccessActivity.class));
                        ProductDetailsActivity.mActivity.finish();
                        mActivity.finish();
//                        String addressArea=tv_show_addr.getText().toString();
//                        String phone=et_phone.getText().toString();
//                        String addressDetails=et_address_details.getText().toString();
//                        String name=et_name.getText().toString();
//                        Address addr=new Address();
//                        addr.setName(name);
//                        addr.setPhone(phone);
//                        addr.setAddressArea(addressArea);
//                        addr.setAddressDeatails(addressDetails);
//                        addr.setDefault(true);
//                        result(addr);
                    }else{
                        ToastUtil.ToastShort(mActivity,baseResult.getMessage());
                    }
                    break;

                case MessageCode.GETINFO:
                    InfoBean infoBean= new Gson().fromJson(result,
                            InfoBean.class);
                    if(infoBean.getCode()==1){
                        ll_no_address.setVisibility(View.GONE);
                        ll_have_address.setVisibility(View.VISIBLE);

                        tv_name.setText(infoBean.getData().getName());
                        tv_phone_num.setText(infoBean.getData().getPhone());
                        tv_all_details.setText("收货地址:   "+infoBean.getData().getAddress());
                        shareUtil.setString("name",infoBean.getData().getName());
                        shareUtil.setString("phone",infoBean.getData().getPhone());
                        addr=infoBean.getData().getAddress();

                    }else{
                        ToastUtil.ToastShort(mActivity,infoBean.getMessage());
                    }
                    break;
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    if (data == null) {
                        break;
                    }
                    Address address = (Address) data.getSerializableExtra("address");
                    if(address!=null){
                        ll_no_address.setVisibility(View.GONE);
                        ll_have_address.setVisibility(View.VISIBLE);

                        tv_name.setText(address.getName());
                        tv_phone_num.setText(address.getPhone());
                        tv_all_details.setText("收货地址:   "+address.getAddressArea()+address.getAddressDeatails());
                        addr=address.getAddressArea()+address.getAddressDeatails();
                    }else{
                        ll_no_address.setVisibility(View.VISIBLE);
                        ll_have_address.setVisibility(View.GONE);
                        addr="";
                    }
                    break;
            }
        }

    }
}
