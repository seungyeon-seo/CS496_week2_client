package com.example.cs496_week2_client.ui.login;

import com.example.cs496_week2_client.models.User;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class UserService {
    private Retrofit retrofitClient;
    AuthAPI auth;

    public UserService(Retrofit retrofit) {
        retrofitClient = retrofit;
        auth = retrofitClient.create(AuthAPI.class);
    }

    // TODO 유저 정보 수정 API
}

interface AuthAPI {
    @GET("user/login/{token}")
    Call<User> login(@Path("token") String token);

    @POST("user/register/{token}/{nickname}")
    Call<User> register(@Path("token") String token, @Path("nickname") String nickname);
}