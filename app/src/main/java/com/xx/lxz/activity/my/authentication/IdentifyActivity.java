package com.xx.lxz.activity.my.authentication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.xx.lxz.bean.Common;
import com.xx.lxz.bean.StepMode;
import com.xx.lxz.bean.TargetEntity;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.PermissionUtils;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.SizeUtils;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomDialog;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.StepView;
import com.xx.lxz.widget.itemdecoration.LinearLayoutDecoration;

import net.arvin.selector.SelectorHelper;
import net.arvin.selector.data.ConstantData;
import net.arvin.selector.utils.PSGlideUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IdentifyActivity extends BaseActivity {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.step_view)
    StepView mStepView;

    @BindView(R.id.iv_front_after_choose)
    ImageView iv_front_after_choose;

    @BindView(R.id.iv_idcard_after_choose)
    ImageView iv_idcard_after_choose;

    @BindView(R.id.iv_takephoto_after_choose)
    ImageView iv_takephoto_after_choose;

    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.et_id_num)
    EditText et_id_num;
    @BindView(R.id.et_name)
    EditText et_name;

    private SharedPreferencesUtil shareUtil;
    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    private Dialog mDialog;
    private List<TargetEntity> dates = new ArrayList<>();

    private File fileCropUri1 ;
    private File fileCropUri2 ;
    private File fileCropUri3 ;


    private String fromFlag="1";
    private ArrayList<String> selectedPictures = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        mActivity=IdentifyActivity.this;
        ButterKnife.bind(this);
//        List<String> steps = Arrays.asList(new String[]{"身份认证", "","","","",""});

        List<StepMode> stepModes=new ArrayList<>();

        StepMode stepMode1=new StepMode();
        stepMode1.setStep(1);
        stepMode1.setTextInfo("身份认证");
        StepMode stepMode2=new StepMode();
        stepMode2.setStep(2);
        stepMode2.setTextInfo("");
        StepMode stepMode3=new StepMode();
        stepMode3.setStep(3);
        stepMode3.setTextInfo("");
        StepMode stepMode4=new StepMode();
        stepMode4.setStep(4);
        stepMode4.setTextInfo("");
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
        mStepView.selectedStep(1);
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        tv_title.setText("身份信息");
        shareUtil = SharedPreferencesUtil.getinstance(mActivity);
        customProgressDialog=new CustomProgressDialog(mActivity);
//        View topView = StatusBarUtil.createTranslucentStatusBarView(this, getResources().getColor(R.color.white));
//        ll_top.addView(topView);

        customProgressDialog=new CustomProgressDialog(mActivity);
    }

    @OnClick({R.id.iv_back,R.id.iv_front_after_choose,R.id.iv_idcard_after_choose,R.id.iv_takephoto_after_choose,R.id.btn_next})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.iv_front:
                fromFlag="1";
                showDialog();
                break;
            case R.id.iv_front_after_choose:
                fromFlag="1";
                showDialog();
                break;
            case R.id.iv_idcard_after_choose:
                fromFlag="2";
                showDialog();
                break;
            case R.id.iv_takephoto_after_choose:
                fromFlag="3";
                showDialog();
                break;
            case R.id.btn_next:
//                ToastUtil.ToastShort(mActivity,"下一步");
                String name=et_name.getText().toString().trim();
                String idNum=et_id_num.getText().toString().trim();
                String IDFrontImg=shareUtil.getString("IDFrontImg");
                String IDBackImg=shareUtil.getString("IDBackImg");
                String CurrentImg=shareUtil.getString("CurrentImg");
                if(IDFrontImg==null&&IDFrontImg.equals("")){
                    ToastUtil.ToastShort(mActivity,"身份证正面不能为空");
                    return;
                }
                if(IDBackImg==null&&IDBackImg.equals("")){
                    ToastUtil.ToastShort(mActivity,"身份证背面不能为空");
                    return;
                }
                if(CurrentImg==null&&CurrentImg.equals("")){
                    ToastUtil.ToastShort(mActivity,"身份证正面不能为空");
                    return;
                }

                if(name.equals("")){
                    ToastUtil.ToastShort(mActivity,"姓名不能为空");
                    return;
                }
                if(idNum.equals("")){
                    ToastUtil.ToastShort(mActivity,"身份证号不能为空");
                    return;
                }

                if(fileCropUri1!=null){
                    if(!fileCropUri1.exists()){
                        ToastUtil.ToastShort(mActivity,"请上传身份证正面照");
                        return;
                    }
                }else{
                    ToastUtil.ToastShort(mActivity,"请上传身份证正面照");
                    return;
                }
                if(fileCropUri2!=null){
                    if(!fileCropUri2.exists()){
                        ToastUtil.ToastShort(mActivity,"请上传身份证背面照");
                        return;
                    }
                }else{
                    ToastUtil.ToastShort(mActivity,"请上传身份证背面照");
                    return;
                }
                if(fileCropUri3!=null){
                    if(!fileCropUri3.exists()){
                        ToastUtil.ToastShort(mActivity,"请上传本人正面照");
                        return;
                    }
                }else{
                    ToastUtil.ToastShort(mActivity,"请上传本人正面照");
                    return;
                }
                Map<String ,Object> objectList=new HashMap<>();
                objectList.put("identity_frontfile",fileCropUri1);
                objectList.put("identity_reversefile",fileCropUri2);
                objectList.put("cus_photofile",fileCropUri3);
                objectList.put("name",name);
                objectList.put("identity",idNum);

