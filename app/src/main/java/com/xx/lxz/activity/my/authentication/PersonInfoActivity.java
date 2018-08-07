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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xx.lxz.App;
import com.xx.lxz.BuildConfig;
import com.xx.lxz.R;
import com.xx.lxz.activity.MainActivity;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.bean.Common;
import com.xx.lxz.bean.PerInfoBean;
import com.xx.lxz.bean.StepMode;
import com.xx.lxz.bean.TargetEntity;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.PhotoUtils;
import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.SizeUtils;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.ChangeDatePopwindow;
import com.xx.lxz.widget.CustomDialog;
import com.xx.lxz.widget.CustomProgressDialog;
import com.xx.lxz.widget.StepView;
import com.xx.lxz.widget.itemdecoration.LinearLayoutDecoration;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonInfoActivity extends BaseActivity {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_yes)
    ImageView iv_yes;
    @BindView(R.id.iv_no)
    ImageView iv_no;

    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;//性别
    @BindView(R.id.ll_birth_date)
    LinearLayout ll_birth_date;//出生年月
    @BindView(R.id.ll_media)
    LinearLayout ll_media;//芝麻信用视频
    @BindView(R.id.rl_idcard_front)
    RelativeLayout rl_idcard_front;//工作证
    @BindView(R.id.iv_front)
    ImageView iv_front;//
    @BindView(R.id.iv_work_pic)
    ImageView iv_work_pic;//工作证照片

    @BindView(R.id.et_mingz)
    EditText et_mingz;//民族
    @BindView(R.id.et_company_address)
    EditText et_company_address;//公司地址
    @BindView(R.id.et_work)
    EditText et_work;//工作证
    @BindView(R.id.et_money_earn)
    EditText et_money_earn;//月收入
    @BindView(R.id.et_bank_id)
    EditText et_bank_id;//银行卡卡号
    @BindView(R.id.et_bank_account)
    EditText et_bank_account;//银行开户行

    @BindView(R.id.et_jiguan)
    EditText et_jiguan;//籍贯
    @BindView(R.id.et_huji)
    EditText et_huji;//户籍
    @BindView(R.id.et_cur_addr)
    EditText et_cur_addr;//现在居住地址

    @BindView(R.id.tv_birth_date)
    TextView tv_birth_date;//出生年月
    @BindView(R.id.tv_sex)
    TextView tv_sex;//性别
    @BindView(R.id.tv_media_path)
    TextView tv_media_path;//视频路劲
    @BindView(R.id.tv_bian1)
    TextView tv_bian1;
    @BindView(R.id.tv_bian2)
    TextView tv_bian2;
    @BindView(R.id.tv_bian3)
    TextView tv_bian3;

    @BindView(R.id.step_view)
    StepView mStepView;
    @BindView(R.id.btn_save)
    Button btn_save;

    private Activity mActivity;
    private boolean isStudent = true;
    private Dialog mDialog;
    private List<TargetEntity> dates = new ArrayList<>();
    private CustomProgressDialog customProgressDialog;
    private String path;

    //拍照或相册
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CODE_VEDIO_REQUEST = 0xa3;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;
    private File vedio;

    private ImageLoader imageLoader = null;
    private DisplayImageOptions options = null;
    private SharedPreferencesUtil shareUtil;
    private boolean isEdit=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity = PersonInfoActivity.this;
        tv_title.setText("个人信息");
        shareUtil = SharedPreferencesUtil.getinstance(mActivity);
        customProgressDialog = new CustomProgressDialog(mActivity);

        tv_bian1.setText("入校时间");
        tv_bian2.setText("所属院校");
        tv_bian3.setText("学生证编号");
        iv_yes.setBackground(getResources().getDrawable(R.mipmap.tab_seclect));
        iv_no.setBackground(getResources().getDrawable(R.mipmap.tab_unseclect));
        rl_idcard_front.setVisibility(View.GONE);
