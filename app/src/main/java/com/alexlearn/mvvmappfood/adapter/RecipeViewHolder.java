package com.alexlearn.mvvmappfood.adapter;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.alexlearn.mvvmappfood.R;
import com.alexlearn.mvvmappfood.models.Recipe;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;


public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title, publisher, socialScore;
    AppCompatImageView image;
    OnRecipeListener onRecipeListener;
    RequestManager requestManager;
    ViewPreloadSizeProvider viewPreloadSizeProvider;

    public RecipeViewHolder(@NonNull View itemView,
                            OnRecipeListener onRecipeListener,
                            RequestManager requestManager,
                            ViewPreloadSizeProvider viewPreloadSizeProvider) {
        super(itemView);

        this.onRecipeListener = onRecipeListener;
        this.requestManager = requestManager;
        this.viewPreloadSizeProvider = viewPreloadSizeProvider;

        title = itemView.findViewById(R.id.recipe_title);
        publisher = itemView.findViewById(R.id.recipe_publisher);
        socialScore = itemView.findViewById(R.id.recipe_social_score);
        image = itemView.findViewById(R.id.recipe_image);

        itemView.setOnClickListener(this);
    }

    public void onBind(Recipe recipe){

            requestManager
                .load(recipe.getImage_url())
                .into(image);

        title.setText(recipe.getTitle());
        publisher.setText(recipe.getPublisher());
        socialScore.setText(String.valueOf(Math.round(recipe.getSocial_rank())));

        //image we want to cache
        viewPreloadSizeProvider.setView(image);
    }

    @Override
    public void onClick(View view) {
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }
}
