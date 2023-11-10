package com.example.my_app_java_android;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_app_java_android.category.CategoryCreateActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int select = item.getItemId();
        if (select == R.id.m_categories) {
            Intent intent = new Intent(BaseActivity.this, CategoriesActivity.class);
            startActivity(intent);
            return true;
        }
        if (select == R.id.m_create) {
            Intent intent = new Intent(BaseActivity.this, CategoryCreateActivity.class);
            startActivity(intent);
            return true;
        }
        if (select == R.id.m_login) {
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        if (select == R.id.m_register) {
            Intent intent = new Intent(BaseActivity.this, RegisterActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}