package com.alexlearn.mvvmappfood.persistance;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

//converts array of data into coma separated list of strings
public class Converters {


    @TypeConverter
    public static String[] fromString(String value){
        Type listType = new TypeToken<String []>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(String[] list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;

    }
}
