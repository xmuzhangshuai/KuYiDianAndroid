package com.xxn.kudian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;

public class PersonInfoActivity extends BaseActivity implements OnClickListener {

	private ImageView sideslipView; //侧滑按键
	private ImageView personInfo;//个人信息界面
	private Intent intent;
	private TextView modification;//修改个人信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_person_info);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		sideslipView = (ImageView) findViewById(R.id.sideslip_btn);
		personInfo = (ImageView) findViewById(R.id.person_info_btn); 
		modification = (TextView) findViewById(R.id.modification_info);
	}

	@Override
	protected void initView() {
		sideslipView.setOnClickListener(this);
		personInfo.setOnClickListener(this);
		modification.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sideslip_btn:
			intent = new Intent(PersonInfoActivity.this,SideslipActivity.class);
			startActivity(intent);
			break;
		case R.id.person_info_btn:
			intent = new Intent(PersonInfoActivity.this,PersonInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.modification_info:
			intent = new Intent(PersonInfoActivity.this,ModificationInfoActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
