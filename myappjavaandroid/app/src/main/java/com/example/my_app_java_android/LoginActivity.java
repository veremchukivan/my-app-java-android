package com.example.my_app_java_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.my_app_java_android.category.CategoryCreateActivity;
import com.example.my_app_java_android.dto.category.CategoryItemDTO;
import com.example.my_app_java_android.services.ApplicationNetwork;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout tlEmail;
    TextInputLayout tlPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickLogin(View view)
    {
        try {
            String email = tlEmail.getEditText().getText().toString().trim();
            String password = tlPassword.getEditText().getText().toString().trim();

            Map<String, RequestBody> params = new HashMap<>();
            params.put("email", RequestBody.create(MediaType.parse("text/plain"), email));
            params.put("password", RequestBody.create(MediaType.parse("text/plain"), password));

            ApplicationNetwork
                    .getInstance()
                    .getCategoriesApi()
                    .create(params)
                    .enqueue(new Callback<CategoryItemDTO>() {
                        @Override
                        public void onResponse(Call<CategoryItemDTO> call, Response<CategoryItemDTO> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(CategoryCreateActivity.this, CategoriesActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryItemDTO> call, Throwable t) {
                            String info=t.getMessage();
                            Log.d("info", info);
                        }
                    });
        }
        catch(Exception ex) {
            Log.d("Pip", ex.getMessage().toString());
        }
    }
}