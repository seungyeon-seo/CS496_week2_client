package com.example.cs496_week2_client.contacts;

import com.example.cs496_week2_client.models.ContactModel;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public class ContactDataService {
    private String BASE_URL = "http://192.249.18.216/test/";

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
//    @GET("get")
//    Call<Test> selectOne();

//    @GET("get2")
//    Call<Test> selectById(@Path("id") long id);

}
interface InsertAPI{
    @POST("contact/insert")
    Call<ContactModel> insertContact(@Body HashMap<String, Object> param);
}

interface UpdateAPI{
//    @PUT("contact/update")
//    Call<Contact> updateContact(@Body Contact param);
}

interface DeleteAPI{
//    @POST("delete/{id}")
//    Call<Test> deleteOne(@Path("id") long id);
}