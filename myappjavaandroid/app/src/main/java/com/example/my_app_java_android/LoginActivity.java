package com.example.my_app_java_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.my_app_java_android.category.CategoryCreateActivity;
import com.example.my_app_java_android.dto.auth.LoginDTO;
import com.example.my_app_java_android.dto.auth.LoginResponseDTO;
import com.example.my_app_java_android.dto.auth.UserDTO;
import com.example.my_app_java_android.dto.category.CategoryItemDTO;
import com.example.my_app_java_android.services.ApplicationNetwork;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout tlEmail;
    EditText tlPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tlEmail = findViewById(R.id.tlEmail);
        tlPassword = findViewById(R.id.tlPassword);
    }

    public void onClickLogin(View view)
    {
        try {
            String email = tlEmail.getEditText().getText().toString().trim();
            String password = tlPassword.getText().toString();

            LoginDTO model = new LoginDTO();
            model.setEmail(email);
            model.setPassword(password);

            ApplicationNetwork
                    .getInstance()
                    .getAuthApi()
                    .login(model)
                    .enqueue(new Callback<LoginResponseDTO>() {
                        @Override
                        public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                            if (response.isSuccessful()) {
                                String token = response.body().getToken();

                                try {
                                    Claims claims = Jwts.parser()
                                            .setSigningKey("Ujfq4LDCDFXNtm4BW9cg2ZTFMdx223YZgX83rup6m32NKbJuKHWu73qGdhn3x3nH")
                                            .parseClaimsJws(token)
                                            .getBody();

                                    String userId = claims.get("id", String.class);
                                    String firstName = claims.get("firstName", String.class);
                                    String lastName = claims.get("lastName", String.class);
                                    String email = claims.get("email", String.class);
                                    String image = claims.get("image", String.class);

                                    UserDTO user = new UserDTO();
                                    user.setId(Integer.parseInt(userId));
                                    user.setFirstName(firstName);
                                    user.setLastName(lastName);
                                    user.setEmail(email);
                                    user.setImage(image);

                                    Log.d("User Debug Info", "User ID: " + user.getId());
                                    Log.d("User Debug Info", "First Name: " + user.getFirstName());
                                    Log.d("User Debug Info", "Last Name: " + user.getLastName());
                                    Log.d("User Debug Info", "Email: " + user.getEmail());
                                    Log.d("User Debug Info", "Image: " + user.getImage());
                                } catch (Exception e) {
                                    Log.e("Token Decoding Error", e.getMessage());
                                }

                                Intent intent = new Intent(LoginActivity.this, CategoriesActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Log.d("test", "t");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
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