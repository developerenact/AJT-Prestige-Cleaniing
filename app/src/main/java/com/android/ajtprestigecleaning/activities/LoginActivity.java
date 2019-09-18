package com.android.ajtprestigecleaning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends BaseActivityk {
    FragmentManager manager;
    SignInFragment signInFragment;
    SignUpFragment signUpFragment;
    FrameLayout frameLayout;
    View signin_line,signup_line;
    TextView tv_signin,tv_signup;
    FragmentTransaction fragmentTransaction;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);

        PagerAdapter pageAdapter=new com.android.ajtprestigecleaning.adapter.PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    protected int getLayoutResourceId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_login;
    }
}
