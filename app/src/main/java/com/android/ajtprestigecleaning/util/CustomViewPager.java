package com.android.ajtprestigecleaning.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    boolean enable = true;

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (enable) {
            return super.onTouchEvent(ev);

        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (enable) {
            return super.onInterceptTouchEvent(ev);

        } else {
            return false;
        }


    }

    public void setPageEnbled(boolean enable) {
        this.enable = enable;
    }
}
