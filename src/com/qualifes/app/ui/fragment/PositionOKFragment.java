package com.qualifes.app.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.qualifes.app.R;
import com.qualifes.app.manager.PositionManager;
import com.qualifes.app.ui.adapter.PositionAdapter;
import com.qualifes.app.ui.AddPositionActivity;
import com.qualifes.app.ui.PositionActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PositionOKFragment extends Fragment {

    private SharedPreferences sp;
    private View mView;
    private SwipeMenuListView listView;
    PositionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.position_ok, container, false);
        initView();
        return mView;
    }

    private void initView() {
        listView = (SwipeMenuListView) mView.findViewById(R.id.listView);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
        mView.findViewById(R.id.add).setOnClickListener((PositionActivity) getActivity());
        mView.findViewById(R.id.icon_in).setOnClickListener((PositionActivity) getActivity());
        getPos();
        listView = (SwipeMenuListView) mView.findViewById(R.id.listView);
        createSwipeMenu();
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
                if(adapter != null) {
                    adapter.delete(position);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPos();
    }

    private void getPos() {
        PositionManager positionManager = PositionManager.getInstance();
        positionManager.getPosition(sp.getString("token", ""), getPosition, getActivity());
    }

    Handler getPosition = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    JSONArray data = (JSONArray) msg.obj;
                    JSONArray noDefault = new JSONArray();
                    for (int i = 0; i < data.length(); i++) {
                        final JSONObject obj = data.getJSONObject(i);
                        if (obj.getInt("is_default") == 0) {
                            noDefault.put(obj);
                        } else {
                            ((TextView) getActivity().findViewById(R.id.defaultname)).setText(obj.getString("consignee"));
                            ((TextView) getActivity().findViewById(R.id.defaultphone)).setText(obj.getString("mobile"));
                            ((TextView) getActivity().findViewById(R.id.defaultaddress)).setText(obj.getString("province_name") + "省" + obj.getString("city_name") + "市" + obj.getString("district_name") + obj.getString("address"));
                            getActivity().findViewById(R.id.defaultposition).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), AddPositionActivity.class);
                                    intent.putExtra("position", obj.toString());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                    adapter = new PositionAdapter();
                    adapter.setContent(noDefault, getActivity());
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
