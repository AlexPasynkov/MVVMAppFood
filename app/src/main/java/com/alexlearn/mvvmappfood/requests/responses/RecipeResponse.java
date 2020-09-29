package com.alexlearn.mvvmappfood.requests.responses;

import com.alexlearn.mvvmappfood.models.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeResponse {
    //В этом классе мы обрабываем конкретный рецепт и вынимаем из него всю инфу

    //Например такое https://recipesapi.herokuapp.com/api/get?rId=41470

    // Дает понять retrofit что надо искать и скачивать с API сервиса
    @SerializedName("recipe")
    //Командой Expose мы говорим ретрофиту что нужно включить Json-Converter и переработать полученную информацию
    @Expose()
    private Recipe recipe;

    @SerializedName("error")
    @Expose()
    private String error;

    public Recipe getRecipe(){
        return recipe;
    }
    public String getError(){
        return error;
    }
    // Переводим в стринг полученный рецепт

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipe=" + recipe +
                ", error='" + error + '\'' +
                '}';
    }
}
