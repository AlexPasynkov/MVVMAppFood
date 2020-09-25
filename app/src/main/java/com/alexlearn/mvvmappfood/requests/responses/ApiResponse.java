package com.alexlearn.mvvmappfood.requests.responses;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse <T>{

    //If the network is down, this error message will be displayed
    public ApiResponse<T> create(Throwable error){
        return new ApiErrorResponse<>(!error.getMessage().equals("") ? error.getMessage() : "Unknown Error\nCheck network connection");
    }

    public ApiResponse<T> create(Response<T> response){
        if(response.isSuccessful()){
            T body = response.body();

            if(body == null || response.code() == 204){// 204 is empty response code
                return new ApiEmptyResponse<>();
            }else {
                return new ApiSuccessResponse<>(body);
            }
        }
        else{
            String errorMsg = "";
            try {
                errorMsg = response.errorBody().string();
            }catch (IOException e){
                e.printStackTrace();
                errorMsg = response.message();
            }

            return new ApiErrorResponse<>(errorMsg);
        }
    }

    public class ApiSuccessResponse<T> extends ApiResponse<T>{
        private T body;

        ApiSuccessResponse(T body){
            this.body = body;
        }

        //Body of request
        public T getBody() {
            return body;
        }
    }

    public class ApiErrorResponse<T> extends ApiResponse<T>{
        private String errorMessage;

        ApiErrorResponse(String errorMessage){
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage(){
            return errorMessage;
        }
    }

    public class ApiEmptyResponse<T> extends ApiResponse<T>{

    }
}
