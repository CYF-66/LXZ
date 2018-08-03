package com.xx.lxz.activity.my.authentication;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xx.lxz.App;
import com.xx.lxz.R;
import com.xx.lxz.activity.MainActivity;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.Common;
import com.xx.lxz.bean.StepMode;
import com.xx.lxz.bean.TargetEntity;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.PhotoUtils;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.SizeUtils;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomDialog;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.StepView;
import com.xx.lxz.widget.itemdecoration.LinearLayoutDecoration;

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

    //拍照或相册
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    private File fileUri1 = new File(Environment.getExternalStorageDirectory().getPath() + "/front_photo.jpg");
    private File fileCropUri1 = new File(Environment.getExternalStorageDirectory().getPath() + "/front_crop_photo.jpg");
    private File fileUri2 = new File(Environment.getExternalStorageDirectory().getPath() + "/back_photo.jpg");
    private File fileCropUri2 = new File(Environment.getExternalStorageDirectory().getPath() + "/back_crop_photo.jpg");
    private File fileUri3 = new File(Environment.getExternalStorageDirectory().getPath() + "/icon_photo.jpg");
    private File fileCropUri3 = new File(Environment.getExternalStorageDirectory().getPath() + "/icon_crop_photo.jpg");

    private Uri imageUri1;
    private Uri cropImageUri1;
    private Uri imageUri2;
    private Uri cropImageUri2;
    private Uri imageUri3;
    private Uri cropImageUri3;
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    private ImageLoader imageLoader = null;
    private DisplayImageOptions options = null;

    private String fromFlag="1";

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

//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration
//                .createDefault(mActivity));
//        options = new DisplayImageOptions.Builder()
//                .displayer(new RoundedBitmapDisplayer(0xff000000, 10))
//                .showStubImage(R.mipmap.addpic).cacheInMemory().cacheOnDisc()
//                .build();

        customProgressDialog=new CustomProgressDialog(mActivity);

