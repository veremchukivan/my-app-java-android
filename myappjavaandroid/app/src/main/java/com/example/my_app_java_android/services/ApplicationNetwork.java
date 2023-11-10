package com.example.my_app_java_android.services;

import com.example.my_app_java_android.constants.Urls;
import com.example.my_app_java_android.network.AuthApi;
import com.example.my_app_java_android.network.CategoriesApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationNetwork {
    private static ApplicationNetwork instance;

    private Retrofit retrofit;

    public ApplicationNetwork() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Urls.LOCAL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApplicationNetwork getInstance() {
        if(instance==null)
            instance=new ApplicationNetwork();
        return instance;
    }

    public CategoriesApi getCategoriesApi() {
        return retrofit.create(CategoriesApi.class);
    }
    public AuthApi getAuthApi() {
        return retrofit.create(AuthApi.class);
    }
}