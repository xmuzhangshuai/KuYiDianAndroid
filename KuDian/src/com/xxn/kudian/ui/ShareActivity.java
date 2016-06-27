package com.xxn.kudian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;
import com.xxn.kudian.utils.ToastTool;

public class ShareActivity extends BaseActivity implements OnClickListener{

	private ImageView sideslipView; //侧滑按键
	private Intent intent;
	private Button share;
	private TextView shareCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_share);
		findViewById();
		initView();

	}

	@Override
	protected void findViewById() {
		sideslipView = (ImageView) findViewById(R.id.sideslip_btn);
		share = (Button) findViewById(R.id.share_button);
		shareCode = (TextView) findViewById(R.id.share_code);
	}

	@Override
	protected void initView() {
		sideslipView.setOnClickListener(this);
		share.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sideslip_btn:
			intent = new Intent(ShareActivity.this, SideslipActivity.class);
			startActivity(intent);
			break;

		case R.id.share_button:
			ToastTool.showShort(getApplicationContext(), "分享成功！");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			finish();
			break;

		default:
			break;
		}

	}

}
