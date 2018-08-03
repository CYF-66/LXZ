package com.xx.lxz.activity.my;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xx.lxz.R;
import com.xx.lxz.base.BaseActivity;
import com.xx.lxz.util.StatusBarUtil;
import com.xx.lxz.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bqs.crawler.cloud.sdk.BqsCrawlerCloudSDK.REQUEST_CODE;

public class CusSerActivity extends BaseActivity {

    //标题
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_cus_phone)
    TextView tv_cus_phone;//打电话给客服

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_ser);
        StatusBarUtil.setStatusBarColor(this, this.getResources().getColor(R.color.colorStatue), true);
        ButterKnife.bind(this);
        mActivity=CusSerActivity.this;
        tv_title.setText("客服");
    }

    @OnClick({R.id.iv_back,R.id.tv_cus_phone})

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_cus_phone://打电话
                testCallPhone(tv_cus_phone.getText().toString());
                break;
        }
    }

    private void testCallPhone(String phone) {

        if (Build.VERSION.SDK_INT >= 23) {

            //判断有没有拨打电话权限
            if (PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                //请求拨打电话权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

            } else {
                callPhone(phone);
            }

        } else {
            callPhone(phone);
        }
    }

    /**
     * 打电话给某人
     *
     * @param phone
     */
    private void callPhone(String phone) {
        //直接拨号
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        //此处不判断就会报错
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        }
    }


    /**
     * 请求权限的回调方法
     * @param requestCode    请求码
     * @param permissions    请求的权限
     * @param grantResults   权限的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE && PermissionChecker.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            ToastUtil.ToastShort(mActivity,"授权成功");
            callPhone(tv_cus_phone.getText().toString());
        }
    }

}