//        List<String> steps = Arrays.asList(new String[]{"身份认证","信息认证","","","",""});
        List<StepMode> stepModes = new ArrayList<>();

        StepMode stepMode1 = new StepMode();
        stepMode1.setStep(1);
        stepMode1.setTextInfo("身份认证");
        StepMode stepMode2 = new StepMode();
        stepMode2.setStep(2);
        stepMode2.setTextInfo("信息认证");
        StepMode stepMode3 = new StepMode();
        stepMode3.setStep(3);
        stepMode3.setTextInfo("");
        StepMode stepMode4 = new StepMode();
        stepMode4.setStep(4);
        stepMode4.setTextInfo("");
        StepMode stepMode5 = new StepMode();
        stepMode5.setStep(5);
        stepMode5.setTextInfo("");
        StepMode stepMode6 = new StepMode();
        stepMode6.setStep(6);
        stepMode6.setTextInfo("");

        stepModes.add(stepMode1);
        stepModes.add(stepMode2);
        stepModes.add(stepMode3);
        stepModes.add(stepMode4);
        stepModes.add(stepMode5);
        stepModes.add(stepMode6);
        mStepView.setSteps(stepModes);
        mStepView.selectedStep(2);

        getPersonInfo();
    }

    @OnClick({R.id.iv_back, R.id.btn_save, R.id.iv_yes, R.id.iv_no, R.id.ll_birth_date, R.id.ll_sex,R.id.ll_media,R.id.iv_front,R.id.iv_work_pic})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.iv_yes://
                if(isEdit){
                    isStudent = true;
                    iv_yes.setBackground(getResources().getDrawable(R.mipmap.tab_seclect));
                    iv_no.setBackground(getResources().getDrawable(R.mipmap.tab_unseclect));
                    rl_idcard_front.setVisibility(View.GONE);
                    iv_work_pic.setVisibility(View.GONE);
                    tv_bian1.setText("入校时间");
                    tv_bian2.setText("所属院校");
                    tv_bian3.setText("学生证编号");
                    et_company_address.setText("");
                    et_work.setText("");
                    et_money_earn.setText("");
                }
                break;
            case R.id.iv_no://返回
                if(isEdit){
                    isStudent = false;
                    iv_yes.setBackground(getResources().getDrawable(R.mipmap.tab_unseclect));
                    iv_no.setBackground(getResources().getDrawable(R.mipmap.tab_seclect));
                    rl_idcard_front.setVisibility(View.VISIBLE);
                    iv_work_pic.setVisibility(View.VISIBLE);
                    tv_bian1.setText("公司地址");
                    tv_bian2.setText("月收入");
                    tv_bian3.setText("月消费");
                    et_company_address.setText("");
                    et_work.setText("");
                    et_money_earn.setText("");
                }
                break;
            case R.id.ll_birth_date://出生年月
                if(isEdit){
                    selectDate();
                }
                break;
            case R.id.ll_sex://性别
                if(isEdit){
                    showDialog();
                }
                break;
            case R.id.iv_front://选择
//                showDialog();
                if(isEdit){
                    showPicDialog();
                }
                break;
            case R.id.iv_work_pic://性别
//                showDialog();
                if(isEdit){
                    showPicDialog();
                }
                break;
            case R.id.ll_media://选取文件
                if(isEdit){
                    intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent,
                            CODE_VEDIO_REQUEST);
                }
                break;
            case R.id.btn_save://保存

                String sex=tv_sex.getText().toString();
                String mingzu=et_mingz.getText().toString();
                String birthday=tv_birth_date.getText().toString();

                String jiguan=et_jiguan.getText().toString();
                String huji=et_huji.getText().toString();
                String curAddr=et_cur_addr.getText().toString();

                String bian1=et_company_address.getText().toString();
                String bian2=et_work.getText().toString();
                String bian3=et_money_earn.getText().toString();

                String bankId=et_bank_id.getText().toString();
                String bankFench=et_bank_account.getText().toString();

                String video=tv_media_path.getText().toString();

                if(TextUtils.isEmpty(sex)){
                    ToastUtil.ToastShort(mActivity,"性别不能为空");
                    return;
                }
                if(TextUtils.isEmpty(mingzu)){
                    ToastUtil.ToastShort(mActivity,"名族不能为空");
                    return;
                }
                if(TextUtils.isEmpty(birthday)){
                    ToastUtil.ToastShort(mActivity,"出生年月不能为空");
                    return;
                }
                if(TextUtils.isEmpty(jiguan)){
                    ToastUtil.ToastShort(mActivity,"籍贯不能为空");
                    return;
                }
                if(TextUtils.isEmpty(huji)){
                    ToastUtil.ToastShort(mActivity,"户籍不能为空");
                    return;
                }
                if(TextUtils.isEmpty(curAddr)){
                    ToastUtil.ToastShort(mActivity,"居住地址不能为空");
                    return;
                }
                if(TextUtils.isEmpty(bian1)){
                    if(isStudent){
                        ToastUtil.ToastShort(mActivity,"入校时间不能为空");
                    }else{
                        ToastUtil.ToastShort(mActivity,"公司地址不能为空");
                    }

                    return;
                }
                if(TextUtils.isEmpty(bian2)){
                    if(isStudent){
                        ToastUtil.ToastShort(mActivity,"所属院校不能为空");
                    }else{
                        ToastUtil.ToastShort(mActivity,"工作证不能为空");
                    }
                    return;
                }
                if(TextUtils.isEmpty(bian3)){
                    ToastUtil.ToastShort(mActivity,"性别不能为空");if(isStudent){
                        ToastUtil.ToastShort(mActivity,"学生证编号不能为空");
                    }else{
                        ToastUtil.ToastShort(mActivity,"月收入/月消费不能为空");
                    }
                    return;
                }

                if(TextUtils.isEmpty(bankId)){
                    ToastUtil.ToastShort(mActivity,"银行卡卡号不能为空");
                    return;
                }
                if(TextUtils.isEmpty(bankFench)){
                    ToastUtil.ToastShort(mActivity,"银行卡开户行不能为空");
                    return;
                }
                if(TextUtils.isEmpty(video)){
                    ToastUtil.ToastShort(mActivity,"芝麻信用视频不能为空");
                    return;
                }

                Map<String ,Object> objectList=new HashMap<>();
                if(isStudent){
                    objectList.put("type","2");//学生
                    objectList.put("school",bian2);//在读学校
                    objectList.put("indatestring",bian1);//入校时间
                    objectList.put("studentcard",bian3);//学生证编号
                }else{
                    objectList.put("type","1");//成人
                    objectList.put("company",bian1);//公司地址
                    objectList.put("income",bian2);//月收入
                    objectList.put("consume",bian3);//月消费
                    objectList.put("workcardFile",fileCropUri);
                }
