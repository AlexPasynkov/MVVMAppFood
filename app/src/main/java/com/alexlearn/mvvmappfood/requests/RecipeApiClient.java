package com.alexlearn.mvvmappfood.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alexlearn.mvvmappfood.AppExecutors;
import com.alexlearn.mvvmappfood.models.Recipe;
import com.alexlearn.mvvmappfood.requests.responses.RecipeResponse;
import com.alexlearn.mvvmappfood.requests.responses.RecipeSearchResponse;
import com.alexlearn.mvvmappfood.util.Constans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.alexlearn.mvvmappfood.util.Constans.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;
    //Variable for getting a single recipe from the list
    private MutableLiveData<Recipe> mRecipe;
    private RetrieveRecipeRunnable mRetrieveRecipeRunnable;

    public static RecipeApiClient getInstance(){
        if(instance == null){
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient(){
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipe;
    }

    public void searchRecipesApi(String query, int pageNumber){
        if(mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable = null;
        }

        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query, pageNumber);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipesRunnable);
        //Если в течении 3 секунд нет ответа от сервера мы прерываем связь
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS );
    }

    public void searchRecipeById(String recipeId){
        if(mRetrieveRecipeRunnable != null){
            mRetrieveRecipeRunnable = null;
        }
        mRetrieveRecipeRunnable = new RetrieveRecipeRunnable(recipeId);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipeRunnable);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let the user know that the time is out
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    //Мы создали класс внутри класса RecipeApiClient для отправки и получения запроса
    private class RetrieveRecipesRunnable implements Runnable{
        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }
        //Оформляем запрос по поиску списка рецептов
        @Override
        public void run() {
            try {
                Response response = getRecipes(query, pageNumber).execute();
                if(cancelRequest){
                    return;
                }
                if (response.code() == 200){
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                    if(pageNumber == 1){
                        mRecipes.postValue(list);
                    }
                        // Если мы хотим вызвать дополнительные рецепт и дополнить полученный список
                    else {
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }
                }
                //Если за код ответа не равен 200
                else {
                    String error = response.errorBody().string();
                    Log.i(TAG, "run: " + error);
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }
        }

        private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber){
            return ServiceGenerator.getRecipeApi().searchRecipe(
                    Constans.API_KEY,
                    query,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest(){
            Log.d(TAG, "cancelRequest");
            cancelRequest = true;
        }
    }

    private class RetrieveRecipeRunnable implements Runnable {
        private String recipeId;
        boolean cancelRequest;

        public RetrieveRecipeRunnable(String recipeId) {
            this.recipeId = recipeId;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipe(recipeId).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    Recipe recipe = ((RecipeResponse) response.body()).getRecipe();
                    mRecipe.postValue(recipe);
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mRecipe.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipe.postValue(null);
            }
        }


        private Call<RecipeResponse> getRecipe(String recipeId) {
            return ServiceGenerator.getRecipeApi().getRecipe(
                    Constans.API_KEY,
                    recipeId
            );
        }
        private void cancelRequest(){
            Log.d(TAG, "cancelRequest");
            cancelRequest = true;
        }
    }

    public void cancelRequest(){
        if(mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable.cancelRequest();
        }
        if(mRetrieveRecipeRunnable != null ){
            mRetrieveRecipeRunnable.cancelRequest();
        }
    }
}
