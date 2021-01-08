package com.example.cs496_week2_client.api;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

class PostService {
    private Retrofit retrofitClient;
    
    public PostService(Retrofit retrofit) {
        retrofitClient = retrofit;
    }

    SelectAPI select = retrofitClient.create(SelectAPI.class);
    InsertAPI insert = retrofitClient.create(InsertAPI.class);
    UpdateAPI update = retrofitClient.create(UpdateAPI.class);
    DeleteAPI delete = retrofitClient.create(DeleteAPI.class);
}

interface SelectAPI{
    @GET("select/{id}")
    Call<Post> selectOne(@Path("id") long id);

    @GET("select")
    Call<List<Post>> selectAll();
}
interface InsertAPI{
    @POST("insert")
    Call<Post> insertOne(@Body Map<String, String> map);
}

interface UpdateAPI{
    @POST("update/{id}")
    Call<Post> updateOne(@Path("id") long id, @Body Map<String, String> map);
}

interface DeleteAPI{
    @POST("delete/{id}")
    Call<ResponseBody> deleteOne(@Path("id") long id);
}
