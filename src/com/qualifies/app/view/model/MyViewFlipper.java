package com.qualifies.app.view.model;

import com.qualifies.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class MyViewFlipper extends ViewFlipper {

	Context mContext;
	public MyViewFlipper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void startFlipping(Context context) {
		// TODO Auto-generated method stub
		Animation lInAnim = AnimationUtils.loadAnimation(context, R.anim.push_left_in);		// ���󻬶�������Ľ���Ч����alpha 0.1  -> 1.0��
		Animation lOutAnim = AnimationUtils.loadAnimation(context, R.anim.push_left_out); 	// ���󻬶��Ҳ໬���Ľ���Ч����alpha 1.0  -> 0.1��

		this.setInAnimation(lInAnim);
		this.setOutAnimation(lOutAnim);
		super.startFlipping();
	}
}
