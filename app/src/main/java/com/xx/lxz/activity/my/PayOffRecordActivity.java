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
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.PayRecord;
import com.xx.lxz.model.PayRecordHolder;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.swipetoloadlayout.OnLoadMoreListener;
import com.xx.lxz.widget.swipetoloadlayout.OnRefreshListener;
import com.xx.lxz.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayOffRecordActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

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
    private PayRecord dataMsg;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_off_record);

        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=PayOffRecordActivity.this;
        tv_title.setText("租金缴纳记录");
        customProgressDialog=new CustomProgressDialog(mActivity);

        ll_no_data.setVisibility(View.GONE);
        super_recyclerview.setVisibility(View.VISIBLE);

        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        super_recyclerview.init(manager,this,this);
        super_recyclerview.setRefreshEnabled(false);
        super_recyclerview.setLoadingMoreEnable(false);

        getBills();
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
    private void getBills() {
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

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.GETBILLS,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.GETBILLS;
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
                case MessageCode.GETBILLS:
                    dataMsg = new Gson().fromJson(result,
                            PayRecord.class);
                    if(dataMsg.getCode()==1){

                        if(dataMsg.getData().size()>0){
                            adapter = new ModelRecyclerAdapter(PayRecordHolder.class,dataMsg.getData());
                            super_recyclerview.setAdapter(adapter);
                        }else{
                            ll_no_data.setVisibility(View.VISIBLE);
                            super_recyclerview.setVisibility(View.GONE);
                        }
                    }else{
                        ToastUtil.ToastShort(mActivity,dataMsg.getMessage());
                    }
                    break;
            }

        }
    };

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
