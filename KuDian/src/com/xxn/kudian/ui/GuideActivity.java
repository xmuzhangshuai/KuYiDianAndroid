package com.xxn.kudian.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;
import com.xxn.kudian.base.BaseApplication;
import com.xxn.kudian.utils.NetworkUtils;
import com.xxn.kudian.utils.ServerUtil;
import com.xxn.kudian.utils.SharePreferenceUtil;
import com.xxn.kudian.utils.UserPreference;
/**
 * 类名称：GuideActivity
 * 类描述：引导页面，首次运行进入首次引导页面，GuidePagerActivity;
 * 用户没有登录则进入登录/注册页面；
 * 用户已经登录过则进入主页面加载页，
 * 期间完成程序的初始化工作。 
 * 
 */
public class GuideActivity extends BaseActivity {

	public SharedPreferences locationPreferences;// 记录用户位置
	SharedPreferences.Editor locationEditor;
	private SharePreferenceUtil sharePreferenceUtil;
	private UserPreference userPreference;
//	LocationTool locationTool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		// 获取启动的次数
		sharePreferenceUtil = new SharePreferenceUtil(this, SharePreferenceUtil.USE_COUNT);
		int count = sharePreferenceUtil.getUseCount();
		userPreference = BaseApplication.getInstance().getUserPreference();

		/************ 初始化友盟服务 **************/
		// 禁止友盟禁止默认的页面统计方式
		MobclickAgent.openActivityDurationTrack(false);
		
		if (count == 0) {// 如果是第一次登陆，则启动向导页面
			sharePreferenceUtil.setUseCount(++count);// 次数加1
			startActivity(new Intent(GuideActivity.this, GuidePagerActivity.class));
			setContentView(R.layout.activity_guide);
			findViewById();
			initView();
//			startActivity(new Intent(GuideActivity.this, LoginOrRegisterActivity.class));
		} else {// 如果不是第一次使用,则不启动向导页面，显示欢迎页面。
			if (userPreference.getUserLogin()) {// 如果是已经登陆过
				if (NetworkUtils.isNetworkAvailable(GuideActivity.this)) {//如果网络可用
					setContentView(R.layout.activity_guide);
					findViewById();
					initView();
					ServerUtil.getInstance().login(GuideActivity.this, MainActivity.class);
				} else {
					startActivity(new Intent(GuideActivity.this, MainActivity.class));
				}
			} else {// 如果用户没有登录过或者已经注销
				startActivity(new Intent(GuideActivity.this, LoginActivity.class));
				finish();
			}
			sharePreferenceUtil.setUseCount(++count);// 次数加1
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
	}

	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return this.getString(R.string.version_name) + version;
		} catch (Exception e) {
			e.printStackTrace();
			return this.getString(R.string.can_not_find_version_name);
		}
	}

}
