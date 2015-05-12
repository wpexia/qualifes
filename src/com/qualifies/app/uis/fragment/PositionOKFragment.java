package com.qualifies.app.uis.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.manager.PositionManager;
import com.qualifies.app.uis.adapter.PositionAdapter;
import com.qualifies.app.uis.personal.AddPositionActivity;
import com.qualifies.app.uis.personal.PositionActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PositionOKFragment extends Fragment {

    private SharedPreferences sp;
    private View mView;
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.position_ok, container, false);
        initView();
        return mView;
    }

    private void initView() {
        listView = (ListView) mView.findViewById(R.id.listView);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
        mView.findViewById(R.id.add).setOnClickListener((PositionActivity) getActivity());
        mView.findViewById(R.id.icon_in).setOnClickListener((PositionActivity) getActivity());
        listView = (ListView) getActivity().findViewById(R.id.listView);

        PositionManager positionManager = PositionManager.getInstance();
        positionManager.getPosition(sp.getString("token", ""), getPosition, getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
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
                    PositionAdapter adapter = new PositionAdapter();
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
