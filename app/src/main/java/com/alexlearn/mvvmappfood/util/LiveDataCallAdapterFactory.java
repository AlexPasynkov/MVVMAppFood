package com.alexlearn.mvvmappfood.util;

import androidx.lifecycle.LiveData;

import com.alexlearn.mvvmappfood.requests.responses.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    /**
     * This method performs a number of checks and then returns the response type for the retrofit requests
     * (@bodyType is the ResponseType. It can be RecipeResponse or RecipeSearchResponse)
     *
     * CHECK #1) returnType returns liveData
     * CHECK #2) Type liveData<T> is of ApiResponse.class
     * CHECK #3) Make sure ApiResponse is parameterized. Aka: ApiResponse<T> exists
     */


    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        //Check #1
        //Make sure that call adapter is returning a type of LiveData
        if(CallAdapter.Factory.getRawType(returnType) != LiveData.class){
            return null;
        }

        //Check #2
        //Type that LiveData is wrapping
        Type observableType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) returnType);

        //Check if its of type ApiResponse
        Type rawObservableType = CallAdapter.Factory.getRawType(observableType);
        if(rawObservableType != ApiResponse.class){
            throw new IllegalArgumentException("Type should be a defined resource");
        }

        //Check #3
        //Check if ApiResponse is parameterized. AKA: Does ApiResponse<T> exist?(must wrap around T)
        //FYI: T is either RecipeResponse or T will be a RecipeSearchResponse
        if(!(observableType instanceof ParameterizedType)){
            throw new IllegalArgumentException("Resource must be parameterized");
        }

        Type bodyType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) observableType);
        return new LiveDataCallAdapter<Type>(bodyType);
    }
}
