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
    private String BASE_URL = "http://192.249.18.224/test/";

    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    SelectAPI select = retrofitClient.create(SelectAPI.class);
    InsertAPI insert = retrofitClient.create(InsertAPI.class);
    UpdateAPI update = retrofitClient.create(UpdateAPI.class);
    DeleteAPI delete = retrofitClient.create(DeleteAPI.class);
}

interface SelectAPI{
    @GET("contact/get")
    Call<ArrayList<ContactModel>> getContacts();
}

interface InsertAPI{
    @POST("contact/insert")
    Call<ContactModel> insertContact(@Body HashMap<String, Object> param);
}

interface UpdateAPI{
    @PUT("contact/update/status")
    Call<ContactModel> setStatus(@Body HashMap<String, Object> param);
}

interface DeleteAPI{
//    @POST("delete/{id}")
//    Call<Test> deleteOne(@Path("id") long id);
}