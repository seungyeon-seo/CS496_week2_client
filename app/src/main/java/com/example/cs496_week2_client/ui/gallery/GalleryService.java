package com.example.cs496_week2_client.ui.gallery;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class GalleryService {
    private Retrofit retrofitClient;
    ImageAPI image;

    public GalleryService(Retrofit retrofit) {
        retrofitClient = retrofit;
        image = retrofitClient.create(ImageAPI.class);
    }

}

interface ImageAPI {
    @Multipart
    @POST("upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);
}