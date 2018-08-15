package com.xx.lxz.activity.my.authentication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.xx.lxz.App;
import com.xx.lxz.R;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.CommonContactBean;
import com.xx.lxz.bean.StepMode;
import com.xx.lxz.bean.TargetEntity;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.SizeUtils;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomDialog;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.StepView;
import com.xx.lxz.widget.itemdecoration.LinearLayoutDecoration;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonContactActivity extends BaseActivity {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.step_view)
    StepView mStepView;

    @BindView(R.id.btn_add)
    Button btn_add;
    @BindView(R.id.btn_save)
    Button btn_save;

    @BindView(R.id.et_relative1_name)
    EditText et_relative1_name;
    @BindView(R.id.et_relative1_phone)
    EditText et_relative1_phone;
    @BindView(R.id.et_relative1_address)
    EditText et_relative1_address;

    @BindView(R.id.et_relative2_name)
    EditText et_relative2_name;
    @BindView(R.id.et_relative2_phone)
    EditText et_relative2_phone;
    @BindView(R.id.et_relative2_address)
    EditText et_relative2_address;

    @BindView(R.id.ll_relative1_sex)
    LinearLayout ll_relative1_sex;
    @BindView(R.id.ll_relative2_sex)
    LinearLayout ll_relative2_sex;
    @BindView(R.id.ll_relative2)
    LinearLayout ll_relative2;
    @BindView(R.id.ll_relative1_relationship)
    LinearLayout ll_relative1_relationship;
    @BindView(R.id.ll_relative2_relationship)
    LinearLayout ll_relative2_relationship;

    @BindView(R.id.tv_relative1_sex)
    TextView tv_relative1_sex;
    @BindView(R.id.tv_relative2_sex)
    TextView tv_relative2_sex;
    @BindView(R.id.tv_relative1_relationship)
    TextView tv_relative1_relationship;
    @BindView(R.id.tv_relative2_relationship)
    TextView tv_relative2_relationship;

    private String from="relative1";
    private Activity mActivity;
    private Dialog mDialog;
    private List<TargetEntity> dates = new ArrayList<>();
    private CustomProgressDialog customProgressDialog;
    private boolean isEdit=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_contact);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity= CommonContactActivity.this;
        customProgressDialog = new CustomProgressDialog(mActivity);
        tv_title.setText("常用联系人");

