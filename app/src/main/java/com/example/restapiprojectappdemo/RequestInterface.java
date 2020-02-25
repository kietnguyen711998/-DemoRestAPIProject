package com.example.restapiprojectappdemo;

import com.example.restapiprojectappdemo.model.ServerRequest;
import com.example.restapiprojectappdemo.model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {
    @POST("learn2crack-login-register/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
