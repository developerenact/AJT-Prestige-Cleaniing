package com.android.ajtprestigecleaning.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.ChangePasswordPojo.ChangePasswordPojo;
import com.android.ajtprestigecleaning.model.ResetPassword.ResetPassword;
import com.android.ajtprestigecleaning.util.Constants;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {
    TextView ed_oldPass, ed_newPass, ed_confirmPass,label_change,label_pass,toolbar_label;
    ImageView back, change_pass;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(ChangePasswordActivity.this,R.color.white));
        }
        label_change = findViewById(R.id.label_change);
        label_pass = findViewById(R.id.label_password);
        ed_oldPass = findViewById(R.id.et_old_pass);
        ed_newPass = findViewById(R.id.et_new_password);
        ed_confirmPass = findViewById(R.id.et_confirm_password);
        back = findViewById(R.id.back);
        change_pass = findViewById(R.id.chnagePass);
        toolbar_label=findViewById(R.id.toolbar_label);


        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.ttf");
        label_change.setTypeface(custom_font2);

        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        label_pass.setTypeface(custom_font);
        toolbar_label.setTypeface(custom_font);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_oldPass.getText().toString().isEmpty()) {
                    ed_oldPass.setError(getApplicationContext().getString(R.string.old_password_validation));
                    ed_oldPass.requestFocus();
                } else if (ed_newPass.getText().toString().isEmpty()) {
                    ed_newPass.setError(getApplicationContext().getString(R.string.new_pass_validation));
                    ed_newPass.requestFocus();
                }
                else if (ed_confirmPass.getText().toString().isEmpty()) {
                    ed_confirmPass.setError(getApplicationContext().getString(R.string.reenter_new_pass));
                    ed_confirmPass.requestFocus();
                }
                else if (!ed_newPass.getText().toString().matches(ed_confirmPass.getText().toString())) {
                    ed_confirmPass.setError(getApplicationContext().getString(R.string.pass_confirmpass_notmatch));
                    ed_confirmPass.requestFocus();
                }

                else {
                    chnagePasswordApi();
                }
            }
        });

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_change_password;
    }


    public void chnagePasswordApi() {
       // showLoader(ChangePasswordActivity.this);
        showProgress();
        if (isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<ChangePasswordPojo> call = service.chnagePass(Paper.book().read(Constants.USERID, "2"), getMd5Hash(ed_oldPass.getText().toString()), getMd5Hash(ed_confirmPass.getText().toString()));
            call.enqueue(new Callback<ChangePasswordPojo>() {
                @Override
                public void onResponse(Call<ChangePasswordPojo> call, Response<ChangePasswordPojo> response) {
                    if (response.isSuccessful()) {
                       // hideLoader();
                        hideProgress();
                        customDialog( response.body().getMessage(),ChangePasswordActivity.this);
                        ed_oldPass.setText("");
                        ed_newPass.setText("");
                        ed_confirmPass.setText("");
                    } else {
                       // hideLoader();
                        hideProgress();
                        Toast.makeText(ChangePasswordActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<ChangePasswordPojo> call, Throwable t) {
                   // hideLoader();
                    hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(ChangePasswordActivity.this, getApplicationContext().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            //hideLoader();
            hideProgress();
            customDialog( getApplicationContext().getString(R.string.no_internet),ChangePasswordActivity.this);

        }

    }

    public static String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }


}
