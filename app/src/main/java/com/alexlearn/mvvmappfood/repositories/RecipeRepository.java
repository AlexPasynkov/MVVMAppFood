package com.alexlearn.mvvmappfood.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.alexlearn.mvvmappfood.AppExecutors;
import com.alexlearn.mvvmappfood.models.Recipe;
import com.alexlearn.mvvmappfood.persistance.RecipeDao;
import com.alexlearn.mvvmappfood.persistance.RecipeDatabase;
import com.alexlearn.mvvmappfood.requests.responses.ApiResponse;
import com.alexlearn.mvvmappfood.requests.responses.RecipeSearchResponse;
import com.alexlearn.mvvmappfood.util.NetworkBoundResource;
import com.alexlearn.mvvmappfood.util.Resource;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeDao recipeDao;

    public static RecipeRepository getInstance(Context context){
        if(instance == null){
            instance = new RecipeRepository(context);
        }
        return instance;
    }

    private RecipeRepository(Context context){
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }

    //switching between db and retrofit request
    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber){
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()){
            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.searchRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return null;
            }
        }.getAsLiveData();
    }
}
