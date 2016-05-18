package com.janyee.bladea.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.janyee.bladea.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 自定义ListView，下拉刷新、上拉自动加载更多
 * @author liujing
 * @version 1.0
 */
public abstract class PullListView<T,V extends View> extends ListView implements OnScrollListener {

	private final int PULL_DOWN_REFRESH = 0;//下拉状态
	private final int RELEASE_REFRESH = 1;//松开状态
	private final int REFRESHING = 2;//刷新中状态
	private int currentState = PULL_DOWN_REFRESH;
	private int mListViewOnScreenY = -1;
	private int downY = -1;
	
	private boolean isLoadingMore = false;
	private boolean isEnabledPullDownRefresh = false;
	private boolean isEnabledLoadMore = false;
	private AutoFreshDataAdapter<T,V> adapter;
	
	//头布局、脚布局及高度
	private View mFootView;
	private LinearLayout mHeaderView;
	private int mFooterViewHeight;
	private int mPullDownHeaderViewHeight;
	
	//mHeaderView中组件及动画
	private View mCustomHeaderView;//用户自定义头布局
	private View mPullDownHeader;//下拉刷新头布局
	private RotateAnimation upAnimation,downAnimation;
	private ImageView ivArrow;
	private ProgressBar mProgressBar;
	private TextView tv_statue,tv_time;

	public PullListView(Context context) {
		this(context, null);
	}

