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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;
import com.xxn.kudian.base.BaseApplication;
import com.xxn.kudian.customwidget.MyAlertDialog;

public class MyCollectActivity extends BaseActivity implements OnClickListener,	OnItemClickListener, OnPageChangeListener{

	private ImageView sideslipView; //侧滑按键
	private ImageView personInfo;//个人信息界面
	private ListView collectList;//收藏列表
	private CollectAdapter collectAdapter;
	private Intent intent;
	private ViewPager viewPager;
	private LinearLayout llPoints;
	private int[] imageIds = { R.drawable.image1, R.drawable.image2,
			R.drawable.image3, R.drawable.image4, R.drawable.image5 };
	private ImageView[] images; // 图片数据源
	private List<View> points = new ArrayList<View>();
	private int currentPos = 0;
	public static final int WHAT_AUTO_RUN = 1;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_collect);
		findViewById();
		initView();

	}

	@Override
	protected void findViewById() {
		sideslipView = (ImageView) findViewById(R.id.sideslip_btn);
		personInfo = (ImageView) findViewById(R.id.person_info_btn); 
		collectList = (ListView) findViewById(R.id.collect_list);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		llPoints = (LinearLayout) findViewById(R.id.ll_points);
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
		collectList.setOnItemClickListener(this);
		collectAdapter = new CollectAdapter();
		collectList.setAdapter(collectAdapter);
		initImages();
		initPoints();
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

	private void bindListener() {
		viewPager.setOnPageChangeListener(this);
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


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sideslip_btn:
			intent = new Intent(MyCollectActivity.this,SideslipActivity.class);
			startActivity(intent);
			break;
		case R.id.person_info_btn:
			intent = new Intent(MyCollectActivity.this,PersonInfoActivity.class);
			startActivity(intent);
			break;

		case R.id.cancel_collect:
			sureCancelCollect();

			break;

		default:
			break;
		}

	}

	/**
	 * 确认取消收藏
	 */
	private void sureCancelCollect() {
		final MyAlertDialog dialog = new MyAlertDialog(MyCollectActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("是否确定取消该商家收藏？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				//确定取消收藏操作

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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		intent = new Intent(MyCollectActivity.this, MerchantInfoActivity.class);

		intent.putExtra("merchantName", "商家" + position);
		startActivity(intent);
	}

	private final class CollectAdapter extends BaseAdapter{
		private class ViewHolder {
			public ImageView merchantImage;
			public TextView merchantName;
			public TextView merchantAddress;
			public TextView goodsQuantity;
			public View cancelCollect;
		}
		@Override
		public int getCount() {
			return 20;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = LayoutInflater.from(MyCollectActivity.this).inflate(R.layout.collect_list_item, null);
				holder = new ViewHolder();
				holder.merchantImage = (ImageView) view.findViewById(R.id.merchant_image);
				holder.merchantName = (TextView) view.findViewById(R.id.merchant_name);
				holder.merchantAddress = (TextView) view.findViewById(R.id.merchant_address);
				holder.goodsQuantity = (TextView) view.findViewById(R.id.goods_quantity);
				holder.cancelCollect = view.findViewById(R.id.cancel_collect);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}
			holder.merchantImage.setImageResource(R.drawable.merchant_image1);
			holder.merchantName.setText("商家" + position);
			holder.cancelCollect.setOnClickListener(MyCollectActivity.this);
			return view;
		}

	}

	private class MyAdapter extends PagerAdapter {

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
