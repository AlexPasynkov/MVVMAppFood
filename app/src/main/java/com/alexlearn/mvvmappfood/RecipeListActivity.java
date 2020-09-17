package com.alexlearn.mvvmappfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;

import com.alexlearn.mvvmappfood.adapter.OnRecipeListener;
import com.alexlearn.mvvmappfood.adapter.RecipeRecyclerAdapter;
import com.alexlearn.mvvmappfood.models.Recipe;

import com.alexlearn.mvvmappfood.util.Testing;
import com.alexlearn.mvvmappfood.viewmodels.RecipeListViewModel;

import java.util.List;

//Этот класс наследует класс BaseActivity(тот класс, который мы сделали руками без помощи программы)
//Базовый класс уже наследует AppCompatActivity. Поэтому наследник RecipeListActivity тоже автоматические наследует AppCompatActivity без необходимости
// это тут в классе где-то указывать
public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";

    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = findViewById(R.id.recipe_list);

        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        initRecyclerView();
        subscribeObserver();
        initSearchView();

    }

    private void subscribeObserver(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null){
                    Testing.printRecipes(recipes, "recipes test");
                    mRecipeAdapter.setRecipes(recipes);
                }
            }
        });
    }

    private void initRecyclerView(){
       mRecipeAdapter = new RecipeRecyclerAdapter( this);
       mRecyclerView.setAdapter(mRecipeAdapter);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }

    private void initSearchView(){
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mRecipeAdapter.displayLoading();
                mRecipeListViewModel.searchRecipesApi(s, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
    //private void testRetrofitRequest(){


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
  //  }



}