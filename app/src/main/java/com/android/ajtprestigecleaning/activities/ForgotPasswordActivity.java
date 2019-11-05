package com.android.ajtprestigecleaning.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
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

public class ForgotPasswordActivity extends BaseActivity {
    TextView label_pass,label_welcome;
    EditText et_email;
    ImageView back, forgot_pass;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(ForgotPasswordActivity.this,R.color.white));
        }
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
                    et_email.requestFocus();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()) {
                    et_email.setError("Please enter valid email address");
                    et_email.requestFocus();
                } else {
                    resetPass();
                }
            }
        });


    }

    @Override
    public int getLayoutResourceId() {
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        return R.layout.activity_forgot_password;
    }


    public void resetPass() {
        showProgress();
     //  showLoader(ForgotPasswordActivity.this);
        if (isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<ResetPassword> call = service.forgotPassword(et_email.getText().toString());
            call.enqueue(new Callback<ResetPassword>() {
                @Override
                public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                    if (response.isSuccessful()) {
                     // hideLoader();
                        hideProgress();
                        forgotPassCustomDialog( response.body().getMessage(),ForgotPasswordActivity.this);
                    } else {
                       // hideLoader();
                       hideProgress();
                        Toast.makeText(ForgotPasswordActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<ResetPassword> call, Throwable t) {
                   // hideLoader();
                    hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(ForgotPasswordActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
           // hideLoader();
            hideProgress();
            customDialog(getApplicationContext().getString(R.string.no_internet),ForgotPasswordActivity.this);

        }

    }

}
