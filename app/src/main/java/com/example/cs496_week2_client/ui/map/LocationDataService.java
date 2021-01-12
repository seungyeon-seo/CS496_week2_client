package com.example.cs496_week2_client.ui.map;


import com.example.cs496_week2_client.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
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

    @GET("user/location/{userId}/{latitude}/{longitude}")
    Call<User> setUserLocation(@Path("userId") String userId, @Path("latitude") String latitude, @Path("longitude") String longitude);

    @GET("user/status/{userId}/{status}")
    Call<User> setUserStatus(@Path("userId") String userId, @Path("status") String status);

}

