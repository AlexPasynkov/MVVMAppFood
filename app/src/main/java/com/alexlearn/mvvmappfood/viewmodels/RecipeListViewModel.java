package com.alexlearn.mvvmappfood.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexlearn.mvvmappfood.models.Recipe;
import com.alexlearn.mvvmappfood.repositories.RecipeRepository;
import com.alexlearn.mvvmappfood.requests.RecipeApiClient;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
   // private RecipeApiClient mRecipeApiClient;
    private boolean mIsViewingRecipes;

    public RecipeListViewModel() {
        mIsViewingRecipes = false;
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }

    //подключаем RecipeListViewModel к RecipeRepository
    public void searchRecipesApi(String query, int pageNumber){
        mIsViewingRecipes = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }

    public boolean isViewingRecipes(){
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes){
        mIsViewingRecipes = isViewingRecipes;
    }
}
