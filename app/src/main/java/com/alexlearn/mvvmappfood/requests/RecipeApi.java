package com.alexlearn.mvvmappfood.requests;

import androidx.lifecycle.LiveData;

import com.alexlearn.mvvmappfood.requests.responses.ApiResponse;
import com.alexlearn.mvvmappfood.requests.responses.RecipeResponse;
import com.alexlearn.mvvmappfood.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {
    //https://www.food2fork.com/api/search?key=dadc639rhf83&q=bacon%20onion&page=1

    //?key=dadc639rhf83 - это ключ
    // & - это начало нового запроса
    //q=bacon%20onion - второй запрос
    //&page=1 - третий запрос

    //Search request
    @GET("api/search")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );

    //GET specific recipe request
    //https://recipesapi.herokuapp.com/api/get?rId=41470

    @GET("api/get")
    LiveData<ApiResponse<RecipeResponse>> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );
}
