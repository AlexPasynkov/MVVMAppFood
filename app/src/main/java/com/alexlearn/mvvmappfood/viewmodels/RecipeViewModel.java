package com.alexlearn.mvvmappfood.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alexlearn.mvvmappfood.models.Recipe;
import com.alexlearn.mvvmappfood.repositories.RecipeRepository;
import com.alexlearn.mvvmappfood.requests.responses.RecipeResponse;
import com.alexlearn.mvvmappfood.util.Resource;


public class RecipeViewModel extends AndroidViewModel{

    private RecipeRepository recipeRepository;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        recipeRepository  = RecipeRepository.getInstance(application);
    }

    public LiveData<Resource<Recipe>> searchRecipeApi(String recipeId){
        return recipeRepository.searchRecipesApi(recipeId);
    }
}
