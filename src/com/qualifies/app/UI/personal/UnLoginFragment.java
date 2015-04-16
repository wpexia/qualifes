package com.qualifies.app.ui.personal;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qualifies.app.R;
import com.qualifies.app.ui.LoginActivity;
import com.qualifies.app.ui.RegisterActivity;


public class UnLoginFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_unlogin, container, false);
        view.findViewById(R.id.personal_login).setOnClickListener(this);
        view.findViewById(R.id.personal_register).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.personal_login: {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.personal_register: {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
            break;
        }
    }
}