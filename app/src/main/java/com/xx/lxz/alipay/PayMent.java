package com.xx.lxz.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.xx.lxz.api.HttpConstant;
import com.xx.lxz.api.HttpService;
import com.xx.lxz.api.MessageCode;
import com.xx.lxz.base.BaseResult;
import com.xx.lxz.util.NetUtil;
import com.xx.lxz.util.ToastUtil;
import com.xx.lxz.widget.CustomProgressDialog;

import org.json.JSONObject;

import java.util.Map;

/**
 * 调用支付宝支付
 * @author cyf
 */

@SuppressLint("HandlerLeak")
public class PayMent {

	private Handler handler;
	private Activity activity;
	private CustomProgressDialog dialog;
	String orderInfo="";

	public PayMent(Activity activity,Handler handler) {
		this.handler = handler;
		this.activity = activity;
		dialog = new CustomProgressDialog(activity);
	}

	public void requestPayInfo(final String bid,final String pid) {

		if (!NetUtil.checkNet(activity)) {
			ToastUtil.ToastShort(activity, "网络异常");
		} else {
			dialog.show();
			// 完整的符合支付宝参数规范的订单信息
			//http请求获取字符串
			class StartThread extends Thread {
				@Override
				public void run() {
					super.run();
					try {
						JSONObject jsonobject = new JSONObject();
						jsonobject.put("bid", bid);
						jsonobject.put("pid", pid);
						String result= HttpService.httpClientPost(activity, HttpConstant.GETPAYRESULT,jsonobject);

						Message msg = payhandler.obtainMessage();
						msg.what = MessageCode.GETPAYSIGNS;
						msg.obj = result;
						msg.sendToTarget();
					}catch (Exception e){
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
	Handler payhandler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.dismiss();
			String result = (String) msg.obj;
			if (result == null || result.equals("")||result.equals("null")) {
                ToastUtil.ToastShort(activity, "支付出错!");
				return;
			}

			Log.i("TEST", "result---" + result);
			switch (msg.what) {
			case MessageCode.GETPAYSIGNS:// 查询支付参数

				/**
				 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
				 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
				 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
				 *
				 * orderInfo的获取必须来自服务端；
				 */
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap("测试","0.01","测试商品");
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
//        final String orderInfo = orderParam + "&" + sign;

				BaseResult<String> baseResult=new Gson().fromJson(result,
						BaseResult.class);

				if(baseResult.getCode()==1){
					orderInfo=baseResult.getData();

					if(TextUtils.isEmpty(orderInfo)){
						ToastUtil.ToastShort(activity, "支付出错!");
						return;
					}
					Runnable payRunnable = new Runnable() {

						@Override
						public void run() {
							// 构造PayTask 对象
							PayTask alipay = new PayTask(activity);
							// 调用支付接口，获取支付结果
							Map<String, String> result = alipay.payV2(orderInfo, true);

							Message msg = handler.obtainMessage();
//							Message msg = new Message();
							msg.what = MessageCode.PAYRORDERESULT;
							msg.obj = result;
							handler.sendMessage(msg);
						}
					};

					// 必须异步调用
					Thread payThread = new Thread(payRunnable);
					payThread.start();
				}else{
					ToastUtil.ToastShort(activity,baseResult.getMessage());
				}
				break;
			default:
				break;
			}
		}
	};

}
