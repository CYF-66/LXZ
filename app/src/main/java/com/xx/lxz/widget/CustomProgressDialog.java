package com.xx.lxz.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.xx.lxz.R;


public class CustomProgressDialog extends Dialog {

	public CustomProgressDialog(Context context) {
		 this(context, R.style.MyDialog);
		// TODO Auto-generated constructor stub
	}

	 public CustomProgressDialog(Context context, int theme) {
		super(context,theme);
		 this.setContentView(R.layout.progress);  
		 this.getWindow().getAttributes().gravity = Gravity.CENTER;
	}

	@Override  
	    public void onWindowFocusChanged(boolean hasFocus) {  
	  
	        if (!hasFocus) {  
	            dismiss();  
	        }  
	    }

}
