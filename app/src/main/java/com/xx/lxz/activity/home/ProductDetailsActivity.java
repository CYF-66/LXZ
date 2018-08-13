package com.xx.lxz.activity.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.activity.my.LoginActivity;
import com.xx.lxz.activity.my.authentication.AlipayAuthenActivity;
import com.xx.lxz.activity.my.authentication.CommonContactActivity;
import com.xx.lxz.activity.my.authentication.ContactActivity;
import com.xx.lxz.activity.my.authentication.IdentifyActivity;
import com.xx.lxz.activity.my.authentication.OperatorAuthenActivity;
import com.xx.lxz.activity.my.authentication.PersonInfoActivity;
import com.xx.lxz.activity.my.authentication.TBAuthenActivity;
import com.xx.lxz.activity.my.authentication.XueXinAuthenActivity;
import com.xx.lxz.adapter.ModelRecyclerAdapter;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.Apply;
import com.xx.lxz.bean.ProductDetails;
import com.xx.lxz.bean.ProductProper;
import com.xx.lxz.bean.ProductProperBean;
import com.xx.lxz.model.ProductProperHolder;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.SizeUtils;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.itemdecoration.GridLayoutDecoration;
import com.xx.lxz.widget.swipetoloadlayout.OnLoadMoreListener;
import com.xx.lxz.widget.swipetoloadlayout.OnRefreshListener;

import net.arvin.selector.utils.PSGlideUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductDetailsActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_phone_name)
    TextView tv_phone_name;
    @BindView(R.id.tv_price_of_one)
    TextView tv_price_of_one;
    @BindView(R.id.tv_total_price)
    TextView tv_total_price;
    @BindView(R.id.iv_img_url)
    ImageView iv_img_url;
    @BindView(R.id.btn_sure_apply)
    Button btn_sure_apply;

    @BindView(R.id.recyclerview_network)
    RecyclerView recyclerview_network;
    @BindView(R.id.recyclerview_color)
    RecyclerView recyclerview_color;
    @BindView(R.id.recyclerview_room)
    RecyclerView recyclerview_room;
    @BindView(R.id.recyclerview_rent_date)
    RecyclerView recyclerview_rent_date;
    public static Activity mActivity;

    private String product_id;//商品id
    private String product_leaseterm;//周期
    private String sku_id;//sku_id
    private String type;//type
    private String msgid;//msgid

    private List<ProductProper> networkList=new ArrayList<>();//网络
    private List<ProductProper> colorList=new ArrayList<>();//颜色
    private List<ProductProper> roomList=new ArrayList<>();//内存
    private List<ProductProper> rentDateList=new ArrayList<>();//租期

    private ProductDetails productDetails;
    private CustomProgressDialog customProgressDialog;
    private ModelRecyclerAdapter modelRecyclerAdapterNetWork;
    private ModelRecyclerAdapter modelRecyclerAdapterColor;
    private ModelRecyclerAdapter modelRecyclerAdapterRoom;
    private ModelRecyclerAdapter modelRecyclerAdapterRentDate;
    private List<ProductProperBean> properBeanList=new ArrayList<>();

    private String selectColor="";//选择的颜色
    private String selectNetWork="";//选择的网络
    private String selectRoom="";//选择的内存
    private String selectZq="";//选择的租期
    private String selectSignPrice="";//签约价
    private String selectCirclePrice="";//每期价格

    private SharedPreferencesUtil shareUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=ProductDetailsActivity.this;
        tv_title.setText("商品详情");
        product_id =  getIntent().getStringExtra("product_id");
        type =  getIntent().getStringExtra("type");
        msgid =  getIntent().getStringExtra("msgid");

        customProgressDialog=new CustomProgressDialog(mActivity);
        shareUtil = SharedPreferencesUtil.getinstance(mActivity);

        recyclerview_network.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerview_network.setHasFixedSize(true);
        recyclerview_network.addItemDecoration(new GridLayoutDecoration(SizeUtils.dp2px(mActivity, 10), SizeUtils.dp2px(mActivity, 10), 3));

        recyclerview_color.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerview_color.setHasFixedSize(true);
        recyclerview_color.addItemDecoration(new GridLayoutDecoration(SizeUtils.dp2px(mActivity, 10), SizeUtils.dp2px(mActivity, 10), 3));

        recyclerview_room.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerview_room.setHasFixedSize(true);
        recyclerview_room.addItemDecoration(new GridLayoutDecoration(SizeUtils.dp2px(mActivity, 10), SizeUtils.dp2px(mActivity, 10), 3));


        recyclerview_rent_date.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerview_rent_date.setHasFixedSize(true);
        recyclerview_rent_date.addItemDecoration(new GridLayoutDecoration(SizeUtils.dp2px(mActivity, 10), SizeUtils.dp2px(mActivity, 10), 3));

        queryProductDetails(product_id);
    }

    @OnClick({R.id.iv_back,R.id.btn_sure_apply})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.btn_sure_apply:

                if (!shareUtil.getBoolean("IsLogin")) {
                    intent=new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }else{
                    getApplyStep();
                }

