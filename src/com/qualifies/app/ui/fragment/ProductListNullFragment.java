package com.qualifies.app.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qualifies.app.R;

public class ProductListNullFragment extends Fragment {
    private String content = "";

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_null, container, false);
        ((TextView) view.findViewById(R.id.content)).setText(content);
        return view;
    }
}
