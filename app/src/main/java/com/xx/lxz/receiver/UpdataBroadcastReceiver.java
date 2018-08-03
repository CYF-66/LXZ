package com.xx.lxz.receiver;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.xx.lxz.util.SharedPreferencesUtil;
import com.xx.lxz.util.ToastUtil;

import java.io.File;

/**
 * 广播通知
 * 自动安装
 */
public class UpdataBroadcastReceiver extends BroadcastReceiver{

    @SuppressLint("NewApi")
    public void onReceive(Context context, Intent intent) {
        SharedPreferencesUtil shareUtil = SharedPreferencesUtil.getinstance(context);
        long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long refernece = shareUtil.getlong("refernece");
        if (refernece != myDwonloadID) {
            return;
        }

        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);
        installAPK(context,downloadFileUri);
    }


    /**
     * 安装apk
     * @param context
     * @param apk
     */
    private void installAPK(Context context,Uri apk ) {
        if (Build.VERSION.SDK_INT < 23) {
            Intent intents = new Intent();
            intents.setAction("android.intent.action.VIEW");
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(apk);
            intents.setDataAndType(apk, "application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intents);
        } else {
            File file = queryDownloadedApk(context);
            if (file.exists()) {
                openFile(file, context);
            }

        }
    }

    /**
     * 通过downLoadId查询下载的apk，解决6.0以后安装的问题
     * @param context
     * @return
     */
    public static File queryDownloadedApk(Context context) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        SharedPreferencesUtil shareUtil = SharedPreferencesUtil.getinstance(context);
//        long downloadId = SharePreHelper.getIns().getLongData("refernece", -1);
        long downloadId = shareUtil.getlong("refernece");
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;

    }

    @SuppressLint("WrongConstant")
    private void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        try {
            context.startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            ToastUtil.ToastShort(context,"没有找到打开此类文件的程序");
        }

    }

    /**
     * 获取手机设备号
     * @param var0
     * @return
     */
    private String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }
}
