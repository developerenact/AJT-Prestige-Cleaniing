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
import com.android.ajtprestigecleaning.activities.LoginActivity;
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
    TextView label_member,lebel_info,label_welcome;
    EditText et_email, et_username, et_pass, et_phone,et_fname,et_lname;
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
        label_welcome = view.findViewById(R.id.label_welcome);
        et_username = view.findViewById(R.id.et_username);
        et_phone = view.findViewById(R.id.et_phone);
        et_pass = view.findViewById(R.id.et_pass);
        et_fname = view.findViewById(R.id.et_fname);
        et_lname = view.findViewById(R.id.et_lname);
        signup = view.findViewById(R.id.signup);
        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Medium.ttf");
        label_member.setTypeface(custom_font);

        Typeface custom_font2 = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Light.ttf");
        label_welcome.setTypeface(custom_font2);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_fname.getText().toString().isEmpty()) {
                    et_fname.setError(getActivity().getString(R.string.firstname_validation));
                    et_fname.requestFocus();
                }

                else if (et_lname.getText().toString().isEmpty()) {
                    et_lname.setError(getActivity().getString(R.string.lastname_validation));
                    et_lname.requestFocus();
                }
               else if (et_email.getText().toString().isEmpty()) {
                    et_email.setError(getActivity().getString(R.string.emptyemail_validation));
                    et_email.requestFocus();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()) {
                    et_email.setError(getActivity().getString(R.string.email_validation));
                    et_email.requestFocus();
                } else if (et_username.getText().toString().isEmpty()) {
                    et_username.setError(getActivity().getString(R.string.username_validation));
                    et_username.requestFocus();
                } else if (et_phone.getText().toString().isEmpty()) {
                    et_phone.setError(getActivity().getString(R.string.emptyphone_validation));
                    et_phone.requestFocus();
                } else if (et_phone.getText().toString().length() < 10) {
                    et_phone.setError(getActivity().getString(R.string.phone_length_validation));
                    et_phone.requestFocus();
                } else if (et_pass.getText().toString().isEmpty()) {
                    et_pass.setError(getActivity().getString(R.string.emptypassword_validation));
                    et_pass.requestFocus();
                } else if (et_pass.getText().toString().length() < 6) {
                    et_pass.setError(getActivity().getString(R.string.password_length_validation));
                    et_pass.requestFocus();
                } else {
                    register();
                }
            }
        });

        return view;
    }


    public void register() {
        //showLoader(getActivity());
        ((LoginActivity)getActivity()).showProgress();
        if (((LoginActivity)getActivity()).isNetworkAvailable()) {
            ApiInterface service = BaseUrl.CreateService(ApiInterface.class);
            Call<RegisterPojo> call = service.userRegister(et_username.getText().toString(), getMd5Hash(et_pass.getText().toString()), et_email.getText().toString(), et_phone.getText().toString(),et_fname.getText().toString(),et_lname.getText().toString());
            call.enqueue(new Callback<RegisterPojo>() {
                @Override
                public void onResponse(Call<RegisterPojo> call, Response<RegisterPojo> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 0) {
                           // hideLoader();
                            ((LoginActivity)getActivity()).hideProgress();
                            Paper.book().write(Constants.ISLOGIN,"true");
                            Paper.book().write(Constants.USERID,response.body().getData().getId());
                            Paper.book().write(Constants.EMAIL,response.body().getData().getEmail());
                            Paper.book().write(Constants.USERNAME,response.body().getData().getUserName());
                            Paper.book().write(Constants.FIRSTNAME,response.body().getData().getFirstName());
                            Paper.book().write(Constants.LASTNAME,response.body().getData().getLastName());
                            Paper.book().write(Constants.USERIMAGE,response.body().getData().getImage());
                            Intent intent = new Intent(getContext(), DashboardActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        } else {
                            ((LoginActivity)getActivity()).customDialog( response.body().getMessage(),getContext());
                           // hideLoader();
                            ((LoginActivity)getActivity()).hideProgress();

                        }

                    } else {
                      // hideLoader();
                        ((LoginActivity)getActivity()).hideProgress();
                        Toast.makeText(getContext(), getActivity().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();


                    }
                }

                @Override
                public void onFailure(Call<RegisterPojo> call, Throwable t) {
                    //hideLoader();
                    ((LoginActivity)getActivity()).hideProgress();
                    Log.d("otp", t.getMessage());
                    Toast.makeText(getContext(), getActivity().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                }
            });
        } else {
          //  hideLoader();
            ((LoginActivity)getActivity()).hideProgress();
            ((LoginActivity)getActivity()).customDialog( getActivity().getString(R.string.no_internet),getContext());

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
