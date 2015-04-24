package com.qualifies.app.view.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class MyListView extends ListView{
	
	/**
	 * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE
	 */	
	int mLastMotionY ;
	
	boolean bottomFlag;
	
	public MyListView(Context context) {
		super(context);
	}
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	//用于拦截手势�?
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		//
		if(bottomFlag){
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		
		return super.onInterceptTouchEvent(ev);
	}
	/***
	 * 针对touch 事件进行监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		//获取 触控点相对于屏幕的位�?
		int y = (int) ev.getRawY();
		
		switch (ev.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// deltaY > 0 存在纵向滑动事件
			int deltaY = y - mLastMotionY;
			
			if(deltaY>0){
				View child = getChildAt(0);
				if(child!=null){
					
					//如果能看到的第一�? 是listview的第�?�? 而且 这一item的最顶部也是�?顶部 则把控制权交给父�?
					if (getFirstVisiblePosition() == 0
							&& child.getTop() == 0) {
							bottomFlag = false;
							getParent().requestDisallowInterceptTouchEvent(false); 
						}
					
					int top = child.getTop();
					int padding = getPaddingTop();
					if (getFirstVisiblePosition() == 0
							&& Math.abs(top - padding) <= 8) {
							bottomFlag = false;
							getParent().requestDisallowInterceptTouchEvent(false); 
				
						}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		
		
	
		return super.onTouchEvent(ev);
	}
	
	public void setBottomFlag(boolean flag){
		bottomFlag = flag;
	}
}
