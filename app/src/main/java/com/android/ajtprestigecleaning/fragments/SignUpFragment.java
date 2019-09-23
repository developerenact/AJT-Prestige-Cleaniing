package com.android.ajtprestigecleaning.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.DashboardActivity;
import com.android.ajtprestigecleaning.apiServices.ApiInterface;
import com.android.ajtprestigecleaning.apiServices.BaseUrl;
import com.android.ajtprestigecleaning.model.RegisterPojo.RegisterPojo;
import com.android.ajtprestigecleaning.util.Constants;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.ajtprestigecleaning.activities.BaseActivityk.customDialog;
import static com.android.ajtprestigecleaning.activities.BaseActivityk.hideLoader;
import static com.android.ajtprestigecleaning.activities.BaseActivityk.isNetworkConnected;
import static com.android.ajtprestigecleaning.activities.BaseActivityk.showAlert;
import static com.android.ajtprestigecleaning.activities.BaseActivityk.showLoader;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
    TextView label_member,lebel_info;
    EditText et_email, et_username, et_pass, et_phone;
    ImageView signup;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        label_member = view.findViewById(R.id.label_member);
        lebel_info = view.findViewById(R.id.label_info);
        et_email = view.findViewById(R.id.et_email);
        et_username = view.findViewById(R.id.et_username);
        et_phone = view.findViewById(R.id.et_phone);
        et_pass = view.findViewById(R.id.et_pass);
        signup = view.findViewById(R.id.signup);
        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        label_member.setTypeface(custom_font);
        lebel_info.setTypeface(custom_font);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().isEmpty()) {
                    et_email.setError("Please enter your email address");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()) {
                    et_email.setError("Please enter valid email address");
                } else if (et_username.getText().toString().isEmpty()) {
                    et_username.setError("Please enter Username");
                } else if (et_phone.getText().toString().isEmpty()) {
                    et_phone.setError("Please Enter phone number");
                } else if (et_phone.getText().toString().length() < 10) {
                    et_phone.setError("Please enter valid phone number");
                } else if (et_pass.getText().toString().isEmpty()) {
                    et_pass.setError("Please enter your password");
                } else if (et_pass.getText().toString().length() < 6) {
                    et_pass.setError("Password should be greater than 6");
                } else {
                    register();
                }
            }
        });

        return view;
    }


    public void register() {
        showLoader(getActivity());
        if (isNetworkConnected(getContext())) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<RegisterPojo> call = service.userRegister(et_username.getText().toString(), getMd5Hash(et_pass.getText().toString()), et_email.getText().toString(), et_phone.getText().toString());
            call.enqueue(new Callback<RegisterPojo>() {
                @Override
                public void onResponse(Call<RegisterPojo> call, Response<RegisterPojo> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 0) {
                            hideLoader();
                            Paper.book().write(Constants.ISLOGIN,"true");
                            Paper.book().write(Constants.USERID,response.body().getData().getId());
                            Paper.book().write(Constants.EMAIL,response.body().getData().getEmail());
                            Paper.book().write(Constants.USERNAME,response.body().getData().getUserName());
                            Intent intent = new Intent(getContext(), DashboardActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        } else {
                            customDialog(getActivity(), response.body().getMessage());
                            hideLoader();
                        }

                    } else {
                        hideLoader();
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<RegisterPojo> call, Throwable t) {
                    hideLoader();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            hideLoader();
            customDialog(getActivity(), "Pleasr check your Internet Connection");

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
