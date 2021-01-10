package com.example.cs496_week2_client.ui.feed;

import com.example.cs496_week2_client.models.Post;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class PostService {
    private Retrofit retrofitClient;
    SelectAPI select;
    InsertAPI insert;
    UpdateAPI update;
    DeleteAPI delete;
    ImageAPI image;

    public PostService(Retrofit retrofitClient) {
        this.retrofitClient = retrofitClient;
        select = retrofitClient.create(SelectAPI.class);
        insert = retrofitClient.create(InsertAPI.class);
        update = retrofitClient.create(UpdateAPI.class);
        delete = retrofitClient.create(DeleteAPI.class);
        image = retrofitClient.create(ImageAPI.class);
    }

}

interface SelectAPI {
    @GET("select/{id}")
    Call<Post> selectOne(@Path("id") long id);

    @GET("select")
    Call<List<Post>> selectAll();
}

interface InsertAPI {
    @POST("insert")
    Call<Post> insertOne(@Body Map<String, String> map);
}

interface UpdateAPI {
    @POST("update/{id}")
    Call<Post> updateOne(@Path("id") long id, @Body Map<String, String> map);
}

interface DeleteAPI {
    @POST("delete/{id}")
    Call<ResponseBody> deleteOne(@Path("id") long id);
}

interface ImageAPI {
    @Multipart
    @POST("upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);
    // TODO token 함께 보내기
}