package com.xxn.kudian.ui;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;
import com.xxn.kudian.base.BaseApplication;
import com.xxn.kudian.base.BaseFragmentActivity;
import com.xxn.kudian.customwidget.MyAlertDialog;
import com.xxn.kudian.utils.AsyncHttpClientTool;
import com.xxn.kudian.utils.FileSizeUtil;
import com.xxn.kudian.utils.ImageTools;
import com.xxn.kudian.utils.JsonTool;
import com.xxn.kudian.utils.LogTool;
import com.xxn.kudian.utils.ToastTool;
import com.xxn.kudian.utils.UserPreference;
/** 
 * 类描述 ：注册
 * 类名： RegisterActivity.java  
 * Copyright:   Copyright (c)2015    
 * Company:     zhangshuai   
 * @author:     zhangshuai    
 * @version:    1.0    
 * 创建时间:    2015-8-10 下午3:57:53  
*/
public class RegisterActivity extends BaseFragmentActivity {

	public static final int CROP = 2;
	public static final int CROP_PICTURE = 3;
	private UserPreference userPreference;

	private Uri lastPhotoUri;
	UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		userPreference = BaseApplication.getInstance().getUserPreference();

		findViewById();
		initView();

		
	}

	@Override
	protected void findViewById() {
	}

	@Override
	protected void initView() {
	}

	@Override
	public void onBackPressed() {
		vertifyToTerminate();
	}

	
	/**
	 * 确认终止注册
	 */
	private void vertifyToTerminate() {
		final MyAlertDialog dialog = new MyAlertDialog(RegisterActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("注册过程中退出，信息将不能保存。是否继续退出？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
				BaseApplication.getInstance().getUserPreference().clear();
			}
		};
		View.OnClickListener cancle = new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		};
		dialog.setPositiveButton("确定", comfirm);
		dialog.setNegativeButton("取消", cancle);
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取 
		case 1:
			if (data != null) {
				startPhotoCrop(data.getData());
			}
			break;
		// 如果是调用相机拍照时 
		case 2:
			if (new File(getImagePath()).exists()) {
				startPhotoCrop(Uri.fromFile(new File(getImagePath())));
			}
			break;
		// 取得裁剪后的图片并上传 
		case 3:
			uploadImage(lastPhotoUri.getPath());
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
		/**使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	/** 
	 * 裁剪图片方法实现 
	 * @param uri 
	 */
	public void startPhotoCrop(Uri uri) {
		lastPhotoUri = Uri.fromFile(new File(getLastImagePath()));

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪 
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 800);
		intent.putExtra("outputY", 800);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, lastPhotoUri);
		intent.putExtra("return-data", false);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		startActivityForResult(intent, 3);
	}

	//获得最终截图路径
	private String getLastImagePath() {
		String path = null;
		SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_PRIVATE);

		// 保存本次截图临时文件名字
		File file = new File(Environment.getExternalStorageDirectory() + "/lanquan");
		if (!file.exists()) {
			file.mkdirs();
		}

		path = file.getAbsolutePath() + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		Editor editor = sharedPreferences.edit();
		editor.putString("tempPath", path);
		editor.commit();
		return path;
	}

	//获得路径
	private String getImagePath() {
		SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_PRIVATE);
		return sharedPreferences.getString("tempPath", "");
	}

	/**
	 * 上传头像
	 * @param filePath
	 */
	public void uploadImage(final String imageUrl) {
		File dir = new File(imageUrl);
		LogTool.e("图片地址" + imageUrl);
		int fileSize = (int) FileSizeUtil.getFileOrFilesSize(imageUrl, 2);
		LogTool.e("文件大小：" + fileSize + "KB");
		final Dialog dialog = showProgressDialog("正在上传头像，请稍后...");
		dialog.setCancelable(false);

		if (dir.exists() && !imageUrl.equals("/") && fileSize < 500 && fileSize > 0) {
			RequestParams params = new RequestParams();
			try {
				params.put("userfile", dir);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
				@Override
				public void onStart() {
					super.onStart();
					dialog.show();
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, String response) {
					LogTool.i("" + statusCode + response);
					JsonTool jsonTool = new JsonTool(response);
					String status = jsonTool.getStatus();
					if (status.equals(JsonTool.STATUS_SUCCESS)) {
						ToastTool.showLong(RegisterActivity.this, "头像上传成功！");
						JSONObject jsonObject = jsonTool.getJsonObject();
						if (jsonObject != null) {
							try {
								userPreference.setU_avatar(jsonObject.getString("url"));
//								RegAccountFragment regAccountFragment = (RegAccountFragment) getSupportFragmentManager().findFragmentByTag("RegAccountFragment");
//								regAccountFragment.showHeadImage(Constants.AppliactionServerIP + jsonObject.getString("url"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					} else if (status.equals(JsonTool.STATUS_FAIL)) {
						LogTool.e("上传头像fail");
					}

					// 删除本地头像
					ImageTools.deleteImageByPath(imageUrl);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
					LogTool.e("头像上传失败！" + errorResponse);
					// 删除本地头像
					ImageTools.deleteImageByPath(imageUrl);
				}

				@Override
				public void onFinish() {
					super.onFinish();
					dialog.dismiss();
					// 删除本地头像
					ImageTools.deleteImageByPath(imageUrl);
					lastPhotoUri = null;
				}
			};
			AsyncHttpClientTool.post("api/file/upload", params, responseHandler);
		} else {
			LogTool.e("本地文件为空");
		}

	}
}