package com.alexlearn.mvvmappfood.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexlearn.mvvmappfood.R;
import com.alexlearn.mvvmappfood.models.Recipe;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CircleImageView categoryImage;
    TextView categoryTitle;
    OnRecipeListener listener;
    RequestManager requestManager;

    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener listener, RequestManager requestManager) {

        super(itemView);
        this.listener = listener;
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);
        this.requestManager = requestManager;

        itemView.setOnClickListener(this);
    }

    public void onBind(Recipe recipe){


        Uri path = Uri.parse("android.resource://com.codingwithmitch.foodrecipes/drawable/" +recipe.getImage_url());
        requestManager
                .load(path)
                .into(categoryImage);

        categoryTitle.setText(recipe.getTitle());
    }

    @Override
    public void onClick(View view) {
        listener.onCategoryClick(categoryTitle.getText().toString());
    }
}
