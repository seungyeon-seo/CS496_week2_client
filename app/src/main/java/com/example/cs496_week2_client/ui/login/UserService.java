package com.example.cs496_week2_client.ui.login;

import com.example.cs496_week2_client.models.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class UserService {
    private Retrofit retrofitClient;
    AuthAPI auth;
    ImageAPI image;

    public UserService(Retrofit retrofit) {
        retrofitClient = retrofit;
        auth = retrofitClient.create(AuthAPI.class);
        image = retrofitClient.create(ImageAPI.class);

    }

    // TODO 유저 정보 수정 API
}

interface AuthAPI {
    @GET("user/login/{token}")
    Call<User> login(@Path("token") String token);

    @POST("user/register/{token}/{nickname}")
    Call<User> register(@Path("token") String token, @Path("nickname") String nickname);
}

interface ImageAPI {
    @Multipart
    @POST("upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);
    // TODO token 함께 보내기
}