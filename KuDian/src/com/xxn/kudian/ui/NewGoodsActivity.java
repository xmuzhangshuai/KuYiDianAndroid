package com.xxn.kudian.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
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

public class NewGoodsActivity extends BaseActivity implements OnClickListener, OnItemClickListener, OnPageChangeListener{

	private ImageView sideslipView; //侧滑按键
	private ImageView personInfo;//个人信息界面
	private ListView newGoodsList;//最新商品列表
	private Intent intent;
	private NewGoodsAdapter newGoodsAdapter;
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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_goods);
		findViewById();
		initView();
	}
	@Override
	protected void findViewById() {
		sideslipView = (ImageView) findViewById(R.id.sideslip_btn);
		personInfo = (ImageView) findViewById(R.id.person_info_btn); 
		newGoodsList = (ListView) findViewById(R.id.new_goods_list);
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
		newGoodsList.setOnItemClickListener(this);	
		newGoodsAdapter = new NewGoodsAdapter();
		newGoodsList.setAdapter(newGoodsAdapter);
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
			intent = new Intent(NewGoodsActivity.this,SideslipActivity.class);
			startActivity(intent);
			break;
		case R.id.person_info_btn:
			intent = new Intent(NewGoodsActivity.this,PersonInfoActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		intent = new Intent(NewGoodsActivity.this, MerchantInfoActivity.class);


		intent.putExtra("merchantName", "商家" + position);
		startActivity(intent);

	}

	private final class NewGoodsAdapter extends BaseAdapter{

		private class ViewHolder {
			public ImageView goodsImage;
			public TextView belongMerchant;
			public TextView goodsName;
			public TextView goodsIntegral;

		}
		@Override
		public int getCount() {
			return 10;
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
				view = LayoutInflater.from(NewGoodsActivity.this).inflate(R.layout.newgoods_list_item, null);
				holder = new ViewHolder();
				holder.goodsImage = (ImageView) view.findViewById(R.id.goods_image);
				holder.goodsName = (TextView) view.findViewById(R.id.goods_name);
				holder.goodsIntegral = (TextView) view.findViewById(R.id.goods_integral);
				holder.belongMerchant = (TextView) view.findViewById(R.id.belong_merchant_name);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}
			holder.goodsImage.setImageResource(R.drawable.goods_imgae1);
			holder.goodsName.setText("商品" + position);
			holder.goodsIntegral.setText("1" + (99 - position));
			holder.belongMerchant.setText("商家" + position);

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
