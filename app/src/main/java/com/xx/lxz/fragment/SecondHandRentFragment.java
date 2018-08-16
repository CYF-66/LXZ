package com.xx.lxz.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.activity.home.ProductDetailsActivity;
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseFragment;
import com.xx.lxz.base.BaseResult;
import com.xx.lxz.bean.Product;
import com.xx.lxz.model.ProductHolder;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.swipetoloadlayout.OnLoadMoreListener;
import com.xx.lxz.widget.swipetoloadlayout.OnRefreshListener;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class SecondHandRentFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.super_recyclerview)
    RecyclerView refreshRecyclerView;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.et_link)
    EditText et_link;

    private ModelRecyclerAdapter modelRecyclerAdapter;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View mConvertView;
    private CustomProgressDialog customProgressDialog;

    private Product productList;
    private String product_id;

    /**
     * 实例化
     * @param sectionNumber
     * @return
     */
    public static SecondHandRentFragment newInstance(int sectionNumber) {
        SecondHandRentFragment fragment = new SecondHandRentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mConvertView = inflater.inflate(R.layout.fragment_second_hand_rent, null);
        return mConvertView;
    }

    @Override
    public void initView() {
        customProgressDialog=new CustomProgressDialog(mContext);
    }

    @Override
    protected void initData() {
        if(modelRecyclerAdapter==null){
            refreshRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
            refreshRecyclerView.setNestedScrollingEnabled(false);
            queryProduct();
        }
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    @OnClick({R.id.btn_submit})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_submit://提交
                String link=et_link.getText().toString();
                if(link.equals("")){
                    ToastUtil.ToastShort(mContext, "请输入链接地址");
                    return;
                }
                addLinkt(link);
                break;
        }
    }

    /**
     * 查询首页产品价格
     */
    private void queryProduct() {
        if (!NetUtil.checkNet(mContext)) {
            ToastUtil.ToastShort(mContext, "网络异常");
        } else {
            customProgressDialog.show();
            class StartThread extends Thread {
                @Override
                public void run() {
                    super.run();
                    try {
                        JSONObject jsonobject = new JSONObject();
//                        jsonobject.put("type", 1);

                        String result= HttpService.httpClientPost(mContext, HttpConstant.QUERYUSERDPRODUCT,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.QUERYUSEDPRODUCT;
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
     * 外部链接申请地址
     */
    private void addLinkt(final String link) {
        if (!NetUtil.checkNet(mContext)) {
            ToastUtil.ToastShort(mContext, "网络异常");
        } else {
            customProgressDialog.show();
            class StartThread extends Thread {
                @Override
                public void run() {
                    super.run();
                    try {
                        JSONObject jsonobject = new JSONObject();
                        jsonobject.put("link", link);

                        String result= HttpService.httpClientPost(mContext, HttpConstant.ADDLINK,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.ADDLINK;
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
                case MessageCode.QUERYUSEDPRODUCT:
                    productList = new Gson().fromJson(result,
                            Product.class);
                    if(productList.getCode()==1){

                        if(productList.getData().size()>0){
                            modelRecyclerAdapter = new ModelRecyclerAdapter(ProductHolder.class,productList.getData());
                            refreshRecyclerView.setAdapter(modelRecyclerAdapter);
                            modelRecyclerAdapter.setOnItemClickListener(new ModelRecyclerAdapter.OnItemClickListener(){
                                @Override
                                public void onItemClick(View view, int position, long id) {

                                    product_id=productList.getData().get(position).getProduct_id();
                                    Intent intent=new Intent(mContext,ProductDetailsActivity.class);
                                    intent.putExtra("product_id",product_id);
                                    intent.putExtra("type","1");
                                    intent.putExtra("msgid","");
                                    startActivity(intent);
                                }
                            });
                        }
                    }else{
                        ToastUtil.ToastShort(mContext,productList.getMessage());
                    }
                    break;
                case MessageCode.ADDLINK:

                    BaseResult<String> baseResult = new Gson().fromJson(result,
                            BaseResult.class);
                    if(baseResult.getCode()==1){

                    }
                    ToastUtil.ToastShort(mContext,baseResult.getMessage());
                        break;
            }

        }
    };

}
