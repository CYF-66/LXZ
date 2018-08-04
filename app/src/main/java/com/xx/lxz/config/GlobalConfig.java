package com.xx.lxz.config;

/**
 * 全局配置
 *
 * Author: cyf
 * Date: 2017-04-14  9:43
 */

public class GlobalConfig {

    //商户号
    public final static String PARTNERID = "xiaoxiang";
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2018052360203156";//后四位XXXX为了保护隐私

    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "wx13788957291@163.com";

    /**
     * 商户私钥，pkcs8格式
     */
    public static final String RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC5zVCd2/AfD6rx\n" +
            "Ug2FzRLXWJe0iWvaYpUnCtnRoYuC/k+7PxcnHA/0cywTxp3SVN0j3QK3PytJnhjN\n" +
            "BFHGjNwU8OzcI99gZpXo+5rHY9qqfY2MryUUcjwK+w8iR5H31Nf14jMt5Zm1Cjxs\n" +
            "j39pWzr07XsLC09uN4JPbtIh+uXZ9jpBNcX3uyh1KV4Isbci/5S2gh6vUpcT2Bpu\n" +
            "/uirYwoL9pfmFtMo5MAl0zZC8QQSaEDoxuvFq3/04DnelxAJcA7cDVz+zuWZ0kG7\n" +
            "16FPRc7yf7qF6mISJXZJ4IbLsCzlXDzsRcH4ivsYcyKCGBapEtH4sas/IHx912G7\n" +
            "Gc33PbkxAgMBAAECggEBALN7jkCv0UlwDSWIqd9ytpynzARuK22OhZ5tcPR8JRSp\n" +
            "LcQnsenpxc/R4eZImvQvXl/ig1kZAGvg+PwzC/vM3lysT2JolMO+1Vl2k5HA995x\n" +
            "4mbgWeViZHYHCBuJH4xUYzn3BJF1lgG4nZ4REHdp1Y38Sjvk9Rn3LT4mQ37nq1nc\n" +
            "RsLxAaPZw7tiuD/Kh64saekguMHofpe46Ob4WoomLeORDAzijmOSB/X50H0+pfxL\n" +
            "0n+c7mRgUSYV4nJibhdnxovpfawLiHSFezzblgIJ8aIxam0tuRQTYKb/k+7Oc8yZ\n" +
            "SA08ySsddqJ3NBVlRYkgbqz9zNU/NO10u0a7KQmchwECgYEA7BN/SljZ/g7OBNut\n" +
            "oni+vZyMYN9DdIislRuYonZ2rzj3GfqX4vZuWdFrNrcvMi2ofA4pO/somWL45Dah\n" +
            "SLTWcIM7A3VDTsS/PIptKALm5q8CwDEU3hWpdnjRoMYoAU5ukXPt78fiGnvYnBk7\n" +
            "FL0nXoea+YUhrKFzt1wy2QHEn2kCgYEAyXug0oZcXxfNOJbm1rI6JLwR+aYxhU5/\n" +
            "/+6Tiqo4fWYpVn5vJ44fuGrRRUeZf77cNZQ9xxPeo3NaarLTeEKbJbMgPdsYHVet\n" +
            "wBF5qqhTPxsm4Fw1jY31oatOGR/nTOnmtkPFcdpS2iymVqzTIQQkSlHFqrXjA8Nw\n" +
            "4J+bqed92okCgYB6rfMjJqM6BGWJXsOPv+GXVWgZ20wJnhDZd7rNCnTIv/ihNnmB\n" +
            "x4A3Cr4FdBKBp/p7ZTyGYBGMDEO9DKqpxtf9JiUfuetdXp7mjiUFeuSVEnOCfr8S\n" +
            "Xf1vhH+jZfZaH3EUA0OtrJjSHHPHQtFSCK4R0LR3YHjMZiZlLfN6lBGiqQKBgFcG\n" +
            "ehFUjYNZUX/W1f/33VtOjGkLWqpcnLbbwoiLO3kWblZ4p+Qq5BKQuLE22tstggn2\n" +
            "bLSXJXvt8CjT8rkn88FV3GA7SPArldW29pk6uTIxtRoFWfA5V9YrmsEJYat7//XL\n" +
            "0fV6On0X+gTIAy1oj5A2P62bgGF4wkGTTvYVEewZAoGATjtxF8OWGfFp9ssadclH\n" +
            "VNtNRQIWtAaUDrOTWiCJS4ArEWBYPBLYYxezpmWorXK0oKHv6Xdahrd95ab5K85X\n" +
            "iMtpgnTc1DVaA8G++BqMaUU1k1yEYt57GnQ0BGklJTz57pMzkEcqD0J6xJqKOqjR\n" +
            "kx0bdqpUeCKfS9Xyg0Jsf7M=";//后四位XXXX为了保护隐私

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    public final static int CATEGORY_COUNT = 10;

    //订单tab
    public final static String CATEGORY_NAME_COMPLETE = "已完成";
    public final static String CATEGORY_NAME_NOTPAY = "待还";
    public final static String CATEGORY_NAME_CHECK = "审核";

    //首页产品tab
    public final static String CATEGORY_NAME_RENT = "租赁";
    public final static String CATEGORY_NAME_SECOND_HAND_RENT = "二手租赁";


    //SharedPreferences 存储的key
    public final static String CATEGORY_NAME_SHAREDPREFERENCE_KEY = "lxz";


    //刷新位置
    public final static String REFRESHPOSITIO_ADDRESS = "address";
    public final static String REFRESHPOSITIO_ORDER = "order";
    public final static String REFRESHPOSITIO_MSG = "msg";
    public final static String REFRESHPOSITIO_ORDER_PAY = "order_pay";//待还
    public final static String REFRESHPOSITIO_ORDER_CHECK = "order_check";//审核
    public final static String REFRESHPOSITIO_SHOW_ALL = "all";//审核
    //行为
    public final static String ACTIVE_REFRESH = "refresh";//刷新
    public final static String ACTIVE_SKIPTO = "skip";//跳转
    public final static String ACTIVE_PAY = "pay";//z支付
    public final static String ACTIVE_SHOW_EXCEPTION = "exception";//异常信息


}
