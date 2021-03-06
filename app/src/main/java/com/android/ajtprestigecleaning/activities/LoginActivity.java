package com.android.ajtprestigecleaning.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.fragments.SignInFragment;
import com.android.ajtprestigecleaning.fragments.SignUpFragment;
import com.android.ajtprestigecleaning.util.CustomViewPager;
import com.android.ajtprestigecleaning.util.TabLayoutEx;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends BaseActivity {
    FragmentManager manager;
    SignInFragment signInFragment;
    SignUpFragment signUpFragment;
    FrameLayout frameLayout;
    View signin_line,signup_line;
    TextView tv_signin,tv_signup;
    FragmentTransaction fragmentTransaction;
    int pos;

    TabLayoutEx tabLayout;
    CustomViewPager viewPager;
    PagerAdapter pageAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this,R.color.white));
        }
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);
        pageAdapter=new com.android.ajtprestigecleaning.adapter.PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        viewPager.setPageEnbled(false);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setIndicatorWidth(50);
        changeTabsFont();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position==1)
                {
                   SignUpFragment signUpFragment= (SignUpFragment) pageAdapter.instantiateItem(viewPager,position);
                   signUpFragment.refreshScroll();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public int getLayoutResourceId() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_login;
    }

    @Override
    public void onBackPressed() {
        pos=viewPager.getCurrentItem();
        if(pos==1){
            viewPager.setCurrentItem(0);
        }
        else {
            finish();
        }

    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    AssetManager mgr = getApplicationContext().getAssets();
                    Typeface tf = Typeface.createFromAsset(mgr, "fonts/Montserrat-Medium.ttf");//Font file in /assets
                    ((TextView) tabViewChild).setTypeface(tf);
                }
            }
        }
    }


}
