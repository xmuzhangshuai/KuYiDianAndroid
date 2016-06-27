package com.xxn.kudian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;
import com.xxn.kudian.customwidget.MyAlertDialog;
import com.xxn.kudian.utils.CommonTools;

public class MerchantInfoActivity extends BaseActivity implements OnClickListener, OnItemClickListener{

	private ImageView sideslipView; //侧滑按键
	private ImageView personInfo;//个人信息界面
	private ImageView collectView; //收藏按键
	private ImageView merchantImage;//商家图片
	private TextView merchantName;//商家名称
	private TextView merchantAddress;//商家地址
	private ListView goodsList;//商品列表
	private Intent intent;
	private GoodsAdapter goodsAdapter;
	private boolean isCollect = false;//是否收藏过

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_merchant_info);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		sideslipView = (ImageView) findViewById(R.id.sideslip_btn);
		personInfo = (ImageView) findViewById(R.id.person_info_btn); 
		collectView = (ImageView) findViewById(R.id.collect_image);
		merchantImage = (ImageView) findViewById(R.id.merchant_image);
		merchantName = (TextView) findViewById(R.id.merchant_name);
		merchantAddress = (TextView) findViewById(R.id.merchant_address);
		goodsList = (ListView) findViewById(R.id.goods_list);

	}

	@Override
	protected void initView() {
		merchantName.setText(getIntent().getStringExtra("merchantName"));

		sideslipView.setOnClickListener(this);
		personInfo.setOnClickListener(this);
		collectView.setOnClickListener(this);
		goodsAdapter = new GoodsAdapter();
		goodsList.setAdapter(goodsAdapter);
		goodsList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sideslip_btn:
			intent = new Intent(MerchantInfoActivity.this,SideslipActivity.class);
			startActivity(intent);
			break;
		case R.id.person_info_btn:
			intent = new Intent(MerchantInfoActivity.this,PersonInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.collect_image:
			if(isCollect){
				CommonTools.showShortToast(MerchantInfoActivity.this, "取消收藏成功！");
				collectView.setImageResource(R.drawable.uncollect_img);
				isCollect = false;
			}else{
				CommonTools.showShortToast(MerchantInfoActivity.this, "添加收藏成功！");
				collectView.setImageResource(R.drawable.collected_img);
				isCollect = true;
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 确认兑换该商品
	 */
	private void sureExchange() {
		final MyAlertDialog dialog = new MyAlertDialog(MerchantInfoActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("是否确定兑换该商品？");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				//确定兑换操作
				sureExchangeAgain();
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

	/**
	 * 再次确认兑换该商品
	 */
	private void sureExchangeAgain() {
		final MyAlertDialog dialog = new MyAlertDialog(MerchantInfoActivity.this);
		dialog.setTitle("提示");
		dialog.setMessage("请再次确认是否兑换该商品，点击确认之后将扣除积分！");
		View.OnClickListener comfirm = new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				//确定兑换操作

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
		sureExchange();
	}

	private final class GoodsAdapter extends BaseAdapter{
		private class ViewHolder {
			public ImageView goodsImage;
			public TextView goodsName;
			public TextView goodsIntegral;
//			public TextView 
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
				view = LayoutInflater.from(MerchantInfoActivity.this).inflate(R.layout.goods_list_item, null);
				holder = new ViewHolder();
				holder.goodsImage = (ImageView) view.findViewById(R.id.goods_image);
				holder.goodsName = (TextView) view.findViewById(R.id.goods_name);
				holder.goodsIntegral = (TextView) view.findViewById(R.id.goods_integral);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}
			//			holder.goodsImage
			holder.goodsName.setText("全家桶" + position);
			holder.goodsIntegral.setText((99 - position) + "");

			return view;
		}
	}



}
