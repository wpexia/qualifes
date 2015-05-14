package com.qualifies.app.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qualifies.app.R;
import com.qualifies.app.ui.OrderConfirmActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderConfirmOkFragment extends Fragment {
    private JSONObject position;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.order_confirm_headok, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mView.findViewById(R.id.chooseposition).setOnClickListener((OrderConfirmActivity) getActivity());
        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        try {
            ((TextView) mView.findViewById(R.id.name)).setText(position.getString("consignee"));
            String positionStr = position.getString("province_name") + "省" + position.getString("city_name") + "市" + position.getString("district_name") + position.getString("address");
            if (positionStr.length() > 21) {
                positionStr = positionStr.substring(0, 20) + "…";
            }
            ((TextView) mView.findViewById(R.id.position)).setText(positionStr);
            ((TextView) mView.findViewById(R.id.mobie)).setText(position.getString("mobile"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void changeView(JSONObject position) {
        this.position = position;
        refresh();
    }
}
