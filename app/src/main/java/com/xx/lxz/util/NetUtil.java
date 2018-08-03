package com.xx.lxz.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;

public class NetUtil {

    /**
     * 检查网络̬
     *
     * @author cyf
     *
     */
    public static boolean checkNet(Context context) {
        try {
            if(context!=null){
                ConnectivityManager connectivity = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            Log.e("weizhi", e.getMessage());
        }
        return false;
    }

    /**
     * 检查wifi是否可用
     *
     * @param inContext
     * @return
     */
    public static boolean isWiFiActive(Context inContext) {
        ConnectivityManager connectivity = (ConnectivityManager) inContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检测网络状态
     * @param activity
     */
/*	public static void NetConnect(final Activity activity){
     TANetworkStateReceiver.registerObserver(new TANetChangeObserver()
        {
            @Override
            public void onConnect(netType type)
            {
                super.onConnect(type);
                Toast.makeText(activity, "onConnect",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDisConnect()
            {
                super.onDisConnect();
                Toast.makeText(activity, "onDisConnect",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    /**
     * dp转换px
     *
     * @author cyf
     *
     */
    public static int dp2px(Context context,int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 返回版本名字
     * 对应build.gradle中的versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName="";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        String versionCode ="";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = String.valueOf(packInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId="";
        try{
            deviceId = tm.getDeviceId();
        }catch (Exception e){
            e.getMessage();
        }

        if (deviceId == null) {
            return "";
        } else {
            return deviceId;
        }
    }
}