//        List<String> steps = Arrays.asList(new String[]{"身份认证","信息认证", "联系人信息", "常用联系人","",""});
        List<StepMode> stepModes=new ArrayList<>();

        StepMode stepMode1=new StepMode();
        stepMode1.setStep(1);
        stepMode1.setTextInfo("身份认证");
        StepMode stepMode2=new StepMode();
        stepMode2.setStep(2);
        stepMode2.setTextInfo("信息认证");
        StepMode stepMode3=new StepMode();
        stepMode3.setStep(3);
        stepMode3.setTextInfo("联系人信息");
        StepMode stepMode4=new StepMode();
        stepMode4.setStep(4);
        stepMode4.setTextInfo("常用联系人");
        StepMode stepMode5=new StepMode();
        stepMode5.setStep(5);
        stepMode5.setTextInfo("");
        StepMode stepMode6=new StepMode();
        stepMode6.setStep(6);
        stepMode6.setTextInfo("");

        stepModes.add(stepMode1);
        stepModes.add(stepMode2);
        stepModes.add(stepMode3);
        stepModes.add(stepMode4);
        stepModes.add(stepMode5);
        stepModes.add(stepMode6);
        mStepView.setSteps(stepModes);
        mStepView.selectedStep(4);
    }

    @OnClick({R.id.iv_back,R.id.btn_add,R.id.btn_save,R.id.ll_relative1_sex,R.id.ll_relative2_sex,
            R.id.ll_relative1_relationship,R.id.ll_relative2_relationship})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.btn_add://展开亲属2
                ll_relative2.setVisibility(View.VISIBLE);
                btn_add.setVisibility(View.GONE);

                break;
            case R.id.ll_relative1_sex:
                if(isEdit){
                    from="relative1";
                    showDialog("sex");
                }
                break;
            case R.id.ll_relative2_sex:
                if(isEdit){
                    from="relative2";
                    showDialog("sex");
                }
                break;
            case R.id.ll_relative1_relationship:
                if(isEdit){
                    from="relative1";
                    showDialog("relationship");
                }
                break;
            case R.id.ll_relative2_relationship:
                if(isEdit){
                    from="relative2";
                    showDialog("relationship");
                }
                break;
            case R.id.btn_save://
                String relative1Name=et_relative1_name.getText().toString();
                String relative2Name=et_relative2_name.getText().toString();
                String relative1Phone=et_relative1_phone.getText().toString();
                String relative2Phone=et_relative2_phone.getText().toString();
                String relative1Address=et_relative1_address.getText().toString();
                String relative2Address=et_relative2_address.getText().toString();

                String relative1Sex=tv_relative1_sex.getText().toString();
                String relative2Sex=tv_relative2_sex.getText().toString();
                String relative1= tv_relative1_relationship.getText().toString();
                String relative2=tv_relative2_relationship.getText().toString();

                if(TextUtils.isEmpty(relative1Name)){
                    ToastUtil.ToastShort(mActivity,"联系人1姓名不能为空");
                    return;
                }
                if(TextUtils.isEmpty(relative1Sex)){
                    ToastUtil.ToastShort(mActivity,"联系人1性别不能为空");
                    return;
                }
                if(TextUtils.isEmpty(relative1Phone)){
                    ToastUtil.ToastShort(mActivity,"联系人1手机号不能为空");
                    return;
                }
                if(TextUtils.isEmpty(relative1)){
                    ToastUtil.ToastShort(mActivity,"联系人1关系不能为空");
                    return;
                }
                if(TextUtils.isEmpty(relative1Address)){
                    ToastUtil.ToastShort(mActivity,"联系人1备注不能为空");
                    return;
                }
                if(TextUtils.isEmpty(relative2Name)){
                    ToastUtil.ToastShort(mActivity,"联系人2姓名不能为空");
                    return;
                }
                if(TextUtils.isEmpty(relative2Sex)){
                    ToastUtil.ToastShort(mActivity,"联系人2性别不能为空");
                    return;
                }
                if(TextUtils.isEmpty(relative2Phone)){
                    ToastUtil.ToastShort(mActivity,"联系人2手机号不能为空");
                    return;
                }
                if(TextUtils.isEmpty(relative2)){
                    ToastUtil.ToastShort(mActivity,"联系人2关系不能为空");
                    return;
                }
                if(TextUtils.isEmpty(relative2Address)){
                    ToastUtil.ToastShort(mActivity,"联系人2备注不能为空");
                    return;
                }


                setContactInfo(relative1Name,relative1Sex,relative1Phone,relative1,relative1Address,
                        relative2Name,relative2Sex,relative2Phone,relative2,relative2Address);
                break;
        }
    }
    /**
     * 性别选择框
     */
    private void showDialog(final String type){

        if(dates.size()>0){
            dates.clear();
        }
        if(type.equals("sex")){
            dates.add(new TargetEntity("男","man"));
            dates.add(new TargetEntity("女","woman"));
        }else{
            dates.add(new TargetEntity("朋友","friend"));
            dates.add(new TargetEntity("同事","colleague"));
            dates.add(new TargetEntity("其他","other"));
        }
        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(mActivity).inflate(R.layout.layout_bottom_recyclerview, null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new LinearLayoutDecoration(mActivity, LinearLayoutDecoration.VERTICAL, SizeUtils.dp2px(mActivity, 1), ContextCompat.getColor(mActivity, R.color.line_gray)));

        BaseQuickAdapter adapter = new BaseQuickAdapter<TargetEntity, BaseViewHolder>(R.layout.item_work_choose_time, dates) {
            @Override
            protected void convert(BaseViewHolder helper, TargetEntity item) {
                TextView tvChooseTime = helper.getView(R.id.tv_choose_time);
                tvChooseTime.setText(item.getText());
//                tvChooseTime.setSelected(item.isSelected());
            }
        };
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                adapter.notifyDataSetChanged();

                if(type.equals("sex")){
                    if(from.equals("relative1")){
                        tv_relative1_sex.setText(dates.get(position).getText());
                    }else{
                        tv_relative2_sex.setText(dates.get(position).getText());
                    }
                }else{
                    if(from.equals("relative1")){
                        tv_relative1_relationship.setText(dates.get(position).getText());
                    }else{
                        tv_relative2_relationship.setText(dates.get(position).getText());
                    }
                }
                mDialog.dismiss();
            }
        });

