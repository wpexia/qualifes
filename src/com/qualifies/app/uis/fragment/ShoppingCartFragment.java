package com.qualifies.app.uis.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.qualifies.app.R;
import com.qualifies.app.uis.adapter.ShoppingCartAdapter;

public class ShoppingCartFragment extends Fragment {
    private View mView;
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.shopping_cart, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) mView.findViewById(R.id.content);
        ShoppingCartAdapter adapter = new ShoppingCartAdapter(getActivity());
        listView.setAdapter(adapter);
    }
}
