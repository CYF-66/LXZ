package com.xx.lxz.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.adapter.OrderAdapter;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.base.BaseFragment;
import com.xx.lxz.bean.Order;
import com.xx.lxz.bean.OrderDTO;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.PublicParams;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.swipetoloadlayout.OnLoadMoreListener;
import com.xx.lxz.widget.swipetoloadlayout.OnRefreshListener;
import com.xx.lxz.widget.swipetoloadlayout.SuperRefreshRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.xx.lxz.api.MessageCode.GETPAYORDERLIST;

/**
 * Created by chenyf45 on 2018/5/29.
 */

public class ToBePayOrderFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.super_recyclerview)
    SuperRefreshRecyclerView refreshRecyclerView;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View mConvertView;

    private CustomProgressDialog customProgressDialog;
    private int currentpage=1;//当前页
    private List<OrderDTO> dataList=new ArrayList<>();
    /**
     * 实例化
     * @param sectionNumber
     * @return
     */
    public static ToBePayOrderFragment newInstance(int sectionNumber) {
        ToBePayOrderFragment fragment = new ToBePayOrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mConvertView = inflater.inflate(R.layout.fragment_tobepay_order, null);
        return mConvertView;
    }

    @Override
    public void initView() {
        customProgressDialog=new CustomProgressDialog(mContext);
    }

    @Override
    protected void initData() {
        currentpage=1;
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        refreshRecyclerView.init(manager,this,this);
        refreshRecyclerView.setRefreshEnabled(true);
        refreshRecyclerView.setLoadingMoreEnable(false);

//        OrderAdapter adapter = new OrderAdapter(mContext, dataList);
//        refreshRecyclerView.setAdapter(adapter);

        getBookList();
    }

    /**
     * 获取订单列表
     */
    private void getBookList() {
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
                        jsonobject.put("state", 1);
                        jsonobject.put("pageNum ", currentpage);
                        jsonobject.put("pageSize", 12);

                        String result= HttpService.httpClientPost(mContext, HttpConstant.QUERYORDERLIST,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = GETPAYORDERLIST;
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
                case GETPAYORDERLIST:
                    Order order = new Gson().fromJson(result,
                            Order.class);

                    if(order.getCode()==1){
                        if(order.getData().getList()!=null&&order.getData().getList().size()>0){
                            ll_no_data.setVisibility(View.GONE);
                            refreshRecyclerView.setVisibility(View.VISIBLE);
                            if(!dataList.isEmpty()){
                                dataList.clear();
                            }
                            List<OrderDTO> data=new ArrayList<>();

                            for (int i = 0; i < order.getData().getList().size(); i++) {
                                //详情数据
                                List<Order.data.OrderMode.OrderDetailsMode> detailsDates=order.getData().getList().get(i).getPaymentList();

                                OrderDTO bean = new OrderDTO();
                                bean.setContentDates(order.getData().getList().get(i));
                                bean.setDetailsDates(detailsDates);
                                dataList.add(bean);
                            }
                            OrderAdapter adapter = new OrderAdapter(mContext, dataList,"1");
                            refreshRecyclerView.setAdapter(adapter);

                        }else{
                            ll_no_data.setVisibility(View.VISIBLE);
                            refreshRecyclerView.setVisibility(View.GONE);
//                            ToastUtil.ToastShort(mContext,"没有数据！！！");
                        }
                    }else{
                        ll_no_data.setVisibility(View.VISIBLE);
                        refreshRecyclerView.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    private void onLoad() {
        refreshRecyclerView.setRefreshing(false);
        refreshRecyclerView.setLoadingMore(false);
    }

    @Override
    public void onLoadMore() {
        currentpage++;
        new AsyncTask<String, Integer, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject obj = null;
                try {
                    JSONObject jsonobject = new JSONObject();
                    jsonobject.put("state", 1);
                    jsonobject.put("pageNum ", currentpage);
                    jsonobject.put("pageSize", 12);

                    String result= HttpService.httpClientPost(mContext, HttpConstant.QUERYORDERLIST,jsonobject);

                    if ("".equals(result) || null == result || "null".equals(result)) {
                        return obj;
                    }
                    try {
                        obj = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return obj;
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                if ("".equals(result) || null == result || "null".equals(result)) {
                    onLoad();
                    return;
                }
                String Rresult = result.toString();
                Order order = new Gson().fromJson(Rresult,
                        Order.class);

                if(order.getCode()==1){
                    if(order.getData().getList()!=null&&order.getData().getList().size()>0){
                        ll_no_data.setVisibility(View.GONE);
                        refreshRecyclerView.setVisibility(View.VISIBLE);
//                        if(!dataList.isEmpty()){
//                            dataList.clear();
//                        }
                        for (int i = 0; i < order.getData().getList().size(); i++) {
                            //详情数据
                            List<Order.data.OrderMode.OrderDetailsMode> detailsDates=order.getData().getList().get(i).getPaymentList();

                            OrderDTO bean = new OrderDTO();
                            bean.setContentDates(order.getData().getList().get(i));
                            bean.setDetailsDates(detailsDates);
                            dataList.add(bean);
                        }
                        OrderAdapter adapter = new OrderAdapter(mContext, dataList,"1");
                        refreshRecyclerView.setAdapter(adapter);

                    }else{
                        ll_no_data.setVisibility(View.VISIBLE);
                        refreshRecyclerView.setVisibility(View.GONE);
                    }
                    onLoad();
                }else{
                    ll_no_data.setVisibility(View.VISIBLE);
                    refreshRecyclerView.setVisibility(View.GONE);
                    onLoad();
                }
            }
        }.execute(new String[]{});
    }

    @Override
    public void onRefresh() {
        currentpage=1;
        new AsyncTask<String, Integer, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject obj = null;
                try {
                    JSONObject jsonobject = new JSONObject();
                    jsonobject.put("state", 1);
                    jsonobject.put("pageNum ", currentpage);
                    jsonobject.put("pageSize", 12);

                    String result= HttpService.httpClientPost(mContext, HttpConstant.QUERYORDERLIST,jsonobject);

                    if ("".equals(result) || null == result || "null".equals(result)) {
                        return obj;
                    }
                    try {
                        obj = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return obj;
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                if ("".equals(result) || null == result || "null".equals(result)) {
                    onLoad();
                    return;
                }
                String Rresult = result.toString();
                Order order = new Gson().fromJson(Rresult,
                        Order.class);

                if(order.getCode()==1){
                    if(order.getData().getList()!=null&&order.getData().getList().size()>0){
                        ll_no_data.setVisibility(View.GONE);
                        refreshRecyclerView.setVisibility(View.VISIBLE);
                        if(!dataList.isEmpty()){
                            dataList.clear();
                        }
                        for (int i = 0; i < order.getData().getList().size(); i++) {
                            //详情数据
                            List<Order.data.OrderMode.OrderDetailsMode> detailsDates=order.getData().getList().get(i).getPaymentList();

                            OrderDTO bean = new OrderDTO();
                            bean.setContentDates(order.getData().getList().get(i));
                            bean.setDetailsDates(detailsDates);
                            dataList.add(bean);
                        }
                        OrderAdapter adapter = new OrderAdapter(mContext, dataList,"1");
                        refreshRecyclerView.setAdapter(adapter);

                    }else{
                        ll_no_data.setVisibility(View.VISIBLE);
                        refreshRecyclerView.setVisibility(View.GONE);
                    }
                    onLoad();
                }else{
                    ll_no_data.setVisibility(View.VISIBLE);
                    refreshRecyclerView.setVisibility(View.GONE);
                    onLoad();
                }
            }
        }.execute(new String[]{});
    }

    @Override
    public void onResume() {
        super.onResume();
        PublicParams.orderType="1";
    }
}
