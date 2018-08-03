package com.xx.lxz.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

public class DownloadTask {

    /**
     * 下载apk
     *
     * @param context
     * @param url
     */
    public static void downLoadAPK(Context context, String url) {

        if (TextUtils.isEmpty(url)) {
            return;
        }

        try {
            String serviceString = Context.DOWNLOAD_SERVICE;
            final DownloadManager downloadManager = (DownloadManager) context.getSystemService(serviceString);

            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.allowScanningByMediaScanner();
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setMimeType("application/vnd.android.package-archive");

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/xky/","xunkeyi.apk");
            if (file.exists()){
                file.delete();
            }
            request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath()+"/xky/", "xunkeyi.apk");
            long refernece = downloadManager.enqueue(request);
            SharedPreferencesUtil shareUtil = SharedPreferencesUtil.getinstance(context);
            shareUtil.setlong("refernece",refernece);
//            SharePreHelper.getIns().setLongData("refernece", refernece);
        } catch (Exception exception) {
            ToastUtil.ToastShort(context,"下载失败!");
        }

    }
}
