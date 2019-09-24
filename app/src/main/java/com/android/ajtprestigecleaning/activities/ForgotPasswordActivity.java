package com.android.ajtprestigecleaning.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.LoginPojo.LoginPojo;
import com.android.ajtprestigecleaning.model.ResetPassword.ResetPassword;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivityk {
    TextView label_pass,label_welcome;
    EditText et_email;
    ImageView back, forgot_pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        label_pass = findViewById(R.id.label_pass);
        label_welcome = findViewById(R.id.label_welcome);
        et_email = findViewById(R.id.et_email);
        forgot_pass = findViewById(R.id.forgot_pass);
        back = findViewById(R.id.back);
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        label_pass.setTypeface(custom_font);

        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.ttf");
        label_welcome.setTypeface(custom_font2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().isEmpty()) {
                    et_email.setError("Please enter your email address");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()) {
                    et_email.setError("Please enter valid email address");
                } else {
                    resetPass();
                }
            }
        });


    }

    @Override
    protected int getLayoutResourceId() {
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        return R.layout.activity_forgot_password;
    }


    public void resetPass() {
        showLoader(ForgotPasswordActivity.this);
        if (isNetworkConnected(ForgotPasswordActivity.this)) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<ResetPassword> call = service.forgotPassword(et_email.getText().toString());
            call.enqueue(new Callback<ResetPassword>() {
                @Override
                public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                    if (response.isSuccessful()) {
                       hideLoader();
                        customDialog(ForgotPasswordActivity.this, response.body().getMessage());
                    } else {
                        hideLoader();
                        Toast.makeText(ForgotPasswordActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<ResetPassword> call, Throwable t) {
                    hideLoader();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(ForgotPasswordActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            hideLoader();
            customDialog(ForgotPasswordActivity.this, getApplicationContext().getString(R.string.no_internet));

        }

    }




}
