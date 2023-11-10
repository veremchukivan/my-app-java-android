package com.example.my_app_java_android.category;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_app_java_android.R;

public class CategoryCardViewHolder extends RecyclerView.ViewHolder {
    private ImageView categoryImage;
    private TextView categoryName;

    public CategoryCardViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryImage = itemView.findViewById(R.id.categoryImage);
        categoryName = itemView.findViewById(R.id.categoryName);
    }
    public ImageView getCategoryImage() {
        return categoryImage;
    }
    public TextView getCategoryName() {
        return categoryName;
    }
}