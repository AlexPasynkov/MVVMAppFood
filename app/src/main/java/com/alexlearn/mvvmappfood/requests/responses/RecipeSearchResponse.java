package com.alexlearn.mvvmappfood.requests.responses;

import com.alexlearn.mvvmappfood.models.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeSearchResponse {

    //Эта страница служит для обработки запросов на странице поиска
    // https://recipesapi.herokuapp.com/api/search?q=chicken&page=3

    // Дает понять retrofit что надо искать и скачивать с API сервиса
    @SerializedName("count")
    //Командой Expose мы говорим ретрофиту что нужно включить Json-Converter и переработать полученную информацию
    @Expose()
    public int count;


    //Список всех рецептов, которые будут выведены на экран при единичном запросе
    @SerializedName("recipes")
    @Expose()
    private List<Recipe> recipes;

    @SerializedName("error")
    @Expose()
    private String error;

    public String getError(){
        return error;
    }

    public int getCount() {
        return count;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    @Override
    public String toString() {
        return "RecipeSearchResponse{" +
                "count=" + count +
                ", recipes=" + recipes +
                ", error='" + error + '\'' +
                '}';
    }
}
