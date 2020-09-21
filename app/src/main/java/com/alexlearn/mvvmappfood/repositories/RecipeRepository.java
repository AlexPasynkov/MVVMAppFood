package com.alexlearn.mvvmappfood.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexlearn.mvvmappfood.models.Recipe;
import com.alexlearn.mvvmappfood.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
   private RecipeApiClient mRecipeApiClient;
   private String mQuery;
   private int mPageNumber;

    public static RecipeRepository getInstance(){
        if(instance == null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    private RecipeRepository(){
        mRecipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeApiClient.getRecipes();
    }
    public LiveData <Recipe> getRecipe(){
        return mRecipeApiClient.getRecipe();
    }


    public void searchRecipeById(String recipeId){
        mRecipeApiClient.searchRecipeById(recipeId);
    }
    //Подключаем RecipeRepository к RecipeApiClient
    public void searchRecipesApi(String query, int pageNumber){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mRecipeApiClient.searchRecipesApi(query, pageNumber);
    }
    public void searchNextPage(){
        searchRecipesApi(mQuery, mPageNumber + 1);
    }
    public void cancelRequest(){
        mRecipeApiClient.cancelRequest();
    }


}
