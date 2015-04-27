package com.qualifies.app.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qualifies.app.R;

public class PositionChoseFragment extends Fragment {
    View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.position_chose, container, false);
        return mView;
    }
}
