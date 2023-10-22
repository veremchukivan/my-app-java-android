package com.example.my_app_java_android;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApiService {
    @GET("api/categories/list")
    Call<List<Category>> getCategories();
}