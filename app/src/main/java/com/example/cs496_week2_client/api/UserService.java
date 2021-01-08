package com.example.cs496_week2_client.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

class UserService {
    private Retrofit retrofitClient;

    public UserService(Retrofit retrofit) {
        retrofitClient = retrofit;
    }

    AuthAPI auth = retrofitClient.create(AuthAPI.class);
    // TODO 유저 정보 수정
}

interface AuthAPI {
    @GET("auth/{token}")
    Call<User> login(@Path("token") String token);

    @POST("auth/register")
    Call<User> register(@Body Map<String, String> map);
}
