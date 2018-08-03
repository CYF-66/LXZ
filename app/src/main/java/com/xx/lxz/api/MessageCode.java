package com.xx.lxz.api;

public class MessageCode {

    //获取验证码token
    public final static int YZM=0X01;
    //发送注册验证码
    public final static int REGISTERCODE=0X02;
    //注册
    public final static int REGISTER=0X03;
    //发送注册验证码
    public final static int LOGINCODE=0X04;
    //登录
    public final static int LOGIN=0X05;
    //查询租赁tab页面产品信息
    public final static int QUERYPRODUCT=0X06;
    //查询二手租赁tab 页面产品信息
    public final static int QUERYUSEDPRODUCT=0X07;
    //用户头像上传
    public final static int UPLOADCUSICON=0X08;
    //获取订单列表 已完成
    public final static int GETCOMPLETEORDERLIST=0X09;
    //获取订单列表 审核
    public final static int GETCHECKORDERLIST=0X10;
    //获取订单列表 待还
    public final static int GETPAYORDERLIST=0X11;
    //用户头像上传
    public final static int UPLOADHEADIMG=0X12;
    //支付参数服务器加密返回
    public final static int PAYHANDLER=0X13;
    //支付结果
    public final static int PAYRORDERESULT=0X14;
    //添加链接
    public final static int ADDLINK=0X15;
    //添加链接
    public final static int GETIDENAUTHINFO=0X16;
    //加载个人信息
    public final static int GETPERSONINFO=0X17;
    //加载联系人信息
    public final static int GETCONTACTINFO=0X18;
    //第三方登录成功回调
    public final static int SUCCESSREQUEST=0X19;
    //运营商
    public final static int TOMOBILE=0X20;
    //淘宝
    public final static int TOTAOBAO=0X21;
    //支付宝
    public final static int TOALIPAY=0X22;
    //学信网
    public final static int TOXUEXIN=0X23;
    //我的消息
    public final static int MYMESSAGE=0X24;
    //我的消息
    public final static int SAVEADDR=0X25;
    //获取申请步数
    public final static int GETAPPLYSTEP=0X26;
    //获取申请步数
    public final static int COMPLEMENTCUSINFO=0X27;
    //亲属联系人信息
    public final static int SAVECUSINFO=0X28;
    //联系人信息
    public final static int SAVECUSCONTACTINFO=0X29;
    //查询我的还款账单
    public final static int GETBILLS=0X30;
    //获取支付宝支付
    public final static int GETPAYSIGNS=0X31;
    //保存客户个人信息
    public final static int SAVECURINFO=0X32;
    //申请确认
    public final static int APPLYSURE=0X31;
    //跳过
    public final static int SKIP=0X32;
    //获取信息
    public final static int GETINFO=0X33;
    //获取客户信息
    public final static int GETCUSINFO=0X34;
    //检查跟新
    public final static int CHECKVERSION=0X35;
}
