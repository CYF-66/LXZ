package com.xx.lxz.util;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;

public class MyCountTimer {
	

	public   int TIME_COUNT = 60;
	private TextView btn;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 要做的事情
			TIME_COUNT = TIME_COUNT - 1;
			if (TIME_COUNT >= 0) {
				Log.e("AA", "距重发验证码还有" + TIME_COUNT+"秒");
				onTick(TIME_COUNT);
			} else {
				onFinish();
			}

		}
	};

	public MyCountTimer(long millisInFuture, long countDownInterval,
                        TextView btn, int endStrRid) {
		this.btn = btn;
	}

	public MyCountTimer(TextView btn) {
		this.btn = btn;
	}

	Timer task;

	public class MyThread implements Runnable {
		@Override
		public void run() {
			while (TIME_COUNT >= 0) {
				try {
					Message message = new Message();
					handler.sendMessage(message);// 发送消息
					Thread.sleep(1000);// 线程暂停1秒，单位毫秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void start() {
		TIME_COUNT=60;
		new Thread(new MyThread()).start();
	}

	public void onFinish() {
//		btn.setText("验证码");
		btn.setTextColor(Color.parseColor("#ffffff"));
		btn.setEnabled(true);
	}

	public void onTick(long millisUntilFinished) {
		btn.setEnabled(false);
		btn.setTextColor(Color.parseColor("#D7D7D7"));
		btn.setText(millisUntilFinished+"");
	}


}
