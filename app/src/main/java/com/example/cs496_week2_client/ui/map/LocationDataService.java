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
    Retrofit retrofitClient;
    LocationAPI location;

    public LocationDataService(Retrofit retrofit) {
        retrofitClient = retrofit;
        location  = retrofitClient.create(com.example.cs496_week2_client.ui.map.LocationAPI.class);
    }
}

interface LocationAPI{
    @GET("group/members/{groupCode}")
    Call<ArrayList<User>> getMembers(@Path("groupCode") String groupCode);

    @GET("user/sync/{userId}/{status}/{latitude}/{longitude}")
    Call<User> setUserStatusLocation(@Path("userId") String userId, @Path("status") String status, @Path("latitude") String latitude, @Path("longitude") String longitude);
}

