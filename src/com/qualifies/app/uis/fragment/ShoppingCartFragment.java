package com.qualifies.app.uis.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.manager.ShoppingCartManager;
import com.qualifies.app.uis.adapter.ShoppingCartAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingCartFragment extends Fragment {
    private View mView;
    private ListView listView;
    private SharedPreferences sp;
    private ShoppingCartAdapter adapter;

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

        ShoppingCartManager manager = ShoppingCartManager.getInstance();
        manager.getShoppingCart(sp.getString("token", ""), getShoppingCartHandler, getActivity());

    }

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
                    CheckBox checkTotal = (CheckBox) getActivity().findViewById(R.id.totalcheckBox);
                    checkTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