//                objectList.put("",IDFrontImg);
//                objectList.put("",IDBackImg);
//                objectList.put("",CurrentImg);

                submitMsg(objectList);

                break;
        }
    }

    private void showDialog(){

        if(dates.size()>1){
            dates.clear();
        }
        dates.add(new TargetEntity("相册","fromImg"));
        dates.add(new TargetEntity("拍照","takePhoto"));
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
                for (int i = 0; i < dates.size(); i++) {
                    dates.get(i).setSelected(false);
                }
                dates.get(position).setSelected(true);
                adapter.notifyDataSetChanged();

                if(dates.get(position).getText().equals("拍照")){
                    takePhoto();
                }else{
                    selectPicture();
                }
                mDialog.dismiss();
            }
        });

        if (mDialog == null) {
            mDialog = new CustomDialog.Builder(mActivity)
                    .setNotitle(true)
                    .setCancelable(true)
                    .setContentView(recyclerView)
                    .setBottomDialog(true)
                    .setWidth(App.SCREEN_WIDTH)
                    .create();
        }
        mDialog.show();
    }
    private void selectPicture() {
        checkPermission(new CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                SelectorHelper.selectPicture(mActivity, true,
                        true, 1001);
            }
        }, "需要拍照和读取文件权限", Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void takePhoto() {
        checkPermission(new CheckPermListener() {
            @Override
            public void agreeAllPermission() {
                SelectorHelper.takePhoto(mActivity, true, 1001);
            }
        }, "需要拍照和读取文件权限", Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 拍照或从相册获取回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1001:
                    ArrayList<String> backPics = data.getStringArrayListExtra(ConstantData.KEY_BACK_PICTURES);
                    if (backPics != null && backPics.size() > 0) {
                        selectedPictures.clear();
                        selectedPictures.addAll(backPics);

                        if(fromFlag.equals("1")){
                            PSGlideUtil.loadImage(this, backPics.get(0), iv_front_after_choose);
                            shareUtil.setString("IDFrontImg", backPics.get(0));
                            fileCropUri1=new File(backPics.get(0));
                        }else if(fromFlag.equals("2")){
                            PSGlideUtil.loadImage(this, backPics.get(0), iv_idcard_after_choose);
                            shareUtil.setString("IDFrontImg", backPics.get(0));
                            fileCropUri2=new File(backPics.get(0));
                        }else{
                            PSGlideUtil.loadImage(this, backPics.get(0), iv_takephoto_after_choose);
                            shareUtil.setString("IDFrontImg", backPics.get(0));
                            fileCropUri3=new File(backPics.get(0));
                        }
                    }
                    break;
                default:
            }
        }
    }

    /**
     * 请求服务器  上传图片
     */
    public void submitMsg(final Map<String ,Object> objectList) {
        if (!NetUtil.checkNet(mActivity)) {
            ToastUtil.ToastShort(mActivity, "网络异常");
        } else {
            customProgressDialog.show();
            class StartThread extends Thread {
                @Override
                public void run() {
                    super.run();
                    try {
//                        JSONObject jsonObject=new JSONObject();
//                        jsonObject.put("headimg ",file );


                        String result= HttpService.httpClientUploadFile(mActivity, HttpConstant.IDENAUTH,objectList);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.UPLOADHEADIMG;
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
                case MessageCode.UPLOADHEADIMG:
                    Common baseResult = new Gson().fromJson(result,
                            Common.class);

                    if(baseResult.getCode()==1){
                        //保存头像地址
//                        shareUtil.setString("UserIcon",baseResult.getData());
                        shareUtil.setString("name",et_name.getText().toString());
                        int step=baseResult.getData().getStep();
                        startActivity(new Intent(mActivity,PersonInfoActivity.class));
                        finish();

                    }else{
                        ToastUtil.ToastShort(mActivity,baseResult.getMessage());
                    }
                    break;
            }

        }
    };
}
