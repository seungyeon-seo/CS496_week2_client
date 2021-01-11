package com.example.cs496_week2_client.ui.map;

import com.example.cs496_week2_client.models.MemLocation;

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

public class LocationDataService {
    private String BASE_URL = "http://192.249.18.224/test/";

    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    SelectAPI select = retrofitClient.create(com.example.cs496_week2_client.ui.map.SelectAPI.class);
    InsertAPI insert = retrofitClient.create(com.example.cs496_week2_client.ui.map.InsertAPI.class);
    UpdateAPI update = retrofitClient.create(com.example.cs496_week2_client.ui.map.UpdateAPI.class);
    DeleteAPI delete = retrofitClient.create(com.example.cs496_week2_client.ui.map.DeleteAPI.class);
}

interface SelectAPI{
    @GET("location/get")
    Call<ArrayList<MemLocation>> getLocations();
}

interface InsertAPI{
//    @POST("contact/insert")
//    Call<MemLocation> insertContact(@Body HashMap<String, Object> param);
}

interface UpdateAPI{
    @PUT("contact/update/status")
    Call<MemLocation> setLocation(@Body HashMap<String, Object> param);
}

interface DeleteAPI {
//    @POST("delete/{id}")
//    Call<Test> deleteOne(@Path("id") long id);
}