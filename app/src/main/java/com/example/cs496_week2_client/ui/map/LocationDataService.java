package com.example.cs496_week2_client.ui.map;

import com.example.cs496_week2_client.models.MemLocation;
import com.example.cs496_week2_client.models.User;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class LocationDataService {
    private String BASE_URL = "http://192.249.18.224/test/";

    // TODO Api 안으로 옮겨서 retrofitClient 하나만 만들도록 하기
    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    LocationAPI location = retrofitClient.create(com.example.cs496_week2_client.ui.map.LocationAPI.class);
}

interface LocationAPI{
    @GET("group/members/{groupCode}")
    Call<ArrayList<User>> getMembers(@Path("groupCode") String groupCode);

    @GET("user/sync/{userId}/{status}/{latitude}/{longitude}")
    Call<User> setUserStatusLocation(@Path("userId") String userId, @Path("status") String status, @Path("latitude") String latitude, @Path("longitude") String longitude);
}

