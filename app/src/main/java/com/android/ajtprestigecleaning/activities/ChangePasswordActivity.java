package com.android.ajtprestigecleaning.activities;

import androidx.appcompat.app.AppCompatActivity;

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

public class ChangePasswordActivity extends BaseActivityk {
    TextView ed_oldPass, ed_newPass, ed_confirmPass;
    ImageView back, change_pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ed_oldPass = findViewById(R.id.et_old_pass);
        ed_newPass = findViewById(R.id.et_new_password);
        ed_confirmPass = findViewById(R.id.et_confirm_password);
        back = findViewById(R.id.back);
        change_pass = findViewById(R.id.chnagePass);

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
                    ed_oldPass.setError("Please enter your Old Password");
                } else if (ed_newPass.getText().toString().isEmpty()) {
                    ed_newPass.setError("Please enter New Password");
                }
                else if (ed_confirmPass.getText().toString().isEmpty()) {
                    ed_confirmPass.setError("Please Re-enter your New Password");
                }
                else if (!ed_newPass.getText().toString().matches(ed_confirmPass.getText().toString())) {
                    ed_confirmPass.setError("Password and Confirm password do not match");
                }

                else {
                    chnagePasswordApi();
                }
            }
        });

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_change_password;
    }


    public void chnagePasswordApi() {
        showLoader(ChangePasswordActivity.this);
        if (isNetworkConnected(ChangePasswordActivity.this)) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<ChangePasswordPojo> call = service.chnagePass(Paper.book().read(Constants.USERID, "1"), getMd5Hash(ed_oldPass.getText().toString()), getMd5Hash(ed_confirmPass.getText().toString()));
            call.enqueue(new Callback<ChangePasswordPojo>() {
                @Override
                public void onResponse(Call<ChangePasswordPojo> call, Response<ChangePasswordPojo> response) {
                    if (response.isSuccessful()) {
                        hideLoader();
                        customDialog(ChangePasswordActivity.this, response.body().getMessage());
                        ed_oldPass.setText("");
                        ed_newPass.setText("");
                        ed_confirmPass.setText("");
                    } else {
                        hideLoader();
                        Toast.makeText(ChangePasswordActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<ChangePasswordPojo> call, Throwable t) {
                    hideLoader();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(ChangePasswordActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            hideLoader();
            customDialog(ChangePasswordActivity.this, "Pleasr check your Internet Connection");

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
