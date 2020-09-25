package com.alexlearn.mvvmappfood.util;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.alexlearn.mvvmappfood.AppExecutors;
import com.alexlearn.mvvmappfood.requests.responses.ApiResponse;

//CacheObject Type for the Resource data (database cache)
//RequestObject: Type for the API response (network request)

public abstract class NetworkBoundResource <CacheObject, RequestObject> {

    private static final String TAG = "NetworkBoundResource";

    private AppExecutors appExecutors;
    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }

    private void init(){
        //update LiveData for loading status (We have 3 statuses in Resource file from util Package. This is for LOADING)
        //something is loading, we prepare everything to get data
        results.setValue((Resource<CacheObject>) Resource.loading(null));

        //Observe Livedata source from local database
        final LiveData<CacheObject> dbSource = loadFromDb();

        //if data is retrieved from the local database method onChanged will start
        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                results.removeSource(dbSource);
                //decide if we want to refresh the cache
                if(shouldFetch(cacheObject)){
                    //get data from network
                    fetchFromNetwork(dbSource);
                }else {
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        //sending value to ui
                        public void onChanged(CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }
            }
        });
    }

    /**
     * 1) observe local db
     * 2) if<condition/> query the network
     * 3) stop observing the local db
     * 4) insert new data into local db
     * 5)begin observing db again to see refreshed data from network
     * @param dbSource
     */
    private void fetchFromNetwork(final LiveData<CacheObject> dbSource){
        Log.d(TAG, "fetchFromNetwork: called");

        //update LiveData for loading status
        results.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                setValue(Resource.loading(cacheObject));
            }
        });

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();
        results.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(ApiResponse<RequestObject> requestObjectApiResponse) {
                results.removeSource(dbSource);
                results.removeSource(apiResponse);

                /*
                3 cases:
                1) ApiSuccessResponse
                2) ApiErrorResponse
                3) ApiEmptyResponse
                 */
                if(requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse){
                    Log.d(TAG, "onChanged: ApiSuccess");
                    //We need diskIO because method saveCallResult must be done on backGround thread
                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            //save the response to the local db
                            saveCallResult((RequestObject) processResponse((ApiResponse.ApiSuccessResponse)requestObjectApiResponse));
                            //we use mainThread because we use method setValue
                            //set Value sets value immediately
                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                        @Override
                                        public void onChanged(CacheObject cacheObject) {
                                            setValue(Resource.success(cacheObject));
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse){
                    Log.d(TAG, "onChanged: ApiEmptyResponse");
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setValue(Resource.success(cacheObject));
                                }
                            });
                        }
                    });
                }
                else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse){
                    Log.d(TAG, "onChanged: ApiErrorResponse");
                    results.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            setValue(
                                    Resource.error(
                                            ((ApiResponse.ApiErrorResponse) requestObjectApiResponse).getErrorMessage(),
                                            cacheObject
                                    )
                            );
                        }
                    });
                }
            }
        });
    }

    private CacheObject processResponse(ApiResponse.ApiSuccessResponse response){
        return (CacheObject) response.getBody();
    }

    private void setValue(Resource<CacheObject> newValue){
        if(results.getValue() != newValue){
            results.setValue(newValue);
        }
    }
    //called to save the result of the API response into the data base
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    //Called with the data in the database to decide whether to fetch
    //potentially updated data from the network
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    //Called to get the cached data from the database
    @NonNull @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    //Called to create the API call
    @NonNull @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    //Called when the fatch fails. The child class may want to reset components
    //like rate limiter
    //    @MainThread
    //    protected void onFatchFailed();

    //Returns a LiveData object that represents the resource that`s implemented
    // in the base class;
    public final LiveData<Resource<CacheObject>> getAsLiveData(){
        return results;
    }
}
