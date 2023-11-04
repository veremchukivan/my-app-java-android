package com.example.my_app_java_android.dto.category;

import android.graphics.drawable.Drawable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;

public class CategoryCreateDTO {
    private RequestBody name;
    private MultipartBody image;
    private RequestBody description;

    public RequestBody getName() {
        return name;
    }

    public void setName(RequestBody name) {
        this.name = name;
    }

    public MultipartBody getImage() {
        return image;
    }

    public void setImage(MultipartBody image) {
        this.image = image;
    }

    public RequestBody getDescription() {
        return description;
    }

    public void setDescription(RequestBody description) {
        this.description = description;
    }
}