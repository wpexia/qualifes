package com.qualifes.app.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qualifes.app.R;
import com.qualifes.app.manager.OrderManager;
import com.qualifes.app.ui.*;
import com.qualifes.app.ui.adapter.OrderListAdapter;
import com.qualifes.app.ui.fragment.LoginFragment;
import com.qualifes.app.ui.fragment.UnLoginFragment;
import org.json.JSONArray;

public class PersonalFragment extends Fragment implements View.OnClickListener {


    private SharedPreferences sp;

    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private UnLoginFragment unLoginFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager = getFragmentManager();
        sp = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        updateView();
        getActivity().findViewById(R.id.personal_history).setOnClickListener(this);
        getActivity().findViewById(R.id.personal_follow).setOnClickListener(this);
        getActivity().findViewById(R.id.personal_position).setOnClickListener(this);
        getActivity().findViewById(R.id.personal_money).setOnClickListener(this);
        getActivity().findViewById(R.id.nopayorder).setOnClickListener(this);
        getActivity().findViewById(R.id.noshiporder).setOnClickListener(this);
        getActivity().findViewById(R.id.orderlist).setOnClickListener(this);

    }


    Handler getBadgeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONArray data = (JSONArray) msg.obj;
                if (data.length() > 0) {
                    ((TextView) getActivity().findViewById(R.id.newBadge)).setText(String.valueOf(data.length()));
                    getActivity().findViewById(R.id.newBadge).setVisibility(View.VISIBLE);
                } else {
                    getActivity().findViewById(R.id.newBadge).setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        if (sp.contains("token")) {
            if (loginFragment == null) {
                loginFragment = new LoginFragment();
                transaction.add(R.id.personal_fragment, loginFragment);
            } else {
                transaction.show(loginFragment);
            }
        } else {
            if (unLoginFragment == null) {
                unLoginFragment = new UnLoginFragment();
                transaction.add(R.id.personal_fragment, unLoginFragment);
            } else {
                transaction.show(unLoginFragment);
            }
        }
        transaction.commit();
        if (sp.contains("token")) {
            OrderManager manager = OrderManager.getInstance();
            manager.getOrder(sp.getString("token", ""), "2", "0,3,5", "1,5", getBadgeHandler);
        } else {
            getActivity().findViewById(R.id.newBadge).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.personal_history: {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.personal_follow: {
                Intent intent = new Intent(getActivity(), FollowActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.personal_position: {
                Intent intent = new Intent(getActivity(), PositionActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.personal_money: {
                Intent intent = new Intent(getActivity(), MoneyActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.nopayorder: {
                Intent intent = new Intent(getActivity(), OrderNoPayActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.noshiporder: {
                Intent intent = new Intent(getActivity(), OrderNoShipActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.orderlist: {
                Intent intent = new Intent(getActivity(), OrderListActivity.class);
                startActivity(intent);
            }
        }
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (loginFragment != null) {
            transaction.hide(loginFragment);
        }
        if (unLoginFragment != null) {
            transaction.hide(unLoginFragment);
        }
    }
}
