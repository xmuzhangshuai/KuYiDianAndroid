package com.xxn.kudian.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;
import com.xxn.kudian.utils.DensityUtil;

public class MainActivity extends BaseActivity implements OnClickListener,OnPageChangeListener {

	private ViewPager viewPager;
	private LinearLayout llPoints;
	private int[] imageIds = { R.drawable.image1, R.drawable.image2,
			R.drawable.image3, R.drawable.image4, R.drawable.image5 };
	private ImageView[] images; // 图片数据源
	private List<View> points = new ArrayList<View>();
	private int currentPos = 0;
	public static final int WHAT_AUTO_RUN = 1;
	private ImageView sideslipView; //侧滑按键
	private ImageView personInfo;//个人信息界面
	private Handler handler;
	private Button merchantList; //商家列表
	private Button merchantMap; //地图
	private Button newGoods; //最新商品
	private Button myCollege; //我的收藏
	private Button integralBoard;	//排行榜
	private Button recommend;//酷易点推荐
	private Intent intent;
	//	private ScrollView scrollView;
	private LinearLayout nearby;//附近商家
	private int[] nearbyImage = { R.drawable.nearby1, R.drawable.nearby2,
			R.drawable.nearby3, R.drawable.nearby4, R.drawable.nearby5, 
			R.drawable.nearby1, R.drawable.nearby2, R.drawable.nearby3,
			R.drawable.nearby4, R.drawable.nearby5 };
	private LayoutInflater mInflater;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		findViewById();
		initView();

	}
	@Override
	protected void findViewById() {
		sideslipView = (ImageView) findViewById(R.id.sideslip_btn);
		personInfo = (ImageView) findViewById(R.id.person_info_btn); 
		merchantList = (Button) findViewById(R.id.merchant);
		merchantMap = (Button) findViewById(R.id.map);
		newGoods = (Button) findViewById(R.id.new_goods);
		myCollege = (Button) findViewById(R.id.my_collect);
		integralBoard = (Button) findViewById(R.id.integral_board);
		recommend = (Button) findViewById(R.id.recommend);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		llPoints = (LinearLayout) findViewById(R.id.ll_points);
		//		scrollView = (ScrollView) findViewById(R.id.nearby_scrollview);
		nearby = (LinearLayout) findViewById(R.id.nearby);
		mInflater = LayoutInflater.from(this);
		handler = new Handler() {

			@Override
			public void handleMessage(android.os.Message msg) {
				if (msg.what == WHAT_AUTO_RUN) {
					points.get(currentPos % images.length).setEnabled(false);
					viewPager.setCurrentItem(++currentPos);
					sendEmptyMessageDelayed(WHAT_AUTO_RUN, 5000);
				}
			}

		};
	}

	@Override
	protected void initView() {
		sideslipView.setOnClickListener(this);
		personInfo.setOnClickListener(this);
		merchantList.setOnClickListener(this);
		merchantMap.setOnClickListener(this);
		newGoods.setOnClickListener(this);
		myCollege.setOnClickListener(this);
		integralBoard.setOnClickListener(this);
		recommend.setOnClickListener(this);
		initImages();
		initPoints();
		setNearbyImage();


	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sideslip_btn:
			intent = new Intent(MainActivity.this, SideslipActivity.class);
			startActivity(intent);
			break;
		case R.id.person_info_btn:
			intent = new Intent(MainActivity.this, PersonInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.merchant:
			intent = new Intent(MainActivity.this, MerchantListActivity.class);
			startActivity(intent);
			break;
		case R.id.map:
			intent = new Intent(MainActivity.this, MapActivity.class);
			startActivity(intent);
			break;
		case R.id.new_goods:
			intent = new Intent(MainActivity.this, NewGoodsActivity.class);
			startActivity(intent);
			break;
		case R.id.my_collect:
			intent = new Intent(MainActivity.this, MyCollectActivity.class);
			startActivity(intent);
			break;
		case R.id.integral_board:
			intent = new Intent(MainActivity.this, IntegralBoardActivity.class);
			startActivity(intent);
			break;
		case R.id.recommend:
			intent = new Intent(MainActivity.this, RecommendActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 设置广告下标点
	 */
	private void initPoints() {
		View view = null;
		for (int i = 0; i < images.length; i++) {
			view = new View(this);
			// 设置布局属性
			LayoutParams params = new LayoutParams(10, 10);
			params.rightMargin = 10;
			view.setLayoutParams(params);
			view.setBackgroundResource(R.drawable.selector_point);
			view.setEnabled(false);
			points.add(view);
			// 添加到布局中
			llPoints.addView(view);
		}

		points.get(0).setEnabled(true);
	}

	private void bindListener() {
		viewPager.setOnPageChangeListener(this);
	}

	/**
	 * 设置广告图片
	 */
	private void initImages() {
		images = new ImageView[imageIds.length];
		ImageView iv = null;
		for (int i = 0; i < imageIds.length; i++) {
			iv = new ImageView(this);
			iv.setImageResource(imageIds[i]);
			iv.setScaleType(ScaleType.FIT_XY);
			images[i] = iv;
		}

		// 设置缓存页面的个数
		MyAdapter adapter = new MyAdapter();
		viewPager.setAdapter(adapter);
		currentPos = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % images.length;
		viewPager.setCurrentItem(currentPos);
		bindListener();
		handler.sendEmptyMessageDelayed(WHAT_AUTO_RUN, 5000);
	}
	/**
	 * 设置附近商家图片
	 */
	private void setNearbyImage() {
		for (int i = 0; i < nearbyImage.length; i++)
		{
			View view = mInflater.inflate(R.layout.nearby_item,	nearby, false);
			ImageView img = (ImageView) view.findViewById(R.id.nearby_merchant_image);
			img.setImageResource(nearbyImage[i]);
			view.getLayoutParams().width = DensityUtil.getScreenWidthforDP(getApplicationContext())/2;
			nearby.addView(view);
			final String merchantName = "商家" + i;
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					intent = new Intent(MainActivity.this, MerchantInfoActivity.class);

					intent.putExtra("merchantName", merchantName);
					startActivity(intent);

				}
			});
		}
	}

	public class MyAdapter extends PagerAdapter {

		/**
		 * 页数
		 */
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		/**
		 * 创建页面
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(images[position % images.length]);
			return images[position % images.length];
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		/**
		 * 销毁页面
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(images[position % images.length]);
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		points.get(currentPos % images.length).setEnabled(false);
		points.get(position % images.length).setEnabled(true);
		currentPos = position;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		switch (state) {
		case ViewPager.SCROLL_STATE_IDLE: // 空闲
			if (!handler.hasMessages(WHAT_AUTO_RUN)) {
				handler.sendEmptyMessageDelayed(WHAT_AUTO_RUN, 5000);
			}
			break;
		case ViewPager.SCROLL_STATE_DRAGGING: // 拖拽
			handler.removeMessages(WHAT_AUTO_RUN);
			break;
		case ViewPager.SCROLL_STATE_SETTLING: // 固定
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeMessages(0);
	}

}
