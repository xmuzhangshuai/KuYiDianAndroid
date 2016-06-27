package com.xxn.kudian.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;

public class SideslipActivity extends BaseActivity implements OnClickListener {

	private View offArea;//关闭侧滑页面
	private ImageView headImage;//头像
	private TextView name;//昵称
	private View mainActivity;//主页
	private View myCollect;//我的收藏
	private View myIntegral;//我的积分
	private View share;//分享
	private View help;//帮助
	private View remind;//闹钟
	private View setting;//设置
	private View exit;//登出
	private Intent intent;

	/**
	 * 所有应用
	 */

	private List<PackageInfo> allPackageInfos;

	/**
	 * 系统应用
	 */

	private List<PackageInfo> sysPackageInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sideslip);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		offArea = findViewById(R.id.transparent_area);
		headImage = (ImageView) findViewById(R.id.headimage);
		name = (TextView) findViewById(R.id.name);
		mainActivity = findViewById(R.id.main_activity);
		myCollect = findViewById(R.id.my_collect);
		myIntegral = findViewById(R.id.my_integral);
		share = findViewById(R.id.share);
		help = findViewById(R.id.help);
		remind = findViewById(R.id.remind);
		setting = findViewById(R.id.setting);
		exit = findViewById(R.id.exit);
	}

	@Override
	protected void initView() {
		offArea.setOnClickListener(this);
		mainActivity.setOnClickListener(this);
		myCollect.setOnClickListener(this);
		share.setOnClickListener(this);
		help.setOnClickListener(this);
		remind.setOnClickListener(this);
		setting.setOnClickListener(this);
		exit.setOnClickListener(this);
		//		headImage.setImageResource(resId);
		name.setText("昵称");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.transparent_area:
			finish();
			overridePendingTransition(R.anim.out_to_left, R.anim.out_to_left);
			break;
		case R.id.main_activity:
			intent = new Intent(SideslipActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.my_collect:
			intent = new Intent(SideslipActivity.this, MyCollectActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.my_integral:
			intent = new Intent(SideslipActivity.this, PersonInfoActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.share:
			intent = new Intent(SideslipActivity.this, ShareActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.help:
			//			intent = new Intent(SideslipActivity.this, MainActivity.class);
			//			startActivity(intent);
			finish();
			break;
		case R.id.remind:
			//			intent = new Intent(AlarmClock.ACTION_SET_ALARM);
			//			intent.setAction("android.intent.action.SET_ALARM");
			//			startActivity(intent);
			startSystemAlarm();
			finish();
			break;
		case R.id.setting:
			//			intent = new Intent(SideslipActivity.this, MainActivity.class);
			//			startActivity(intent);
			finish();
			break;
		case R.id.exit:
			intent = new Intent(SideslipActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}

	}

	// 返回键的事件触发
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			// 不存在第二个动画效果
			overridePendingTransition(R.anim.out_to_left, R.anim.out_to_left);
			return false;
		}
		return false;
	}

	/**
	 * 过滤出系统应用
	 */
	private void getSystemApp()	{
		allPackageInfos = getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES); // 取得系统安装所有软件信息
		sysPackageInfos = new ArrayList<PackageInfo>();
		if (allPackageInfos != null && !allPackageInfos.isEmpty()) {
			for (PackageInfo apckageInfo : allPackageInfos) {
				ApplicationInfo appInfo = apckageInfo.applicationInfo;// 得到每个软件信息
				if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
					sysPackageInfos.add(apckageInfo);// 系统软件
				}
			}
		}
	}

	/**
	 * 启动系统闹钟
	 */
	private void startSystemAlarm()	{
		getSystemApp();
		String activityName = "";
		String packageName = "";
		String alarmPackageName = "";
		for (int i = 0; i < sysPackageInfos.size(); i++) {
			PackageInfo packageInfo = sysPackageInfos.get(i);
			packageName = packageInfo.packageName;
			// 包名中包含clock
			if (packageName.indexOf("clock") != -1)	{
				if (!(packageName.indexOf("widget") != -1))	{
					ActivityInfo activityInfo = packageInfo.activities[0];
					// activity名中包含 Alarm和 DeskClock 大部分的闹钟程序名中都是按照这种规则命名 不能保证所有闹钟都是这样的
					if (activityInfo.name.indexOf("Alarm") != -1 || activityInfo.name.indexOf("DeskClock") != -1
							|| activityInfo.name.indexOf("Clock") != -1) {
						activityName = activityInfo.name;
						alarmPackageName = packageName;
					}
				}
			}
		}
		if ((activityName != "") && (alarmPackageName != "")) {
			Intent intent = new Intent();
			intent.setComponent(new ComponentName(alarmPackageName, activityName));
			startActivity(intent);
		}
		else {
			Toast.makeText(this, "启动系统闹钟失败！", Toast.LENGTH_SHORT).show();
		}
	}



}
