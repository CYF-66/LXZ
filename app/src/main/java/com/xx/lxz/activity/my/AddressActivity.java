package com.xx.lxz.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.Addr;
import com.xx.lxz.bean.Address;
import com.xx.lxz.bean.RefreshtEvent;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.model.AddressHolder;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.SizeUtils;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.itemdecoration.LinearLayoutDecoration;
import com.xx.lxz.widget.swipetoloadlayout.OnLoadMoreListener;
import com.xx.lxz.widget.swipetoloadlayout.OnRefreshListener;
import com.xx.lxz.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.super_recyclerview)
    SuperRefreshRecyclerView super_recyclerview;
    @BindView(R.id.btn_add)
    Button btn_add;

    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    private ModelRecyclerAdapter adapter;
    private Addr dataAddr;
    private String comeFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=AddressActivity.this;
        tv_title.setText("地址");
        comeFrom=getIntent().getStringExtra("comeFrom");
        customProgressDialog=new CustomProgressDialog(mActivity);

        ll_no_data.setVisibility(View.GONE);
        super_recyclerview.setVisibility(View.VISIBLE);

        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        super_recyclerview.init(manager,this,this);
        super_recyclerview.setRefreshEnabled(true);

        super_recyclerview.addItemDecoration(new LinearLayoutDecoration(mActivity, LinearLayoutDecoration.VERTICAL, SizeUtils.dp2px(mActivity, 10), ContextCompat.getColor(mActivity, R.color.qian_gray)));

//        queryMessage();
        dataAddr=new Addr();
        List<Addr.Data> list=new ArrayList<>();
        for(int i=0;i<6;i++){
            Addr.Data data=new Addr().new Data();
            if(i==0){
                data.setDefault(true);
            }else{
                data.setDefault(false);
            }
            data.setName("陈银飞"+i);
            data.setPhone("18217126725");
            data.setAddressArea("上海市普陀区"+i);
            data.setAddressDeatails("清峪路130"+i);
            list.add(data);
        }
        dataAddr.setData(list);
        adapter = new ModelRecyclerAdapter(AddressHolder.class,dataAddr.getData());
        super_recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new ModelRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, long id) {
                if(comeFrom.equals("apply")){
                    Address addr=new Address();
                    addr.setName(dataAddr.getData().get(position).getName());
                    addr.setPhone(dataAddr.getData().get(position).getPhone());
                    addr.setAddressArea(dataAddr.getData().get(position).getAddressArea());
                    addr.setAddressDeatails(dataAddr.getData().get(position).getAddressDeatails());
                    addr.setDefault(dataAddr.getData().get(position).isDefault());
                    result(addr);
                }
            }
        });
    }

    //关闭页面返回输入结果
    private void result(Address address) {
        Intent intent = new Intent();
        intent.putExtra("Address",address);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick({R.id.iv_back,R.id.btn_add})

    public void ck(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.btn_add://添加地址

                intent=new Intent(mActivity, AddAddressActivity.class);
                startActivityForResult(intent,1);
//                finish();
                break;
        }
    }

    /**
     * 刷新结束标志
     */
    private void onLoad() {
        super_recyclerview.setRefreshing(false);
        super_recyclerview.setLoadingMore(false);
    }

    /**
     * 查询地址
     */
    private void queryAddress() {
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
                    dataAddr = new Gson().fromJson(result,
                            Addr.class);
                    if(dataAddr.getCode()==1){

                        if(dataAddr.getData().size()>0){
                            adapter = new ModelRecyclerAdapter(AddressHolder.class,dataAddr.getData());
                            super_recyclerview.setAdapter(adapter);
                        }else{
                            ll_no_data.setVisibility(View.VISIBLE);
                            super_recyclerview.setVisibility(View.GONE);
                        }
                    }else{
                        ToastUtil.ToastShort(mActivity,dataAddr.getMessage());
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
            if(event.getMrefreshPosition().getActive().equals(GlobalConfig.ACTIVE_REFRESH)){
                if(event.getMrefreshPosition().getPosition().equals(GlobalConfig.REFRESHPOSITIO_ADDRESS)){
                    dataAddr.getData().remove(event.getMrefreshPosition().getDataPosition());
                    adapter.notifyDataSetChanged();
                }
            }else{
                Intent intent=new Intent(mActivity,AddAddressActivity.class);
                intent.putExtra("address",dataAddr.getData().get(event.getMrefreshPosition().getDataPosition()));
                startActivityForResult(intent,1);
            }
        }
    }

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
                        //刷新
//                        queryMessage();
                    }
                    break;
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

    @Override
    public void onLoadMore() {

        onLoad();
    }

    @Override
    public void onRefresh() {
        onLoad();
    }
}
