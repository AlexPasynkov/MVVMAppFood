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
    //cancel request while you press back in search mode
    private boolean mIsPerformingQuery;

    public RecipeListViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }

    //подключаем RecipeListViewModel к RecipeRepository
    public void searchRecipesApi(String query, int pageNumber){
        mIsViewingRecipes = true;
        mIsPerformingQuery = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }

    public void searchNextPage(){
        if(!mIsPerformingQuery && mIsViewingRecipes){
            mRecipeRepository.searchNextPage();
        }
    }

    public boolean isViewingRecipes(){
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes){
        mIsViewingRecipes = isViewingRecipes;
    }

    public void setIsPerformingQuery(boolean isPerformingQuery){
        mIsPerformingQuery = isPerformingQuery;
    }

    public boolean setIsPerformingQuery(){
       return mIsPerformingQuery;
    }

    public boolean onBackPressed(){
        if(mIsPerformingQuery){
            //cancel query
            mRecipeRepository.cancelRequest();
            setIsPerformingQuery(false);
        }
        if(mIsViewingRecipes){
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }
}
