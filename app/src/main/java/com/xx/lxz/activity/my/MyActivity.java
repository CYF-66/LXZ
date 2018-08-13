package com.xx.lxz.activity.my;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.xx.lxz.App;
import com.xx.lxz.R;
import com.xx.lxz.activity.my.set.SetActivity;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.base.BaseResult;
import com.xx.lxz.bean.RefreshModel;
import com.xx.lxz.bean.RefreshtEvent;
import com.xx.lxz.bean.TargetEntity;
import com.xx.lxz.config.GlobalConfig;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.SizeUtils;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomDialog;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.itemdecoration.LinearLayoutDecoration;

import net.arvin.selector.SelectorHelper;
import net.arvin.selector.data.ConstantData;
import net.arvin.selector.utils.PSGlideUtil;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyActivity extends BaseActivity {

    @BindView(R.id.ll_top)
    LinearLayout ll_top;

    //标题
//    @BindView(R.id.iv_back)
//    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_set)
    ImageView iv_set;
    @BindView(R.id.iv_icon)
    CircleImageView iv_icon;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_update_info)
    TextView tv_update_info;
    @BindView(R.id.tv_login)
    TextView tv_login;

    @BindView(R.id.ll_prepare_check)
    LinearLayout ll_prepare_check;
    @BindView(R.id.ll_prepare_payoff)
    LinearLayout ll_prepare_payoff;
    @BindView(R.id.ll_payoff_record)
    LinearLayout ll_payoff_record;
    @BindView(R.id.ll_identify_info)
    LinearLayout ll_identify_info;
    @BindView(R.id.ll_msg_center)
    LinearLayout ll_msg_center;
    @BindView(R.id.ll_cus_help)
    LinearLayout ll_cus_help;
    @BindView(R.id.ll_set)
    LinearLayout ll_set;

    @BindView(R.id.rl_unlogin)
    RelativeLayout rl_unlogin;
    @BindView(R.id.rl_login)
    RelativeLayout rl_login;

    private SharedPreferencesUtil shareUtil;
    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    private Dialog mDialog;
    private List<TargetEntity> dates = new ArrayList<>();

    //拍照或相册

    private ArrayList<String> selectedPictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        mActivity=MyActivity.this;
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        shareUtil = SharedPreferencesUtil.getinstance(mActivity);
        customProgressDialog=new CustomProgressDialog(mActivity);
//        View topView = StatusBarUtil.createTranslucentStatusBarView(this, getResources().getColor(R.color.white));
//        ll_top.addView(topView);

        customProgressDialog=new CustomProgressDialog(mActivity);

        PSGlideUtil.loadImage(mActivity, R.mipmap.login_logo, iv_icon);

    }

    @OnClick({R.id.iv_set,R.id.tv_update_info,R.id.ll_prepare_check,R.id.ll_prepare_payoff,R.id.ll_payoff_record,R.id.ll_identify_info,
            R.id.ll_msg_center,R.id.ll_cus_help,R.id.ll_set,R.id.tv_login,R.id.iv_icon})
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
            case R.id.iv_set://设置
                intent=new Intent(mActivity, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_update_info://完善个人信息
                intent=new Intent(mActivity, AuthenticationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_prepare_check://待审核租赁订单
                if (!shareUtil.getBoolean("IsLogin")) {
                    intent=new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }else{
                    RefreshModel refreshModel = new RefreshModel();
                    refreshModel.setActive(GlobalConfig.ACTIVE_SKIPTO);
                    refreshModel.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER);
                    refreshModel.setDataPosition(2);
                    EventBus.getDefault().post(
                            new RefreshtEvent(refreshModel));
                }
                break;
            case R.id.ll_prepare_payoff://待结清租赁订单
                if (!shareUtil.getBoolean("IsLogin")) {
                    intent=new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }else{
                    RefreshModel refreshMode2 = new RefreshModel();
                    refreshMode2.setActive(GlobalConfig.ACTIVE_SKIPTO);
                    refreshMode2.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER);
                    refreshMode2.setDataPosition(1);
                    EventBus.getDefault().post(
                            new RefreshtEvent(refreshMode2));
                }
                break;
            case R.id.ll_payoff_record://租赁结清记录
//                RefreshModel refreshMode3 = new RefreshModel();
//                refreshMode3.setActive(GlobalConfig.ACTIVE_SKIPTO);
//                refreshMode3.setPosition(GlobalConfig.REFRESHPOSITIO_ORDER);
//                refreshMode3.setDataPosition(0);
//                EventBus.getDefault().post(
//                        new RefreshtEvent(refreshMode3));

                if (!shareUtil.getBoolean("IsLogin")) {
                    intent=new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }else{
                    intent=new Intent(mActivity, PayOffRecordActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_identify_info://身份信息
                if (!shareUtil.getBoolean("IsLogin")) {
                    intent=new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }else{
                    intent=new Intent(mActivity, AuthenticationActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_msg_center://消息中心
//                if (!shareUtil.getBoolean("IsLogin")) {
//                    intent=new Intent(mActivity, LoginActivity.class);
//                    startActivity(intent);
//                }else{
//                    intent=new Intent(mActivity, MessageActivity.class);
//                    startActivity(intent);
//                }
                intent=new Intent(mActivity, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_cus_help://客服帮助
                intent=new Intent(mActivity, CusSerActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_set://设置
                intent=new Intent(mActivity, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login://登录
                intent=new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_icon://上传头像

//                if (!shareUtil.getBoolean("IsLogin")) {
//                    intent = new Intent(mActivity,
//                            LoginActivity.class);
//                    intent.putExtra("logintext", "进入首页");
//                    startActivity(intent);
//                    this.getParent().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                }else{
//                   showDialog();
//                }
                showDialog();
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
                        PSGlideUtil.loadImage(mActivity, backPics.get(0), iv_icon);
                        saveCusHeadImg(new File(backPics.get(0)));
                    }
                    break;
                default:
            }
        }
    }

    /**
     * 请求服务器  上传图片
     */
    public void saveCusHeadImg(final File file) {
        if (!NetUtil.checkNet(mActivity)) {
            ToastUtil.ToastShort(mActivity, "网络异常");
        } else {
            customProgressDialog.show();
            class StartThread extends Thread {
                @Override
                public void run() {
                    super.run();
                    try {
                        JSONObject jsonObject=new JSONObject();
//                        jsonObject.put("headimg ",file );

                        String result= HttpService.httpClientUpload(mActivity, HttpConstant.UPLOADCUSICON,file);

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
                    BaseResult<String> baseResult = new Gson().fromJson(result,
                            BaseResult.class);

                    if(baseResult.getCode()==1){
                        //保存头像地址
                        shareUtil.setString("UserIcon",baseResult.getData());
                        PSGlideUtil.loadImage(mActivity, baseResult.getData(), iv_icon);
                    }
                    break;
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (!shareUtil.getBoolean("IsLogin")) {
            rl_unlogin.setVisibility(View.VISIBLE);
            rl_login.setVisibility(View.GONE);
        }else{
            rl_unlogin.setVisibility(View.GONE);
            rl_login.setVisibility(View.VISIBLE);
            tv_phone.setText(shareUtil.getString("phone"));

            //        //加载本地头像
            String UserIcon = shareUtil.getString("UserIcon");
            if (UserIcon != null && !UserIcon.equals("")) {
                if(UserIcon.contains("http")){
                    PSGlideUtil.loadImage(mActivity, UserIcon, iv_icon);
                }
            }

        }
//        rl_unlogin.setVisibility(View.VISIBLE);
//        rl_login.setVisibility(View.GONE);
    }
}
