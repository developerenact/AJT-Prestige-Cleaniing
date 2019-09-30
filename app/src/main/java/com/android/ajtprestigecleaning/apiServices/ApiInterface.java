package com.android.ajtprestigecleaning.apiServices;


import com.android.ajtprestigecleaning.model.AddLogPojo.AddLogPojo;
import com.android.ajtprestigecleaning.model.AllJobsPojo.AllJobsPojo;
import com.android.ajtprestigecleaning.model.ChangePasswordPojo.ChangePasswordPojo;
import com.android.ajtprestigecleaning.model.JobDetailPojo.JobDetailPojo;
import com.android.ajtprestigecleaning.model.JobListPojo.JobListPojo;
import com.android.ajtprestigecleaning.model.LoginPojo.LoginPojo;
import com.android.ajtprestigecleaning.model.RegisterPojo.RegisterPojo;
import com.android.ajtprestigecleaning.model.ResetPassword.ResetPassword;
import com.android.ajtprestigecleaning.model.SubmitHourPojo.SubmitHourPojo;
import com.android.ajtprestigecleaning.model.UpdateJobStatusPojo.UpdateJobStatusPojo;
import com.android.ajtprestigecleaning.model.UpdateProfilePojo.UpdateProfilePojo;
import com.google.gson.JsonObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiInterface {

    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("signUp")
    Call<RegisterPojo> userRegister(@Field("userName") String userName,
                                    @Field("password") String password,
                                    @Field("email") String email,
                                    @Field("phone") String phone,
                                    @Field("firstName") String firstName,
                                    @Field("lastName") String lastName);


    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("login")
    Call<LoginPojo> loginUser(@Field("email") String email,
                              @Field("password") String password,
                              @Field("token") String token);

    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("resetPassword")
    Call<ResetPassword> forgotPassword(@Field("email") String email);


    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("jobs")
    Call<AllJobsPojo> getjobs(@Field("userId") int userId,
                              @Field("state") int State);

    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("jobDetail")
    Call<JobDetailPojo> getjobDetail(@Field("jobId") int jobId);

    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("changePassword")
    Call<ChangePasswordPojo> chnagePass(@Field("userId") String userId,
                                        @Field("oldPassword") String oldPassword,
                                        @Field("password") String password);

    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("logout")
    Call<JsonObject> logout(@Field("userId") String userId,
                            @Field("token") String token);

    @Multipart
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("updateProfile")
    Call<UpdateProfilePojo> updateProfile(@PartMap HashMap<String, RequestBody> hashMap);


    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("submitHours")
    Call<SubmitHourPojo> submithour(@Field("jobId") String jobId,
                                    @Field("userId") String userId,
                                    @Field("taskId") String taskId,
                                    @Field("startTime") long startTime,
                                    @Field("endTime") long endTime,
                                    @Field("hours") long hours,
                                    @Field("notes") String notes);

    @Multipart
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("addLogs")
    Call<AddLogPojo> addLogs(@PartMap HashMap<String, RequestBody> hashMap);


    @FormUrlEncoded
    @Headers("Secret-Key:AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @POST("updateJobStatus")
    Call<UpdateJobStatusPojo> updateJobStatus(@Field("jobId") String jobId,
                                              @Field("userId") String userId,
                                              @Field("status") String status);

}
