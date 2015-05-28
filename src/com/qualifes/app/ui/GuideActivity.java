package com.qualifes.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.qualifes.app.R;
import com.tonicsystems.jarjar.asm.Handle;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity {

    private ViewPager mPager;
    private LinearLayout mDotsLayout;
    private ImageButton mBtn;

    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
        sp.edit().putString("first","true").apply();

        mPager = (ViewPager) findViewById(R.id.guide_viewpager);
        mDotsLayout = (LinearLayout) findViewById(R.id.guide_dots);
        mBtn = (ImageButton) findViewById(R.id.guide_btn);

        initPager();
        mPager.setAdapter(new ViewPagerAdapter(viewList));
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                    if (i == arg0) {
                        mDotsLayout.getChildAt(i).setSelected(true);
                    } else {
                        mDotsLayout.getChildAt(i).setSelected(false);
                    }
                }
                if (arg0 == mDotsLayout.getChildCount() - 1) {
                    mBtn.setVisibility(View.VISIBLE);
                } else {
                    mBtn.setVisibility(View.GONE);
                }
                if (arg0 == mDotsLayout.getChildCount()) {
                    Handler x = new Handler();
                    x.postDelayed(new Run(),100);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
//                Log.e("guide", "" + arg0 + "   " + arg1 + "    " + arg2);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        mBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                openHome();
            }
        });
    }

    class Run implements Runnable{
        @Override
        public void run() {
            finish();
        }
    }

    private void initPager() {
        viewList = new ArrayList<View>();
        int[] images = new int[]{R.drawable.first, R.drawable.second, R.drawable.three, R.drawable.fourth};
        for (int i = 0; i < images.length; i++) {
            viewList.add(initView(images[i]));
        }
        initDots(images.length - 1);
    }

    private void initDots(int count) {
        for (int j = 0; j < count; j++) {
            mDotsLayout.addView(initDot());
        }
        mDotsLayout.getChildAt(0).setSelected(true);
    }

    private View initDot() {
        return LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dot, null);
    }

    private View initView(int res) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_guide, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iguide_img);
        imageView.setImageResource(res);
        return view;
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(GuideActivity.this);
    }

    private void openHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    class ViewPagerAdapter extends PagerAdapter {

        private List<View> data;


        public ViewPagerAdapter(List<View> data) {
            super();
            this.data = data;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(data.get(position));
            return data.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(data.get(position));
        }

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
