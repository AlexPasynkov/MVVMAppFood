package com.alexlearn.mvvmappfood.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.alexlearn.mvvmappfood.models.Recipe;
import com.alexlearn.mvvmappfood.repositories.RecipeRepository;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private String mRecipeId;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }

    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }

    public String getRecipeId() {
        return mRecipeId;
    }
}
