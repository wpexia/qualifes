package com.qualifes.app.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qualifes.app.R;
import com.qualifes.app.ui.OrderConfirmActivity;

public class OrderConfirmNullFragment extends Fragment {
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.order_confirm_headnull, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView.findViewById(R.id.chooseposition).setOnClickListener((OrderConfirmActivity) getActivity());
    }
}
