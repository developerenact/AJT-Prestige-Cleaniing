package com.android.ajtprestigecleaning.apiServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by admin on 3/21/2018.
 */

public class BaseUrl {
    public static <S> S CreateService(Class<S> service_class) {

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://enacteservices.net/AJT/POST/v1/")
                .build();
        return retrofit.create(service_class);

    }
}
