package com.xx.lxz.api;

public class HttpConstant {

    /** 正式 */
    public static String IPAddress = "https://91xiaoxiangzu.com/xxzapp/";//https://chnapi.chn68.com/api




    /** token*/
    public final static String REFREASHTOKEN =  "custom/getYzmToken";

    /** 登录验证码*/
    public final static String SENDLOGINCODE =  "custom/sendLoginCode";

    /** 注册验证码*/
    public final static String SENDREGISTERCODE =  "custom/sendRegistCode";

    /** 注册请求 */
    public final static String REGISTER =  "custom/regist";

    /** 登陆请求 */
    public final static String LOGIN =  "custom/login";

    /** 查询租赁tab页面产品信息 */
    public final static String QUERYPRODUCT =  "base/getProduct";

    /** 查询租赁tab页面产品信息 详情*/
    public final static String QUERYPRODUCTDETAILS =  "base/getProductdet";

    /** 查询二手租赁tab 页面产品信息 */
    public final static String QUERYUSERDPRODUCT =  "base/getUsedProduct";

    /** 我的消息 */
    public final static String QUERYMESSAGE =  "base/getNotify";



    /** 用户头像上传 */
    public final static String UPLOADCUSICON =  "custom/saveCusHeadImg";

    /** 查询我的订单列表 */
    public final static String QUERYORDERLIST =  "book/getBookList";

    /** 获取支付宝支付sign */
    public final static String GETPAYRESULT =  "book/getAliPaySign";

    /** 外部链接申请地址 */
    public final static String ADDLINK =  "book/addLink";

    /** 查询我的还款账单 */
    public final static String GETBILLS =  "book/getBills";

    /** 申请确认 */
    public final static String GETAPPLYBOOKS =  "book/applyBook";

    /** 获取信息 */
    public final static String GETINFO =  "book/getApplyInfoByMsgId";

    /** 查询每期账单的详细信息 */
    public final static String GETBILLSWITHID =  "book/getBillWithId";

    /** 客户身份认证 */
    public final static String IDENAUTH =  "custom/idenAuth";

    /** 客户个人信息 */
    public final static String CUSTOMINFO =  "custom/complementCusInfo";

    /** 设置亲属联系人 */
    public final static String CUSTOMRELATIVEINFO =  "custom/complementCusClan";

    /** 设置联系人 */
    public final static String CUSTOMCONTACTINFO =  "custom/complementLinks";

    /** 添加收货地址 */
    public final static String SAVEADDRESS =  "custom/saveAddress";

    /** 加载身份信息 */
    public final static String GETIDENAUTHINFO =  "custom/getIdenAuthInfo";

    /** 加载个人信息 */
    public final static String GETPERSONINFO =  "custom/getPersonInfo";

    /** 客户个人信息完善 */
    public final static String COMPLEMENTCUSINFO =  "custom/complementCusInfo";

    /** 获取联系人信息 */
    public final static String GETCONTACTINFO =  "custom/getLinks";

    /** 获取亲属联系人信息 */
    public final static String GETRELATIVEINFO =  "custom/getKinsfolk";

    /** 运营商认证 */
    public final static String GETMOBILEAUTH =  "custom/toMobileAuth";

    /** 淘宝认证 */
    public final static String GETTBAOBAOAUTH =  "custom/toTaobaoAuth";

    /** 支付宝认证 */
    public final static String GETALIPAYAUTH =  "custom/toZhifbAuth";

    /** 学信网认证 */
    public final static String GETSTUEDNTAUTH =  "custom/toStudentAuth";

    /** 运营商， 淘宝， 支付宝，学信网 调用三方SDK的结果回调 */
    public final static String AUTHREQUEST =  "custom/authResult";

    /** 点击申请订单 查看申请到哪一步 */
    public final static String GETAPPLYSTEP =  "custom/getApplyStep";

    /** 学信跳过 */
    public final static String SKIP =  "custom/skipStudentAuth";

    /** 获取客户信息 */
    public final static String GETCUSINFO =  "custom/getCustomInfo";

    /** 检查跟新 */
    public final static String CHECKVERSION =  "base/checkVersion";

}
