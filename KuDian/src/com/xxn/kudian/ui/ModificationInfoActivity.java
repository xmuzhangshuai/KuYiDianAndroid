package com.xxn.kudian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;
import com.xxn.kudian.base.BaseApplication;
import com.xxn.kudian.customwidget.MyAlertDialog;

public class ModificationInfoActivity extends BaseActivity implements OnClickListener {

	private ImageView sideslipView; //侧滑按键
	private ImageView personInfo;//个人信息界面
	private Intent intent;
	private EditText name;//名字
	private EditText school;//学校
	private EditText phone;//手机号
	private Button sureBtn;//确认修改
	private Button cancelBtn;//取消修改

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_modification);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		sideslipView = (ImageView) findViewById(R.id.sideslip_btn);
		personInfo = (ImageView) findViewById(R.id.person_info_btn); 
		sureBtn = (Button) findViewById(R.id.sure_modification);
		cancelBtn = (Button) findViewById(R.id.cancel_modification);
		name = (EditText) findViewById(R.id.modification_name);
		school = (EditText) findViewById(R.id.modification_school);
		phone = (EditText) findViewById(R.id.modification_phone);
	}

	@Override
	protected void initView() {
		sideslipView.setOnClickListener(this);
		personInfo.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sideslip_btn:
			intent = new Intent(ModificationInfoActivity.this, SideslipActivity.class);
			startActivity(intent);
			break;
		case R.id.person_info_btn:
			intent = new Intent(ModificationInfoActivity.this, PersonInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.sure_modification:
			sureModification();
			break;
		case R.id.cancel_modification:
			sureCancel();
			break;
		default:
			break;
		}

	}
	
	/**
	 * 确认修改信息
	 */
	private void sureModification() {
		final MyAlertDialog dialog = new MyAlertDialog(ModificationInfoActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("确认是否修改信息？");
		View.OnClickListener sure = new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		};
		dialog.setPositiveButton("确定", sure);
		dialog.setNegativeButton("取消", cancle);
		dialog.show();
	}
	

	/**
	 * 确认取消
	 */
	private void sureCancel() {
		final MyAlertDialog dialog = new MyAlertDialog(ModificationInfoActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("确认是否取消修改信息？");
		View.OnClickListener sure = new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		};
		dialog.setPositiveButton("确定", sure);
		dialog.setNegativeButton("取消", cancle);
		dialog.show();
	}

}
