package com.example.my_app_java_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextView txtOperations;
    private TextView txtResult;
    private boolean isNewOperation = false;
    private String currentOperation = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtOperations = findViewById(R.id.txtOperations);
        txtResult = findViewById(R.id.txtResult);
    }

    public void onClickMeHandler(View view) {
        Intent intent = new Intent(MainActivity.this, CategoriesActivity.class);
        startActivity(intent);
        finish();
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