//                objectList.put("name","陈银飞");
                objectList.put("sex",sex);
                objectList.put("ethnic",mingzu);
                objectList.put("birthday",birthday);
                objectList.put("nativeplace",jiguan);
                objectList.put("koseki",huji);
                objectList.put("nowaddress",curAddr);
                objectList.put("bankcard",bankId);
                objectList.put("bankaddress",bankFench);
                objectList.put("zhimfvideoFile",vedio);

//                if(isStudent){
//                    objectList.put("type","2");//学生
//                    objectList.put("school","铜陵学院");//在读学校
//                    objectList.put("indatestring","2018-9-7");//入校时间
//                    objectList.put("studentcard","1106086");//学生证编号
//                }else{
//                    objectList.put("type","1");//成人
//                    objectList.put("company","上海长宁");//公司地址
//                    objectList.put("income","100000");//月收入
//                    objectList.put("consume","100000");//月消费
//                    objectList.put("workcardFile",fileCropUri);
//                }
////                objectList.put("name","陈银飞");
//                objectList.put("sex","男");
//                objectList.put("ethnic","汉族");
//                objectList.put("birthday","2018-8-9");
//                objectList.put("nativeplace","安徽");
//                objectList.put("koseki","安徽铜陵");
//                objectList.put("nowaddress","上海普陀");
//                objectList.put("bankcard","6228269876476153");
//                objectList.put("bankaddress","普陀支行");
//                objectList.put("zhimfvideoFile",vedio);

                submitMsg(objectList);
                break;


        }
    }

    /**
     * 请求服务器  保存个人信息
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

                        String result= HttpService.httpClientUploadFile(mActivity, HttpConstant.CUSTOMINFO,objectList);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.SAVECURINFO;
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
     * 性别和关系选择框
     */
    private void showDialog() {

        if (dates.size() > 0) {
            dates.clear();
        }
        dates.add(new TargetEntity("男", "man"));
        dates.add(new TargetEntity("女", "woman"));
        RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(mActivity).inflate(R.layout.layout_bottom_recyclerview, null);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new LinearLayoutDecoration(mActivity, LinearLayoutDecoration.VERTICAL, SizeUtils.dp2px(mActivity, 1), ContextCompat.getColor(mActivity, R.color.line_gray)));

        BaseQuickAdapter adapter = new BaseQuickAdapter<TargetEntity, BaseViewHolder>(R.layout.item_work_choose_time, dates) {
            @Override
            protected void convert(BaseViewHolder helper, TargetEntity item) {
                TextView tvChooseTime = helper.getView(R.id.tv_choose_time);
                tvChooseTime.setText(item.getText());
                tvChooseTime.setSelected(item.isSelected());
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

                tv_sex.setText(dates.get(position).getText());
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

    private void showPicDialog(){

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
                            newUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID+".fileprovider", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(mActivity, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtil.ToastShort(mActivity, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, mActivity);
//                    saveCusHeadImg(fileCropUri);
                    if (bitmap != null) {
                        rl_idcard_front.setVisibility(View.GONE);
                        iv_work_pic.setVisibility(View.VISIBLE);
                        iv_work_pic.setImageBitmap(bitmap);
//                        shareUtil.setString("UserIcon", cropImageUri.getPath());
                    }
                    break;
                case CODE_VEDIO_REQUEST:
                    try {
                        path=PhotoUtils.getPath(this, data.getData());
                        path=path.replace("file:///","");
//                        Uri url=data.getData();
//                        LogUtil.d("TEST","path="+path);
                        vedio = new File(path);
                        if (!vedio.exists()) {
                            vedio.mkdir();
//                            break;
                        }
                        if (vedio.length() > 100 * 1024 * 1024) {
                            ToastUtil.ToastShort(mActivity,"文件大于100M");
                            break;
                        }

                        if(path.contains("/")){
                            String[] str=path.split("/");
                            tv_media_path.setText(str[str.length-1]);
                        }else{
                            tv_media_path.setText(path);
                        }
                        //传换文件流，上传
//                        submitVedio();
                    } catch (Exception e) {
                    } catch (OutOfMemoryError e) {
                    }
                    break;
                default:
                    break;
            }
        }
    }

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
                            imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
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
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
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
                    imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtil.ToastShort(this, "设备没有SD卡！");
            }
        }
    }


    /**
     * 选择出生年月
     *
     * @return
     */
    private void selectDate() {
        ChangeDatePopwindow mChangeBirthDialog = new ChangeDatePopwindow(
                this);
        mChangeBirthDialog.setDate("2016", "1", "1");
        mChangeBirthDialog.showAtLocation(ll_birth_date, Gravity.BOTTOM, 0, 0);
        mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {

            @Override
            public void onClick(String year, String month, String day) {
                // TODO Auto-generated method stub
                StringBuilder sb = new StringBuilder();
                sb.append(year.substring(0, year.length() - 1)).append("-").append(month.substring(0, month.length() - 1)).append("-").append(day.substring(0, day.length() - 1));
                tv_birth_date.setText(sb.toString());

            }
        });
    }

    /**
     * 获取个人信息
     */
    private void getPersonInfo() {
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

                        String result = HttpService.httpClientPost(mActivity, HttpConstant.GETPERSONINFO, jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.GETPERSONINFO;
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
                case MessageCode.GETPERSONINFO:
                    PerInfoBean common=new Gson().fromJson(result,
                            PerInfoBean.class);
                    if(common.getCode()==1){//进入到身份认证页面
                        isEdit=true;
                    }else{//身份已认证，请不要重复认证
                        isEdit=false;
                        if(common.getData().getType().equals("1")){//成人
                            tv_bian1.setText("公司地址");
                            tv_bian2.setText("月收入");
                            tv_bian3.setText("月消费");
                            et_company_address.setText(common.getData().getCompany());
                            et_work.setText(common.getData().getIncome());
                            et_money_earn.setText(common.getData().getConsume());
                        }else{//学生
                            tv_bian1.setText("入校时间");
                            tv_bian2.setText("所属院校");
                            tv_bian3.setText("学生证编号");
                            et_company_address.setText(common.getData().getSchool());
                            et_work.setText(common.getData().getIndatestring());
                            et_money_earn.setText(common.getData().getStudentcard());

                        }

                        tv_sex.setText(common.getData().getSex());
                        et_mingz.setText(common.getData().getEthnic());
                        tv_birth_date.setText(common.getData().getBirthday());
                        et_jiguan.setText(common.getData().getNativeplace());
                        et_huji.setText(common.getData().getKoseki());
                        et_cur_addr.setText(common.getData().getNowaddress());
                        et_bank_id.setText(common.getData().getBankcard());
                        et_bank_account.setText(common.getData().getBankaddress());
                        tv_media_path.setText(common.getData().getZhimfvideo());

                        et_company_address.setFocusable(false);
                        et_company_address.setFocusableInTouchMode(false);
                        et_work.setFocusable(false);
                        et_work.setFocusableInTouchMode(false);
                        et_money_earn.setFocusable(false);
                        et_money_earn.setFocusableInTouchMode(false);
                        et_mingz.setFocusable(false);
                        et_mingz.setFocusableInTouchMode(false);
                        et_jiguan.setFocusable(false);
                        et_jiguan.setFocusableInTouchMode(false);
                        et_huji.setFocusable(false);
                        et_huji.setFocusableInTouchMode(false);
                        et_cur_addr.setFocusable(false);
                        et_cur_addr.setFocusableInTouchMode(false);
                        et_bank_id.setFocusable(false);
                        et_bank_id.setFocusableInTouchMode(false);
                        et_bank_account.setFocusable(false);
                        et_bank_account.setFocusableInTouchMode(false);


                        ToastUtil.ToastShort(mActivity, common.getMessage());
                    }

                    break;
                case MessageCode.SAVECURINFO:
                    Common baseResult = new Gson().fromJson(result,
                            Common.class);

                    if(baseResult.getCode()==1){
                        //保存头像地址
//                        shareUtil.setString("UserIcon",baseResult.getData());
                        int step=baseResult.getData().getStep();
                        startActivity(new Intent(mActivity,ContactActivity.class));
                        finish();

                    }else{
                        ToastUtil.ToastShort(mActivity,baseResult.getMessage());
                    }
                    break;
            }

        }
    };
}
