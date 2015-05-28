package com.qualifes.app.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qualifes.app.R;
import com.qualifes.app.ui.HomeActivity;
import com.qualifes.app.ui.SettingActivity;


public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().findViewById(R.id.personal_setting_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        changeUsername();
    }


    private void changeUsername() {
        SharedPreferences sp = getActivity().getSharedPreferences("user", Activity.MODE_PRIVATE);
        String username = sp.getString("username","00000000000");
        username = username.substring(0,3) + "****" + username.substring(7,11);
        ((TextView) getActivity().findViewById(R.id.username)).setText(username);
    }
    @Override
    public void onResume() {
        super.onResume();
        changeUsername();
    }
}
