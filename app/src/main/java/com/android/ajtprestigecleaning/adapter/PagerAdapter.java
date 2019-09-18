package com.android.ajtprestigecleaning.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.android.ajtprestigecleaning.fragments.SignInFragment;
import com.android.ajtprestigecleaning.fragments.SignUpFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    String[]frag={"Sign In","Sign Up"};
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return frag[position];
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SignInFragment();

            case 1:
                return new SignUpFragment();


        }
        return null;
    }

    @Override
    public int getCount() {
        return frag.length;
    }
}

