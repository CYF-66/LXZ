package com.xx.lxz.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xx.lxz.R;
import com.xx.lxz.alipay.PayMent;
import com.xx.lxz.alipay.PayResult;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.BillDetails;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xx.lxz.api.MessageCode.PAYRORDERESULT;

public class PayDetailsActivity extends BaseActivity {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_qi)
    TextView tv_qi;
    @BindView(R.id.tv_rent_money)
    TextView tv_rent_money;
    @BindView(R.id.tv_out_of_money)
    TextView tv_out_of_money;
    @BindView(R.id.tv_out_of_day)
    TextView tv_out_of_day;
    @BindView(R.id.tv_deadline)
    TextView tv_deadline;
    @BindView(R.id.tv_real_date)
    TextView tv_real_date;
    @BindView(R.id.tv_pay)
    TextView tv_pay;

    @BindView(R.id.btn_pay)
    Button btn_pay;

    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    private BillDetails dataMsg;
    private String pid;
    private String bid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_details);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        pid=getIntent().getStringExtra("pid");
        bid=getIntent().getStringExtra("bid");
        mActivity=PayDetailsActivity.this;
        customProgressDialog=new CustomProgressDialog(mActivity);
        tv_title.setText("订单支付");
        getBillWithId();
    }

    @OnClick({R.id.iv_back,R.id.btn_pay})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.btn_pay://支付
//                payV2();
                PayMent payMent=new PayMent(mActivity,handler);
                payMent.requestPayInfo(bid,pid);
                break;
        }
    }

    /**
     * 查询账单
     */
    private void getBillWithId() {
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
                        jsonobject.put("pid", pid);

                        String result= HttpService.httpClientPost(mActivity, HttpConstant.GETBILLSWITHID,jsonobject);

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

            switch (msg.what) {
                case MessageCode.GETBILLS:
                    String result = (String) msg.obj;
                    customProgressDialog.dismiss();
                    if (result == null || result == "") {
//                ToastUtil.ToastShort(mActivity, "token验证失败,请重新登录");
                        return;
                    }
                    dataMsg = new Gson().fromJson(result,
                            BillDetails.class);
                    if(dataMsg.getCode()==1){

                        if(dataMsg.getData()!=null){
                            tv_rent_money.setText(dataMsg.getData().getMoney());
                            tv_qi.setText("第"+dataMsg.getData().getCurphase()+"期");
                            tv_out_of_money.setText(dataMsg.getData().getRealydate()+"元");
                            String deadline=dataMsg.getData().getDate();
                            String real=dataMsg.getData().getRealydate();
                            tv_money.setText(dataMsg.getData().getTotalmoney()+"元");
                            tv_out_of_day.setText(dataMsg.getData().getExpectday());
                            tv_deadline.setText(deadline);
                            tv_real_date.setText(real);
                            tv_out_of_money.setText(dataMsg.getData().getExpectfee());
                            tv_pay.setText("￥"+dataMsg.getData().getTotalmoney());
                        }

                    }else{
                        ToastUtil.ToastShort(mActivity,dataMsg.getMessage());
                    }
                    break;
                case PAYRORDERESULT:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };

}
