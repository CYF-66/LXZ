package com.xx.lxz.config;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 全局配置
 *
 * Author: cyf
 * Date: 2017-04-14  9:43
 */

public class GlobalConfig {

    public final static int CATEGORY_COUNT = 10;

    //订单tab
    public final static String CATEGORY_NAME_COMPLETE = "已完成";
    public final static String CATEGORY_NAME_NOTPAY = "待还";
    public final static String CATEGORY_NAME_CHECK = "审核";


    //SharedPreferences 存储的key
    public final static String CATEGORY_NAME_SHAREDPREFERENCE_KEY = "lxz";

}
