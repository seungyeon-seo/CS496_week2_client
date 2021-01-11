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

    // TODO Api 안으로 옮겨서 retrofitClient 하나만 만들도록 하기
    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    SelectAPI select = retrofitClient.create(com.example.cs496_week2_client.ui.map.SelectAPI.class);
    UpdateAPI update = retrofitClient.create(com.example.cs496_week2_client.ui.map.UpdateAPI.class);
}

interface SelectAPI{
    // TODO 서버 API 에 맞춰서 수정
    @GET("group/members")
    Call<ArrayList<MemLocation>> getLocations();
}

interface UpdateAPI{
    // TODO 서버 API 에 맞춰서 수정
    @PUT("user/sync")
    Call<MemLocation> setLocation(@Body HashMap<String, Object> param);
}
