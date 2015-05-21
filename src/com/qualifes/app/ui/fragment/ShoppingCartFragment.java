package com.qualifes.app.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.qualifes.app.R;
import com.qualifes.app.manager.FollowManager;
import com.qualifes.app.manager.HistoryManager;
import com.qualifes.app.manager.ShoppingCartManager;
import com.qualifes.app.ui.*;
import com.qualifes.app.ui.adapter.ShoppingCartAdapter;
import com.qualifes.app.util.OfflineCartDbHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingCartFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private ListView listView;
    private SharedPreferences sp;
    ShoppingCartAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.shopping_cart, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
        listView = (ListView) mView.findViewById(R.id.content);
//        createSwipeMenu();
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }


    private void init() {
        if (sp.contains("token")) {
            ShoppingCartManager manager = ShoppingCartManager.getInstance();
            manager.getShoppingCart(sp.getString("token", ""), getShoppingCartHandler);
        } else {
            OfflineCartDbHelper dbHelper = new OfflineCartDbHelper(getActivity());
            Cursor cursor = dbHelper.select();

            String goodsIds = "";
            while (cursor.moveToNext()) {
                goodsIds += cursor.getString(1) + ",";
            }
            ShoppingCartManager manager = ShoppingCartManager.getInstance();
            manager.getOfflineCart(goodsIds, getOfflineCartHandler);
        }
        ((TextView) getActivity().findViewById(R.id.total)).setText("0.00");
        ((CheckBox) getActivity().findViewById(R.id.totalcheckBox)).setChecked(false);
        getActivity().findViewById(R.id.creatOrder).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.creatOrder: {
                if (sp.contains("token")) {
                    if (adapter != null) {
                        Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                        intent.putExtra("goods", adapter.getGoods());
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
            break;
        }
    }

    Handler getOfflineCartHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {

                    JSONArray data = (JSONArray) msg.obj;
                    OfflineCartDbHelper dbHelper = new OfflineCartDbHelper(getActivity());
                    Cursor cursor = dbHelper.select();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        obj.put("checked", false);
                        cursor.moveToNext();
                        obj.remove("goods_number");
                        obj.put("goods_number", cursor.getString(3));
                        obj.remove("goods_attr");
                        JSONArray newarray = new JSONArray();
                        newarray.put(cursor.getString(4));
                        obj.put("goods_attr", newarray);
                    }

                    Log.e("after", data.toString());
                    final ShoppingCartAdapter adapter = new ShoppingCartAdapter(getActivity(), data);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                    CheckBox checkTotal = (CheckBox) getActivity().findViewById(R.id.totalcheckBox);
                    checkTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (adapter.flag) {
                                adapter.flag = false;
                                return;
                            }
                            if (isChecked) {
                                double total = adapter.selectAll();
                                adapter.notifyDataSetChanged();
                                ((TextView) getActivity().findViewById(R.id.total)).setText(String.valueOf(total));
                            } else {
                                adapter.selectNone();
                                adapter.notifyDataSetChanged();
                                ((TextView) getActivity().findViewById(R.id.total)).setText(String.valueOf("0"));
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    Handler getShoppingCartHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        obj.put("checked", false);
                    }
                    adapter = new ShoppingCartAdapter(getActivity(), data);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                    CheckBox checkTotal = (CheckBox) getActivity().findViewById(R.id.totalcheckBox);
                    checkTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (adapter.flag) {
                                adapter.flag = false;
                                return;
                            }
                            if (isChecked) {
                                double total = adapter.selectAll();
                                adapter.notifyDataSetChanged();
                                ((TextView) getActivity().findViewById(R.id.total)).setText(String.valueOf(total));
                            } else {
                                adapter.selectNone();
                                adapter.notifyDataSetChanged();
                                ((TextView) getActivity().findViewById(R.id.total)).setText(String.valueOf("0"));
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


}
