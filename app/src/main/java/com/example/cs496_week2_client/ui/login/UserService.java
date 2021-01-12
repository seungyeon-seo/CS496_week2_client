package com.example.cs496_week2_client.ui.login;

import com.example.cs496_week2_client.models.Group;
import com.example.cs496_week2_client.models.User;

import java.util.ArrayList;

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
    GroupAPI group;

    public UserService(Retrofit retrofit) {
        retrofitClient = retrofit;
        auth = retrofitClient.create(AuthAPI.class);
        image = retrofitClient.create(ImageAPI.class);
        group = retrofitClient.create(GroupAPI.class);
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
    @POST("upload/{userId}")
    Call<String> postImage(@Path("userId") String userId, @Part MultipartBody.Part image, @Part("name") RequestBody name);
}

interface GroupAPI {
    @GET("group/create/{name}/{userId}")
    Call<Group> createGroup(@Path("name") String name, @Path("userId") String userId);

    @GET("group/join/{code}/{userId}")
    Call<Group> joinGroup(@Path("code") String code, @Path("userId") String userId);

//    @GET("group/exit/{code}/{userId}")
//    Call<String> exitGroup(@Path("code") String code, @Path("userId") String userId);

}
