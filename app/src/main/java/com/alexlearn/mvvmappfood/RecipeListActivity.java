package com.alexlearn.mvvmappfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alexlearn.mvvmappfood.models.Recipe;
import com.alexlearn.mvvmappfood.requests.RecipeApi;
import com.alexlearn.mvvmappfood.requests.ServiceGenerator;
import com.alexlearn.mvvmappfood.requests.responses.RecipeResponse;
import com.alexlearn.mvvmappfood.requests.responses.RecipeSearchResponse;
import com.alexlearn.mvvmappfood.util.Constans;
import com.alexlearn.mvvmappfood.util.Testing;
import com.alexlearn.mvvmappfood.viewmodels.RecipeListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Этот класс наследует класс BaseActivity(тот класс, который мы сделали руками без помощи программы)
//Базовый класс уже наследует AppCompatActivity. Поэтому наследник RecipeListActivity тоже автоматические наследует AppCompatActivity без необходимости
// это тут в классе где-то указывать
public class RecipeListActivity extends BaseActivity {

    private static final String TAG = "RecipeListActivity";

    private RecipeListViewModel mRecipeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        subscribeObserver();

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testRetrofitRequest();
            }
        });

    }

    private void subscribeObserver(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null){
                    Testing.printRecipes(recipes, "recipes test");
                }
            }
        });
    }

    //Подключаем этот метод поиска к RecipeListViewModel
    private void searchRecipesApi(String query, int pageNumber){
        mRecipeListViewModel.searchRecipesApi(query, pageNumber);
    }

    private void testRetrofitRequest(){

        searchRecipesApi("chicken breast", 1);
     //   RecipeApi recipeApi = ServiceGenerator.getRecipeApi();

//        Call<RecipeSearchResponse> responseCall = recipeApi
//                .searchRecipe(Constans.API_KEY,
//                        "chicken breasts",
//                        "1"
//                        );
//        responseCall.enqueue(new Callback<RecipeSearchResponse>() {
//            @Override
//            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
//                Log.d(TAG, "onResponse: server response: " + response.toString());
//                if (response.code() == 200){
//                    Log.d(TAG, "onResponse: " + response.body().toString());
//                    List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
//                    for(Recipe recipe: recipes){
//                        Log.d(TAG, "onResponse: " + recipe.getTitle());
//                    }
//                } else {
//                    try{
//                        Log.d(TAG, "onResponse: " + response.errorBody().string());
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
//
//            }
//        });

//        Call<RecipeResponse> responseCall = recipeApi
//                .getRecipe(Constans.API_KEY,
//                        "41470"
//
//                );
//
//        responseCall.enqueue(new Callback<RecipeResponse>() {
//            @Override
//            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
//                Log.d(TAG, "onResponse: server response: " + response.toString());
//                if (response.code() == 200){
//                    Log.d(TAG, "onResponse: " + response.body().toString());
//                    Recipe recipe = response.body().getRecipe();
//                    Log.d(TAG, "onResponse: RETREIVED RECIPE: " + recipe.toString());
//               } else {
//                   try{
//                       Log.d(TAG, "onResponse: " + response.errorBody().string());
//                    }catch (IOException e){
//                       e.printStackTrace();
//                   }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RecipeResponse> call, Throwable t) {
//
//            }
//        });
//        responseCall.enqueue(new Callback<RecipeSearchResponse>() {
//            @Override
//            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
//
//            }
//        });
    }


}