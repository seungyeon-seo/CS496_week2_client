package com.example.cs496_week2_client.ui.my_page;


import com.example.cs496_week2_client.models.Group;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MyService {
    private Retrofit retrofitClient;
    GroupAPI group;

    public MyService(Retrofit retrofit) {
        retrofitClient = retrofit;
        group = retrofitClient.create(GroupAPI.class);
    }
}

interface GroupAPI {
    @GET("group/exit/{code}/{userId}")
    Call<String> exitGroup(@Path("code") String code, @Path("userId") String userId);

}