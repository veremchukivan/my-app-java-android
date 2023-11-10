package com.example.my_app_java_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.my_app_java_android.application.HomeApplication;
import com.example.my_app_java_android.category.CategoriesAdapter;
import com.example.my_app_java_android.category.CategoryCreateActivity;
import com.example.my_app_java_android.dto.auth.LoginDTO;
import com.example.my_app_java_android.dto.auth.LoginResponseDTO;
import com.example.my_app_java_android.dto.auth.RegisterDTO;
import com.example.my_app_java_android.dto.auth.UserDTO;
import com.example.my_app_java_android.dto.category.CategoryItemDTO;
import com.example.my_app_java_android.services.ApplicationNetwork;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    TextInputLayout tlEmail;
    TextInputLayout tlFirstName;
    TextInputLayout tlLastName;
    EditText tlPassword;
    private String filePath;
    private ImageView ivSelectImage;
    private final String TAG="RegisterActivity";
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        isStoragePermissionGranted();
        tlEmail = findViewById(R.id.tlEmail);
        tlFirstName = findViewById(R.id.tlFirstName);
        tlLastName = findViewById(R.id.tlLastName);
        tlPassword = findViewById(R.id.tlPassword);
        ivSelectImage = findViewById(R.id.ivSelectImage);
        String url = "https://cdn.pixabay.com/photo/2016/01/03/00/43/upload-1118929_1280.png";
        Glide
                .with(HomeApplication.getAppContext())
                .load(url)
                .apply(new RequestOptions().override(300))
                .into(ivSelectImage);
    }

    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void onClickRegister(View view) {
        try {
            String firstName = tlFirstName.getEditText().getText().toString().trim();
            String lastName = tlLastName.getEditText().getText().toString().trim();
            String email = tlEmail.getEditText().getText().toString().trim();
            String password = tlPassword.getText().toString().trim();

            RegisterDTO model = new RegisterDTO();
            model.setFirstName(firstName);
            model.setLastName(lastName);
            model.setEmail(email);
            model.setPassword(password);

            String base64Image = convertImageToBase64(filePath);

            model.setImage(base64Image);

            ApplicationNetwork
                    .getInstance()
                    .getAuthApi()
                    .register(model)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, CategoriesActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Log.d("error", "error");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            String info=t.getMessage();
                            Log.d("info", info);
                        }
                    });
        } catch (Exception ex) {
            Log.e("Registration Error", ex.getMessage(), ex);
        }
    }
    private String convertImageToBase64(String filePath) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("Base64 Conversion Error", e.getMessage(), e);
            return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected image
            Uri uri = data.getData();

            Glide
                    .with(HomeApplication.getAppContext())
                    .load(uri)
                    .apply(new RequestOptions().override(300))
                    .into(ivSelectImage);

            // If you want to get the file path from the URI, you can use the following code:
            filePath = getPathFromURI(uri);
        }
    }

    // This method converts the image URI to the direct file system path of the image file
    private String getPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return null;
    }
}