package com.xx.lxz.update;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xx.lxz.R;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.bean.CheckVersionBean;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.util.ToolUtils;
import com.xx.lxz.widget.MyDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * APP更新
 *
 * @author cyf
 */
@SuppressLint({"HandlerLeak", "SdCardPath"})
public class UpdateManager {

    private Context context;
    private String versionName;

    // 返回的安装包url是
    private String apkUrl = "http://file3.chn68.com/download/forex/chn_1.0.4.apk";

    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private static final String savePath = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            + "/updateDemo/";// 保存apk的文件夹
    private static final String saveFileName = savePath + "XXZ.apk";

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private TextView tv_progress;

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    tv_progress.setText(progress + "");
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 实例化
     * @param context
     * @param versionName
     */
    public UpdateManager(Context context, String versionName) {
        this.context = context;
        this.versionName = versionName;

    }

    /**
     * 检查服务器版本号和本地版本号是否需要更新
     */
    public void checkUpdateInfo() {

        if (!NetUtil.checkNet(context)) {
            ToastUtil.ToastShort(context, "网络异常");
        } else {
            class StartThread extends Thread {
                @Override
                public void run() {
                    super.run();
                    try {
                        JSONObject jsonobject = new JSONObject();
                        jsonobject.put("type", "1");
                        jsonobject.put("version", versionName);
                        String result = HttpService.httpClientPost(context, HttpConstant.CHECKVERSION, jsonobject);

                        Message msg = handler.obtainMessage();
                        msg.what = MessageCode.CHECKVERSION;
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
     * 获取服务器返回数据
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            Log.i("TEST", "更新JSON---" + result);
            if (result == null || "".equals(result)) {
                return;
            }
//			dialog.dismiss();
            switch (msg.what) {
                case MessageCode.CHECKVERSION:
                    CheckVersionBean check = new Gson().fromJson(result,
                            CheckVersionBean.class);

                    if (check.getCode() == 1) {
                        boolean HasNew = check.getData().getIsnew();
                        if (HasNew) {
                            apkUrl = check.getData().getDownloadlink();
                            showNoticeDialog();
                        }
                        Log.i("TEST", "新版本名---------->" + " " + check.getData().getVersion() + "老版本名字-----》" + versionName);
                    } else {
                        ToastUtil.ToastShort(context, check.getMessage());
                    }
            }
        }

        ;
    };

    /**
     * 更新APP
     */
    private void showNoticeDialog() {
        MyDialog dialog = null;
        MyDialog.Builder builder = new MyDialog.Builder(context);
        builder.setTitle("笑享租版本更新");
        builder.setMessage("有最新版本哦，亲赶快更新吧~");
        builder.setPositiveButton("立即更新",
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ToolUtils.getbitBitmapUtils(context).clearCache();
                        ToolUtils.getbitBitmapUtils(context).clearDiskCache();
                        ToolUtils.getbitBitmapUtils(context).clearMemoryCache();
                        showDownloadDialog();
                    }
                });

        builder.setNegativeButton("以后再说",
                new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(0);//强制退出
                    }
                });

        if (dialog == null) {
            dialog = builder.create();
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 下载进度条
     */
    private void showDownloadDialog() {
        Builder builder = new Builder(context);
        builder.setTitle("笑享租版本更新");

        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_update_client, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        tv_progress = (TextView) v.findViewById(R.id.tv_progress);
        tv_progress.setText("0");
        builder.setView(v);
        builder.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
                System.exit(0);
            }
        });
        downloadDialog = builder.create();
        downloadDialog.setCancelable(false);
        downloadDialog.show();
        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);

                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

//				File file = new File(savePath);
//				if (!file.exists()) {
//					file.mkdir();
//				} else {
//					file.delete();
//				}

                File dir = StorageUtils.getCacheDirectory(context);

                String apkName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1,

                        apkUrl.length());

                File ApkFile = new File(dir, apkName);
//				String apkFile = saveFileName;
////				File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        downloadDialog.dismiss();
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        File dir = StorageUtils.getCacheDirectory(context);

        String apkName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1,

                apkUrl.length());
        File apkfile = new File(dir, apkName);
        if (!apkfile.exists()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(context, "com.dy.trade.fileprovider", apkfile);//在AndroidManifest中的android:authorities值
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }

    }
}
