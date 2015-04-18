package com.qualifes.view.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView{
	
//	private ScrollViewListener scrollViewListener = null; 
	
	public interface OnGetBottomListener {
        public void onBottom();
    }
	
	
	//�ײ�����
	private OnGetBottomListener onGetBottomListener;
	
	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
//	  public void setScrollViewListener(ScrollViewListener scrollViewListener) {  
//	        this.scrollViewListener = scrollViewListener;  
//	    }  
	  
	//����ScrollView�������̣��������һ��λ�� ������Ϊ������
	    @Override  
	    protected void onScrollChanged(int x, int y, int oldx, int oldy) {  
	    	super.onScrollChanged(x, y, oldx, oldy);  
	    
//	        if(scrollViewListener != null) {  
//	            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);  
//	        }  
	        //
	        if( getChildCount()>=1&&getHeight() + getScrollY() == getChildAt(getChildCount()-1).getBottom()){
	    	//if( getChildCount()>=1&&getHeight() + getScrollY() == 10){
	        	onGetBottomListener.onBottom();				  
			}
	    }  
	    
	    public interface ScrollViewListener {    
	        void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);   
	    }
 
	    public void setBottomListener(OnGetBottomListener listener){
	    	onGetBottomListener = listener;
	    }

	    @Override
	    public boolean onTouchEvent(MotionEvent ev){
	    	return super.onTouchEvent(ev);
	    }
	    
	    @Override
	    public boolean dispatchTouchEvent(MotionEvent ev) {
	    	// TODO Auto-generated method stub
	    	return super.dispatchTouchEvent(ev);
	    }
	    
}
