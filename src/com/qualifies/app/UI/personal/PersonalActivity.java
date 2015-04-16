package com.qualifies.app.ui.personal;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.qualifies.app.R;
import com.qualifies.app.ui.LoginActivity;
import com.qualifies.app.ui.RegisterActivity;

public class PersonalActivity extends Activity implements View.OnClickListener {

    private SharedPreferences sp;


    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private UnLoginFragment unLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences("user", MODE_PRIVATE);
        setContentView(R.layout.personal);
        fragmentManager = getFragmentManager();
        updateView();
    }


    private void updateView() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        if (sp.contains("username")) {
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
    }


    private void hideFragment(FragmentTransaction transaction) {
        if (loginFragment != null) {
            transaction.hide(loginFragment);
        }
        if (unLoginFragment != null) {
            transaction.hide(unLoginFragment);
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.personal_login: {
                Intent intent = new Intent(PersonalActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.personal_register: {
                Intent intent = new Intent(PersonalActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        }
    }

}
