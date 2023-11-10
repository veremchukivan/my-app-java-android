package com.example.my_app_java_android.network;

import com.example.my_app_java_android.dto.category.CategoryItemDTO;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface AuthApi {
    @POST("/api/auth/login")
    public Call<List<CategoryItemDTO>> login();

    @Multipart
    @POST("/api/auth/register")
    public Call<CategoryItemDTO> register(@PartMap Map<String, RequestBody> params,
                                        @PartMap MultipartBody.Part image);
}
