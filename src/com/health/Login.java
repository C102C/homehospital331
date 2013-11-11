package com.health;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import cn.younext.R;

public class Login extends Activity {
	private static final String DEFAULT = "332788";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		final EditText countET = (EditText) this.findViewById(R.id.cardNumAuto);
		countET.setText(DEFAULT);
		Button loginButton = (Button) this.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String count = countET.getText().toString();
				if (DEFAULT.endsWith(count)) {
					Intent intent = new Intent(Login.this, MainUi.class);
					startActivity(intent);
					finish();
				}
			}

		});
	}
}
