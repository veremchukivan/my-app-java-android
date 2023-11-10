package com.example.my_app_java_android.network;

import com.example.my_app_java_android.dto.auth.LoginDTO;
import com.example.my_app_java_android.dto.auth.LoginResponseDTO;
import com.example.my_app_java_android.dto.auth.RegisterDTO;
import com.example.my_app_java_android.dto.category.CategoryItemDTO;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface AuthApi {
    @POST("/api/accounts/login")
    public Call<LoginResponseDTO> login(@Body LoginDTO model);

    @POST("/api/accounts/register")
    public Call<Void> register(@Body RegisterDTO model);

}