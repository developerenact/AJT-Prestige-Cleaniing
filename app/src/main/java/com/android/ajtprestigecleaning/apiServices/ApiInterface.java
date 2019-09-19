package com.android.ajtprestigecleaning.apiServices;


import com.android.ajtprestigecleaning.model.LoginPojo.LoginPojo;
import com.android.ajtprestigecleaning.model.RegisterPojo.RegisterPojo;
import com.android.ajtprestigecleaning.model.ResetPassword.ResetPassword;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("signUp")
    Call<RegisterPojo> userRegister(@Field("userName") String userName,
                                    @Field("password") String password,
                                    @Field("email") String email,
                                    @Field("phone") String phone);


    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("login")
    Call<LoginPojo> loginUser(@Field("email") String email,
                                 @Field("password") String password);

    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("resetPassword")
    Call<ResetPassword> forgotPassword(@Field("email") String email);
}
