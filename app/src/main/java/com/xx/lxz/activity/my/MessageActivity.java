package com.xx.lxz.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
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
import com.xx.lxz.activity.home.ProductDetailsActivity;
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.Apply;
import com.xx.lxz.bean.Msg;
import com.xx.lxz.bean.RefreshtEvent;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.model.MsgHolder;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.swipetoloadlayout.OnLoadMoreListener;
import com.xx.lxz.widget.swipetoloadlayout.OnRefreshListener;
import com.xx.lxz.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.super_recyclerview)
    SuperRefreshRecyclerView super_recyclerview;

    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    private ModelRecyclerAdapter adapter;
    private Msg dataMsg;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=MessageActivity.this;
        tv_title.setText("消息中心");
        customProgressDialog=new CustomProgressDialog(mActivity);

        ll_no_data.setVisibility(View.GONE);
        super_recyclerview.setVisibility(View.VISIBLE);

        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        super_recyclerview.init(manager,this,this);
        super_recyclerview.setRefreshEnabled(false);
        super_recyclerview.setLoadingMoreEnable(false);

        queryMessage();
    }
    @OnClick({R.id.iv_back})

    public void ck(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
        }
    }

    /**
     * 查询消息
     */
    private void queryMessage() {
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

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.QUERYMESSAGE,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.MYMESSAGE;
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
     * 查询申请情况
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
//                        jsonobject.put("type", 1);

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
                case MessageCode.MYMESSAGE:
                    dataMsg = new Gson().fromJson(result,
                            Msg.class);
                    if(dataMsg.getCode()==1){

                        if(dataMsg.getData().size()>0){
                            adapter = new ModelRecyclerAdapter(MsgHolder.class,dataMsg.getData());
                            super_recyclerview.setAdapter(adapter);
                        }else{
                            ll_no_data.setVisibility(View.VISIBLE);
                            super_recyclerview.setVisibility(View.GONE);
                        }
                    }else{
                        ToastUtil.ToastShort(mActivity,dataMsg.getMessage());
                    }
                    break;

                case MessageCode.GETAPPLYSTEP:
                    Apply apply=new Gson().fromJson(result,
                            Apply.class);
                    if(apply.getCode()==1){
                        switch (apply.getData().getStep()){
                            case 1://身份信息页面
                                startActivity(new Intent(mActivity, IdentifyActivity.class));
                                finish();
                                break;
                            case 2://个人信息页面
                                startActivity(new Intent(mActivity, PersonInfoActivity.class));
                                finish();
                                break;
                            case 3://亲属联系人信息页面
                                startActivity(new Intent(mActivity, ContactActivity.class));
                                finish();
                                break;
                            case 4://常用联系人信息页面
                                startActivity(new Intent(mActivity, CommonContactActivity.class));
                                finish();
                                break;
                            case 5://运营商认证
                                startActivity(new Intent(mActivity, OperatorAuthenActivity.class));
                                finish();
                                break;
                            case 6://支付宝认证
                                startActivity(new Intent(mActivity, AlipayAuthenActivity.class));
                                finish();
                                break;
                            case 7://淘宝认证
                                startActivity(new Intent(mActivity, TBAuthenActivity.class));
                                finish();
                                break;
                            case 8://学信网认证
                                startActivity(new Intent(mActivity, XueXinAuthenActivity.class));
                                finish();
                                break;
                            case 9://确认订单
                                Intent intent=new Intent(mActivity,ProductDetailsActivity.class);
                                intent.putExtra("product_id","");
                                intent.putExtra("type","2");
                                intent.putExtra("msgid",id);
                                startActivity(intent);
                                finish();
                                break;
                        }
                    }
                    break;
            }

        }
    };
    /**
     * 接收消息
     * @param event
     */
    @Subscriber
    public void onEventMainThread(RefreshtEvent event) {

        if(event.getMrefreshPosition()!=null){
            if(event.getMrefreshPosition().getActive().equals(GlobalConfig.ACTIVE_SKIPTO)){
                if(event.getMrefreshPosition().getPosition().equals(GlobalConfig.REFRESHPOSITIO_MSG)){
                    id=event.getMrefreshPosition().getId();
                    getApplyStep();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);// 反注册EventBus
    }

    /**
     * 刷新结束标志
     */
    private void onLoad() {
        super_recyclerview.setRefreshing(false);
        super_recyclerview.setLoadingMore(false);
    }

    @Override
    public void onLoadMore() {

        onLoad();
    }

    @Override
    public void onRefresh() {
        onLoad();
    }
}
