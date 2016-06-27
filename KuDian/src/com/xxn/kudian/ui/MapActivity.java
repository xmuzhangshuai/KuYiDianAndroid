package com.xxn.kudian.ui;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;
import com.xxn.kudian.utils.LogTool;

public class MapActivity extends BaseActivity implements OnClickListener, OnMarkerClickListener {

	private ImageView sideslipView; //侧滑按键
	private ImageView personInfo;//个人信息界面
	private ImageView location;//快速定位
	private Intent intent;
	private MapView mMapView = null; //百度地图控件
	private BaiduMap mBaiduMap;
	LocationClient mLocClient;
	private MyLocationListenner myListener;
	private boolean isFirstLoc = true; // 是否首次定位
	private	MapStatusUpdate mapStatusUpdate;
	private LatLng mLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
		//注意该方法要再setContentView方法之前实现  
		SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.activity_map);
		findViewById();
		initView();
		location();
		addMarker();
	}

	@Override
	protected void findViewById() {
		sideslipView = (ImageView) findViewById(R.id.sideslip_btn);
		personInfo = (ImageView) findViewById(R.id.person_info_btn); 
		location = (ImageView) findViewById(R.id.location_btn);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
	}

	@Override
	protected void initView() {
		sideslipView.setOnClickListener(this);
		personInfo.setOnClickListener(this);
		location.setOnClickListener(this);
		mBaiduMap.setOnMarkerClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sideslip_btn:
			intent = new Intent(MapActivity.this,SideslipActivity.class);
			startActivity(intent);
			break;
		case R.id.person_info_btn:
			intent = new Intent(MapActivity.this,PersonInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.location_btn:
			// 地图中心点显示到定位点
			if (mLocation != null) {
				// 设置地图中心点以及缩放级别(50米)
				MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(mLocation,
						mBaiduMap.getMapStatus().zoom);
				mBaiduMap.animateMapStatus(mapStatusUpdate);
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 开始定位
	 */
	private void location() {

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(getApplicationContext());
		myListener = new MyLocationListenner();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPS
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setScanSpan(5 * 60 * 60 * 1000);// 设置发起定位请求的间隔时间为3000ms
		mLocClient.setLocOption(option);// 使用设置
		mLocClient.start();// 开启定位SDK
		//		mLocClient.requestLocation();// 开始请求位置
	}

	/**
	 * 添加商家marker
	 */
	private void addMarker() {
		//		//定义Maker坐标点  
		//		LatLng point = new LatLng(24.44547, 118.131476);  
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
				.fromResource(R.drawable.map_merchant_marker_img);  
		//		//构建MarkerOption，用于在地图上添加Marker  
		//		OverlayOptions option = new MarkerOptions()  
		//		    .position(point)  
		//		    .icon(bitmap);  
		//		//在地图上添加Marker，并显示  
		//		mBaiduMap.addOverlay(option);

		for (int i = 0; i < 5; i++) {
			//定义Maker坐标点  
			LatLng point = new LatLng(24.44547 - i * 0.0005, 118.131476 - i * 0.0005);  
			//构建MarkerOption，用于在地图上添加Marker  
			OverlayOptions option = new MarkerOptions()  
			.position(point)  
			.icon(bitmap);  
			//在地图上添加Marker，并显示  
			mBaiduMap.addOverlay(option);
		}
	}

	/**
	 * 商家marker点击事件监听
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		//点击marker将当前marker定位到中心点
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(marker.getPosition());
		mBaiduMap.setMapStatus(mapStatusUpdate);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//获得marker中的数据           
		InfoWindow mInfoWindow;  
		//生成一个TextView用于在地图中显示InfoWindow  
		View view = LayoutInflater.from(MapActivity.this).inflate(R.layout.map_item, null);

		final TextView location = new TextView(getApplicationContext());  
		location.setBackgroundResource(R.drawable.location_tips);  
		location.setPadding(30, 20, 30, 50); 
		location.setText("商家1");  
		//将marker所在的经纬度的信息转化成屏幕上的坐标  
		final LatLng ll = marker.getPosition();  
		Point p = mBaiduMap.getProjection().toScreenLocation(ll);  
		//        Log.e(TAG, "--!" + p.x + " , " + p.y);  
		p.y -= 47;  
		LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);  
		//为弹出的InfoWindow添加点击事件  
		mInfoWindow = new InfoWindow(view, llInfo, 10);
		//		mInfoWindow = new InfoWindow(location, llInfo, 10);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MapActivity.this, MerchantInfoActivity.class);
				intent.putExtra("merchantName", location.getText().toString());
				startActivity(intent);
			}
		});

		//		location.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View v) {
		//				Intent intent = new Intent(MapActivity.this, MerchantInfoActivity.class);
		//				intent.putExtra("merchantName", location.getText().toString());
		//				startActivity(intent);
		//			}
		//		});


		//		mInfoWindow = new InfoWindow(location, llInfo,  
		//				new InfoWindow.OnInfoWindowClickListener() {  
		//
		//			@Override  
		//			public void onInfoWindowClick() {  
		//				//隐藏InfoWindow  
		//				mBaiduMap.hideInfoWindow();  
		//			}  
		//		});  
		//显示InfoWindow  
		mBaiduMap.showInfoWindow(mInfoWindow);
		//设置详细信息布局为可见  
		//		mMarkerInfoLy.setVisibility(View.VISIBLE);
		//根据商家信息为详细信息布局设置信息  
		//		popupInfo(mMarkerInfoLy, info);  
		return true;  
	}  

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
			.accuracy(location.getRadius())	// 此处设置开发者获取到的方向信息，顺时针0-360
			.direction(100).latitude(location.getLatitude())
			.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData); //设置定位数据
			mLocation = new LatLng(locData.latitude, locData.longitude);
			LogTool.e(locData.latitude + "," + locData.longitude);
			mapStatusUpdate = MapStatusUpdateFactory.newLatLng(mLocation);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(17.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
			}
			//			mBaiduMap.animateMapStatus(mapStatusUpdate);
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override  
	protected void onDestroy() {  
		super.onDestroy();  
		//在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
		mMapView.onDestroy();  
	}  
	@Override  
	protected void onResume() {  
		super.onResume();  
		//在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
		mMapView.onResume();  
	}  
	@Override  
	protected void onPause() {  
		super.onPause();  
		//在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
		mMapView.onPause();  
	}


}
