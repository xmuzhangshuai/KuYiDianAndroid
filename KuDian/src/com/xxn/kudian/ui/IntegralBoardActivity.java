package com.xxn.kudian.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xxn.kudian.R;
import com.xxn.kudian.base.BaseActivity;

public class IntegralBoardActivity extends BaseActivity implements OnClickListener{

	private ImageView sideslipView; //侧滑按键
	private ImageView personInfo;//个人信息界面
	private Intent intent;
	private ListView integralSortList;
	private IntegralBoardAdapter integralBoardAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_integral_board);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		sideslipView = (ImageView) findViewById(R.id.sideslip_btn);
		personInfo = (ImageView) findViewById(R.id.person_info_btn); 
		integralSortList = (ListView) findViewById(R.id.integral_board_list);

	}

	@Override
	protected void initView() {
		sideslipView.setOnClickListener(this);
		personInfo.setOnClickListener(this);

		integralBoardAdapter = new IntegralBoardAdapter();
		integralSortList.setAdapter(integralBoardAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sideslip_btn:
			intent = new Intent(IntegralBoardActivity.this,SideslipActivity.class);
			startActivity(intent);
			break;
		case R.id.person_info_btn:
			intent = new Intent(IntegralBoardActivity.this,PersonInfoActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	private final class IntegralBoardAdapter extends BaseAdapter{
		private class ViewHolder {
			public ImageView userImage;
			public TextView userName;
			public TextView userCollege;
			public TextView userIntegral;
			public TextView userSort;
			public TextView level;
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
				view = LayoutInflater.from(IntegralBoardActivity.this).inflate(R.layout.integral_board_list_item, null);
				holder = new ViewHolder();
				holder.userImage = (ImageView) view.findViewById(R.id.integral_board_image);
				holder.userName = (TextView) view.findViewById(R.id.integral_board_name);
				holder.userIntegral = (TextView) view.findViewById(R.id.integral_board_integral);
				holder.userCollege = (TextView) view.findViewById(R.id.integral_board_college);
				holder.userSort = (TextView) view.findViewById(R.id.integral_sort);
				holder.level = (TextView) view.findViewById(R.id.level);
				view.setTag(holder); // 给View添加一个格外的数据
			} else {
				holder = (ViewHolder) view.getTag(); // 把数据取出来
			}
			//			holder.userImage
			holder.userName.setText("刘云" + position);
			holder.userCollege.setText("厦门大学");
			holder.userIntegral.setText("1" + (99 - position));
			holder.userSort.setText((position + 4) + "");
			holder.level.setText("50");
			return view;
		}
	}

}
