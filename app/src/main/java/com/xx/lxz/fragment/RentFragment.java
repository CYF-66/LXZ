package com.xx.lxz.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.activity.home.ProductDetailsActivity;
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseFragment;
import com.xx.lxz.bean.Product;
import com.xx.lxz.model.ProductHolder;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.swipetoloadlayout.OnLoadMoreListener;
import com.xx.lxz.widget.swipetoloadlayout.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RentFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.vp_home)
    ViewPager vp_home;

    @BindView(R.id.indicator)
    LinearLayout indicator;
    @BindView(R.id.ll_xiangl)
    LinearLayout ll_xiangl;//项链
    @BindView(R.id.ll_xiangs)
    LinearLayout ll_xiangs;//香水
    @BindView(R.id.ll_bw)
    LinearLayout ll_bw;//宝马
    @BindView(R.id.ll_fll)
    LinearLayout ll_fll;//法拉利
    @BindView(R.id.tv_she_more)
    TextView tv_she_more;//奢侈品更多
    @BindView(R.id.tv_car_more)
    TextView tv_car_more;//租车更多

    @BindView(R.id.super_recyclerview)
    RecyclerView refreshRecyclerView;

    private ModelRecyclerAdapter modelRecyclerAdapter;

    private List<ImageView> indicator_list; // 圆点的集合
    private List<ImageView> imageView_list; // 滑动的图片集合
    private int currentItem = 0; // 当前图片的索引号
    private int current = 0;
    private int oldPosition = 0;
    private MyAdapter adapter;
    private Handler mHandler;
    int image[] = { R.mipmap.home_banner4, R.mipmap.home_banner3,
            R.mipmap.home_banner1,
    };

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
    public static RentFragment newInstance(int sectionNumber) {
        RentFragment fragment = new RentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mConvertView = inflater.inflate(R.layout.fragment_rent, null);
        return mConvertView;
    }

    @Override
    public void initView() {
        customProgressDialog=new CustomProgressDialog(mContext);
    }

    @Override
    protected void initData() {

        if(mHandler==null){
            init();
            refreshRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
            refreshRecyclerView.setNestedScrollingEnabled(false);
            queryProduct();
        }
    }

    /**
     * 初始化轮播图 图片
     */
    private void init() {
        mHandler=new Handler();
        imageView_list = new ArrayList<ImageView>();
        for (int i = 0; i < image.length; i++) {
            ImageView imgView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            imgView.setId(i);
            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // params.setMargins(0, 0, 5, 0);
            imgView.setLayoutParams(params);
            imageView_list.add(imgView);
            imgView.setBackgroundResource(image[i]);
//            imageLoader.displayImage(advertisement.getSignUrl(), imgView,
//                    options);
//            Log.i("TEST",
//                    "advertisement.getSignUrl()-----》"
//                            + advertisement.getSignUrl());
            // imageLoader.displayImage("http://pic1.nipic.com/2008-12-09/200812910493588_2.jpg",
            // imgView, options);

        }

        indicator_list = new ArrayList<ImageView>();

        initIndicator(imageView_list.size());

        adapter = new MyAdapter();
        vp_home.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        vp_home.setOnPageChangeListener(new MyPageChangeListener());
        Log.i("TEST", "广告个数=" + imageView_list.size());
        mHandler.removeCallbacks(runnable);//把之前的接口移除掉
        mHandler.postDelayed(runnable, 2000);
        Log.i("TEST", "imageView_list长度222-----》" + imageView_list.size());

    }

    /**
     * 初始化点
     * @param length
     */
    private void initIndicator(int length) {
        View v = mConvertView.findViewById(R.id.indicator);// 线性水平布局，负责动态调整导航图标
        ((ViewGroup) v).removeAllViews();
        indicator_list.clear();
        for (int i = 0; i < length; i++) {
            ImageView imgView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 5, 0);
            imgView.setLayoutParams(params);
            if (i == 0) { // 初始化第一个为选中状态
                imgView.setBackgroundResource(R.mipmap.indicator_focused);
            } else {
                imgView.setBackgroundResource(R.mipmap.indicator_normal);
            }
            ((ViewGroup) v).addView(imgView);
            indicator_list.add(imgView);
        }

    }

    @OnClick({R.id.ll_xiangl,R.id.ll_xiangs,R.id.ll_bw,R.id.ll_fll,R.id.tv_she_more,R.id.tv_car_more})

    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.ll_xiangl://项链
                ToastUtil.ToastShort(mContext,"敬请期待");
                break;
            case R.id.ll_xiangs://香水
                ToastUtil.ToastShort(mContext,"敬请期待");
                break;
            case R.id.ll_bw://宝马
                ToastUtil.ToastShort(mContext,"敬请期待");
                break;
            case R.id.ll_fll://法拉利
                ToastUtil.ToastShort(mContext,"敬请期待");
                break;
            case R.id.tv_she_more://奢侈品更多
                ToastUtil.ToastShort(mContext,"敬请期待");
                break;
            case R.id.tv_car_more://租车更多
                ToastUtil.ToastShort(mContext,"敬请期待");
                break;
        }
    }
    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageView_list == null ? 0 : imageView_list.size();
//            return Integer.MAX_VALUE;//返回一个无限大的值，可以 无限循环
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ((ViewPager) view).addView(imageView_list.get(position % imageView_list.size()));
            return imageView_list.get(position % imageView_list.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // container.removeView(object);
            ((ViewPager) container).removeView(imageView_list.get(position % imageView_list.size()));
        }

    }

    public class ViewHandler {
        ImageView iv_adver;// 消息时间
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {


        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            indicator_list.get(oldPosition).setBackgroundResource(
                    R.mipmap.indicator_normal);
            indicator_list.get(position).setBackgroundResource(
                    R.mipmap.indicator_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    Runnable runnable=new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            if(vp_home!=null){
                vp_home.setCurrentItem(current);
            }
            if(adapter!=null){
                adapter.notifyDataSetChanged();
            }
            if(imageView_list!=null){
                current++;
                current = current % imageView_list.size();
            }
            if(mHandler!=null){
                mHandler.postDelayed(this, 2000);
            }
        }
    };

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

                        String result= HttpService.httpClientPost(mContext, HttpConstant.QUERYPRODUCT,jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.QUERYPRODUCT;
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
                case MessageCode.QUERYPRODUCT:
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

            }

        }
    };

}
