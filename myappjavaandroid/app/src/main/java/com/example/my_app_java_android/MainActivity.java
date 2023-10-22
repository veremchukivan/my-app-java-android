package com.example.my_app_java_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView txtOperations;
    private TextView txtResult;
    private boolean isNewOperation = false;
    private ImageView imgAvatar;
    private String currentOperation = "";
    private static final String BASE_URL = "https://spu123.itstep.click/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtOperations = findViewById(R.id.txtOperations);
        txtResult = findViewById(R.id.txtResult);
        imgAvatar = findViewById(R.id.imgAvatar);
        GetCategories();
        String url = "https://kovbasa.itstep.click/images/mala.jpeg";
        Glide
                .with(this)
                .load(url)
                .apply(new RequestOptions().override(600))
                .into(imgAvatar);
    }

    public void GetCategories()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CategoryApiService categoryApiService = retrofit.create(CategoryApiService.class);

        Call<List<Category>> call = categoryApiService.getCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body();
                    for (Category category : categories) {
                        Log.d("Category", "ID: " + category.getId());
                        Log.d("Category", "Name: " + category.getName());
                        Log.d("Category", "Description: " + category.getDescription());
                        Log.d("Category", "Image URL: " + category.getImage());
                    }
                } else {
                    Log.e("API Error", "Failed to fetch data");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API Error", "API call failed", t);
            }
        });
    }

    public void EraseHandler(View view) {
        String txtResultText = txtResult.getText().toString();
        if (!txtResultText.equals("")) {
            txtResult.setText(txtResultText.substring(0, txtResultText.length() - 1));
        }
    }

    public void ClearHandler(View view) {
        txtResult.setText("");
        txtOperations.setText("");
        currentOperation = "";
    }

    public void CalculateHandler(View view) {
        String txtOperationsText = txtOperations.getText().toString();
        String txtResultText = txtResult.getText().toString();

        if (!txtOperationsText.isEmpty() && !txtResultText.isEmpty()) {
            double num1 = Double.parseDouble(txtOperationsText.substring(0, txtOperationsText.length() - 1));
            double num2 = Double.parseDouble(txtResultText);

            double result = Calculate(num1, num2);

            txtResult.setText(String.valueOf(result));

            txtOperations.setText("");
            currentOperation = "";
        }
    }

    public void NumberHandler(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        String txtResultText = txtResult.getText().toString();
        String txtOperationsText = txtOperations.getText().toString();
        boolean isOperator = isOperator(buttonText);

        if (isOperator && !txtResultText.equals("-")) {
            if (txtResultText.equals("")) {
                if (buttonText.equals("-"))
                    txtResult.append(buttonText);
                else
                    return;
            }
            else if (!txtResultText.equals("") && !txtResultText.equals("-") && buttonText.equals(".") && !txtResultText.contains(".")) {
                txtResult.append(buttonText);
            }
            else if (!Objects.equals(currentOperation, "")) {
                double num1 = Double.parseDouble(txtOperationsText.substring(0, txtOperationsText.length() - 1));
                double num2 = Double.parseDouble(txtResultText);

                double result = Calculate(num1, num2);

                currentOperation = buttonText;
                txtOperations.setText(result + currentOperation);
                isNewOperation = true;
            }
            else  {
                currentOperation = buttonText;
                txtOperations.append(txtResult.getText() + buttonText);
                isNewOperation = true;
            }
        } else if (!isOperator){
            if (isNewOperation) {
                txtResult.setText(buttonText);
                isNewOperation = false;
            }
            else
                txtResult.append(buttonText);
        }
    }

    private boolean isOperator(String text) {
        return text.equals("+") || text.equals("-") || text.equals("*") || text.equals("/") || text.equals(".");
    }

    private double Calculate(double num1, double num2) {
        switch (currentOperation) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    return Double.NaN;
                }
        }
        return Double.NaN;
    }
}