//        String IDFrontImg=shareUtil.getString("IDFrontImg");
//        String IDBackImg=shareUtil.getString("IDBackImg");
//        String CurrentImg=shareUtil.getString("CurrentImg");
//        if(IDFrontImg!=null){
//            rl_idcard_front.setVisibility(View.GONE);
//            iv_front_after_choose.setVisibility(View.VISIBLE);
//            imageLoader.displayImage("file://" + IDFrontImg, iv_front_after_choose, options);
//        }else{
//            rl_idcard_front.setVisibility(View.VISIBLE);
//            iv_front_after_choose.setVisibility(View.GONE);
//        }
//        if(IDBackImg!=null){
//            rl_idcard_back.setVisibility(View.GONE);
//            iv_idcard_after_choose.setVisibility(View.VISIBLE);
//            imageLoader.displayImage("file://" + IDBackImg, iv_idcard_after_choose, options);
//        }else{
//            rl_idcard_back.setVisibility(View.VISIBLE);
//            iv_idcard_after_choose.setVisibility(View.GONE);
//            imageLoader.displayImage("file://" + IDFrontImg, iv_id_back, options);
//        }
//        if(CurrentImg!=null){
//            rl_take_photo.setVisibility(View.GONE);
//            iv_takephoto_after_choose.setVisibility(View.VISIBLE);
//            imageLoader.displayImage("file://" + CurrentImg, iv_takephoto_after_choose, options);
//        }else{
//            rl_take_photo.setVisibility(View.VISIBLE);
//            iv_takephoto_after_choose.setVisibility(View.GONE);
//        }
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

                if(!fileCropUri1.exists()){
                    ToastUtil.ToastShort(mActivity,"请上传身份证正面照");
                    return;
                }
                if(!fileCropUri2.exists()){
                    ToastUtil.ToastShort(mActivity,"请上传身份证背面照");
                    return;
                }
                if(!fileCropUri3.exists()){
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
                    if(fromFlag.equals("1")){
                        cropImageUri1 = Uri.fromFile(fileCropUri1);
                        PhotoUtils.cropImageUri(mActivity, imageUri1, cropImageUri1, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    }else if(fromFlag.equals("2")){
                        cropImageUri2 = Uri.fromFile(fileCropUri2);
                        PhotoUtils.cropImageUri(mActivity, imageUri2, cropImageUri2, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    }else{
                        cropImageUri3 = Uri.fromFile(fileCropUri3);
                        PhotoUtils.cropImageUri(mActivity, imageUri3, cropImageUri3, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    }
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        if(fromFlag.equals("1")){
                            cropImageUri1 = Uri.fromFile(fileCropUri1);
                            Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                newUri = FileProvider.getUriForFile(mActivity, "com.xx.lxz.fileprovider", new File(newUri.getPath()));
                            }
                            PhotoUtils.cropImageUri(mActivity, newUri, cropImageUri1, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);

                        }else if(fromFlag.equals("2")){
                            cropImageUri2 = Uri.fromFile(fileCropUri2);
                            Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                newUri = FileProvider.getUriForFile(mActivity, "com.xx.lxz.fileprovider", new File(newUri.getPath()));
                            }
                            PhotoUtils.cropImageUri(mActivity, newUri, cropImageUri2, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                        }else{
                            cropImageUri3 = Uri.fromFile(fileCropUri3);
                            Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                newUri = FileProvider.getUriForFile(mActivity, "com.xx.lxz.fileprovider", new File(newUri.getPath()));
                            }
                            PhotoUtils.cropImageUri(mActivity, newUri, cropImageUri3, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                        }


                    } else {
                        ToastUtil.ToastShort(mActivity, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap;
                    if(fromFlag.equals("1")){
                        bitmap = PhotoUtils.getBitmapFromUri(cropImageUri1, mActivity);
                        Log.i("TEST","fileCropUri1="+fileCropUri1);
                        Log.i("TEST","cropImageUri1.getPath()="+cropImageUri1.getPath());
                    }else if(fromFlag.equals("2")){
                        bitmap = PhotoUtils.getBitmapFromUri(cropImageUri2, mActivity);
                        Log.i("TEST","fileCropUri2="+fileCropUri2);
                        Log.i("TEST","cropImageUri2.getPath()="+cropImageUri2.getPath());
                    }else{
                        bitmap = PhotoUtils.getBitmapFromUri(cropImageUri3, mActivity);
                        Log.i("TEST","fileCropUri3="+fileCropUri3);
                        Log.i("TEST","cropImageUri3.getPath()="+cropImageUri3.getPath());
                    }

//                    saveCusHeadImg(fileCropUri);
                    if (bitmap != null) {
                        if(fromFlag.equals("1")){
                            iv_front_after_choose.setImageBitmap(bitmap);
                            shareUtil.setString("IDFrontImg", cropImageUri1.getPath());
                        }else if(fromFlag.equals("2")){
                            iv_idcard_after_choose.setImageBitmap(bitmap);
                            shareUtil.setString("IDBackImg", cropImageUri2.getPath());
                        }else{
                            iv_takephoto_after_choose.setImageBitmap(bitmap);
                            shareUtil.setString("CurrentImg", cropImageUri3.getPath());
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
                        if(fromFlag.equals("1")){

                            imageUri1 = Uri.fromFile(fileUri1);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                imageUri1 = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri1);//通过FileProvider创建一个content类型的Uri
                            }
                            PhotoUtils.takePicture(this, imageUri1, CODE_CAMERA_REQUEST);

                        }else if(fromFlag.equals("2")){
                            imageUri2 = Uri.fromFile(fileUri2);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                imageUri2 = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri2);//通过FileProvider创建一个content类型的Uri
                            }
                            PhotoUtils.takePicture(this, imageUri2, CODE_CAMERA_REQUEST);
                        }else{
                            imageUri3 = Uri.fromFile(fileUri3);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                imageUri3 = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri3);//通过FileProvider创建一个content类型的Uri
                            }
                            PhotoUtils.takePicture(this, imageUri3, CODE_CAMERA_REQUEST);
                        }


                    } else {
                        ToastUtil.ToastShort(mActivity, "设备没有SD卡！");
                    }

                    if (hasSdcard()) {
                        if(fromFlag.equals("1")){
                            imageUri1 = Uri.fromFile(fileUri1);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                imageUri1 = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri1);//通过FileProvider创建一个content类型的Uri
                            }
                            PhotoUtils.takePicture(this, imageUri1, CODE_CAMERA_REQUEST);
                        }else if(fromFlag.equals("2")){
                            imageUri2 = Uri.fromFile(fileUri2);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                imageUri2 = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri2);//通过FileProvider创建一个content类型的Uri
                            }
                            PhotoUtils.takePicture(this, imageUri2, CODE_CAMERA_REQUEST);
                        }else{
                            imageUri3 = Uri.fromFile(fileUri3);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                imageUri3 = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri3);//通过FileProvider创建一个content类型的Uri
                            }
                            PhotoUtils.takePicture(this, imageUri3, CODE_CAMERA_REQUEST);
                        }

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
                if(fromFlag.equals("1")){
                    imageUri1 = Uri.fromFile(fileUri1);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri1 = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri1);//通过FileProvider创建一个content类型的Uri
                    }
                    PhotoUtils.takePicture(this, imageUri1, CODE_CAMERA_REQUEST);
                }else if(fromFlag.equals("2")){
                    imageUri2 = Uri.fromFile(fileUri2);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri2 = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri2);//通过FileProvider创建一个content类型的Uri
                    }
                    PhotoUtils.takePicture(this, imageUri2, CODE_CAMERA_REQUEST);
                }else{
                    imageUri3 = Uri.fromFile(fileUri3);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri3 = FileProvider.getUriForFile(this, "com.xx.lxz.fileprovider", fileUri3);//通过FileProvider创建一个content类型的Uri
                    }
                    PhotoUtils.takePicture(this, imageUri3, CODE_CAMERA_REQUEST);
                }

            } else {
                ToastUtil.ToastShort(this, "设备没有SD卡！");
            }
        }
    }
}
