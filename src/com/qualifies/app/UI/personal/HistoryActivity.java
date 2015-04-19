package com.qualifies.app.ui.personal;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.qualifies.app.R;
import com.qualifies.app.ui.adapter.ProductListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryActivity extends Activity {
    private SwipeMenuListView listView;
    private ProductListAdapter historyAdapter;
    private List<HashMap<String, Object>> data;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        initView();
    }

    private void initView() {
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        createSwipeMenu();
        data = getData();
        historyAdapter = new ProductListAdapter(this, data);
        listView.setAdapter(historyAdapter);
        listView.setDividerHeight(0);
        text = (TextView)findViewById(R.id.title);
        text.setText("浏览历史");
    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFF, 0x33,
                        0x00)));
                deleteItem.setWidth(140);
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.w("SwipeMenuListView", "OnClick" + String.valueOf(position) + "    " + String.valueOf(index));
                historyAdapter.delete(position);
                return true;// false : not close the menu; true : close the menu
            }
        });
    }

    private List<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 1; i <= 40; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", "婴儿神仙水 Mommy bilss gripe 缓解 胀气 吐奶等");
            map.put("image", "http://test.qualifes.com/images/201410/thumb_img/90_thumb_G_1414501935478.jpg");
            map.put("place", "产地 美国" + String.valueOf(i));
            map.put("discount", "55% 折扣");
            map.put("price", "￥298.00");
            map.put("oldPrice", "￥589.00");
            list.add(map);
        }
        return list;
    }

}
