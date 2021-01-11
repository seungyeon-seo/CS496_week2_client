package com.example.cs496_week2_client.ui.contacts;

import com.example.cs496_week2_client.models.ContactModel;

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

public class ContactDataService {
    private String BASE_URL = "http://192.249.18.231/";

    // TODO Api 안으로 옮겨서 retrofitClient 하나만 만들도록 하기
    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    SelectAPI select = retrofitClient.create(SelectAPI.class);
    InsertAPI insert = retrofitClient.create(InsertAPI.class);
}

interface SelectAPI{
    @GET("contact/{userId}")
    Call<ArrayList<ContactModel>> getContacts(@Path("userId") String userId);
}

interface InsertAPI{
    @POST("contact/insert/{userId}")
    Call<ContactModel> insertContact(@Body HashMap<String, Object> param, @Path("userId") String userId);
}
