package com.alexlearn.mvvmappfood.util;

import android.util.Log;

import com.alexlearn.mvvmappfood.models.Recipe;

import java.util.List;

public class Testing {

    public static void printRecipes(List<Recipe> list, String tag){
        for(Recipe recipe : list){
            Log.d(tag, "printRecipes: " + recipe.getTitle());
        }
    }
}
