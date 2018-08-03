package com.xx.lxz.activity.my;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xx.lxz.App;
import com.xx.lxz.R;
import com.xx.lxz.activity.MainActivity;
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
import com.xx.lxz.util.PhotoUtils;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.SizeUtils;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.BadgeView;
import com.xx.lxz.widget.CustomDialog;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.itemdecoration.LinearLayoutDecoration;

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
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    private ImageLoader imageLoader = null;
    private DisplayImageOptions options = null;
    private BadgeView badge;

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

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
                .createDefault(mActivity));
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(0xff000000, 10))
                .showStubImage(R.mipmap.login_logo).cacheInMemory().cacheOnDisc()
                .build();

        customProgressDialog=new CustomProgressDialog(mActivity);

//        badge = new BadgeView(this, iv_msg);
//        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
//        badge.setTextColor(Color.WHITE); // 文本颜色
//        badge.setBadgeBackgroundColor(Color.RED); // 提醒信息的背景颜色，自己设置
//        badge.setTextSize(8); // 文本大小
//        badge.setBadgeMargin(0); //各边间隔
//        badge.setText("");
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
                if (!shareUtil.getBoolean("IsLogin")) {
                    intent=new Intent(mActivity, LoginActivity.class);
                    startActivity(intent);
                }else{
                    intent=new Intent(mActivity, MessageActivity.class);
                    startActivity(intent);
                }
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
                    autoObtainCameraPermission();
                }else{
                    autoObtainStoragePermission();
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
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(mActivity, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(mActivity, "com.xx.lxz.fileprovider", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(mActivity, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtil.ToastShort(mActivity, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, mActivity);
                    saveCusHeadImg(fileCropUri);
                    if (bitmap != null) {
                        iv_icon.setImageBitmap(bitmap);
                        shareUtil.setString("UserIcon", cropImageUri.getPath());
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
                    }
                    break;
            }

        }
    };

    /**
     * 申请权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE:
//                dialog.dismiss();
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        }
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtil.ToastShort(this, "设备没有SD卡！");
                    }
                } else {
                    ToastUtil.ToastShort(this, "请允许打开相机！！");
                }
                break;
            case STORAGE_PERMISSIONS_REQUEST_CODE:
//                dialog.dismiss();
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    ToastUtil.ToastShort(this, "请允许打操作SDCard！！");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 自动获取sdk权限
     */
    private void autoObtainStoragePermission() {
        mDialog.dismiss();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.mainActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        mDialog.dismiss();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtil.ToastShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(MainActivity.mainActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    imageUri = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtil.ToastShort(this, "设备没有SD卡！");
            }
        }
    }


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
                    imageLoader.displayImage(UserIcon, iv_icon, options);// 设置图片
                }else{
                    imageLoader.displayImage("file://" + UserIcon, iv_icon, options);// 设置图片
                }
            }

        }
//        rl_unlogin.setVisibility(View.VISIBLE);
//        rl_login.setVisibility(View.GONE);
    }
}
