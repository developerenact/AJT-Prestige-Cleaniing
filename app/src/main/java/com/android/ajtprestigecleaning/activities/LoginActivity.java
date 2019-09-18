package com.android.ajtprestigecleaning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.fragments.SignInFragment;
import com.android.ajtprestigecleaning.fragments.SignUpFragment;

public class LoginActivity extends AppCompatActivity {
    FragmentManager manager;
    SignInFragment signInFragment;
    SignUpFragment signUpFragment;
    FrameLayout frameLayout;
    View signin_line,signup_line;
    TextView tv_signin,tv_signup;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        frameLayout=findViewById(R.id.container);
        signin_line=findViewById(R.id.sign_in_view);
        signup_line=findViewById(R.id.sign_up_view);
        tv_signin=findViewById(R.id.label_signin);
        tv_signup=findViewById(R.id.label_signup);
        final FragmentManager fm = getSupportFragmentManager();
        signInFragment = new SignInFragment();
        signUpFragment = new SignUpFragment();
        fm.beginTransaction().add(R.id.container, signInFragment).commit();
        signin_line.setVisibility(View.VISIBLE);

        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.container, signInFragment).commit();
                signin_line.setVisibility(View.VISIBLE);
                signup_line.setVisibility(View.GONE);

            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.container, signUpFragment).commit();
                signup_line.setVisibility(View.VISIBLE);
                signin_line.setVisibility(View.GONE);

            }
        });



    }
}