//                ToastUtil.ToastShort(mActivity,"确认申请");
                break;
        }
    }

    /**
     * 查询产品详情
     */
    private void queryProductDetails(final String product_id) {
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
                        jsonobject.put("id", product_id);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.QUERYPRODUCTDETAILS,jsonobject);

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
    @SuppressLint("HandlerLeak")
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
                    productDetails = new Gson().fromJson(result,
                            ProductDetails.class);
                    if(productDetails.getCode()==1){

                        if(productDetails.getData()!=null&&!productDetails.getData().equals("")){
                            tv_phone_name.setText(productDetails.getData().getProduct_name());
                            PSGlideUtil.loadImage(mActivity, productDetails.getData().getProduct_detimg(), iv_img_url);

                            if(productDetails.getData().getProdPrices()!=null&&productDetails.getData().getProdPrices().size()>0){

                                for(ProductDetails.Data.ProductPrice data:productDetails.getData().getProdPrices()){
                                    ProductProperBean productProperBean=new ProductProperBean();
                                    productProperBean.setSku_id(data.getSku_id());
                                    productProperBean.setSignprice(data.getSignprice());
                                    productProperBean.setCycleprice(data.getCycleprice());

                                    List<ProductDetails.Data.ProductPrice.ProductPropes> productPropesList=data.getProdProps();
                                    if(productPropesList!=null&&productPropesList.size()>0){
                                        for(ProductDetails.Data.ProductPrice.ProductPropes productPropes:productPropesList){
                                            switch (productPropes.getProp_name()){
                                                case "网络":
                                                    productProperBean.setNetwork(productPropes.getProp_val());
                                                    break;
                                                case "颜色":
                                                    productProperBean.setColor(productPropes.getProp_val());
                                                    break;
                                                case "内存":
                                                    productProperBean.setRoom(productPropes.getProp_val());
                                                    break;
                                                case "租期":
//                                                    if(productPropes.getProp_val().equals("6月")||productPropes.getProp_val().equals("9月")){
//                                                        if(TextUtils.isEmpty(selectZq)){
//                                                            selectZq=productPropes.getProp_val();
//                                                            selectNetWork=productProperBean.get
//                                                        }
//                                                    }
                                                    productProperBean.setZuqi(productPropes.getProp_val());
                                                    break;
                                            }
                                        }
                                    }
                                    properBeanList.add(productProperBean);
                                }

                            }
                            if(productDetails.getData().getPropList()!=null&&productDetails.getData().getPropList().size()>0){

                                for(ProductDetails.Data.ProductList data:productDetails.getData().getPropList()){
                                    List<ProductDetails.Data.ProductList.ProdPropValList> productPropesList=data.getProdPropValList();
                                    if(productPropesList!=null&&productPropesList.size()>0){
                                        for(ProductDetails.Data.ProductList.ProdPropValList productPropes:productPropesList){
                                            ProductProper productProper=new ProductProper();
                                            productProper.setProperName(data.getProp_name());
                                            productProper.setProperValue(productPropes.getProp_val());
                                            switch (data.getProp_name()){
                                                case "网络":
                                                    productProper.setSeclect(false);
                                                    networkList.add(productProper);
                                                    break;
                                                case "颜色":
                                                    productProper.setSeclect(false);
                                                    colorList.add(productProper);
                                                    break;
                                                case "内存":
                                                    productProper.setSeclect(false);
                                                    roomList.add(productProper);
                                                    break;
                                                case "租期":
                                                    productProper.setSeclect(false);
                                                    rentDateList.add(productProper);
                                                    break;
                                            }
                                        }
                                    }
                                }

                                modelRecyclerAdapterNetWork = new ModelRecyclerAdapter(ProductProperHolder.class,networkList);
                                recyclerview_network.setAdapter(modelRecyclerAdapterNetWork);

                                modelRecyclerAdapterColor = new ModelRecyclerAdapter(ProductProperHolder.class,colorList);
                                recyclerview_color.setAdapter(modelRecyclerAdapterColor);

                                modelRecyclerAdapterRoom = new ModelRecyclerAdapter(ProductProperHolder.class,roomList);
                                recyclerview_room.setAdapter(modelRecyclerAdapterRoom);

                                modelRecyclerAdapterRentDate = new ModelRecyclerAdapter(ProductProperHolder.class,rentDateList);
                                recyclerview_rent_date.setAdapter(modelRecyclerAdapterRentDate);

                                modelRecyclerAdapterNetWork.setOnItemClickListener(new ModelRecyclerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position, long id) {
                                        for(int i=0;i<networkList.size();i++){
                                            if(i==position){
                                                networkList.get(i).setSeclect(true);
                                            }else{
                                                networkList.get(i).setSeclect(false);
                                            }
                                        }
                                        modelRecyclerAdapterNetWork.notifyDataSetChanged();
                                        selectNetWork=networkList.get(position).getProperValue();
                                        //筛选
                                        query();
                                    }
                                });
                                modelRecyclerAdapterColor.setOnItemClickListener(new ModelRecyclerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position, long id) {
                                        for(int i=0;i<colorList.size();i++){
                                            if(i==position){
                                                colorList.get(i).setSeclect(true);
                                            }else{
                                                colorList.get(i).setSeclect(false);
                                            }
                                        }
                                        modelRecyclerAdapterColor.notifyDataSetChanged();
                                        selectColor=colorList.get(position).getProperValue();
                                        //筛选
                                        query();
                                    }
                                });
                                modelRecyclerAdapterRoom.setOnItemClickListener(new ModelRecyclerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position, long id) {
                                        for(int i=0;i<roomList.size();i++){
                                            if(i==position){
                                                roomList.get(i).setSeclect(true);
                                            }else{
                                                roomList.get(i).setSeclect(false);
                                            }
                                        }
                                        modelRecyclerAdapterRoom.notifyDataSetChanged();
                                        selectRoom=roomList.get(position).getProperValue();

                                        //筛选
                                        query();
                                    }
                                });
                                modelRecyclerAdapterRentDate.setOnItemClickListener(new ModelRecyclerAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position, long id) {
                                        if(rentDateList.get(position).getProperValue().equals("3月")||rentDateList.get(position).getProperValue().equals("9月")){
                                            ToastUtil.ToastShort(mActivity,"该租期业务开展中,敬请期待");
                                        }else{
                                            for(int i=0;i<rentDateList.size();i++){
                                                if(i==position){
                                                    rentDateList.get(i).setSeclect(true);
                                                }else{
                                                    rentDateList.get(i).setSeclect(false);
                                                }
                                            }
                                            modelRecyclerAdapterRentDate.notifyDataSetChanged();
                                            selectZq=rentDateList.get(position).getProperValue();
                                            //筛选
                                            query();
                                        }
                                    }
                                });
                            }

                            for(ProductProperBean data:properBeanList){
                                if(data.getZuqi().equals("3月")||data.getZuqi().equals("9月")){
                                    continue;
                                }else{
                                    selectNetWork=data.getNetwork();
                                    selectRoom=data.getRoom();
                                    selectColor=data.getColor();
                                    selectZq=data.getZuqi();
                                    sku_id=data.getSku_id();
                                    selectSignPrice=data.getSignprice();
                                    selectCirclePrice=data.getCycleprice();
                                    tv_price_of_one.setText("￥"+data.getCycleprice());
                                    tv_total_price.setText("签约价￥"+data.getSignprice());
                                    break;
                                }
                            }

                            for(ProductProper color:colorList){
                                if(color.getProperValue().equals(selectColor)){
                                    color.setSeclect(true);
                                }
                            }
                            for(ProductProper room:roomList){
                                if(room.getProperValue().equals(selectRoom)){
                                    room.setSeclect(true);
                                }
                            }
                            for(ProductProper network:networkList){
                                if(network.getProperValue().equals(selectNetWork)){
                                    network.setSeclect(true);
                                }
                            }
                            for(ProductProper zuqi:rentDateList){
                                if(zuqi.getProperValue().equals(selectZq)){
                                    zuqi.setSeclect(true);
                                }
                            }
                        }else{
                            ToastUtil.ToastShort(mActivity,productDetails.getMessage());
                        }
                    }else{
                        ToastUtil.ToastShort(mActivity,productDetails.getMessage());
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
                                break;
                            case 3://亲属联系人信息页面
                                startActivity(new Intent(mActivity, ContactActivity.class));
                                break;
                            case 4://常用联系人信息页面
                                startActivity(new Intent(mActivity, CommonContactActivity.class));
                                break;
                            case 5://运营商认证
                                startActivity(new Intent(mActivity, OperatorAuthenActivity.class));
                                break;
                            case 6://支付宝认证
                                startActivity(new Intent(mActivity, AlipayAuthenActivity.class));
                                break;
                            case 7://淘宝认证
                                startActivity(new Intent(mActivity, TBAuthenActivity.class));
                                break;
                            case 8://学信网认证
                                startActivity(new Intent(mActivity, XueXinAuthenActivity.class));
                                break;
                            case 9://确认订单
                                Intent intent=new Intent(mActivity, ApplyActivity.class);
                                intent.putExtra("product_id",product_id);
                                intent.putExtra("sku_id",sku_id);
                                intent.putExtra("type",type);
                                intent.putExtra("msgid",msgid);
                                intent.putExtra("product_name",tv_phone_name.getText().toString());
                                intent.putExtra("product_zq",selectZq);
                                intent.putExtra("product_room",selectRoom);
                                intent.putExtra("product_network",selectNetWork);
                                intent.putExtra("product_color",selectColor);
                                intent.putExtra("product_sign_price",selectSignPrice);
                                intent.putExtra("product_circle_price",selectCirclePrice);
                                startActivity(intent);
                                break;
                        }
                    }
                    break;
            }

        }
    };

    private void query() {
        for(ProductProperBean productProperBean:properBeanList){
            if(productProperBean.getNetwork().equals(selectNetWork)){
                if(productProperBean.getColor().equals(selectColor)){
                    if(productProperBean.getRoom().equals(selectRoom)){
                        if(productProperBean.getZuqi().equals(selectZq)){
                            sku_id=productProperBean.getSku_id();
                            selectSignPrice=productProperBean.getSignprice();
                            selectCirclePrice=productProperBean.getCycleprice();
                            tv_price_of_one.setText("￥"+productProperBean.getCycleprice());
                            tv_total_price.setText("签约价￥"+productProperBean.getSignprice());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
    }
}