	public PullListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		adapter=new AutoFreshDataAdapter<T, V>(context) {
			@Override
			public List<T> refreshWork() {
				return PullListView.this.refresh();
			}

			@Override
			public List<T> loadMoreWork() {
				return PullListView.this.loadMore();
			}

			@Override
			public V getView(Context context, int position, T data) {
				return PullListView.this.getView(context,position,data);
			}
			@Override
			public V update(V v, int position, T data) {
				return PullListView.this.update(v,position,data);
			}
		};
		setAdapter(adapter);
		initPullDownHeaderView();
		initLoadMoreFooterView();
	}

	
	private void initLoadMoreFooterView() {
		//加载更多的布局文件
		mFootView = View.inflate(getContext(), R.layout.pull_listview_footer,
				null);
		mFootView.measure(0, 0);//测量
		mFooterViewHeight = mFootView.getMeasuredHeight();
		//隐藏脚布局
		mFootView.setPadding(0,-mFooterViewHeight,0,0);
		addFooterView(mFootView);
		setOnScrollListener(this);
	}

	private void initPullDownHeaderView() {
		//下拉刷新的布局文件
		mHeaderView = (LinearLayout) View.inflate(getContext(),
				R.layout.pull_listview_header, null);
		mPullDownHeader = mHeaderView
				.findViewById(R.id.ll_refresh_pull_down_header);
		ivArrow = (ImageView) mHeaderView
				.findViewById(R.id.iv_refresh_header_arrow);
		mProgressBar = (ProgressBar) mHeaderView
				.findViewById(R.id.pb_refresh_header);
		tv_statue = (TextView) mHeaderView
				.findViewById(R.id.tv_refresh_header_status);
		tv_time = (TextView) mHeaderView
				.findViewById(R.id.tv_refresh_header_time);
		mPullDownHeader.measure(0, 0);//测量
		mPullDownHeaderViewHeight = mPullDownHeader.getMeasuredHeight();
		//隐藏头布局
		mPullDownHeader.setPadding(0, -mPullDownHeaderViewHeight, 0, 0);
		addHeaderView(mHeaderView);
		initAnimation();
	}
	
	/**
	 * 添加额外的头布局，比如轮播图
	 * @param v 自定义头布局
	 */
	public void addListViewCustomHeaderView(View v) {
		mCustomHeaderView = v;
		mHeaderView.addView(mCustomHeaderView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (downY == -1)
				downY = (int) ev.getY();
			//是否启用下拉刷新
			if (!isEnabledPullDownRefresh)
				break;
			if (currentState == REFRESHING)
				break;
			//解决用户添加header与下拉刷新header的冲突
			if (mCustomHeaderView != null) {
				int[] location = new int[2];
				if (mListViewOnScreenY == -1) {
					this.getLocationOnScreen(location);
					mListViewOnScreenY = location[1];
				}
				mCustomHeaderView.getLocationOnScreen(location);
				if (location[1] < mListViewOnScreenY) {
					break;
				}
			}

			int moveY = (int) ev.getY();
			int diffY = (moveY - downY)/2;
			if (diffY > 0 && getFirstVisiblePosition() == 0) {
				int paddingTop = -mPullDownHeaderViewHeight + diffY;
				if (paddingTop < 0 && currentState != PULL_DOWN_REFRESH) {
					//当前没有完全显示且当前状态为松开刷新，进入下拉刷新
					currentState = PULL_DOWN_REFRESH;
					refreshPullDownState();
				} else if (paddingTop > 0 && currentState != RELEASE_REFRESH) {
					//当前完全显示且当前状态为下拉刷新，进入松开刷新
					currentState = RELEASE_REFRESH;
					refreshPullDownState();
				}
				mPullDownHeader.setPadding(0, paddingTop, 0, 0);
				return true;
			}else if(diffY < 0 && getLastVisiblePosition() == getCount()-1){
				//脚布局可向上滑动
				mFootView.setPadding(0,0,0,0);
			}
			break;
		case MotionEvent.ACTION_UP:
			downY = -1;
			if (currentState == PULL_DOWN_REFRESH) {
				//隐藏header
				mPullDownHeader.setPadding(0, -mPullDownHeaderViewHeight, 0, 0);
			} else if (currentState == RELEASE_REFRESH) {
				currentState = REFRESHING;
				refreshPullDownState();
				mPullDownHeader.setPadding(0, 0, 0, 0);
				//回调刷新方法
				if (adapter != null) {
					adapter.refresh();
				}
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	/**
	 * 隐藏头布局或脚布局并重置控件
	 */
	public void OnRefreshDataFinish() {
		if (isLoadingMore) {
			isLoadingMore = false;
			mFootView.setPadding(0,-mFooterViewHeight,0,0);
		} else {
			ivArrow.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
			tv_statue.setText("下拉刷新");
			tv_time.setText("最后刷新时间：" + getCurrentTime());
			mPullDownHeader.setPadding(0, -mPullDownHeaderViewHeight, 0, 0);
			currentState = PULL_DOWN_REFRESH;

		}
	}

	private String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(new Date());
	}

	private void refreshPullDownState() {
		switch (currentState) {
		case PULL_DOWN_REFRESH:
			ivArrow.startAnimation(downAnimation);
			tv_statue.setText("下拉刷新");
			break;
		case RELEASE_REFRESH:
			ivArrow.startAnimation(upAnimation);
			tv_statue.setText("松开刷新");
			break;
		case REFRESHING:
			ivArrow.clearAnimation();
			ivArrow.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
			tv_statue.setText("正在刷新");
			break;
		default:
			break;
		}
	}
	
	/**
	 * 箭头旋转动画
	 */
	private void initAnimation() {
		upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(200);
		upAnimation.setFillAfter(true);

		downAnimation = new RotateAnimation(-180, -360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		downAnimation.setDuration(200);
		downAnimation.setFillAfter(true);
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (!isEnabledLoadMore) {
			return;
		}
		//listView停止状态或惯性滑动状态
		if (scrollState == SCROLL_STATE_IDLE
				|| scrollState == SCROLL_STATE_FLING) {
			//listView已到达最底部
			if ((getLastVisiblePosition() == getCount() - 1) && !isLoadingMore) {
				isLoadingMore = true;
				//展示脚布局
				mFootView.setPadding(0, 0, 0, 0);
				setSelection(getCount());
				if (adapter != null) {
					adapter.loadMore();
				}
			}
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		
	}
	
	/**
	 * 是否启用下拉刷新
	 * @param isEnable
	 */
	public void setEnabledPullDownRefresh(boolean isEnable) {
		isEnabledPullDownRefresh = isEnable;
	}
	
	/**
	 * 是否启用加载更多
	 * @param isEnable
	 */
	public void setEnabledLoadMore(boolean isEnable) {
		isEnabledLoadMore = isEnable;
	}

	protected abstract V getView(Context context,int position,T data);
	protected abstract V update(V v, int position, T data);

	public abstract List<T> refresh();

	public abstract List<T> loadMore();
}
