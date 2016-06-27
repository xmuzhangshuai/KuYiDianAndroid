package com.xxn.kudian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseFragmentActivity;

/**
 * 类名称：LoginOrRegisterActivity 
 * 类描述：注册或登录引导页面 
 */
public class LoginOrRegisterActivity extends BaseFragmentActivity {
	private Button loginButton;
	private Button registerButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_or_register);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		loginButton = (Button) findViewById(R.id.login_btn);
		registerButton = (Button) findViewById(R.id.register_btn);
	}

	@Override
	protected void initView() {
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginOrRegisterActivity.this, LoginActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginOrRegisterActivity.this, RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}
}