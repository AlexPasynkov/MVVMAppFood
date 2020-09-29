package com.alexlearn.mvvmappfood;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import com.alexlearn.mvvmappfood.adapter.OnRecipeListener;
import com.alexlearn.mvvmappfood.adapter.RecipeRecyclerAdapter;
import com.alexlearn.mvvmappfood.models.Recipe;
import com.alexlearn.mvvmappfood.util.Resource;
import com.alexlearn.mvvmappfood.util.VerticalSpacingDecorator;
import com.alexlearn.mvvmappfood.viewmodels.RecipeListViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static com.alexlearn.mvvmappfood.viewmodels.RecipeListViewModel.QUERY_EXHAUSTED;

//Этот класс наследует класс BaseActivity(тот класс, который мы сделали руками без помощи программы)
//Базовый класс уже наследует AppCompatActivity. Поэтому наследник RecipeListActivity тоже автоматические наследует AppCompatActivity без необходимости
// это тут в классе где-то указывать
public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";

    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.search_view);

        mRecipeListViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(RecipeListViewModel.class);

        initRecyclerView();
        initSearchView();
        subscribeObservers();
        setSupportActionBar(findViewById(R.id.toolbar));
    }

    private void subscribeObservers(){

        mRecipeListViewModel.getRecipes().observe(this, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                if(listResource != null){
                    Log.d(TAG, "onChanged: status" + listResource.status);

                    if(listResource.data != null){
                        switch (listResource.status){
                            case LOADING:{
                                if(mRecipeListViewModel.getPageNumber() >1){
                                    mAdapter.displayLoading();
                                } else{
                                    mAdapter.displayOnlyLoading();
                                }
                                break;
                            }
                            case ERROR:{
                                Log.e(TAG, "onChanged: cannot refreshe cache");
                                Log.e(TAG, "onChanged: Error message: " + listResource.message);
                                Log.e(TAG, "onChanged: status: ERROR, #recipes: " + listResource.data);
                                mAdapter.hideLoading();
                                mAdapter.setRecipes(listResource.data);
                                Toast.makeText(RecipeListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                                if(listResource.message.equals(QUERY_EXHAUSTED)){
                                    mAdapter.setQueryExhausted();
                                }
                                break;
                            }
                            case SUCCESS:{
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #Recipes: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setRecipes(listResource.data);
                                break;
                            }
                        }
                    }
                }
            }
        });
        mRecipeListViewModel.getViewState().observe(this, new Observer<RecipeListViewModel.ViewState>() {
            @Override
            public void onChanged(RecipeListViewModel.ViewState viewState) {
                if(viewState != null){
                    switch (viewState){
                        case RECIPES:{
                            //recipe will show automatically to other observer
                            break;
                        }
                        case CATEGORIES:{
                            displaySearchCategories();
                            break;
                        }
                    }
                 }
            }
        });
    }

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);
     return   Glide.with(this)
                .setDefaultRequestOptions(options);
    }



    private void searchRecipeApi(String query){
        mRecyclerView.smoothScrollToPosition(0);
        mRecipeListViewModel.searchRecipesApi(query, 1);
        mSearchView.clearFocus();
    }

    private void initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter( this, initGlide());
        VerticalSpacingDecorator itemDecorator = new VerticalSpacingDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!mRecyclerView.canScrollVertically(1)
                        && mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.RECIPES){
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSearchView(){

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchRecipeApi(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onRecipeClick(int position) {
        //Get to selected recipe
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        searchRecipeApi(category);
    }
    private void displaySearchCategories(){
        mAdapter.displaySearchCategory();
    }

    @Override
    public void onBackPressed() {
        if(mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.CATEGORIES){
            super.onBackPressed();
        } else{
            mRecipeListViewModel.setViewCategories();
        }
    }
}