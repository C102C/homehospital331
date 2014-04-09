package com.health.util;

import cn.younext.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;





public class MyProgressDialog extends ProgressDialog {

	public MyProgressDialog(Context context) {
		super(context);
	}

	public MyProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.half_transparent_dialog);
	}

	public static MyProgressDialog show(Context ctx) {
		MyProgressDialog d = new MyProgressDialog(ctx);
		d.show();
		return d;
	}
}
