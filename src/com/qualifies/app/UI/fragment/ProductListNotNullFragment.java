package com.qualifies.app.ui.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.qualifies.app.R;
import com.qualifies.app.ui.adapter.ProductListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductListNotNullFragment extends Fragment {
    private SwipeMenuListView listView;
    private ProductListAdapter historyAdapter;
    private View mView;
    private boolean hasStar = false;


    public void setStar(boolean star) {
        hasStar = star;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.history_notnull, container, false);
        initView();
        return mView;
    }

    private void initView() {
        listView = (SwipeMenuListView) mView.findViewById(R.id.listView);
        createSwipeMenu();
        historyAdapter = new ProductListAdapter(getData(), hasStar);
        listView.setAdapter(historyAdapter);
        listView.setDividerHeight(0);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyAdapter.setContent(getActivity());
    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
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
