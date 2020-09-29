package com.alexlearn.mvvmappfood.requests;

import com.alexlearn.mvvmappfood.util.Constans;
import com.alexlearn.mvvmappfood.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.alexlearn.mvvmappfood.util.Constans.CONNECTION_TIMEOUT;
import static com.alexlearn.mvvmappfood.util.Constans.READ_TIMEOUT;
import static com.alexlearn.mvvmappfood.util.Constans.WRITE_TIMEOUT;
//addCallAdapterFactory()  - specify how I want to make call to the livedata

public class ServiceGenerator {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constans.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    public static RecipeApi getRecipeApi() {
        return recipeApi;
    }
}
