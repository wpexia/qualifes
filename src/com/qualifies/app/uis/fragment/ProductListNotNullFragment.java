package com.qualifies.app.uis.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.trinea.android.common.view.DropDownListView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.qualifies.app.R;
import com.qualifies.app.manager.HistoryManager;
import com.qualifies.app.uis.adapter.ProductListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class ProductListNotNullFragment extends Fragment {
    private DropDownListView listView;
    private ProductListAdapter historyAdapter;
    private View mView;
    private boolean hasStar = false;
    private LinkedList<HashMap<String, Object>> mData;
    private int managerId;


    public void setStar(boolean star) {
        hasStar = star;
    }

    public void setData(LinkedList<HashMap<String, Object>> data, int id) {
        this.mData = data;
        this.managerId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.history_notnull, container, false);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        listView.onDropDown();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listView.setSelection(1);
    }

    private void initView() {
        listView = (DropDownListView) mView.findViewById(R.id.listView);
        createSwipeMenu();
        listView.setDropDownStyle(true);
        listView.setOnBottomStyle(true);
        listView.setAutoLoadOnBottom(true);
        listView.setShowFooterProgressBar(true);
        listView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {
            @Override
            public void onDropDown() {
                new GetDataTask(true).execute();
            }
        });

        listView.setOnBottomListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetDataTask(false).execute();
            }
        });
        historyAdapter = new ProductListAdapter(mData, hasStar);
        listView.setAdapter(historyAdapter);
        listView.setDividerHeight(0);
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


    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        private boolean isDropDown;

        public GetDataTask(boolean isDropDown) {
            this.isDropDown = isDropDown;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ;
            }
            String[] test = {"", ""};
            return test;
        }

        @Override
        protected void onPostExecute(String[] result) {

            if (isDropDown) {
                historyAdapter.notifyDataSetChanged();
                // should call onDropDownComplete function of DropDownListView at end of drop down complete.
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                listView.onDropDownComplete(getString(R.string.update_at)
                        + dateFormat.format(new Date()));
            } else {
                switch (managerId) {
                    case 0: {
                        HistoryManager historyManager = HistoryManager.getInstance();
                        try {
                            SharedPreferences sp = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
                            historyManager.getHistory(sp.getString("token", ""), handler, getActivity(), mData.size());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    break;
                }
                historyAdapter.notifyDataSetChanged();
                // should call onBottomComplete function of DropDownListView at end of on bottom complete.
                listView.onBottomComplete();
            }

            super.onPostExecute(result);
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    JSONObject resp = (JSONObject) msg.obj;
                    try {
                        JSONObject data = resp.getJSONObject("data");
                        JSONArray goods = data.getJSONArray("data");
                        for (int i = 0; i < goods.length(); i++) {
                            JSONObject good = goods.getJSONObject(i);
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("title", good.getString("goods_name"));
                            map.put("image", good.getString("goods_thumb"));
                            map.put("place", "产地 " + good.getString("origin"));
                            double price = good.getDouble("shop_price");
                            map.put("price", "￥" + price);
                            double oldPrice = good.getDouble("market_price");
                            map.put("oldPrice", "￥" + oldPrice);
                            map.put("discount", (int) ((1 - (price / oldPrice)) * 100) + "% 折扣");
                            mData.add(map);
                        }
                    } catch (JSONException e) {
                        listView.setHasMore(false);
                        listView.onBottomComplete();
                    }
                }
            }
        };
    }

}
