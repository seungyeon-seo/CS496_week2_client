package com.example.cs496_week2_client.ui.contacts;

import com.example.cs496_week2_client.models.ContactModel;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ContactService {
    private Retrofit retrofitClient;
    ContactAPI contact;

    public ContactService(Retrofit retrofit) {
        retrofitClient = retrofit;
         contact = retrofitClient.create(ContactAPI.class);
    }
}

interface ContactAPI{
    @GET("contact/{userId}")
    Call<ArrayList<ContactModel>> getContacts(@Path("userId") String userId);

    @POST("contact/insert/{userId}")
    Call<ContactModel> insertContact(@Body HashMap<String, Object> param, @Path("userId") String userId);
}
