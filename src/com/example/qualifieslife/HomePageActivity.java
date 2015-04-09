package com.example.qualifieslife;

import com.qualifes.adapter.HomePageGridViewAdapter;
import com.qualifes.adapter.HomePageListViewAdapter;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class HomePageActivity extends Activity implements OnClickListener{

	EditText editText;
	ImageButton imageButtonL;
	ImageButton imageButtonR;
	GridView gridView1;
	GridView gridView2;
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.homepage_activity);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void init() {
		editText = (EditText)findViewById(R.id.input);
		imageButtonL = (ImageButton)findViewById(R.id.secondPic);
		imageButtonL.setOnClickListener(this);
		imageButtonR = (ImageButton)findViewById(R.id.thirdPic);
		imageButtonR.setOnClickListener(this);
		
		gridView1 = (GridView)findViewById(R.id.gridView1);
		gridView2 = (GridView)findViewById(R.id.gridView2);
		listView = (ListView)findViewById(R.id.listView);
		
		int size = 5;
		int itemWidth = 200;
		int gridviewWidth = size * itemWidth;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
		
		gridView1.setLayoutParams(params); // 重点
		gridView1.setColumnWidth(itemWidth); // 重点
		gridView1.setStretchMode(GridView.NO_STRETCH);
		gridView1.setNumColumns(size); // 重点
		
		gridView2.setLayoutParams(params); // 重点
		gridView2.setColumnWidth(itemWidth); // 重点
		gridView2.setStretchMode(GridView.STRETCH_SPACING);
		gridView2.setNumColumns(size); // 重点
		
		gridView1.setAdapter(new HomePageGridViewAdapter(getApplicationContext(), null, R.layout.good_group_item));
		gridView2.setAdapter(new HomePageGridViewAdapter(getApplicationContext(), null, R.layout.good_group_item));
		
		listView.setAdapter(new HomePageListViewAdapter(getApplicationContext(),null,R.layout.good_info_item));
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
			case R.id.search:
				break;
			case R.id.secondPic:
				
				break;
			case R.id.thirdPic:
				break;
				
			default:
				break;
		}
	}
}