//        if (mDialog == null) {
        mDialog = new CustomDialog.Builder(mActivity)
                .setNotitle(true)
                .setCancelable(true)
                .setContentView(recyclerView)
                .setBottomDialog(true)
                .setWidth(App.SCREEN_WIDTH)
                .create();
//        }
        mDialog.show();
    }

    /**
     * 获取联系人信息
     */
    private void getContactInfo() {
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
//                        jsonobject.put("id", product_id);

                        String result = HttpService.httpClientPost(mActivity, HttpConstant.GETCONTACTINFO, jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.GETCONTACTINFO;
                        msg.obj = result;
                        msg.sendToTarget();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            StartThread st = new StartThread();
            st.start();
        }
    }

    /**
     * 设置联系人信息
     */
    private void setContactInfo(final String family_linkone_name,final String family_linkone_sex,final String family_linkone_phone,final String family_linkone_relation,final String family_linkone_address,
                                final String family_linktwo_name,final String family_linktwo_sex,final String family_linktwo_phone,final String family_linktwo_relation,final String family_linktwo_address) {
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
                        jsonobject.put("lname1", family_linkone_name);
                        jsonobject.put("lsex1", family_linkone_sex);
                        jsonobject.put("lphone1", family_linkone_phone);
                        jsonobject.put("lrelation1", family_linkone_relation);
                        jsonobject.put("lmemo1", family_linkone_address);
                        jsonobject.put("lname2", family_linktwo_name);
                        jsonobject.put("lsex2", family_linktwo_sex);
                        jsonobject.put("lphone2", family_linktwo_phone);
                        jsonobject.put("lrelation2", family_linktwo_relation);
                        jsonobject.put("lmemo2", family_linktwo_address);

                        String result = HttpService.httpClientPost(mActivity, HttpConstant.CUSTOMCONTACTINFO, jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.SAVECUSCONTACTINFO;
                        msg.obj = result;
                        msg.sendToTarget();
                    } catch (Exception e) {
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
                case MessageCode.GETCONTACTINFO:
                    CommonContactBean contactBean=new Gson().fromJson(result,
                            CommonContactBean.class);
                    if(contactBean.getCode()==1){//进入到身份认证页面
                        if(contactBean.getData()!=null&&contactBean.getData().size()>0){

                            for(int i=0;i<contactBean.getData().size();i++){

                                if(i==0){
                                    et_relative1_name.setText(contactBean.getData().get(i).getLname());
                                    et_relative1_phone.setText(contactBean.getData().get(i).getLphone());
                                    et_relative1_address.setText(contactBean.getData().get(i).getLmemo());
                                    tv_relative1_sex.setText(contactBean.getData().get(i).getLsex());
                                    tv_relative1_relationship.setText(contactBean.getData().get(i).getLrelation());
                                }else if(i==1){
                                    et_relative2_name.setText(contactBean.getData().get(i).getLname());
                                    et_relative2_phone.setText(contactBean.getData().get(i).getLphone());
                                    et_relative2_address.setText(contactBean.getData().get(i).getLmemo());
                                    tv_relative2_sex.setText(contactBean.getData().get(i).getLsex());
                                    tv_relative2_relationship.setText(contactBean.getData().get(i).getLrelation());
                                }
                            }
                        }
                        et_relative1_name.setFocusable(true);
                        et_relative1_name.setFocusableInTouchMode(true);
                        et_relative1_phone.setFocusable(true);
                        et_relative1_phone.setFocusableInTouchMode(true);
                        et_relative1_address.setFocusable(true);
                        et_relative1_address.setFocusableInTouchMode(true);

                        et_relative2_name.setFocusable(true);
                        et_relative2_name.setFocusableInTouchMode(true);
                        et_relative2_phone.setFocusable(true);
                        et_relative2_phone.setFocusableInTouchMode(true);
                        et_relative2_address.setFocusable(true);
                        et_relative2_address.setFocusableInTouchMode(true);

                        isEdit=true;

                        btn_save.setVisibility(View.VISIBLE);
                    }else if(contactBean.getCode()==0){//身份已认证，请不要重复认证
                        if(contactBean.getData()!=null&&contactBean.getData().size()>0){

                            for(int i=0;i<contactBean.getData().size();i++){

                                if(i==0){
                                    et_relative1_name.setText(contactBean.getData().get(i).getLname());
                                    et_relative1_phone.setText(contactBean.getData().get(i).getLphone());
                                    et_relative1_address.setText(contactBean.getData().get(i).getLmemo());
                                    tv_relative1_sex.setText(contactBean.getData().get(i).getLsex());
                                    tv_relative1_relationship.setText(contactBean.getData().get(i).getLrelation());
                                }else if(i==1){
                                    et_relative2_name.setText(contactBean.getData().get(i).getLname());
                                    et_relative2_phone.setText(contactBean.getData().get(i).getLphone());
                                    et_relative2_address.setText(contactBean.getData().get(i).getLmemo());
                                    tv_relative2_sex.setText(contactBean.getData().get(i).getLsex());
                                    tv_relative2_relationship.setText(contactBean.getData().get(i).getLrelation());
                                }
                            }
                        }

                        et_relative1_name.setFocusable(false);
                        et_relative1_name.setFocusableInTouchMode(false);
                        et_relative1_phone.setFocusable(false);
                        et_relative1_phone.setFocusableInTouchMode(false);
                        et_relative1_address.setFocusable(false);
                        et_relative1_address.setFocusableInTouchMode(false);

                        et_relative2_name.setFocusable(false);
                        et_relative2_name.setFocusableInTouchMode(false);
                        et_relative2_phone.setFocusable(false);
                        et_relative2_phone.setFocusableInTouchMode(false);
                        et_relative2_address.setFocusable(false);
                        et_relative2_address.setFocusableInTouchMode(false);

                        isEdit=false;

                        btn_save.setVisibility(View.GONE);
                        ToastUtil.ToastShort(mActivity, contactBean.getMessage());
                    }else{
                        ToastUtil.ToastShort(mActivity, contactBean.getMessage());
                    }
                    break;
                case MessageCode.SAVECUSCONTACTINFO:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code = Integer.parseInt(jsonObject.optString("code"));
                        if (code == 1) {//进入到身份认证页面
//                            ToastUtil.ToastShort(mActivity, jsonObject.optString("message"));
                            startActivity(new Intent(mActivity,OperatorAuthenActivity.class));
                            finish();
//                            Intent intent = new Intent(mActivity, IdentifyActivity.class);
//                            startActivity(intent);
                        } else {//身份已认证，请不要重复认证
                            ToastUtil.ToastShort(mActivity, jsonObject.optString("message"));
                        }

                    } catch (Exception e) {
                        e.getMessage();
                    }
                    break;


            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getContactInfo();
    }


}
