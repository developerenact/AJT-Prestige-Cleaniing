package com.android.ajtprestigecleaning.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.util.Constants;

import io.paperdb.Paper;

public class SplashActivity extends BaseActivityk {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(Paper.book().read(Constants.ISLOGIN,"false").equals("true")){
                    Intent mainIntent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
                else {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }




            }
        }, 3000);
    }

    @Override
    protected int getLayoutResourceId() {
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        return R.layout.activity_splash;
    }
}
