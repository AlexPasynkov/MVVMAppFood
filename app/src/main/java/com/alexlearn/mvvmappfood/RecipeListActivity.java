package com.alexlearn.mvvmappfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;

import com.alexlearn.mvvmappfood.adapter.OnRecipeListener;
import com.alexlearn.mvvmappfood.adapter.RecipeRecyclerAdapter;
import com.alexlearn.mvvmappfood.models.Recipe;

import com.alexlearn.mvvmappfood.util.Testing;
import com.alexlearn.mvvmappfood.util.VerticalSpacingDecorator;
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
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.search_view);

        mRecipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);

        initRecyclerView();
        subscribeObserver();
        initSearchView();
        if(!mRecipeListViewModel.isViewingRecipes())
        {   //display search categories
                        displaySearchCategories();
        }

        setSupportActionBar(findViewById(R.id.toolbar));
    }

    private void subscribeObserver(){
        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if(recipes != null){
                    if(mRecipeListViewModel.isViewingRecipes()){
                        Testing.printRecipes(recipes, "recipes test");
                        //cancel query on back pressed
                        mRecipeListViewModel.setIsPerformingQuery(false);
                        mRecipeAdapter.setRecipes(recipes);
                    }
                }
            }
        });
    }

    private void initRecyclerView(){
       mRecipeAdapter = new RecipeRecyclerAdapter( this);
        VerticalSpacingDecorator itemDecorator = new VerticalSpacingDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
       mRecyclerView.setAdapter(mRecipeAdapter);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

       //init uploading of next 30 recipes on screen
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //this method allows to understand when we reached bottom of list (30 recipies) and what to do next
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!mRecyclerView.canScrollVertically(1)){
                    //search the next page
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {
        //Get to selected recipe
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mRecipeAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        mRecipeAdapter.displayLoading();
        mRecipeListViewModel.searchRecipesApi(category, 1);
        mSearchView.clearFocus();
    }

    private void initSearchView(){

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mRecipeAdapter.displayLoading();
                mRecipeListViewModel.searchRecipesApi(s, 1);
                //cancel request while you press back in search mode
                mSearchView.clearFocus();
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

    private void displaySearchCategories(){
        mRecipeListViewModel.setIsViewingRecipes(false);
        mRecipeAdapter.displaySearchCategory();
    }

    @Override
    public void onBackPressed() {
        if(mRecipeListViewModel.onBackPressed()){
            super.onBackPressed();
        }else{
            displaySearchCategories();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_categories);{
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }
}